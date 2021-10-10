package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.ListManageResource;
import com.xin.portal.system.mapper.ListManageResourceMapper;
import com.xin.portal.system.service.ListManageResourceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author xue
 * @since 2018-12-11
 */
@Service
public class ListManageResourceServiceImpl extends ServiceImpl<ListManageResourceMapper, ListManageResource> implements ListManageResourceService {
	@Override
	public PageModel<ListManageResource> page(ListManageResource query, Integer pageNumber, Integer pageSize) {
		Page<ListManageResource> page = new Page<ListManageResource>(pageNumber, pageSize);
		EntityWrapper<ListManageResource> ew=new EntityWrapper<ListManageResource>(query);
		page = selectPage(page, ew);
		return new PageModel<ListManageResource>(page);
	}
}
