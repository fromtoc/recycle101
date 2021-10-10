package com.xin.portal.system.controller;



import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.cache.CacheManager;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.ExcelUtil;
import com.xin.portal.system.model.DataImport;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.ImportLog;
import com.xin.portal.system.service.DataImportService;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.util.SessionUtil;

/**  
* @Title: com.xin.portal.system.controller.DataImportController 
* @Description: 
* @author zhoujun  
* @date 2018-04-19
* @version V1.0  
*/ 
@Controller
@RequestMapping("/dataImport")
public class DataImportController extends BaseController {

	private static final String DIR = "dataImport/";
	
	@Autowired
	private DataImportService service;
	@Autowired
	private FileService fileService;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-04-19 
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
	 * @Date 2018-04-19 
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}

	
	/**
	 * @Title: findList 
	 * @Description:  TODO
	 * @param query
	 * @param model
	 * @return List<DataImport>
	 * @author zhoujun
	 * @Date 2018年4月19日 上午11:04:11
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<DataImport> list(DataImport query, Model model){
		EntityWrapper<DataImport> warpper = new EntityWrapper<DataImport>(query);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-04-19 
	 */
	@RequestMapping("/edit")
	public String edit(DataImport query, Model model){
		EntityWrapper<DataImport> warpper = new EntityWrapper<DataImport>(query);
		DataImport record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-04-19 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(DataImport record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-04-19 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(DataImport record){
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
	public BaseApi delete(DataImport record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	@RequestMapping(value = "/excel/{tableName}", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi excelDataImport(@RequestParam("file") MultipartFile file,@PathVariable("tableName")String tableName,
			HttpServletRequest request, Model model) {
		
		String templateName = request.getParameter("templateName");
		String dataTime = request.getParameter("dataTime");
		
		// 上传目录为 系统部署路径\\upload
		String uploadPath = getCache(ConfigKeys.SYS_UPLOAD_PATH) + File.separator +"excel";
		FileModel f = fileService.upload(uploadPath,"/upload/excel", file, null,false, "import");
		
		ExcelUtil.readExcelData(f.getSavedPath());
		
		List<Map<Integer, String>> list = ExcelUtil.readExcelData(f.getSavedPath());
		
		ImportLog importLog = new ImportLog();
		importLog.setCreater(SessionUtil.getUserInfo().getUsername());
		importLog.setCreateTime(new Timestamp(System.currentTimeMillis()));
		try {
			dataTime = dataTime.replace("GMT", "").replaceAll("\\(.*\\)", "");
		    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy hh:mm:ss z", Locale.ENGLISH);
	        importLog.setDataTime(format.parse(dataTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
		importLog.setTableName(tableName);
		importLog.setTemplateName(templateName);
		int result = 0;
		if (list!=null && list.size()>1) {
			list.remove(0);
			importLog.setDataSize(list.size());
    		result = service.saveBatch(importLog,list);
		}
		return result>0 ? new BaseApi().put("code",0) : new BaseApi().put("code",1);
	}
	
}
