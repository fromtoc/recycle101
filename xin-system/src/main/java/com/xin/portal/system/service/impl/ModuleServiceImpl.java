package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.Module;
import com.xin.portal.system.model.User;
import com.xin.portal.system.mapper.ModuleMapper;
import com.xin.portal.system.service.ModuleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.bean.PageModel;

/**
 * 模板基础信息表 服务实现类
 *
 * @author xue
 * @since 2018-11-08
 */
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module> implements ModuleService {
	@Autowired
	private ModuleMapper moduleMapper;
	@Override
	public PageModel<Module> page(Module query, Integer pageNumber, Integer pageSize) {
		Page<Module> page = new Page<Module>(query.getPageNumber(),query.getPageSize());
		page.setRecords(moduleMapper.selectPage(page, query));
		return new PageModel<Module>(page);
	}
}
