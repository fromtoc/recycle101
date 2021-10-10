package com.xin.portal.system.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.xin.portal.system.mapper.RoleMapper;
import com.xin.portal.system.model.*;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ConcurrentAccessException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.xin.portal.system.mapper.RoleUserMapper;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.MenuService;
import com.xin.portal.system.service.RolePermissionService;
import com.xin.portal.system.service.UserService;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.SessionUtil;


public class SystemShiroRealm extends AuthorizingRealm {

    private static final Logger logger = LoggerFactory.getLogger(SystemShiroRealm.class);

    @Autowired
	private UserService userService;
    @Autowired
	private BiUserService biUserService;
    @Autowired
	private RoleUserMapper roleUserMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
    private RolePermissionService rolePermissionService;
    @Value("${mstr.enabled}")
	private boolean mstrEnabled;
    @Autowired
    protected CacheManager cacheManager;
    @Autowired
    private MenuService menuService;
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private ConfigService configService;

    /**
     * 权限认证，为当前登录的Subject授予角色和权限 
     * @see ：本例中该方法的调用时机为需授权资源被访问时
     * @see ：并且每次访问需授权资源时都会执行该方法中的逻辑，这表明本例中默认并未启用AuthorizationCache
     * @see ：如果连续访问同一个URL（比如刷新），该方法不会被重复调用，Shiro有一个时间间隔（也就是cache时间，在ehcache-shiro.xml中配置），超过这个时间间隔再刷新页面，该方法会被执行
     */
    @SuppressWarnings("unchecked")
	@Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.info("##################执行Shiro权限认证##################");
      SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
        //角色code集合
      	Object userRoles = SessionUtil.getSession(SessionKeys.USER_ROLES);
		simpleAuthorInfo.addRoles((List<String>) (userRoles == null ? Lists.newArrayList() : userRoles));
		Object userPerms = SessionUtil.getSession(SessionKeys.USER_PERMS);
		simpleAuthorInfo.addStringPermissions((List<String>)(userPerms == null ? Lists.newArrayList() : userPerms));
//		
		logger.info("======用户[{}]的角色信息:{}",principals.getPrimaryPrincipal(),simpleAuthorInfo.getRoles().toString());
	    logger.info("======用户[{}]的权限信息:{}",principals.getPrimaryPrincipal(),simpleAuthorInfo.getStringPermissions().toString());
        
        // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
        return simpleAuthorInfo;
    }

	/**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        //UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken usernamePasswordToken=(UsernamePasswordToken) token;
        logger.info("shiro login: {}",usernamePasswordToken.getUsername());

		User user = userService.findByUsernameAndTenantIds(usernamePasswordToken.getUsername(),usernamePasswordToken.getHost());

		SessionUtil.setSession(SessionKeys.TENANT_ID, user.getTenantId());
        if (user!=null) {
        	//以下为只允许同一账户单个登录

            Config repeatLogin = configService.findByCode(ConfigKeys.REPEAT_LOGIN, user.getTenantId());
    		if("0".equals(repeatLogin.getValue())){//不同意重复登录
    			//判断不重复登录的选择

    			Config notLoginWay = configService.findByCode(ConfigKeys.NOT_ALLOW_REPEAT_LOGIN_WAY, user.getTenantId());
    			Collection<Session> sessions = sessionDAO.getActiveSessions();
				String idUser = user.getId();
				if(sessions.size()>0) {
    				for (Session session : sessions) {
    					//System.out.println("all  session :" + session);
    					//获得session中已经登录用户的名字
    					if(null!=session.getAttribute("user")) {
    						UserInfo loginUser = (UserInfo) session.getAttribute("user");
    						//System.out.println("login user " + loginUser.getUsername());
    						if (idUser.equals(loginUser.getId())) {  //这里的username也就是当前登录的username
    							if("0".equals(notLoginWay.getValue())){//0、已登录不允许再登陆。
    								logger.info("user has login did not login second :"+usernamePasswordToken.getUsername());
    			    				throw new ConcurrentAccessException();
    			    			}else{//后登录踢出先登录
    			    				session.setTimeout(0);  //这里就把session清除，
        							logger.info("clean the user session for first Login"+usernamePasswordToken.getUsername());
    			    			}
    						}
    					}
    				}
    			}
    		}

        	if (Integer.valueOf(1).equals(user.getState())) {
        		throw new LockedAccountException();//账号状态禁用
        	} else if (user.getActivateEndTime()!=null && new Date().after(user.getActivateEndTime())) {
        		throw new ExpiredCredentialsException();//账号过期
        	}else {
        		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(usernamePasswordToken.getUsername(), user.getPassword().trim(), getName());
        		if (getCredentialsMatcher().doCredentialsMatch(token,authenticationInfo)) {
        			UserInfo userInfo = new UserInfo(user.getId()).selectById();
        			userInfo.setUsername(user.getUsername());
        			SessionUtil.setSession(SessionKeys.USER, userInfo);
        			/*
        			EntityWrapper<BiUser> ewBiUser = new EntityWrapper<>();
        			ewBiUser.where("id in (select mapping_user_id from t_user_mapping where user_id = {0}  )", user.getId());
        			List<BiUser> biUsers = biUserService.selectList(ewBiUser);
        			for (BiUser mstrUser : biUsers) {
        				if (BiType.MSTR.equals(mstrUser.getType())) {
        					SessionUtil.setSession(SessionKeys.USER_MAPPING_MSTR, mstrUser);
        				} else if (BiType.BO.equals(mstrUser.getType())) {
        					SessionUtil.setSession(SessionKeys.USER_MAPPING_BO, mstrUser);
        				}
        			}
        			if (mstrEnabled) {
        				EntityWrapper<BiProject> biProject = new EntityWrapper<>(new BiProject());
        				List<BiProject> projects = biProjectService.selectList(biProject);
	        			if (projects.size()>0) {
	        				Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
	        				for (int i=0;i<projects.size();i++) {
	        					cache.put(Constant.CACHE_MSTR_PROJECT+i, projects.get(i));
	        				}
	        			}
	        			BiUser query = new BiUser();
        				query.setUserId(user.getId());
        				query.setType(BiType.MSTR);
        				BiUser mstrUser = biUserService.find(query);
        				if (mstrUser!=null) {
        					SessionUtil.setSession(SessionKeys.USER_MAPPING_MSTR, mstrUser);
        				}
        			}*/


					List<Menu> list = menuService.findUserMenus(user.getId());
					List<Menu> uMenus = buildByRecursive(list);
					uMenus.sort((a, b) -> a.getSort() - b.getSort());
					List<Menu> userMenus  = new ArrayList<>();
					for(Menu menu : uMenus){
						String  linkUrl = menu.getLinkUrl();
						if((linkUrl!=null && linkUrl.trim().length() > 0) || (menu.getResourceId() != null && menu.getResourceId().trim().length() > 0)){
							userMenus.add(menu);
						}else{
							List<Menu> isHave = menu.getChildren();
							if(isHave != null && isHave.size()>0){
								userMenus.add(menu);
							}
						}
					}
					Integer colorNum=0;
					for (Menu menu : userMenus) {
						List<Menu> children = menu.getChildren();
						if (children!=null && children.size()>0) {
							children.sort((a, b) -> a.getSort() - b.getSort());
						}
						menu.setChildren(children);
						colorNum+=1;
						if(colorNum>4){
							colorNum=1;
						}
						menu.setColorNum(colorNum);

						String title = menu.getName();
						if(title.length()>4){
							menu.setShortTitle(title.substring(0,4));
						}else{
							menu.setShortTitle(title);
						}
					}

    				SessionUtil.setSession(SessionKeys.USER_MENUS, userMenus);
					String userMenusJson = JSONObject.toJSONString(userMenus);
					SessionUtil.setSession(SessionKeys.USER_MENUS_JSON, userMenusJson);
					logger.info(userMenusJson);
        			
					EntityWrapper<RoleUser> ew = new EntityWrapper<>();
        			ew.eq("user_id", user.getId());
        			List<RoleUser> roleUsers = roleUserMapper.selectList(ew);
//        			List<RoleUser> roleUsers = roleUserMapper.findRoleCodes(user.getId());
        			
//        			Map<String, List<String>> roleMap = getRoleMap(roleUsers);
//        			SessionUtil.setSession(SessionKeys.USER_ROLES, roleMap.get(ROLE_MAP_KEY_CODE));
//        			String roleIds = StringUtils.join(roleMap.get(ROLE_MAP_KEY_ID),",");
//        			List<String> perms = rolePermService.findPermCode(roleIds);
//        			SessionUtil.setSession(SessionKeys.USER_PERMS, perms);
        			if (roleUsers!=null && roleUsers.size()>0) {
        				//当前用户所拥有的所有角色的roleID集合
        				List<String> roleIds = roleUsers.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
						//当前用户所拥有的所有角色的code集合
						List<Role> roles = roleMapper.selectBatchIds(roleIds);
						List<String> roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toList());
						for(String code : roleCodes){
							roleIds.add(code);
						}
						// 查到权限数据，返回授权信息(要包括 上边的permissions)
        				SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        				SessionUtil.setSession(SessionKeys.USER_ROLES, roleIds);
        				logger.info("roleIds: {}",roleIds);
        				simpleAuthorizationInfo.addRoles(roleIds);
        		        // 将上边查询到授权信息填充到simpleAuthorizationInfo对象中
        				EntityWrapper<RolePermission> ewRolePermission = new EntityWrapper<>();
        				ewRolePermission.in("role_id", roleIds);
        				List<RolePermission> rolePermissions = rolePermissionService.selectList(ewRolePermission);
        				List<String> permissions = rolePermissions.stream().map(item -> item.getResourceId()+":"+item.getCode()).collect(Collectors.toList());
        		        logger.info("permissions: {}",permissions);
        				simpleAuthorizationInfo.addStringPermissions(permissions);
        				SessionUtil.setSession(SessionKeys.USER_PERMS, permissions);
        		        
        				/*	
        				EntityWrapper<RoleResource> ewRoleResource = new EntityWrapper<>();
        				ewRoleResource.in("role_id", roleIds);
        				
        				List<RoleResource> roleResourceList = roleResourceService.selectList(ewRoleResource);
        				List<String> resourceIds = roleResourceList.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
        				
        				EntityWrapper<Resource> ewResource = new EntityWrapper<>();
        				ewResource.in("id", resourceIds);
        				ewResource.orderBy("sort", true);
        				List<Resource> resources = resourceService.selectList(ewResource);
        				if (resources!=null) {
//        					List<Resource> userMenus = toTreeList(resources);
        					List<Resource> userMenus = buildByRecursive(resources);
        					SessionUtil.setSession(SessionKeys.USER_MENUS, userMenus);
        					String userMenusJson = JSONObject.toJSONString(userMenus);
        					SessionUtil.setSession(SessionKeys.USER_MENUS_JSON, userMenusJson);
        					logger.info(userMenusJson);
        					
        				}*/
        			}
        		}
        		return authenticationInfo;
        	}
        }else{
        	return null;
        }
        
    }
    
    
    /** * 使用递归方法建树 * @param treeNodes * @return */
	public static List<Menu> buildByRecursive(List<Menu> treeNodes) {
		List<Menu> trees = new ArrayList<Menu>();
		for (Menu treeNode : treeNodes) {
			if ("0".equals(treeNode.getParentId())) {
				Menu menu = findChildren(treeNode, treeNodes);
				if(menu != null){
					trees.add(menu);
				}
            }
		}
		return trees;
	}

	/**     * 递归查找子节点     * @param treeNodes     * @return     */    
	public static Menu findChildren(Menu treeNode,List<Menu> treeNodes) {      
		for (Menu it : treeNodes) {        
			if(treeNode.getId().equals(it.getParentId())) {                
				if (treeNode.getChildren() == null) {                    
					treeNode.setChildren(new ArrayList<Menu>());                
				}
				Menu menu = findChildren(it,treeNodes);
				if(menu != null){
					treeNode.getChildren().add(menu);  
				}
			}        
		}
		if((treeNode.getLinkUrl() != null && treeNode.getLinkUrl().trim().length() > 0)
				|| (treeNode.getFileId() != null && treeNode.getFileId().trim().length() > 0)
				|| (treeNode.getChildren() != null && treeNode.getChildren().size() > 0)
				|| (treeNode.getResourceId() != null && treeNode.getResourceId().trim().length() > 0)){
			return treeNode;
		}
		return null;
	}
    
    

}