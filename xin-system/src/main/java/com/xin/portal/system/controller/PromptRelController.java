package com.xin.portal.system.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.PromptRelService;

/**  
* @Title: com.xin.portal.system.controller.PromptRelController 
* @Description: 
* @author zhoujun  
* @date 2018-01-09
* @version V1.0  
*/ 
@Controller
@RequestMapping("/promptRel")
public class PromptRelController extends BaseController {

	private static final String DIR = "promptRel/";
	
	@Autowired
	private PromptRelService service;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-09 
	 */
	@RequestMapping("/index")
	public String index(){
		return DIR + "index";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-09 
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}

	
	/**
	 * @Title: select 
	 * @Description:  TODO
	 * @param id
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-09 
	 */
	@RequestMapping("/findList")
	@ResponseBody
	public List<PromptRel> findList(PromptRel query, Model model){
	/*	if("".equals(query.getResourceId())||query.getResourceId()==null){
			return null;
		}*/
		return service.findList(query);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-09 
	 */
	@RequestMapping("/edit")
	public String edit(PromptRel query, Model model){
		model.addAttribute("record", service.find(query));
		return DIR + "edit";
	}
	
	@RequestMapping("/PromptResourceInfo")
	public String PromptResourceInfo(PromptRel record, Model model){
		model.addAttribute("promptId", record.getPromptId());
		return DIR + "promptResource";
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-09 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(PromptRel record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}

	@RequestMapping("/saveBatch")
	@ResponseBody
	public BaseApi saveBatch(@RequestParam("promptRelsJSON")String promptRelsJSON,String resourceId){
		List<PromptRel> list = JSONObject.parseArray(promptRelsJSON, PromptRel.class);
		return  service.saveBatch(resourceId,list) ? BaseApi.success() : BaseApi.error();
	}
	
	
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-09 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(PromptRel record){
		record.setHidden(record.getHidden()==null?1:record.getHidden());
		record.setRequired(record.getRequired()==null?1:record.getRequired());
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
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(PromptRel record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/getPromptDict")
	@ResponseBody
	public BaseApi getPromptDict(PromptRel query){
		//根据筛选ID 获取 dict
		return service.getPromptDict(query.getPromptId());
	}
	
	@RequestMapping("/getPromptResource")
	@ResponseBody
	public PageModel<Resource> delete(PromptRel record, Integer pageNumber, Integer pageSize){
		return service.findResourceList(record,pageNumber,pageSize);
	}
	
}
