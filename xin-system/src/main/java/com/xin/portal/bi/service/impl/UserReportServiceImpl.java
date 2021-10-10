package com.xin.portal.bi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.bi.mapper.UserReportMapper;
import com.xin.portal.bi.model.UserReport;
import com.xin.portal.bi.service.UserReportService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.UserShare;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-06-05
 */
@Service
public class UserReportServiceImpl extends ServiceImpl<UserReportMapper, UserReport> implements UserReportService {

	@Autowired
	private UserReportMapper mapper;
	
	@Override
	public PageModel<UserReport> page(UserReport query) {
		Page<UserReport> page = new Page<UserReport>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.findList(page, query));
		return new PageModel<UserReport>(page);
	}
	
	@Override
	public PageModel<UserReport> pageShare(UserReport query) {
		Page<UserReport> page = new Page<UserReport>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.findShareList(page, query));
		return new PageModel<UserReport>(page);
	}

	@Override
	public boolean updateNum(String id, String colName, String methodType) {
		return mapper.updateNum(id,colName,methodType) > 0 ? true : false;
	}
	
	@Override
	public UserShare findReportConmmont(UserShare uquery){
		return mapper.findReportConmmont(uquery);
	}

}
