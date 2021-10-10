package com.xin.portal.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.service.DataPermissionTypeService;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.PromptRelService;
import com.xin.portal.system.service.PromptService;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.model.DataPermissionType;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.Prompt;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.bean.BaseController;

/**  
* @Title: com.xin.portal.system.controller.PromptController 
* @Description: 
* @author zhoujun  
* @date 2018-01-08 ${time}
* @version V1.0  
*/ 
@Controller
@RequestMapping("/prompt")
public class PromptController extends BaseController {

	private static final String DIR = "prompt/";
	
	@Autowired
	private PromptService service;
	@Autowired
	private PromptRelService promptRelService;
	@Autowired
	private DictService dictService;
	@Autowired
	private DataPermissionTypeService dataPermissionTypeService;
	

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-08 ${time}
	 */
	@RequestMapping("/index")
	public String index(Model model){
		List<Dict> dictInputType = dictService.listItem(new Dict("input_type"));
		model.addAttribute("dictInputType", JSON.toJSONString(dictInputType));
		return DIR + "index";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-08 ${time}
	 */
	@RequestMapping("/add")
	public String add(Model model){
		EntityWrapper<DataPermissionType> ew = new EntityWrapper<>();
		ew.orderBy("sort", true);
		ew.ne("parent_id", "0");
		List<DataPermissionType> dpType = dataPermissionTypeService.selectList(ew);
		model.addAttribute("dpType", dpType);
		return DIR + "add";
	}
	
	@RequestMapping("/select")
	public String select() {
		return DIR + "select";
	}

	@RequestMapping("/selectSmartBi")
	public String selectSmartBi() {
		return DIR + "selectSmartBi";
	}
	/**
	 * @Title: page 
	 * @Description:  TODO
	 * @param query
	 * @return PageModel<Prompt>
	 * @author zhoujun
	 * @Date 2018-01-08 ${time}
	 */
	@SystemLog(module = LanguageParam.PROMPT_MANAGEMENT , operation= LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<Prompt> page(Prompt query){
		return service.page(query);
	}

	@SystemLog(module = LanguageParam.PROMPT_MANAGEMENT , operation= LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping("/pageSmartBi")
	@ResponseBody
	public PageModel<Prompt> pageSmartBi(Prompt query){
		return service.pageSmartBi(query);
	}
	
	@RequestMapping("/PromptResourceInfo")
	public String PromptResourceInfo(String dictCode, Model model) {
		model.addAttribute("dictCode",dictCode);
		return DIR + "promptInfo";
	}
	/**
	 * @Title: select 
	 * @Description:  TODO
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-08 ${time}
	 */
	@SystemLog(module = LanguageParam.PROMPT_MANAGEMENT , operation=LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping("/findList")
	@ResponseBody
	public List<Prompt> findList(Prompt query, Model model){
		EntityWrapper<Prompt> warpper = new EntityWrapper<Prompt>(query);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-08 ${time}
	 */
	@RequestMapping("/edit")
	public String edit(Prompt query, Model model){
		EntityWrapper<Prompt> warpper = new EntityWrapper<Prompt>(query);
		Prompt record = service.selectOne(warpper);
		if (StringUtils.isNotEmpty(record.getDictCode())) {
			Dict dict = dictService.find(new Dict(record.getDictCode()));
			record.setDictName(dict.getDictName());
		}
		List<Dict> recordtype= dictService.findBytype();
		//数据权限类型
		EntityWrapper<DataPermissionType> ew = new EntityWrapper<>();
		ew.orderBy("sort", true);
		ew.ne("parent_id", "0");
		List<DataPermissionType> dpType = dataPermissionTypeService.selectList(ew);
		model.addAttribute("recordtype", recordtype);
		model.addAttribute("record", record);
		model.addAttribute("dpType", dpType);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-08 ${time}
	 */
	@SystemLog(module = LanguageParam.PROMPT_MANAGEMENT , sort="name", name="name", operation=LanguageParam.ACTIONADD, type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Prompt record){
		record.setUpdateTime(new Date());
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-08 ${time}
	 */
	@SystemLog(module = LanguageParam.PROMPT_MANAGEMENT , sort="name", name="name", operation=LanguageParam.ACTIONUPDATE, type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(Prompt record){
		record.setUpdateTime(new Date());
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.PROMPT_MANAGEMENT , operation=LanguageParam.ACTIONDELETE, sort="tfToName1", before="tfToName1",
			tfToName1="record,id,name,com.xin.portal.system.mapper.PromptMapper,selectById", type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(Prompt record){
		PromptRel promptRel = new PromptRel();
		promptRel.setPromptId(record.getId());
		List<PromptRel> rec = promptRelService.check(promptRel);
		if (rec.size() >0) {
			BaseApi baseApi = new BaseApi();
			baseApi.put("code","3");
			return baseApi;
		} else {
			return service.del(record) ? BaseApi.success() : BaseApi.error();
		}
	}
	
	@RequestMapping("/getPromptDictInfo")
	@ResponseBody
	public PageModel<Prompt> getPromptDictInfo(Prompt record){
		return service.findPromptDictInfo(record);
	}
	@RequestMapping("/getCascadeChildrenId")
	@ResponseBody
	public List<PromptRel> getCascadeChildrenId(PromptRel record){
		EntityWrapper<PromptRel> ew = new EntityWrapper<>();
		ew.eq("id", record.getId());
		PromptRel proRel=promptRelService.selectOne(ew);
		//拿到提示信息
		Prompt prompt=service.selectById(proRel.getPromptId());
		BaseApi baseApi = new BaseApi();
		if(StringUtils.isNotEmpty(prompt.getCascadeCode())){
			//获取页面上级联的子筛选的ID
			//拿到PromptRel的ID
			EntityWrapper<PromptRel> ew_pr= new EntityWrapper<>();
			ew_pr.eq("prompt_id", prompt.getCascadeCode());
			List<PromptRel> pr=promptRelService.selectList(ew_pr);
			
			//根据级联Prompt字段拿到Prompt的dict_code
			Prompt p=service.selectById(prompt.getCascadeCode());
			
			for(int i=0;i<pr.size();i++){
				pr.get(i).setCode(p.getCode());
			}
			return pr;
		}
		else{
			return null;
		}
	}
	@RequestMapping("/getCascadeChildrenList")
	@ResponseBody
	public List<Dict> getCascadeChildrenList(PromptRel record){
		EntityWrapper<PromptRel> ew = new EntityWrapper<>();
		ew.eq("id", record.getId());
		PromptRel proRel=promptRelService.selectOne(ew);
		Prompt prompt=service.selectById(proRel.getPromptId());
		BaseApi baseApi = new BaseApi();
		if(StringUtils.isNotEmpty(prompt.getCascadeCode())){
			if(StringUtils.isNotEmpty(record.getDictCode())){
				EntityWrapper<Dict> ew_di= new EntityWrapper<>();
				ew_di.eq("parent_dict_code", record.getDictCode());
				List<Dict> dict=dictService.selectList(ew_di);
				return dict;
			}
			//如果record.getDictCode()为空，也就是前端选择了“请选择”之后
			else{
				EntityWrapper<Prompt> ew_pr= new EntityWrapper<>();
				ew_pr.eq("id", prompt.getCascadeCode());
				Prompt pr=service.selectOne(ew_pr);
				
				EntityWrapper<Dict> ew_di= new EntityWrapper<>();
				ew_di.eq("dict_code", pr.getDictCode());
				ew_di.isNotNull("item_name");
				List<Dict> dict=dictService.selectList(ew_di);
				return dict;
			}
			
		}
		return null;
	}
}
