package com.xin.portal.system.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.ImportLogService;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.ImportLog;

import com.xin.portal.system.bean.BaseController;

/**  
* @Title: com.xin.portal.system.controller.ImportLogController 
* @Description: 
* @author zhoujun  
* @date 2018-04-20
* @version V1.0  
*/ 
@Controller
@RequestMapping("/importLog")
public class ImportLogController extends BaseController {

	private static final String DIR = "importLog/";
	
	@Autowired
	private ImportLogService service;
	@Autowired
	private FileService fileService;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-04-20 
	 */
	@RequestMapping("/index")
	public String index(Model model){
		FileModel fileQuery = new FileModel();
    	fileQuery.setBusinessType("template");
    	List<FileModel> templates = fileService.findList(fileQuery);
    	List<JSONObject> list = new ArrayList<>();
    	for (FileModel file : templates) {
    		JSONObject jObj = new JSONObject();
    		jObj.put("itemName", file.getNameBefore());
    		jObj.put("itemValue", file.getBusinessInfo());
    		list.add(jObj);
    	}
    	model.addAttribute("templates", list.toString());
		return DIR + "index";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-04-20 
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}

	/**
	 * @Title: page 
	 * @Description:  TODO
	 * @param query
	 * @return PageModel<ImportLog>
	 * @author zhoujun
	 * @Date 2018-04-20 
	 */
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<ImportLog> page(ImportLog query){
		return service.page(query);
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param ImportLog
	 * @param Model
	 * @return List<ImportLog>
	 * @author zhoujun
	 * @Date 2018-04-20 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<ImportLog> list(ImportLog query, Model model){
		EntityWrapper<ImportLog> warpper = new EntityWrapper<ImportLog>(query);
		warpper.orderBy("create_time", false);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-04-20 
	 */
	@RequestMapping("/edit")
	public String edit(ImportLog query, Model model){
		EntityWrapper<ImportLog> warpper = new EntityWrapper<ImportLog>(query);
		ImportLog record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-04-20 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(ImportLog record){
		return service.insert(record) ? BaseApi.success() : BaseApi.error();
	}
	
	
	
	
}
