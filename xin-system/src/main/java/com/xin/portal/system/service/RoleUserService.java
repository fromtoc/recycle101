package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.model.UserInfo;

public interface RoleUserService extends IService<RoleUser> {

	List<String> findRoleByUser(RoleUser query);

	boolean saveRoleUsersByUserId(String userId, String[] roleIds);

	boolean saveRoleUsersByRoleId(String roleId, String roleCode, String[] userIds);

	int delete(RoleUser query);

	void deleteByRoleId(String roleId);

	List<UserInfo> findUsersByRole(RoleUser roleUser);
	/**对比修改用户的角色前后菜单的变化  userId：被修改用户ID； roleIds修改角色后的id */
	void compareAfterUpdateRoleMenuChangeAddMessage(String userId,String[] roleIds);
	/**对比修改角色中用户前后菜单的变化  roleId：被修改的角色； userIds 角色中删除的用户ID */
	void compareAfterUpdateUsersRoleMenuChangeAddMessage(String roleId, String[] userIds, boolean isDelete);
	
	

}
