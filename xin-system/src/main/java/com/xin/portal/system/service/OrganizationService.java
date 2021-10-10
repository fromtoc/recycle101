package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.util.AdUtils.entity.AdDepartment;

public interface OrganizationService  extends IService<Organization> {

    void importOrgForAD(String tenantId);

    Organization selectByExtId(String extId,String tenantId);
	
}
