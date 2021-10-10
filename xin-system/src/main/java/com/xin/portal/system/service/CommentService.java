package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.AtUser;
import com.xin.portal.system.model.Comment;
import com.xin.portal.system.model.Issue;
import com.xin.portal.system.model.IssueRecord;

/**
 *  服务类
 *
 * @author xue
 * @since 2018-12-03
 */
public interface CommentService extends IService<Comment> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author xue
	 * @Date 2018-12-03 
	 */
	PageModel<Comment> page(Comment query, Integer pageNumber, Integer pageSize);

	List<Comment> selectByResourceId(String resourceId);
	
	PageModel<Comment> selectCommentMore(Comment query , Integer pageNumber , Integer pageSize);

	void addMessageAfterSaveComment(String userId, Comment comment);
	
	/**
	 * 问题反馈消息回复后,添加消息中心消息提示
	 * @param userId
	 * @param comment
	 * @param issue
	 */
	void addIssueMessageAfterSaveComment(String userId, IssueRecord issueRecord, Issue issue);
	
	List<AtUser> remindUser(String resourceId, String tenantId);

	

}
