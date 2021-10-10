package com.xin.portal.system.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xin.portal.system.model.SysRelease;
import com.xin.portal.system.service.SysReleaseService;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.portal.system.controller.SysReleaseController 
* @Description: 版本发布
* @author zhoujun  
* @date 2018-10-23
* @version V1.0  
*/ 
@Controller
@RequestMapping("/sysRelease")
public class SysReleaseController extends BaseController {

	private static final String PATH = "sysRelease/";
	
	@Autowired
	private SysReleaseService service;
	
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
	
	@RequestMapping(value="/timeline",method=RequestMethod.GET)
	public String timeline(Model model){
		EntityWrapper<SysRelease> ew = new EntityWrapper<>();
		ew.orderBy("release_time", false);
		List<SysRelease> list = service.selectList(ew);
		model.addAttribute("list", list);
		return PATH + "timeline";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-23 
	 */
	@SystemLog(module=LanguageParam.RELEASE_VERSION ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(SysRelease query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param SysRelease
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-23 
	 */
	@SystemLog(module=LanguageParam.RELEASE_VERSION ,operation=LanguageParam.DETAILSQUERY ,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(SysRelease query){
		EntityWrapper<SysRelease> warpper = new EntityWrapper<SysRelease>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param SysRelease
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.RELEASE_VERSION ,operation=LanguageParam.DETAILSQUERY ,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		SysRelease result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-23 
	 */
	@SystemLog(module=LanguageParam.RELEASE_VERSION ,operation=LanguageParam.ACTIONADD,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(SysRelease record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-23 
	 */
	@SystemLog(module=LanguageParam.RELEASE_VERSION ,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(SysRelease record){
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
	@SystemLog(module=LanguageParam.RELEASE_VERSION ,operation=LanguageParam.ACTIONDELETE,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	
}

