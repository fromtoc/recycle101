package com.xin.portal.system.controller;

import com.xin.portal.system.model.Comment;
import com.xin.portal.system.model.Issue;
import com.xin.portal.system.service.IssueService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.IssueRecord;
import com.xin.portal.system.service.CommentService;
import com.xin.portal.system.service.IssueRecordService;


/**
* @Title: com.xin.portal.controller.IssueRecordController 
* @Description: 模块名称
* @author zhoujun  
* @date 2018-12-03
* @version V1.0  
*/ 
@Controller
@RequestMapping("/issueRecord")
public class IssueRecordController extends BaseController {

	private static final String PATH = "issueRecord/";
	
	@Autowired
	private IssueRecordService service;

	@Autowired
	private IssueService issueService;

	@Autowired
	private CommentService commentService;


	
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 */
	@SystemLog(module=LanguageParam.QUESTION_REPLY ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi page(IssueRecord query){
		return BaseApi.page(service.page(query));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param query
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BaseApi list(IssueRecord query){
		EntityWrapper<IssueRecord> warpper = new EntityWrapper<IssueRecord>(query);
		return BaseApi.list(service.selectList(warpper));
	}

	/**
	 * @Title: save
	 * @Description:  保存
	 * @param record
	 */
	@SystemLog(module=LanguageParam.QUESTION_REPLY ,operation=LanguageParam.ACTIONADD ,type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	@Transactional
	public BaseApi save(IssueRecord record){
		
		Issue issue = new Issue();
		issue.setId(record.getIssueId());
		issue.setIsReply("1");
		issueService.updateById(issue);
		
		Issue i = issueService.selectIssueById(record.getIssueId());
		//记入问题反馈  除了自己以外的其他人，如果拥有这个资源的权限，并且表现为菜单，可以接收到这个消息。
		boolean result = service.insertRecord(record);
		if(result){
			commentService.addIssueMessageAfterSaveComment(SessionUtil.getUserId(),record,i);
			return BaseApi.data(record);
		}

		return  BaseApi.error();
	}


	/**
	 * @Title: save
	 * @Description:  删除
	 * @param id
	 */
	@SystemLog(module=LanguageParam.QUESTION_REPLY ,operation=LanguageParam.ACTIONDELETE ,type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(String id){
		EntityWrapper<IssueRecord> ew = new EntityWrapper<>();
		ew.eq("id",id);
		return service.delete(ew) ? BaseApi.success() : BaseApi.error();
	}

}

