package com.xin.portal.system.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.aspose.slides.Collections.ArrayList;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ThirdAppParam;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.model.WxPush;
import com.xin.portal.system.model.WxPushRecord;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.JobAndTriggerService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.service.ThirdAppParamService;
import com.xin.portal.system.service.WxPushRecordService;
import com.xin.portal.system.service.WxPushService;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

@Controller
@RequestMapping("/scheduleTask")
public class ScheduleTaskController  extends BaseController {
	
	private static final String PATH = "scheduleTask/";
	
    @Autowired
    private JobAndTriggerService jobAndTriggerService;
    @Autowired
	private WxPushService service;
    @Autowired
    private WxPushRecordService wxPushRecordService;
    @Autowired
    private ThirdAppParamService thirdAppParamService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ResourceService resourceService;
    
    @RequestMapping(value="/{userType}/index",method=RequestMethod.GET)
    public String index(@PathVariable("userType")String userType,Model model){
        if(WxPush.TASK_USER_MANAGE.equals(userType)){
			//System.out.println("进入管理操作界面");
		}else if(WxPush.TASK_USER_COMMON.equals(userType)){
			//System.out.println("进入普通用户操作界面");
		}else{//如果什么都不传默认是普通用户（由于这里没有对用户角色的校验所以这样处理）
			userType=WxPush.TASK_USER_COMMON;
		}
		model.addAttribute("userType", userType);
		return PATH + "index_"+userType;
	}
	
	@RequestMapping(value="/{userType}/add",method=RequestMethod.GET)
	public String add(@PathVariable("userType")String userType,Model model){
		model.addAttribute("userType", userType);
		UserInfo record=SessionUtil.getUserInfo();
		model.addAttribute("record", record);
		//查询到已经添加的应用。不控制权限
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
		ew.eq("is_enable", "1");
		List<ThirdAppParam> selectList = thirdAppParamService.selectList(ew);
		model.addAttribute("agents",JSON.toJSONString(selectList));//需要根据设置的应用选择
		//model.addAttribute("SYS_URL",cacheManager.getCache(Constant.CACHE_DEFAULT).get(Constant.ConfigKeys.SYS_URL).get());//改为从应用中获取
		
		return PATH + "add_"+userType;
	}
	
	/**
	 * 工具栏添加个人订阅
	 * @param resourceId
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/common/addByTool",method=RequestMethod.GET)
	public String addByTool(String resourceId,Model model){
		UserInfo record=SessionUtil.getUserInfo();
		model.addAttribute("record", record);
		//查询到已经添加的应用。不控制权限
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
		ew.eq("is_enable", "1");
		List<ThirdAppParam> selectList = thirdAppParamService.selectList(ew);
		model.addAttribute("agents",JSON.toJSONString(selectList));//需要根据设置的应用选择
		//model.addAttribute("SYS_URL",cacheManager.getCache(Constant.CACHE_DEFAULT).get(Constant.ConfigKeys.SYS_URL).get());//改为从应用中获取
		
		Resource res=resourceService.selectById(resourceId);
		model.addAttribute("resourceId", resourceId);
		model.addAttribute("resourceName", res.getName());
		return PATH + "add_commonByTool";
	}
	
	@RequestMapping(value="/pushDetailInfo",method=RequestMethod.GET)
	public String pushDetailInfo(){
		return PATH + "pushInfo";
	}
	
	@RequestMapping(value="/{userType}/edit",method=RequestMethod.GET)
	public String edit(@PathVariable("userType")String userType,Model model){
		model.addAttribute("userType", userType);
        //查询到已经添加的应用。不控制权限
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
		ew.eq("is_enable", "1");
		List<ThirdAppParam> selectList = thirdAppParamService.selectList(ew);
		model.addAttribute("agents",JSON.toJSONString(selectList));//需要根据设置的应用选择
        //model.addAttribute("SYS_URL",cacheManager.getCache(Constant.CACHE_DEFAULT).get(Constant.ConfigKeys.SYS_URL).get());
		return PATH + "edit_"+userType;
	}

	@RequestMapping(value="/select/img",method=RequestMethod.GET)
	public String selectImg(Model model){
		EntityWrapper<FileModel> ew = new EntityWrapper<>();
		ew.orderBy("create_time", false);
		List<String> fileType = new ArrayList();
		fileType.add("picture");//默认图
		fileType.add("thumbnail");//缩略图
		fileType.add("banner");//轮播图
		ew.in("business_type", fileType);
		List<FileModel> fileModel=fileService.selectList(ew);
		model.addAttribute("fileModel", fileModel);
		return PATH + "select_img";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-02-11 
	 */
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.PAGING_QUERY,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(WxPush query, Integer pageNumber, Integer pageSize){
		query.setTenantId(getTenantId());
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param WxPush
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-02-11 
	 */
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(WxPush query){
		query.setTenantId(getTenantId());
		EntityWrapper<WxPush> warpper = new EntityWrapper<WxPush>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param WxPush
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.DETAILSQUERY,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		WxPush result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param WxPush
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-02-11 
	 */
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.ACTIONADD,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(WxPush record){
		if(WxPush.TASK_TYPE_RUN.equals(record.getTaskType())){//脚本
			return new BaseApi().put("code", "-2");
		}
		record.setCreater(SessionUtil.getUserId());
		record.setCreateTime(new Date());
		record.setTenantId(getTenantId());
		boolean result = service.save(record);
		return result ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.SCHEDULE_START,type=LogType.update)
	@PostMapping("/start")
	@ResponseBody
	public BaseApi start(WxPush query){
		WxPush record = query.selectById();
		BaseApi result = service.startTask(record);
		return result;
	}
	
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.SCHEDULE_PAUSE,type=LogType.update)
	@PostMapping("/pause")
	@ResponseBody
	public BaseApi pause(WxPush query){
		WxPush record = query.selectById();
		BaseApi result = service.pauseTask(record);
		return result;
	}
	
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.SCHEDULE_RESTORE,type=LogType.update)
	@PostMapping("/restore")
	@ResponseBody
	public BaseApi restore(WxPush query){
		WxPush record = query.selectById();
		BaseApi result = service.restoreTask(record);
		return result;
	}

	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.MANUAL_EXECUTION_ONCE,type=LogType.update)
	@PostMapping("/send")
	@ResponseBody
	public BaseApi send(HttpServletRequest request, HttpServletResponse response, WxPush query){
		WxPush record = query.selectById();
		if(WxPush.PUSH_TYPE_WECHAT.equals(record.getPushType())){//微信
			try {
				request.setAttribute("record", record);
				request.getRequestDispatcher("/wxPush/sendMessage").forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}else if(WxPush.PUSH_TYPE_LINE.equals(record.getPushType())){//line
			try {
				request.setAttribute("record", record);
				request.getRequestDispatcher("/lineMessageBot/sendMessage").forward(request, response);
			} catch (ServletException | IOException e) {
				e.printStackTrace();
			}
		}else{
			boolean result = service.runTaskOnce(record);
			return result ? BaseApi.success() : BaseApi.error();
		}
		return null;
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param WxPush
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-02-11 
	 */
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.ACTION_RENEWAL,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(WxPush record){
		record.setUpdater(SessionUtil.getUserId());
		record.setUpdateTime(new Date());
		return service.updatePush(record)? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.SCHEDUL_TASKS,operation=LanguageParam.ACTIONDELETE,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
	    WxPush record = service.selectById(id);
	    try {
			jobAndTriggerService.deleteJob(record.getTriggerName(), record.getTriggerGroup(), record.getJobName(), record.getJobGroup());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
        return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping(value="/cronValidate")
	@ResponseBody
	public BaseApi cronValidate(@RequestParam("cronFormula") String cronFormula){
		return CronExpression.isValidExpression(cronFormula)?BaseApi.success():BaseApi.error();
	}
	
	@RequestMapping(value="/pushInfoPage")
	@ResponseBody
	public BaseApi cronValidate(WxPushRecord recourd, Integer pageNumber, Integer pageSize){
		recourd.setTenantId(getTenantId());
		return BaseApi.page(wxPushRecordService.page(recourd, pageNumber, pageSize));
	}

}
