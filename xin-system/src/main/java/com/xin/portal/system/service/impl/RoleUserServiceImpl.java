package com.xin.portal.system.service.impl;

import java.util.*;

import com.xin.portal.system.mapper.*;
import com.xin.portal.system.model.*;

import com.xin.portal.system.util.webSocketLog.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xin.portal.system.service.RoleUserService;
import com.xin.portal.system.util.SessionUtil;

@Service
public class RoleUserServiceImpl extends ServiceImpl<RoleUserMapper, RoleUser> implements RoleUserService {
	
	private Logger logger = LoggerFactory.getLogger(RoleUserServiceImpl.class);
	
	@Autowired
	private RoleUserMapper mapper;

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private MessageCenterMapper messageCenterMapper;
	@Autowired
	private UserMapper userMapper;
	
	@Override
	public List<String> findRoleByUser(RoleUser query) {
		return mapper.findRoleByUser(query);
	}

	@Transactional
	@Override
	public boolean saveRoleUsersByUserId(String userId, String[] roleIds) {
		//删除所有该用户与角色的信息
		Map<String, Object> map = new HashMap<>();
		map.put("user_id", userId);
		deleteByMap(map);
		//查询出租户对应的role表中的所有人EVERYONE的roleId，将roleId添加到批量插入的list中
		Role query = new Role();
		query.setCode("EVERYONE");
		List<String> roleIdsList = new ArrayList<>();
		roleIdsList.add(roleMapper.selectOne(query).getId());
		//将用户选择的roleId添加到批量插入的list中
		if(roleIds!=null && roleIds.length>0){
			for(String id :roleIds){
				roleIdsList.add(id);
			}
		}
		//操作数据库
		List<RoleUser> list = Lists.newArrayList();
		for (String roleId : roleIdsList) {
			RoleUser roleUser = new RoleUser();
			roleUser.setRoleId(roleId);
			roleUser.setUserId(userId);
			list.add(roleUser);
		}

		return insertBatch(list);
	}

	@Override
	public boolean saveRoleUsersByRoleId(String roleId, String roleCode,
			String[] userIds) {
		List<RoleUser> list = Lists.newArrayList();
		for (String userId : userIds) {
			RoleUser roleUser = new RoleUser();
			roleUser.setRoleId(roleId);
			roleUser.setUserId(userId);
			roleUser.setRoleCode(roleCode);
			list.add(roleUser);
		}
		return insertBatch(list);
	}

	@Override
	public int delete(RoleUser query) {
		return mapper.delete(query);
	}

	@Override
	public void deleteByRoleId(String roleId) {
		mapper.deleteByRoleId(roleId);
	}

	@Override
	public List<UserInfo> findUsersByRole(RoleUser roleUser) {
		return mapper.findUsersByRole(roleUser);
	}

	@Override
	public void compareAfterUpdateRoleMenuChangeAddMessage(String userId, String[] roleIds) {
		try {
			//由于roleCode 为everyone的角色不会回传需要手动输入。
			//获取roleCode 为everyone的角色id 
			EntityWrapper<Role> ewru = new EntityWrapper<>();
			ewru.where("upper(code) = 'EVERYONE'");
			Role rl = roleMapper.selectList(ewru).get(0);
			//通过userId 查询到用户当前的角色
			RoleUser roleUser = new RoleUser();
			roleUser.setUserId(userId);
			roleIds = putToArray(roleIds, rl.getId());
			List<String> roleList0 = mapper.findRoleByUser(roleUser);
			List<String> roleList1 = (roleIds!=null&&roleIds.length>0)?Arrays.asList(roleIds):null;
			//对比，如果没有变化则菜单没有变
			boolean isEqule = isListEqual(roleList0, roleList1);
			//如果有变化
			if(!isEqule){
				//查询出变化前后的资源，对比找出失去的和添加的资源权限（view）
				List<String> resourceList0 = (roleList0!=null&&roleList0.size()>0)?rolePermissionMapper.findDistinctResourceIdByRoleId(roleList0,Permission.VIEW):null;//原有的资源
				List<String> resourceList1 = (roleList1!=null&&roleList1.size()>0)?rolePermissionMapper.findDistinctResourceIdByRoleId(roleList1,Permission.VIEW):null;//修改后的资源
				Map<String, List<String>> diffrent = getDiffrent(resourceList0, resourceList1); 
				List<String> deleteResource = diffrent.get("list1");//被删除的权限
				List<String> addResource = diffrent.get("list2");//新增的权限
				//查看资源是否表现在菜单中，将表现在菜单中的资源找出来
				List<Menu> deleteMenus = (deleteResource!=null&&deleteResource.size()>0)?menuMapper.findMenuFromResourceIds(deleteResource):null;
				List<Menu> addMenus = (addResource!=null&&addResource.size()>0)?menuMapper.findMenuFromResourceIds(addResource):null;
				//将这些信息存入消息中心
				if((deleteMenus!=null && deleteMenus.size()>0) || (addMenus!=null && addMenus.size()>0)){
					MessageCenter msg = new MessageCenter();
					msg.setTitle("权限变更通知");
					msg.setIsRead(0);
					msg.setCreateTime(new Date());
					msg.setReceiveUser(userId);
					msg.setProduceUser(SessionUtil.getUserId());
					msg.setType(MessageCenter.NOTICE_MESSAGE);
					String content = "";
					if(deleteMenus!=null && deleteMenus.size()>0){
						for (int i = 0; i < deleteMenus.size(); i++) {
							content +=(i!=deleteMenus.size()-1)?"[" + deleteMenus.get(i).getName() +"],":"[" + deleteMenus.get(i).getName() +"]";
						}
						msg.setContent("您失去了菜单"+content+"的访问权限");
					}
					if(addMenus!=null && addMenus.size()>0){
						for (int i = 0; i < addMenus.size(); i++) {
							content +=(i!=addMenus.size()-1)?"[" + addMenus.get(i).getName() +"],":"[" + addMenus.get(i).getName() +"]";
						}
						msg.setContent("您得到了菜单"+content+"的访问权限");
					}
					messageCenterMapper.insert(msg);
					WebSocketServer web=new WebSocketServer();
					web.setonMessage("4",userId);
				}
			}

		} catch (Exception e) {
			logger.error("组织管理用户编辑角色--添加消息中心消息失败！！！");
			e.printStackTrace();
		}
	}
	
	@Override
	public void compareAfterUpdateUsersRoleMenuChangeAddMessage(String roleId, String[] userId, boolean isDelete) {
		try {
			//通过角色查询到角色的资源权限
			List<String> deleteOrAddResource = rolePermissionMapper.findDistinctResourceIdByRoleId(Arrays.asList(new String[]{roleId}),Permission.VIEW);
			//遍历用户获取到用户各个用户的资源权限（除了roleId以外的权限）
			Map<String, List<String>> userResourceMap = new HashMap<>();
			for (int i = 0; i < userId.length; i++) {
				List<String> userResourceList = rolePermissionMapper.findDistinctResourceIdByUserIdAndNotEqualRoleId(roleId,userId[i],Permission.VIEW);
				userResourceMap.put(userId[i], userResourceList);
			}
			//对比找到减少或者增加的权限
			for (Map.Entry<String, List<String>> entry : userResourceMap.entrySet()) {
				List<String> userResource = entry.getValue();
				Map<String, List<String>> diffrent = getDiffrent(deleteOrAddResource, userResource); 
				List<String> deleteOrAddList = diffrent.get("list1");
				//找到资源表现的菜单
				List<Menu> deleteOrAddMenus = (deleteOrAddList!=null&&deleteOrAddList.size()>0)?menuMapper.findMenuFromResourceIds(deleteOrAddList):null;
				//记录变更信息到消息中心
				if(deleteOrAddMenus!=null && deleteOrAddMenus.size()>0){
					MessageCenter msg = new MessageCenter();
					msg.setTitle("权限变更通知");
					msg.setIsRead(0);
					msg.setCreateTime(new Date());
					msg.setReceiveUser(entry.getKey());
					msg.setProduceUser(SessionUtil.getUserId());
					msg.setType(MessageCenter.NOTICE_MESSAGE);
					String content = isDelete==true?"您失去了菜单":"您得到了菜单";
					for (int i = 0; i < deleteOrAddMenus.size(); i++) {
						content +=(i!=deleteOrAddMenus.size()-1)?"[" + deleteOrAddMenus.get(i).getName() +"],":"[" + deleteOrAddMenus.get(i).getName() +"]";
					}
					msg.setContent(content+"的访问权限");
					messageCenterMapper.insert(msg);
					WebSocketServer web=new WebSocketServer();
					web.setonMessage("4",msg.getReceiveUser());
				}
			}

		} catch (Exception e) {
			logger.error("角色管理角色编辑角色--添加消息中心消息失败！！！");
			e.printStackTrace();
		}
		
	} 
	
	private boolean isListEqual(List<String> l0, List<String> l1){
		boolean flag = false;
		if((l0==null||l0.size()<=0) && (l1==null||l1.size()<=0)){
			return true;
		}else if((l0==null||l0.size()<=0)){
			return false;
		}else if((l1==null||l1.size()<=0)){
			return false;
		}else{
			boolean flag1 = l0.containsAll(l1);
			boolean flag2 = l1.containsAll(l0);
			flag = flag1 && flag2;
		}
		return flag;
	}
	
	private String[] putToArray(String[] arr,String params){
		if(arr!=null && arr.length>0){
			String[] newArr = new String[arr.length+1];
			System.arraycopy(arr, 0, newArr, 0, arr.length);
			newArr[newArr.length-1] = params;
			return newArr;
		}
		return new String[]{params};
	}
	
	private Map<String, List<String>> getDiffrent(List<String> list1, List<String> list2) { 
		Map<String, List<String>> diff = new HashMap<>();
		String diffString1 = "list1"; //max  对应于参数中的list1
	    String diffString2 = "list2"; //min  对应于参数中的list2
		if((list1!=null && list1.size()>0) && (list2!=null && list2.size()>0)){
	        List<String> diff1 = new ArrayList<String>();  
	        List<String> diff2 = new ArrayList<String>(); 
	        List<String> maxList = list1;  
	        List<String> minList = list2;  
	        if(list2.size() > list1.size()){  
	             maxList = list2;  
	             minList = list1; 
	             diffString1 = "list2";
	             diffString2 = "list1";
	        }  
	        Map<String,Integer> map = new HashMap<String,Integer>(maxList.size());  
	        for (String string : maxList) {  
	             map.put(string, 1);  
	        }  
	        for (String string : minList) {  
	        	if(map.get(string)!=null){  
	                 map.put(string, 2);  
	                 continue;  
	            }  
	        	diff1.add(string); //得到min中的不同值
	        }  
	        diff.put(diffString2, diff1);
	        for(Map.Entry<String, Integer> entry:map.entrySet()) {  
	        	if(entry.getValue()==1) {  
	        		diff2.add(entry.getKey()); //得到max中的不同值 
	            }  
	        }  
	        diff.put(diffString1, diff2);
		}
        else {
        	if(list1!=null && list1.size()>0){
        		diff.put(diffString1, list1);
        	}
        	if(list2!=null && list2.size()>0){
        		diff.put(diffString2, list2);
        	}
		}
        return diff;  
    }

}
