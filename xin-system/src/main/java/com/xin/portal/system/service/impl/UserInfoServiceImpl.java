package com.xin.portal.system.service.impl;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.xin.portal.system.mapper.*;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.OrganizationService;
import com.xin.portal.system.service.RoleService;
import com.xin.portal.system.util.AdUtils.ADUtils;
import com.xin.portal.system.util.AdUtils.entity.AdEntity;
import com.xin.portal.system.util.AdUtils.entity.AdUser;
import com.xin.portal.system.util.RSAUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.microstrategy.web.objects.admin.users.WebUser;
import com.xin.portal.bi.mstr.api.FolderApi;
import com.xin.portal.bi.mstr.api.UserApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.service.UserInfoService;
import com.xin.portal.system.util.PasswordUtils;
import com.xin.portal.system.util.SessionUtil;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
	
	private static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	
	@Value("${system.default_password:123456}")
	private String defaultPassword;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ConfigService configService;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private BiServerMapper biServerMapper;
	@Autowired
	private BiProjectMapper biProjectMapper;
	@Autowired
	private BiMappingMapper biMappingMapper;
	@Autowired
	private BiUserMapper biUserMapper;
	@Autowired
	private BiIndependentMapper independMapper;
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleUserMapper roleUserMapper;
	@Autowired
	private UserWxWorkMapper userWxWorkMapper;
	@Autowired
	private UserOrganizationMapper userOrganizationMapper;
	
	
	public PageModel<UserInfo> page(UserInfo query,String status) {
		//根据orgId获取当前节点及所有叶子节点的orgid，然后set到
		
		Page<UserInfo> page = new Page<UserInfo>(query.getPageNumber(),query.getPageSize());
//		RoleOrganization roleOrg = new RoleOrganization();
//		roleOrg.setUserId(SessionUtil.getUserId());
//		List<String> orgIds = roleOrganizationMapper.findOrganizationIdsByUser(roleOrg);
//		query.setOrgIdIn(StringUtils.join(orgIds.toArray(), ","));
		List<UserInfo> list= new ArrayList<>();
		if("1".equals(status)){
			list=userInfoMapper.findList(page, query);
		}else if("2".equals(status)){
			list=userInfoMapper.findAdUserInfoList(page, query);
		}
		
		for(UserInfo a:list){
			/*if(a.getOrgId()!=null&&a.getOrgId()!=""){
				List<String> orgName=new ArrayList<String>();
				getOrgNameByID(a.getOrgId(),orgName);
				//倒叙
				Collections.reverse(orgName);
				String name="";
				for (String s : orgName) {
					 name=name.concat(s).concat("/");
				}
				a.setOrgName(name);
			}*/
			//修改为多组织了，就不需要查询结构了，直接查询所属组织
			//通过用户id获取用户的所有组织id和组织名称，写入到用户信息中userOrgs。
			List<Organization> userOrgs = userOrganizationMapper.selectOrgbyUserId(a.getId());
			a.setUserOrgs(userOrgs);
			//查询用户集成应用内容，步骤不能影响整体的查询，所以捕获异常
			try {
				List<UserWxWork> userWxWork = userWxWorkMapper.findUserRelationApp(a.getId(), a.getTenantId());
				if(userWxWork != null && userWxWork.size() > 0){
					String appTypes = userWxWork.stream().map(UserWxWork::getAppType).collect(Collectors.joining(","));
					a.setWxbind(appTypes);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		page.setRecords(list);
		return new PageModel<UserInfo>(page);
	}
	// 引入了兼职组织，显示组织的时候不需要显示组织层级，所以本方法废弃
	/*private String getOrgNameByID(String orgId,List<String> orgName) {
		Organization  organization=new Organization();
		organization.setId(orgId);
		organization=getOrgNameById(orgId);
		if(organization==null){
			return "";
		}
		orgName.add(organization.getName()==null?"":organization.getName());
		if(organization.getParentId()!=null&&organization.getParentId()!=""){
			Wrapper<Organization> wrapper=new EntityWrapper<Organization>();
			wrapper.eq("id", organization.getParentId());
			Organization organization2=new Organization();
			organization2=organization2.selectOne(wrapper);
			if(organization2!=null&&organization2.getParentId()!=null&&organization2.getParentId()!=""){
				return getOrgNameByID(organization2.getId(), orgName);
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	private Organization getOrgNameById(String orgId) {
			Organization  organization=new Organization();
			organization.setId(orgId);
			organization=organization.selectById(orgId);
			return organization;
	}*/
	@Override
	public UserInfo findUserInfo(UserInfo query) {
		return userInfoMapper.findUserInfo(query);
	}
	
	@Transactional
	@Override
	public boolean save(UserInfo userInfo,String configLocale,String status,String tenantId,String roleCode) throws Exception {
//		try {
			User user = new User();
			if("2".equals(status)){//AD域用户首次登录创建账号
				String DOMAIN_IP  = configService.findByCode("DOMAIN_IP",tenantId).getValue();
				String DOMAIN_NAME  = configService.findByCode("DOMAIN_NAME",tenantId).getValue();
				AdEntity adEntity = new AdEntity();
				adEntity.setDomain(DOMAIN_NAME);
				adEntity.setLdapIP(DOMAIN_IP);
				adEntity.setUsername(userInfo.getUsername());
				adEntity.setPassword(Encrypt(userInfo.getPassword()));
				adEntity.setLdapPORT("389");
				AdUser adUser = ADUtils.getAdUser(adEntity);
				user.setStatus("2");
				user.setExtId(adUser.getId());
				userInfo.setPassword(PasswordUtils.hash(adEntity.getPassword()));
				userInfo.setMstrUser(0);
				userInfo.setPhone(adUser.getTelephone());
				userInfo.setEmail(adUser.getMail());
				userInfo.setRealname(adUser.getDisplayName());
				userInfo.setGender(1);
				//EntityWrapper<Organization> wrapper = new EntityWrapper<>();
				Organization result = organizationService.selectByExtId(adUser.getDepartment(),tenantId);
				if(result==null){
					organizationService.importOrgForAD(tenantId);
					result = organizationService.selectByExtId(adUser.getDepartment(),tenantId);
				}
				userInfo.setOrgCode(result.getCode());
				userInfo.setOrgId(result.getId());
				userInfo.setMstrUser(0);
			}else{
				user.setStatus("1");
				userInfo.setPassword(PasswordUtils.hash(defaultPassword));
			}
			user.setUsername(userInfo.getUsername());
			user.setPassword(userInfo.getPassword());
			user.setRegisterTime(new Date());
			user.setResetKey("0");
			user.setState(0);
			user.setLoginCount(0);
			user.setErrorCount(0);
			user.setPwdExpireTime(userInfo.getPwdExpireTime());
			user.setActivateStartTime(userInfo.getActivateStartTime());
			user.setActivateEndTime(userInfo.getActivateEndTime());
			user.setIsDelete(0);
			user.setTenantId(tenantId);
			boolean result = true;
			if(userMapper.insertUserTD(user)!=1){
				result=false;
			}
			if (result) {
				userInfo.setId(user.getId());
				userInfo.setAvatar("/images/avatar.jpg");
				userInfo.setCode(userInfo.getUsername());
				userInfo.setPreferenceType("orgHome");//默认首选项
				userInfo.setLocale(configLocale);//默认语言
				userInfo.setTenantId(tenantId);
				boolean resultInfo = true;
				if(userInfoMapper.insertUserInfoTD(userInfo)!=1){
					resultInfo = false;
				}
				//添加用户的主组织到用户组织关系表中
				boolean resultUserOrg = true;
				if(resultInfo){
					List<Organization> userOrgs = userInfo.getUserOrgs();
					if(userOrgs != null && userOrgs.size() > 0){
						//只有 兼职组织。在新建中主组织以用户信息字段中携带的org_id为准（因为新建的时候只能是主组织）
						//兼容组织是后新增的，这个list是上传后根据兼容组织code查询写入的。
						UserOrganization userOrgDepyty = null;
						for (Organization org : userOrgs) {
							userOrgDepyty = new UserOrganization();
							userOrgDepyty.setUserId(user.getId());
							userOrgDepyty.setOrgId(org.getId());
							userOrgDepyty.setIsDeputy(1);
							userOrganizationMapper.insert(userOrgDepyty);
						}
					}
					//直接将用户传入的组织id写入到组织与用户关系表中（主组织）
					UserOrganization userOrg = new UserOrganization();
					userOrg.setUserId(user.getId());
					userOrg.setOrgId(userInfo.getOrgId());
					userOrg.setIsDeputy(0);
					resultUserOrg = userOrganizationMapper.insert(userOrg)!=1?false:true;
				}
				//添加角色
				Role resultRole = roleService.selectByCode(roleCode,tenantId);
				RoleUser roleUser = new RoleUser();
				roleUser.setRoleCode(resultRole.getCode());
				roleUser.setRoleId(resultRole.getId());
				roleUser.setUserId(user.getId());
				roleUser.setTenantId(tenantId);
				roleUserMapper.insertRoleUserTD(roleUser);
				//同步创建mstr用户 （注：如果是同步创建mstr用户的话，默认用户与项目是一对一的关系。一个用户只能在一个server中创建用户，并且只能在一个项目中有自主分析地址）
				if (userInfo.getMstrUser()!=null && userInfo.getMstrUser().intValue()==1) {//同步创建mstr用户 | 参数：serverId，projectId，username，password
					//获取server和project
					BiServer biserver = biServerMapper.selectById(userInfo.getMstrServerId());
					BiProject biproject = biProjectMapper.selectById(userInfo.getMstrProjectId());
					boolean isSucc = searchAndCreateMstrUserAndFolder(userInfo,biserver,biproject);
					System.out.println("创建mstr用户："+isSucc);
					logger.error("创建mstr用户："+isSucc);
					if(userInfo.getRemberMstrChoice()!=null && "1".equals(userInfo.getRemberMstrChoice())){//记住mstr选项
						//设置is_indepen_pro,所有的mstr项目中只能有一个自动同步的默认项目
						biProjectMapper.updateProjectIndepend(biproject.getId());
					}
					if (isSucc) {
						return result && resultInfo && resultUserOrg;
					}	
				}
				return result && resultInfo && resultUserOrg;
			}

		return false;
	}

	@Transactional
	@Override
	public boolean saveAdministrator(UserInfo userInfo,String configLocale,String status,String tenantId,String roleCode) throws Exception {
		User user = new User();
		//系统设置认证AD域时创建账号
		String DOMAIN_IP  = configService.findByCode("DOMAIN_IP",tenantId).getValue();
		String DOMAIN_NAME  = configService.findByCode("DOMAIN_NAME",tenantId).getValue();
		AdEntity adEntity = new AdEntity();
		adEntity.setDomain(DOMAIN_NAME);
		adEntity.setLdapIP(DOMAIN_IP);
		adEntity.setUsername(userInfo.getUsername());
		adEntity.setPassword(userInfo.getPassword());
		adEntity.setLdapPORT("389");
		AdUser adUser = ADUtils.getAdUser(adEntity);
		user.setStatus("2");
		user.setExtId("domain_admin");
		userInfo.setPassword(PasswordUtils.hash(adEntity.getPassword()));
		userInfo.setMstrUser(0);
		userInfo.setPhone(adUser.getTelephone());
		userInfo.setEmail(adUser.getMail());
		userInfo.setRealname(adUser.getDisplayName());
		userInfo.setGender(1);
		Organization resultOrg = organizationService.selectByExtId(adUser.getDepartment(),tenantId);
		userInfo.setOrgCode(resultOrg.getCode());
		userInfo.setOrgId(resultOrg.getId());
		userInfo.setMstrUser(0);

		user.setUsername(userInfo.getUsername());
		user.setPassword(userInfo.getPassword());
		user.setRegisterTime(new Date());
		user.setResetKey("0");
		user.setState(0);
		user.setPwdExpireTime(userInfo.getPwdExpireTime());
		user.setActivateStartTime(userInfo.getActivateStartTime());
		user.setActivateEndTime(userInfo.getActivateEndTime());
		user.setIsDelete(0);
		boolean result = user.insert();
		if (result) {
			userInfo.setId(user.getId());
			userInfo.setAvatar("/images/avatar.jpg");
			userInfo.setCode(userInfo.getUsername());
			userInfo.setPreferenceType("orgHome");//默认首选项
			userInfo.setLocale(configLocale);//默认语言
			boolean resultInfo = userInfo.insert();
			boolean resultUserOrg = false;
			if(resultInfo){
				List<Organization> userOrgs = userInfo.getUserOrgs();
				if(userOrgs != null && userOrgs.size() > 0){
					//只有 兼职组织。在新建中主组织以用户信息字段中携带的org_id为准（因为新建的时候只能是主组织）
					//兼容组织是后新增的，这个list是上传后根据兼容组织code查询写入的。
					UserOrganization userOrgDepyty = null;
					for (Organization org : userOrgs) {
						userOrgDepyty = new UserOrganization();
						userOrgDepyty.setUserId(user.getId());
						userOrgDepyty.setOrgId(org.getId());
						userOrgDepyty.setIsDeputy(1);
						userOrganizationMapper.insert(userOrgDepyty);
					}
				}
				//直接将用户传入的组织id写入到组织与用户关系表中（主组织）
				UserOrganization userOrg = new UserOrganization();
				userOrg.setUserId(user.getId());
				userOrg.setOrgId(userInfo.getOrgId());
				userOrg.setIsDeputy(0);
				resultUserOrg = userOrganizationMapper.insert(userOrg)!=1?false:true;
			}

			//添加角色
			Role role = new Role();
			EntityWrapper<Role> wrapper = new EntityWrapper<>();
			role.setCode(roleCode);
			role.setTenantId(tenantId);
			wrapper.setEntity(role);
			Role resultRole = role.selectOne(wrapper);
			if(resultRole==null && roleCode.equals("admin_system")){
				role.setCode("admin");
				resultRole = role.selectOne(wrapper);
			}
			RoleUser roleUser = new RoleUser();
			roleUser.setRoleCode(resultRole.getCode());
			roleUser.setRoleId(resultRole.getId());
			roleUser.setUserId(user.getId());
			roleUser.insert();
			//同步创建mstr用户 （注：如果是同步创建mstr用户的话，默认用户与项目是一队一的关系。一个用户只能在一个server中创建用户，并且只能在一个项目中有自主分析地址）
			if (userInfo.getMstrUser()!=null && userInfo.getMstrUser().intValue()==1) {//同步创建mstr用户 | 参数：serverId，projectId，username，password
				//获取server和project
				BiServer biserver = biServerMapper.selectById(userInfo.getMstrServerId());
				BiProject biproject = biProjectMapper.selectById(userInfo.getMstrProjectId());
				boolean isSucc = searchAndCreateMstrUserAndFolder(userInfo,biserver,biproject);
				System.out.println("创建mstr用户："+isSucc);
				logger.error("创建mstr用户："+isSucc);
				if(userInfo.getRemberMstrChoice()!=null && "1".equals(userInfo.getRemberMstrChoice())){//记住mstr选项
					//设置is_indepen_pro,所有的mstr项目中只能有一个自动同步的默认项目
					biProjectMapper.updateProjectIndepend(biproject.getId());
				}
				if (isSucc) {
					return result && resultInfo && resultUserOrg;
				}
			}
			return result && resultInfo && resultUserOrg;

		}

		return false;
	}


	@Transactional
	@Override
	public void updateADUser(String username,String password,String tenantId){
		try{
			String DOMAIN_IP  = configService.findByCode("DOMAIN_IP",tenantId).getValue();
			String DOMAIN_NAME  = configService.findByCode("DOMAIN_NAME",tenantId).getValue();
			AdEntity adEntity = new AdEntity();
			adEntity.setDomain(DOMAIN_NAME);
			adEntity.setLdapIP(DOMAIN_IP);
			adEntity.setUsername(username);
			adEntity.setPassword(Encrypt(password));
			adEntity.setLdapPORT("389");
			AdUser adUser = ADUtils.getAdUser(adEntity);
			EntityWrapper<User> ew = new EntityWrapper<>();
			User query = new User();
			query.setExtId(adUser.getId());
			query.setTenantId(tenantId);
			ew.setEntity(query);
			User result = query.selectOne(ew);
			if(result!=null && !"".equals(result)){
				String userId = result.getId();
				userMapper.updateADUser(PasswordUtils.hash(Encrypt(password)),userId,tenantId);
				Organization org = new Organization();
				org.setExtId(adUser.getDepartment());
				org.setTenantId(tenantId);
				EntityWrapper<Organization> entityWrapper = new EntityWrapper<>();
				entityWrapper.setEntity(org);
				Organization resultOrg= org.selectOne(entityWrapper);
				if(resultOrg==null){
					organizationService.importOrgForAD(tenantId);
					resultOrg= org.selectOne(entityWrapper);
				}
				UserInfo userInfo = new UserInfo();
				userInfo.setPhone(adUser.getTelephone());
				userInfo.setEmail(adUser.getMail());
				userInfo.setRealname(adUser.getDisplayName());
				userInfo.setOrgId(resultOrg.getId());
				userInfo.setOrgCode(resultOrg.getCode());
				userInfo.setId(userId);
				userInfoMapper.updateADUser(userInfo);

				//查询用户组织，看主组织是否有变化
				List<Organization> userOrgs = userOrganizationMapper.selectOrgbyUserId(userId);
				if(resultOrg.getId()!=null && resultOrg.getId().length()>0){
					//新组织与主组织相同不修改，新组织不是主组织也不是兼容组织 直接修改主组织
					//兼职组织与修改后的组织相同，需要将原主组织换成兼职组织，兼职组织换成主
					boolean isSameMainOrg = false;
					boolean isSameDeputyOrg = false;
					String oldOrgId = "";
					for (Organization organization : userOrgs) {
						if(organization.getIsDeputy() == 0){
							oldOrgId = organization.getId();
						}
						if(organization.getIsDeputy() == 0 && organization.getId().equals(resultOrg.getId())){
							isSameMainOrg = true;
						}
						if(organization.getIsDeputy() != 0 && organization.getId().equals(resultOrg.getId())){
							isSameDeputyOrg = true;
						}
					}
					if(!isSameMainOrg && !isSameDeputyOrg){//新组织既不是原主组织也不是原兼职组织
						//修改主组织关联表信息
						EntityWrapper<UserOrganization> ewuo = new EntityWrapper<>();
						ew.eq("user_id", userId);
						ew.eq("is_deputy", 0);
						UserOrganization userOrg = new UserOrganization();
						userOrg.setOrgId(resultOrg.getId());
						userOrganizationMapper.update(userOrg, ewuo);
					}else if(!isSameMainOrg && isSameDeputyOrg){//新组织不是原主组织但是原兼职组织之一
						//修改兼职组织关联为主
						EntityWrapper<UserOrganization> ewNew = new EntityWrapper<>();
						ewNew.eq("user_id", userId);
						ewNew.eq("org_id", resultOrg.getId());
						UserOrganization userOrgNew = new UserOrganization();
						userOrgNew.setIsDeputy(0);
						userOrganizationMapper.update(userOrgNew, ewNew);
						//修改主组织关联为兼职
						EntityWrapper<UserOrganization> ewOld = new EntityWrapper<>();
						ewOld.eq("user_id", userId);
						ewOld.eq("org_id", oldOrgId);
						UserOrganization userOrgOld = new UserOrganization();
						userOrgOld.setIsDeputy(1);
						userOrganizationMapper.update(userOrgOld, ewOld);
					}
				}
			}
		}catch (Exception e){
			logger.info(e.getMessage());
		}

	}




	@Override
	public List<UserInfo> selectUser(UserInfo query) {
		return userInfoMapper.selectUser(query);
	}
	@Override
	public int insertList(List<UserInfo> userInfoList) {
		return userInfoMapper.insertList(userInfoList);
	}
	@Override
	public PageModel<UserInfo> pageRoleUser(UserInfo query) {
		Page<UserInfo> page = new Page<UserInfo>(query.getPageNumber(),query.getPageSize());
		page.setRecords(userInfoMapper.findRoleUserList(page, query));
		return new PageModel<UserInfo>(page);
	}
	
	private boolean searchAndCreateMstrUserAndFolder(UserInfo record,BiServer biServer, BiProject biProject) {
		boolean isSuccess = false;
		try {
			//查看用户是否已经创建
			WebUser search = UserApi.search(biServer, record.getUsername());
			String mstrUserId = search!=null?search.getID():null;
			//存在，获取Id，不存在创建获取id。存储到t_bi_user表中
			BiUser biUser = createOrReuseMstrUser(biServer, record, mstrUserId);
			biUserMapper.insert(biUser);
			//存储映射关系t_bi_mapping
			BiMapping mapping = new BiMapping();
			mapping.setUserId(record.getId());
			mapping.setBiUserId(biUser.getId());
			mapping.setType(biServer.getTypeName());
			biMappingMapper.insert(mapping);
			//查询自主分析文件夹是否存在 |使用新创建的用户创建文件夹
//			WebIServerSession serverSessionNew = MstrSessionFactory.getSession(mstrProject,userInfo.getUsername(), defaultPassword);
//          String folder = FolderApi.createFolder(serverSessionNew, userInfo.getUsername());
			biProject.setDefaultUid(record.getUsername());
			biProject.setDefaultPwd(defaultPassword);
			biProject.setIp(biServer.getServer());
			biProject.setPort(biServer.getPort());
			String mstrFolderId = FolderApi.searchFolder(biProject, record.getUsername());
			//不存在创建获取id，存在获取id
			BiIndependent independ = createOrReuseMstrFolder(biServer, biUser, biProject, record, mstrFolderId);
			//存入t_bi_independent表中
			independMapper.insert(independ);
			isSuccess = true;
		} catch (Exception e) {
			// TODO: handle exception
			isSuccess = false;
		}
		return isSuccess;
	}
	
	private BiUser createOrReuseMstrUser(BiServer biServer,UserInfo record,String mstrUserId){
		BiUser biUser = new BiUser();
		biUser.setPassword(defaultPassword);
		biUser.setUsername(record.getUsername());
		biUser.setBiServerId(biServer.getId());
		biUser.setType(String.valueOf(BiServer.TYPE_MSTR));
		if(mstrUserId!=null){
			biUser.setBiObjectId(mstrUserId);
		}else{
			String objectId = UserApi.createUserAndGrant(biServer, biUser);
			biUser.setBiObjectId(objectId);
		}
		logger.error("创建biuser成功");
		return biUser;
	}
	
	private BiIndependent createOrReuseMstrFolder(BiServer biServer,BiUser biUser, BiProject biProject, UserInfo record,String mstrFolderId){
		BiIndependent biIndep = new BiIndependent();
		biIndep.setBiUserId(biUser.getId());
		biIndep.setBiProjectId(biProject.getId());
		biIndep.setBiServerId(biServer.getId());
		biIndep.setBiOwnFolderName(record.getUsername());
		biIndep.setCreateTime(new Date());
		biIndep.setCreater(SessionUtil.getUserId());
		if(mstrFolderId!=null){
			biIndep.setBiOwnFolderId(mstrFolderId);
		}else{
			mstrFolderId = FolderApi.createFolder(biProject, record.getUsername());
			biIndep.setBiOwnFolderId(mstrFolderId);
		}
		return biIndep;
	}
	
	@Override
	public  boolean updatePersonnalRecordById(UserInfo userInfo){
		return userInfoMapper.updatePersonnalRecordById(userInfo);
	}

	private String Encrypt(String password) throws Exception {
		// TODO Auto-generated method stub
		String pwd ;
		byte[] en_result = hexStringToBytes(password);
		byte[] de_result = RSAUtils.decrypt(RSAUtils.getKeyPair().getPrivate(),
				en_result);
		StringBuffer sb = new StringBuffer();
		sb.append(new String(de_result));
		pwd = sb.reverse().toString();
		password = URLDecoder.decode(pwd,"UTF-8");
		return password;
	}
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();

		int length = hexString.length() / 2;

		char[] hexChars = hexString.toCharArray();

		byte[] d = new byte[length];

		for (int i = 0; i < length; i++) {

			int pos = i * 2;

			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

		}
		return d;
	}
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
