package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.model.OrgDataPermission;

public interface OrgDataPermissionService extends IService<OrgDataPermission> {
	
	boolean saveOrgDataPermission(String orgId, String[] dataPermissions);
	
}
