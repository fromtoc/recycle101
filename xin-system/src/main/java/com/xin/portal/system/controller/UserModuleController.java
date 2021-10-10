package com.xin.portal.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.UserModuleService;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.model.UserModule;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;

/**  
* @Title: com.xin.portal.system.controller.UserModuleController 
* @Description: 功能名称
* @author xue  
* @date 2018-11-08
* @version V1.0  
*/ 
@Controller
@RequestMapping("/userModule")
public class UserModuleController extends BaseController {

	private static final String PATH = "userModule/";
	
	@Autowired
	private UserModuleService service;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return PATH + "index";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return PATH + "add";
	}
	
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
	 * @Date 2018-11-08 
	 */
	@SystemLog(module= LanguageParam.USER_TEMPLATE ,operation=LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(UserModule query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param UserModule
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.USER_TEMPLATE ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(UserModule query){
		EntityWrapper<UserModule> warpper = new EntityWrapper<UserModule>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param UserModule
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.USER_TEMPLATE ,operation=LanguageParam.DETAILSQUERY , type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		UserModule result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.USER_TEMPLATE ,operation=LanguageParam.ACTIONADD ,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(UserModule record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.USER_TEMPLATE ,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(UserModule record){
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
	@SystemLog(module=LanguageParam.USER_TEMPLATE ,operation=LanguageParam.ACTIONDELETE,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	
}

