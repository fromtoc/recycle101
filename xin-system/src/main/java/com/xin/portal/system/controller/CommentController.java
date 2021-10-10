package com.xin.portal.system.controller;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;


import com.alibaba.fastjson.JSON;
import com.xin.portal.system.model.AtUser;
import com.xin.portal.system.util.webSocketLog.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Comment;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.service.CommentService;
import com.xin.portal.system.service.MessageCenterService;
import com.xin.portal.system.service.ResourceLogService;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.WebUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.portal.system.controller.CommentController 
* @Description: 评论
* @author xue  
* @date 2018-12-03
* @version V1.0  
*/ 
@Controller
@RequestMapping("/comment")
public class CommentController extends BaseController {

	private static final String PATH = "comment/";
	
	@Autowired
	private CommentService service;
	@Autowired
	private ResourceLogService resourceLogService;
	@Autowired
	private MessageCenterService messageCenterService;
	
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIVE_SEE,type=LogType.query)
	@RequestMapping(value="/index/{resourceId}",method=RequestMethod.GET)
	public String index(@PathVariable String resourceId,Model model){
		List<Comment> comments=service.selectByResourceId(resourceId);
		//如果是会话中的本人，将username set为“我”1068339153872150529
		for(Comment a:comments){
			if(SessionUtil.getUserId().equals(a.getUserId())){
				a.setUserName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.ME));
			}
		}
		model.addAttribute("userInfo",SessionUtil.getUserInfo());
		model.addAttribute("comments",comments);
		model.addAttribute("resourceId",resourceId);
		return PATH + "index";
	}


	@RequestMapping(value="/remind/{resourceId}",method=RequestMethod.GET)
	public String remind(@PathVariable String resourceId,Model model){
		List<AtUser> remindList=service.remindUser(resourceId,this.getTenantId());
		model.addAttribute("remindList",JSON.toJSONString(remindList));
		return PATH + "remind";
	}

	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIONADD,type=LogType.add)
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return PATH + "add";
	}
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(){
		return PATH + "edit";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-03 
	 */
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(Comment query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-03 
	 */
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(Comment query){
		EntityWrapper<Comment> warpper = new EntityWrapper<Comment>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.DETAILSQUERY,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		Comment result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-03 
	 */
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIONADD,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(Comment record, HttpServletRequest request){
		record.setUserId(SessionUtil.getUserId());
		record.setCreateTime(new Date());
		record.setAvatar(SessionUtil.getUserInfo().getAvatar());
		
		//资源评论记录
		ResourceLog resourceLog = new ResourceLog();
		resourceLog.setCreater(SessionUtil.getUserId());
		resourceLog.setCreateTime(new Date());
		resourceLog.setType(ResourceLog.TYPE_COMMENT_ADD);
		resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
		resourceLog.setResourceId(record.getResourceId());
		resourceLog.setIp(WebUtil.getIpAddr(request));
		resourceLog.setBrowser(WebUtil.getBrower(request));
		resourceLogService.save(resourceLog);

		//记入消息中心  除了自己以外的其他人，如果拥有这个资源的权限，并且表现为菜单，可以接收到这个消息。

		boolean result = record.insert();
		if(result){
			service.addMessageAfterSaveComment(SessionUtil.getUserId(),record);
			return BaseApi.data(record);
		}

		return  BaseApi.error();

	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-03 
	 */
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(Comment record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author xue
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.ACTIONDELETE,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id, HttpServletRequest request){
		Comment record = service.selectById(id);
		//资源评论记录
		ResourceLog resourceLog = new ResourceLog();
		resourceLog.setCreater(SessionUtil.getUserId());
		resourceLog.setCreateTime(new Date());
		resourceLog.setType(ResourceLog.TYPE_COMMENT_DEL);
		resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
		resourceLog.setResourceId(record.getResourceId());
		resourceLog.setIp(WebUtil.getIpAddr(request));
		resourceLog.setBrowser(WebUtil.getBrower(request));
		resourceLogService.save(resourceLog);
		boolean result = record.deleteById();
		if(result){//删除相应的消息
			messageCenterService.deleteMessageBySourceId(id);
			WebSocketServer web=new WebSocketServer();
			web.onMessage(null,"1");
		}
		return result ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping(value="/management",method=RequestMethod.GET)
	public String management(){
		return PATH + "management";
	}
	
	@RequestMapping(value="/selectCommentMore")
	@ResponseBody
	public BaseApi selectCommentMore(Comment query , Integer pageNumber , Integer pageSize){
		return BaseApi.page(service.selectCommentMore(query, pageNumber, pageSize));
	}
	
	@RequestMapping(value="/container",method=RequestMethod.GET)
	public String container(){
		return PATH + "container";
	}
	
	@RequestMapping(value="/selectByresourceId/{id}")
	@ResponseBody
	public BaseApi selecByresourceId(@PathVariable(value="id") String resourceId, Comment query, Integer pageNumber , Integer pageSize){
		query.setResourceId(resourceId);
		return BaseApi.page(service.selectCommentMore(query, pageNumber, pageSize));
	}
	
	@RequestMapping(value="/content",method=RequestMethod.GET)
	public String content(){
		return PATH + "content";
	}
	
	@SystemLog(module=LanguageParam.COMMENTS ,operation=LanguageParam.REPLY,type=LogType.add)
	@PostMapping(value="/saveComment")
	@ResponseBody
	public BaseApi saveComment(Comment comment, HttpServletRequest request){
		comment.setReplyId(SessionUtil.getUserId());
		comment.setReplyTime(new Date());
		comment.setAvatar(SessionUtil.getUserInfo().getAvatar());
		
		ResourceLog resourceLog = new ResourceLog();
		resourceLog.setCreater(SessionUtil.getUserId());
		resourceLog.setCreateTime(new Date());
		resourceLog.setType(ResourceLog.TYPE_COMMENT_ADD);
		resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
		resourceLog.setResourceId(comment.getResourceId());
		resourceLog.setIp(WebUtil.getIpAddr(request));
		resourceLog.setBrowser(WebUtil.getBrower(request));
		resourceLogService.save(resourceLog);
		return comment.updateById()? BaseApi.data(comment) : BaseApi.error();
	}
	
	@RequestMapping(value="/roofBegin",method=RequestMethod.GET)
	@ResponseBody
	public BaseApi roofBegin(Comment comment){
		EntityWrapper<Comment> ew = new EntityWrapper<>();
		ew.setEntity(comment);
		Comment query = service.selectOne(ew);
		return query != null ? BaseApi.data(query) : BaseApi.error();
	}
}

