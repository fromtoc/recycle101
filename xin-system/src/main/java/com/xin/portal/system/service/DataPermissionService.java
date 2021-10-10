package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.DataPermission;
import com.xin.portal.system.model.OrgDataPermission;

public interface DataPermissionService extends IService<DataPermission> {
	
	PageModel<DataPermission> page(DataPermission query);

	PageModel<OrgDataPermission> pageByOrg(OrgDataPermission query);

	int deleteByOrg(OrgDataPermission query);
	
	List<DataPermission> findByParams(String orgId, String dbtype, String dbname);

}
