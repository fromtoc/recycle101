package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.IssueRecord;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2018-12-03
 */
public interface IssueRecordService extends IService<IssueRecord> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-12-03 
	 */
	PageModel<IssueRecord> page(IssueRecord query);

	boolean insertRecord(IssueRecord record);
}
