package com.xin.portal.system.service.impl;

import java.util.List;

import com.xin.portal.system.model.OrganizationModule;
import com.xin.portal.system.mapper.OrganizationModuleMapper;
import com.xin.portal.system.service.OrganizationModuleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author xue
 * @since 2018-11-08
 */
@Service
public class OrganizationModuleServiceImpl extends ServiceImpl<OrganizationModuleMapper, OrganizationModule> implements OrganizationModuleService {
	@Autowired
	private  OrganizationModuleMapper  mapper;
	@Override
	public PageModel<OrganizationModule> page(OrganizationModule query, Integer pageNumber, Integer pageSize) {
		Page<OrganizationModule> page = new Page<OrganizationModule>(pageNumber, pageSize);
		EntityWrapper<OrganizationModule> ew=new EntityWrapper<OrganizationModule>(query);
		page = selectPage(page, ew);
		return new PageModel<OrganizationModule>(page);
	}

	@Override
	public List<OrganizationModule> getModuleByOrgId(String id) {
		// TODO Auto-generated method stub
		return mapper.getModuleByOrgId(id);
	}
}
