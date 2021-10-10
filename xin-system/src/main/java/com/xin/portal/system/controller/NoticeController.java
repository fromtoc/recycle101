package com.xin.portal.system.controller;



import com.xin.portal.system.util.webSocketLog.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.NoticeService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.Notice;

import com.xin.portal.system.bean.BaseController;

/**  
* @Title: com.xin.portal.system.controller.NoticeController 
* @Description: 
* @author zhoujun  
* @date 2018-03-23
* @version V1.0  
*/ 
@Controller
@RequestMapping("/notice")
public class NoticeController extends BaseController {

	private static final String DIR = "notice/";
	
	@Autowired
	private NoticeService service;
	@Autowired
	private DictService dicterService;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-03-23 
	 */
	@RequestMapping("/index")
	public String index(Model model){
		List<Dict> noticeLevel = dicterService.listItem(new Dict("notice_level"));
		model.addAttribute("noticeLevel", JSONArray.toJSONString(noticeLevel));
		return DIR + "index";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-03-23 
	 */
	@RequestMapping("/add")
	public String add(){
		
		return DIR + "add";
	}

	/**
	 * @Title: page 
	 * @Description:  TODO
	 * @param query
	 * @return PageModel<Notice>
	 * @author zhoujun
	 * @Date 2018-03-23 
	 */
	@SystemLog(module=LanguageParam.NOTICE_MANAGERMENT ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<Notice> page(Notice query){
		return service.page(query);
	}
	
	/**
	 * @Title: select 
	 * @Description:  TODO
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-03-23 
	 */
	@SystemLog(module = LanguageParam.NOTICE_MANAGERMENT ,operation=LanguageParam.ACTIONLISTSEE , type=LogType.query)
	@RequestMapping("/findList")
	@ResponseBody
	public List<Notice> findList(Notice query, Model model){
		EntityWrapper<Notice> warpper = new EntityWrapper<Notice>(query);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-03-23 
	 */
	@RequestMapping("/edit")
	public String edit(Notice query, Model model){
		EntityWrapper<Notice> warpper = new EntityWrapper<Notice>(query);
		Notice record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	@SystemLog(module = LanguageParam.NOTICE_MANAGERMENT ,operation=LanguageParam.DETAILSQUERY , sort="tfToName1",tfToName1="query,id,title,com.xin.portal.system.mapper.NoticeMapper,selectById", type=LogType.query)
	@RequestMapping("/info")
	public String info(Notice query, Model model){
//		EntityWrapper<Notice> warpper = new EntityWrapper<Notice>(query);
		Notice record = service.findOneNotice(query.getId());
		model.addAttribute("record", record);
		return DIR + "info";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-03-23 
	 */
	@SystemLog(module = LanguageParam.NOTICE_MANAGERMENT ,sort="name",name="title",operation=LanguageParam.ACTIONADD, type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Notice record){
		record.setCreater(SessionUtil.getUserId());
		record.setReadNum(0);
		record.setState(0);
//		record.setPublishTime(new Date());
		record.setCreateTime(new Date());
		if (record.getType()==2) {
			record.setContent(record.getLinkUrl());
		}
		if(record.getIsForEver()!=null){
			record.setValidEndTime(null);
			record.setValidStartTime(null);
		}else{
			record.setIsForEver(0);
			if(record.getValidStartTime() ==null || record.getValidEndTime() ==null){
				return new BaseApi().put("code","3");
			/*	return BaseApi.error("有效日期不能为空！");*/
			}
		}
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-03-23 
	 */
	@SystemLog(module = LanguageParam.NOTICE_MANAGERMENT , sort="name",name="title", operation=LanguageParam.ACTIONUPDATE, type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(Notice record){
//		return record.updateById() ? BaseApi.success() : BaseApi.error();
		record.setUpdater(SessionUtil.getUserId());
		record.setUpdateTime(new Date());
		if (record.getType()==2) {
			record.setContent(record.getLinkUrl());
		}
		if(record.getIsForEver()!=null){
			record.setValidEndTime(null);
			record.setValidStartTime(null);
		}else{
			record.setIsForEver(0);
			if(record.getValidStartTime() ==null || record.getValidEndTime() ==null){
				return new BaseApi().put("code","3");
			/*	return BaseApi.error("有效日期不能为空！");*/
			}
		}
		return service.update(record) ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: publish 
	 * @Description:  发布
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018年3月26日 上午10:57:40
	 */
	@SystemLog(module = LanguageParam.NOTICE_MANAGERMENT ,operation=LanguageParam.PUBLISH_UNPUBLISH ,sort="tfToName1",tfToName1="record,id,title,com.xin.portal.system.mapper.NoticeMapper,selectById", type=LogType.update)
	@RequestMapping("/publish")
	@ResponseBody
	public BaseApi publish(Notice record) {
		record.setPublisher(SessionUtil.getUserId());
		record.setPublishTime(new Date());
		if (service.publish(record) == true) {
			WebSocketServer web = new WebSocketServer();

			if (record.getState() == 1) {
				web.onMessage(null,"2");

			} else {
				web.onMessage(null,"1");
			}
			return BaseApi.success();
		} else {
			return BaseApi.error();
		}
	}
	
	
	/**
	 * @Title: delete
	 * @Description:  删除
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.NOTICE_MANAGERMENT , operation=LanguageParam.ACTIONDELETE ,before="tfToName1",sort="tfToName1",
			tfToName1="record,id,title,com.xin.portal.system.mapper.NoticeMapper,selectById",type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(Notice record){
		boolean result = record.deleteById();
		if(result){//删除改公告的已读用户
			service.deleteAlreadyReadUser(record);
		}
		if(result){
			WebSocketServer web=new WebSocketServer();
			web.onMessage(null,"1");
			return BaseApi.success();
		}else{
			return BaseApi.error();
		}

	}


	@RequestMapping("/updateReadNum")
	@ResponseBody
	public BaseApi updateReadNum(Notice query){
		return service.updateReadNum(query.getId()) ? BaseApi.success() : BaseApi.error();
	}

	@RequestMapping("/findOneNotice")
	@ResponseBody
	public Notice findOneNotice(Notice query){
		return service.findOneNotice(query.getId());
	}
	
	@RequestMapping("/setAllNoticeRead")
	@ResponseBody
	public BaseApi setAllNoticeRead(){
		String userId = SessionUtil.getUserId();
		return service.setAllNoticeReadByUserId(userId) ? BaseApi.success() : BaseApi.error();
	}
	
	
}
