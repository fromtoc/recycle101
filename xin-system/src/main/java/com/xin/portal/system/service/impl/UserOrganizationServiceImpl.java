package com.xin.portal.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.mapper.UserOrganizationMapper;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.model.UserOrganization;
import com.xin.portal.system.service.UserOrganizationService;

@Service
public class UserOrganizationServiceImpl extends ServiceImpl<UserOrganizationMapper, UserOrganization> implements UserOrganizationService{

	@Autowired
	private UserOrganizationMapper mapper;
	
	@Override
	public List<Organization> selectOrgbyUserId(String userId) {
		return mapper.selectOrgbyUserId(userId);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BaseApi insertOrUpdateDeputyOrg(String userId, List<String> orgIds) {
		//删除用户的所有兼职组织
		mapper.deleteDeputyOrgByUserId(userId, 1);
		//将ids插入到兼职组织中（ids 为空则不添加）
		if(orgIds != null && orgIds.size() > 0){
			List<UserOrganization> list = new ArrayList<UserOrganization>();
			UserOrganization userOrg = null;
			for (String orgId : orgIds) {
				userOrg = new UserOrganization();
				userOrg.setOrgId(orgId);
				userOrg.setUserId(userId);
				userOrg.setIsDeputy(1);
				list.add(userOrg);
			}
			insertBatch(list, 30);
		}
		return BaseApi.success();
	}
	
}
