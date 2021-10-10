package com.xin.portal.system.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.ServiceStateMapper;
import com.xin.portal.system.mapper.ServiceStateRecordMapper;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.model.ServiceStateRecord;
import com.xin.portal.system.service.ServiceStateRecordService;
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
public class ServiceStateRecordServiceImpl extends ServiceImpl<ServiceStateRecordMapper, ServiceStateRecord> implements ServiceStateRecordService {
	
	@Autowired
	private ServiceStateRecordMapper mapper;
	
	@Override
	public PageModel<ServiceStateRecord> findlist(ServiceStateRecord query) {
		Page<ServiceStateRecord> page = new Page<ServiceStateRecord>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.findList(page,query));
		return new PageModel<ServiceStateRecord>(page);
	}

	@Override
	public boolean deleteRecord(ServiceStateRecord record) {
		return mapper.deleteRecord(record) > 0 ? true:false;
	}

}
