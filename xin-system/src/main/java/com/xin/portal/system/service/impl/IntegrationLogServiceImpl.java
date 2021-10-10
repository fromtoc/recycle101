package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.IntegrationLog;
import com.xin.portal.system.mapper.IntegrationLogMapper;
import com.xin.portal.system.service.IntegrationLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2018-11-27
 */
@Service
public class IntegrationLogServiceImpl extends ServiceImpl<IntegrationLogMapper, IntegrationLog> implements IntegrationLogService {
	
	@Autowired
	private IntegrationLogMapper mapper;
	
	@Override
	public PageModel<IntegrationLog> page(IntegrationLog query, Integer pageNumber, Integer pageSize) {
		Page<IntegrationLog> page = new Page<IntegrationLog>(pageNumber, pageSize);
		EntityWrapper<IntegrationLog> ew=new EntityWrapper<IntegrationLog>(query);
		page = page.setRecords(mapper.findPage(page, query));
		//page = selectPage(page, ew);
		return new PageModel<IntegrationLog>(page);
	}
}
