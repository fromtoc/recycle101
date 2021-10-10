package com.xin.portal.system.service;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.Issue;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2018-12-03
 */
public interface IssueService extends IService<Issue> {
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
	PageModel<Issue> page(Issue query);

	Issue getEnvironmentInfo(HttpServletRequest request);

	Issue selectIssueById(String id);

	Issue updatebyQuery(Issue record);

	boolean updateByIssue(Issue issue);
}
