package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.model.UserOrganization;

public interface UserOrganizationService extends IService<UserOrganization>{

	List<Organization> selectOrgbyUserId(String userId);

	BaseApi insertOrUpdateDeputyOrg(String userId, List<String> orgIds);
	
}
