package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.model.RoleResource;

public interface RoleResourceService extends IService<RoleResource> {

	int saveByRoleId(String roleId, String[] ids);

	int saveByResId(String resId, String[] resIds, String[] delRoleIds,
			String[] saveRoleIds);


}
