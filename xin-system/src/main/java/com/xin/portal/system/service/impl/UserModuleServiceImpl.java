package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.UserModule;
import com.xin.portal.system.mapper.UserModuleMapper;
import com.xin.portal.system.service.UserModuleService;
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
public class UserModuleServiceImpl extends ServiceImpl<UserModuleMapper, UserModule> implements UserModuleService {
	@Override
	public PageModel<UserModule> page(UserModule query, Integer pageNumber, Integer pageSize) {
		Page<UserModule> page = new Page<UserModule>(pageNumber, pageSize);
		EntityWrapper<UserModule> ew=new EntityWrapper<UserModule>(query);
		page = selectPage(page, ew);
		return new PageModel<UserModule>(page);
	}
}
