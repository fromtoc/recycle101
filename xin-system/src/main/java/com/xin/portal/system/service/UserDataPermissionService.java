package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.UserDataPermission;

public interface UserDataPermissionService extends IService<UserDataPermission> {

	PageModel<UserDataPermission> pageUserDataPermission(UserDataPermission query);

	int deleteByUser(UserDataPermission query);

	boolean saveUserDataPermission(String userId, String[] dataPermissions);

	Integer deleteUserPerOfOrg(String orgId, String[] dataPermissions);
	
}
