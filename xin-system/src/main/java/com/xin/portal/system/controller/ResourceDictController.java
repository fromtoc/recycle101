package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.ResourceDict;
import com.xin.portal.system.model.ResourceType;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/resourceDict")
public class ResourceDictController extends BaseController {
	
	private static final String DIR = "resourceDict/";
	
	@Autowired
	private ResourceDictService service;

	@RequestMapping("/index/{id}")
	public String index(Model model,@PathVariable("id")String id){
		List<ResourceDict> list= service.findList(id);
		model.addAttribute("list",list);
		model.addAttribute("size",list.size());
		model.addAttribute("resourceId",id);
		return DIR + "index";
	}

	@GetMapping("/page/{resourceId}")
	@ResponseBody
	public BaseApi page(Model model,@PathVariable("resourceId")String resourceId, Integer pageNumber, Integer pageSize,String name){
		return BaseApi.page(service.page(resourceId,pageNumber,pageSize,name));
	}

	@SystemLog(module = LanguageParam.RESOURCE_DICTIONARY , operation= LanguageParam.EDIT_DICTIONARY , type= LogType.update)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(String JsonData,String resourceId){
		List<ResourceDict> list = JSONObject.parseArray(JsonData, ResourceDict.class);
		return  service.saveBatch(list,resourceId) ? BaseApi.success() : BaseApi.error();
	}

}
