package com.xin.portal.system.controller;

import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.UserSetting;
import com.xin.portal.system.service.UserSettingService;
import com.xin.portal.system.util.webSocketLog.WebSocketServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.MessageCenter;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.MessageCenterService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.portal.controller.MessageCenterController 
* @Description: 消息中心
* @author zhoujun  
* @date 2019-06-21
* @version V1.0  
*/ 
@Controller
@RequestMapping("/messageCenter")
public class MessageCenterController extends BaseController {

	private static final String DIR = "messageCenter/";
	
	@Autowired
	private MessageCenterService service;
	@Autowired
	private UserSettingService userSettingservice;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return DIR + "index";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return DIR + "add";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(){
		return DIR + "edit";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-06-21 
	 */
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(MessageCenter query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-06-21 
	 */
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(MessageCenter query){
		EntityWrapper<MessageCenter> warpper = new EntityWrapper<MessageCenter>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param model
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module = LanguageParam.MESSAGE_NOTICE , operation=LanguageParam.ACTIVE_SEE,  type= LogType.query)
	@GetMapping("/info/{id}")
	public String info(@PathVariable String id,Model model){
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, this.getTenantId());
		MessageCenter result = service.selectById(id);
		if(result.getType()==MessageCenter.INTERACTIVE_MESSAGE && !"".equals(result.getResourceId())){
			Resource res = resourceService.selectById(result.getResourceId());
			model.addAttribute("resource", res);
		}
		String sys_theme = userSettingservice.selectByUserId(SessionUtil.getUserId()).getNavigaStyle();
		if(sys_theme==null && "".equals(sys_theme)){
			sys_theme=config.getValue();
		}
		model.addAttribute("sys_theme", sys_theme);
		model.addAttribute("record", result);
		return DIR + "info";
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-06-21 
	 */
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(MessageCenter record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-06-21 
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(MessageCenter record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/setAllRead")
	@ResponseBody
	public BaseApi updateAllMessageIsRead(){
		String userId = SessionUtil.getUserId();
		return service.updateAllMessageIsRead(userId)?BaseApi.success() : BaseApi.error();
	}

	@RequestMapping("/clearAllMessage")
	@ResponseBody
	public BaseApi clearAllMessage(){
		String userId = SessionUtil.getUserId();
		return service.clearAllMessage(userId)?BaseApi.success() : BaseApi.error();
	}
	
	
}

