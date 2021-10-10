package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.Permission;
import com.xin.portal.system.mapper.PermissionMapper;
import com.xin.portal.system.service.PermissionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2018-11-15
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
	@Override
	public PageModel<Permission> page(Permission query, Integer pageNumber, Integer pageSize) {
		Page<Permission> page = new Page<Permission>(pageNumber, pageSize);
		EntityWrapper<Permission> ew=new EntityWrapper<Permission>(query);
		page = selectPage(page, ew);
		return new PageModel<Permission>(page);
	}
}
