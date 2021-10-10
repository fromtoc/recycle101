package com.xin.portal.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.UserRecordMapper;
import com.xin.portal.system.model.UserRecord;
import com.xin.portal.system.service.UserRecordService;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-12
 */
@Service
public class UserRecordServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements UserRecordService {
	
	@Autowired
	private UserRecordMapper mapper;
	
	@Override
	public PageModel<UserRecord> page(UserRecord query) {
		Page<UserRecord> page = new Page<UserRecord>(query.getPageNumber(),query.getPageSize());
//		EntityWrapper<UserRecord> ew=new EntityWrapper<UserRecord>(query);
//		page = selectPage(page, ew);
		page.setRecords(mapper.findList(query));
		return new PageModel<UserRecord>(page);
	}

	@Override
	public List<UserRecord> findList(UserRecord query) {
		return mapper.findList(query);
		
	}

	@Override
	public PageModel<UserRecord> pageShare(UserRecord query) {
		Page<UserRecord> page = new Page<UserRecord>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.findShareList(query));
		return new PageModel<UserRecord>(page);
	}

	@Override
	public int updateIntroduce(Map map) {
		return mapper.updateIntroduce(map);
	}
}
