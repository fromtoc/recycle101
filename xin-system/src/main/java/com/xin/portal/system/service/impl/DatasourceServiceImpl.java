package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.DatasourceMapper;
import com.xin.portal.system.model.Datasource;
import com.xin.portal.system.service.DatasourceService;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2020-04-21
 */
@Service
public class DatasourceServiceImpl extends ServiceImpl<DatasourceMapper, Datasource> implements DatasourceService {
	@Override
	public PageModel<Datasource> page(Datasource query, Integer pageNumber, Integer pageSize) {
		Page<Datasource> page = new Page<Datasource>(pageNumber, pageSize);
		EntityWrapper<Datasource> ew=new EntityWrapper<Datasource>();
		ew.like(StringUtils.isNotEmpty(query.getDatabaseName()), "database_name", query.getDatabaseName());
		page = selectPage(page, ew);
		return new PageModel<Datasource>(page);
	}
}
