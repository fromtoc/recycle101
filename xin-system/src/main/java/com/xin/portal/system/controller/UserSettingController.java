package com.xin.portal.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.UserSetting;
import com.xin.portal.system.service.UserSettingService;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.portal.controller.UserSettingController 
* @Description: 个人设置
* @author zhoujun  
* @date 2019-07-11
* @version V1.0  
*/ 
@Controller
@RequestMapping("/userSetting")
public class UserSettingController extends BaseController {

	private static final String DIR = "userSetting/";
	
	@Autowired
	private UserSettingService service;
	
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
	 * @Title: list 
	 * @Description:  列表查询
	 * @param UserSetting
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-07-11 
	 */
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(UserSetting query){
		EntityWrapper<UserSetting> warpper = new EntityWrapper<UserSetting>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param UserSetting
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		UserSetting result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-07-11 
	 */
	@SystemLog(module=LanguageParam.USERINFO_6 ,operation=LanguageParam.ACTIONADD ,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(UserSetting record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-07-11 
	 */
	@SystemLog(module=LanguageParam.USERINFO_6 ,operation=LanguageParam.ACTIONUPDATE ,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(UserSetting record){
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
	@SystemLog(module=LanguageParam.USERINFO_6 ,operation=LanguageParam.ACTIONDELETE ,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	
}

