package com.xin.portal.system.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.ServiceStateMapper;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.service.ServiceStateService;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author songyi
 * @since 2018-12-11
 */
@Service
public class ServiceStateServiceImpl extends ServiceImpl<ServiceStateMapper, ServiceState> implements ServiceStateService {
	
	@Autowired
	private ServiceStateMapper mapper;

	@Override
	public PageModel<ServiceState> page(ServiceState query) {
		Page<ServiceState> page = new Page<ServiceState>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.findList(page,query));
		return new PageModel<ServiceState>(page);
	}
	
	@Override
	public PageModel<ServiceState> statePage(ServiceState query) {
		Page<ServiceState> page = new Page<ServiceState>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.findStateList(page,query));
		return new PageModel<ServiceState>(page);
	}

	@Override
	public boolean updateState(ServiceState record) {
		return mapper.updateState(record) > 0 ? true:false;
	}

	@Override
	public boolean updateIsSentMail(Integer isSentMail, String id) {
		return mapper.updateIsSentMail(isSentMail,id) > 0 ? true:false;
	}

	@Override
	public boolean updateServiceState(ServiceState record) {
		return mapper.updateServiceState(record) > 0 ? true:false;
	}

	@Override
	public boolean updateConfig(ServiceState record) {
		return mapper.updateConfig(record) > 0 ? true:false;
	}

	@Override
	public boolean insertRecord(ServiceState record) {
		return mapper.insertRecord(record) > 0 ? true:false;
	}

	@Override
	public boolean deleteRecord(ServiceState record) {
		return mapper.deleteRecord(record) > 0 ? true:false;
	}

}
