package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.IssueMapper;
import com.xin.portal.system.mapper.IssueRecordMapper;
import com.xin.portal.system.model.Issue;
import com.xin.portal.system.model.IssueRecord;
import com.xin.portal.system.service.IssueRecordService;
import com.xin.portal.system.util.SessionUtil;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2018-12-03
 */
@Service
public class IssueRecordServiceImpl extends ServiceImpl<IssueRecordMapper, IssueRecord> implements IssueRecordService {
	
	@Autowired
	private IssueRecordMapper mapper;
	@Autowired
	private IssueMapper issueMapper;
	
	@Override
	public PageModel<IssueRecord> page(IssueRecord query) {
		Page<IssueRecord> page = new Page<IssueRecord>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findPageList(page,query));
		return new PageModel<IssueRecord>(page);
	}

	@Override
	public boolean insertRecord(IssueRecord record) {
		String userId = SessionUtil.getUserId();
		record.setCreater(userId);
		record.setCreateTime(new Date());
		//修改问题状态
		Issue issue = new Issue();
		issue.setId(record.getIssueId());
		issue.setState(2);
		issue.setUpdater(userId);
		issue.setUpdateTime(new Date());
		Integer update = issueMapper.updateById(issue);
		//新增回复
		Integer insert = 0;
		if(update!=null && update>0){
			insert = mapper.insert(record);
		}
		return insert!=1?false:true;
	}
}
