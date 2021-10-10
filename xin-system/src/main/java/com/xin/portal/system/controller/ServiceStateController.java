package com.xin.portal.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.ServiceStateService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.ServiceState;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;

/**  
* @Title: com.xin.portal.system.controller.ServiceStateController 
* @Description: 
* @author songyi  
* @date 2018-12-11
* @version V1.0  
*/ 
@Controller
@RequestMapping("/serviceState")
public class ServiceStateController extends BaseController {

	private static final String DIR = "serviceState/";
	
	@Autowired
	private ServiceStateService service;
	@Autowired
	private DictService dicterService;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author songyi
	 * @Date 2018-12-11
	 */
	@RequestMapping("/index")
	public String index(Model model){
		List<Dict> recordType = dicterService.listItem(new Dict("record_type"));
		List<ServiceState> list = service.selectList(null);
		for(ServiceState s:list) {
			model.addAttribute("serviceState", s);
			break;
		}
		model.addAttribute("recordType", recordType);
		return DIR + "index";
	}
	
	/**
	 * @Title: page 
	 * @Description:  TODO
	 * @param query
	 * @return PageModel<ServiceState>
	 * @author songyi
	 * @Date 2018-12-11
	 */
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<ServiceState> page(ServiceState query){
		return service.page(query);
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author songyi
	 * @Date 2018-12-11
	 */
	@RequestMapping("/add")
	public String add(Model model){
		List<ServiceState> list = service.selectList(null);
		for(ServiceState s:list) {
			model.addAttribute("serviceState", s);
			break;
		}
		return DIR + "add";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加保存
	 * @return String
	 * @author songyi
	 * @Date 2018-12-12
	 */
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(ServiceState record) {
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @return String
	 * @author songyi
	 * @Date 2018-12-12
	 */
	@RequestMapping("/edit")
	public String edit(ServiceState query, Model model){
		EntityWrapper<ServiceState> wrapper = new EntityWrapper<ServiceState>(query);
		ServiceState record = service.selectOne(wrapper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author songyi
	 * @Date 2018-12-12
	 */
	@RequestMapping("update")
	@ResponseBody
	public BaseApi update(ServiceState record) {
		return service.updateById(record) ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新状态
	 * @param query
	 * @return BaseApi
	 * @author songyi
	 * @Date 2018-12-12
	 */
	@RequestMapping("updateState")
	@ResponseBody
	public BaseApi updateState(ServiceState record) {
		return service.updateState(record) ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author songyi
	 * @Date 2018-12-12
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(ServiceState record){
		return service.deleteById(record.getId())? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: saveConfig 
	 * @Description:  更新服务监控配置
	 * @param query
	 * @return BaseApi
	 * @author songyi
	 * @Date 2018-12-13
	 */
	@RequestMapping("saveConfig")
	@ResponseBody
	public BaseApi saveConfig(ServiceState record) {
		return service.updateConfig(record) ? BaseApi.success() : BaseApi.error();
	}
	
}
