package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.RoleModule;
import com.xin.portal.system.mapper.RoleModuleMapper;
import com.xin.portal.system.service.RoleModuleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
public class RoleModuleServiceImpl extends ServiceImpl<RoleModuleMapper, RoleModule> implements RoleModuleService {
	@Override
	public PageModel<RoleModule> page(RoleModule query, Integer pageNumber, Integer pageSize) {
		Page<RoleModule> page = new Page<RoleModule>(pageNumber, pageSize);
		EntityWrapper<RoleModule> ew=new EntityWrapper<RoleModule>(query);
		page = selectPage(page, ew);
		return new PageModel<RoleModule>(page);
	}
}
