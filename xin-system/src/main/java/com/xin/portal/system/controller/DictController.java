package com.xin.portal.system.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.Prompt;
import com.xin.portal.system.model.User;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.PromptService;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.ExcelUtil;
import com.xin.portal.system.util.datasource.DataSourceUtils;

/**
 * @ClassPath: com.at.product.system.DictController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-5-9 上午10:24:37
 */
@Controller
@RequestMapping("/dict")
public class DictController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(DictController.class);
	
	private static final String DIR = "dict/";
	
	@Autowired
	private DictService service;
	@Autowired
	private PromptService promptService;
	@Autowired
	private FileService fileService;
	/**
	 * 数据字典页的跳转方法
	 * added by zhoujun 2017年6月6日 下午3:05:51.
	 * @return
	 */
	@RequestMapping("/index")
	public String index() {
		return DIR + "index";
	}
	/**
	 * 字段设置中添加字段项，如果字段类型是“选择列表”类型，则通过此方法跳转到选择相应数据项的方法
	 * added by zj 2017年6月6日 下午3:25:44.
	 * @return
	 */
	@RequestMapping("/select")
	public String select() {
		return DIR + "select";
	}
	
	/**
	 * @Title: tags 
	 * @Description:  作为标签选择
	 * @return String
	 * @author zhoujun
	 * @Date 2018年1月26日 下午3:49:26
	 */
	@RequestMapping("/tags")
	public String tags(Dict query, Model model) {
		List<Dict> dict = service.listItem(query);
		model.addAttribute("tags", dict);
		return DIR + "tags";
	}
	/**
	 * 
	 * 添加数据字典
	 * added by zhoujun 2017年6月6日 下午3:09:05.
	 * @return
	 */
	@RequestMapping("/add")
	public String add() {
		return DIR + "add";
	}
	
	@RequestMapping("/addItem")
	public String addItem() {
		return DIR + "add_item";
	}
	/**
	 * excel导入字典值
	 * @return
	 */
	@RequestMapping("/import")
	public String importExcel() {
		return DIR + "import";
	}
	

	/**
	 * 表格初始化方法
	 * added by zhoujun 2017年6月6日 下午3:08:12.
	 * @param query
	 * @return
	 */
	@SystemLog(module = LanguageParam.DICTIONARY_MANAGEMENT , operation=LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<Dict> page(Dict query,Integer pageNumber,Integer pageSize){
		return service.page(query,pageNumber,pageSize);
	}
	/**
	 * 添加数据字典方法
	 * added by zhoujun 2017年6月6日 下午3:12:19.
	 * @param dict
	 * @return
	 */
	@SystemLog(module = LanguageParam.DICTIONARY_MANAGEMENT , operation=LanguageParam.ACTIONADD, type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Dict dict){
		EntityWrapper<Dict> ew = new EntityWrapper<>();
		if (!StringUtils.isEmpty(dict.getItemValue())) {
			ew.eq("dict_code", dict.getDictCode())
			  .eq("item_value", dict.getItemValue());
			Dict rec = service.selectOne(ew);
			if (rec!=null) {
				return new BaseApi().put("code",1);
				//return BaseApi.error("字典项名称或字典项值已经存在！");
			}
		} else {
			ew.eq("dict_code", dict.getDictCode());
			Dict rec = service.selectOne(ew);
			if (rec!=null) {
				return new BaseApi().put("code",2);
				//return BaseApi.error("字典编码已经存在！");
			}
		}
		if(dict.getIsSource() != null && 1 == dict.getIsSource()){//检测SQL语句是否有update delete drop alter truncate create grant revoke
			if(!DataSourceUtils.isLegal(dict.getQuerySql())){
				return new BaseApi().put("code",3);
			}
		}else{
			dict.setIsSource(0);
		}
		dict.setIssystem(0);
		boolean result = dict.insert();
		return result  ? BaseApi.success(dict.getId()) : BaseApi.error();
	}
	/**
	 * 数据字典添加页的表格修改方法
	 * added by zhoujun 2017年6月6日 下午3:11:34.
	 * @param dict
	 * @return
	 */
	@SystemLog(module = LanguageParam.DICTIONARY_MANAGEMENT , operation=LanguageParam.ACTIONUPDATE, type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(Dict dict){
		Dict rec = dict.selectById();
		EntityWrapper<Dict> ew = new EntityWrapper<>();
		if (!dict.getDictCode().equals(rec.getDictCode())) {
			ew.eq("dict_code", dict.getDictCode());
			Dict recCode = service.selectOne(ew);
			if (recCode!=null) {
				return new BaseApi().put("code",2);
				//return BaseApi.error("字典编码已经存在！");
			}
			ew.eq("item_value", dict.getItemValue());
			Dict recItem = service.selectOne(ew);
			if (recItem!=null) {
				return new BaseApi().put("code",1);
				//return BaseApi.error("字典项名称或字典项值已经存在！");
			}
		} else {
			ew.eq("dict_code", dict.getDictCode())
			  .eq("item_value", dict.getItemValue());
			Dict recItem = service.selectOne(ew);
			if (recItem!=null && !recItem.getId().equals(rec.getId())) {
				return new BaseApi().put("code",1);
				//return BaseApi.error("字典项名称或字典项值已经存在！");
			}
		}
		if(dict.getIsSource() != null && 1 == dict.getIsSource()){//检测SQL语句是否有update delete drop alter truncate create grant revoke
			if(!DataSourceUtils.isLegal(dict.getQuerySql())){
				return new BaseApi().put("code",3);
			}
		}else{
			dict.setIsSource(0);
		}
		dict.setIssystem(0);
		return service.update(dict) > 0 ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * 编辑数据字典
	 * added by zhoujun 2017年6月6日 下午3:09:38.
	 * @param dictCode
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(Dict query,Model model){
		Dict rcord = service.find(query);
		model.addAttribute("record", rcord);
		return DIR + "edit";
	}
	
	@RequestMapping("/editItem")
	public String editItem(Dict query, Model model) {
		Dict rcord = service.find(query);
		model.addAttribute("record", rcord);
		return DIR + "edit_item";
	}
	/**
	 * 编辑页的初始化表格方法
	 * added by zhoujun 2017年6月6日 下午3:16:54.
	 * @param query
	 * @return
	 */
	@SystemLog(module = LanguageParam.DICTIONARY_MANAGEMENT , operation=LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping("/list")
	@ResponseBody
	public List<Dict> list(Dict query){
		return service.list(query);
	}
	/**
	 * 删除数据字典
	 * added by zhoujun 2017年6月6日 下午3:09:50.
	 * @param dictCode
	 * @return
	 */
	@SystemLog(module = LanguageParam.DICTIONARY_MANAGEMENT , operation=LanguageParam.DICTIONARY_ITEM_DELETE, type=LogType.delete)
	@RequestMapping("/delByCode")
	@ResponseBody
	public BaseApi delByCode(String dictCode){
		//查询要删除的字典管理是否被'筛选管理'
		EntityWrapper<Prompt> warpper = new EntityWrapper<Prompt>();
		warpper.eq("dict_code", dictCode);
		List<Prompt> promptList = promptService.selectList(warpper);
		if (!promptList.isEmpty()) {
			return BaseApi.error("该字典管理被'筛选管理',引用无法删除!");
		}
		return service.delByCode(dictCode)>0 ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * 通过id删除数据项
	 * added by zhoujun 2017年6月6日 下午3:13:01.
	 * @param id
	 * @return
	 */
	@SystemLog(module = LanguageParam.DICTIONARY_MANAGEMENT , operation=LanguageParam.DICTIONARY_VALUE_DELETE, type=LogType.delete)
	@RequestMapping("/delById")
	@ResponseBody
	public BaseApi delById(String id){
		return service.delById(id)>0 ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * 数据字典添加页的表格初始化方法
	 * @Title: listItem 
	 * @Description:  TODO
	 * @param code
	 * @return List<Dict>
	 * @author zhoujun
	 * @Date 2017-5-12 上午9:47:30
	 */
	@RequestMapping("/listItem")
	@ResponseBody
	public List<Dict> listItem(Dict query){
		return service.listItem(query);
	}
	
	@SystemLog(module = LanguageParam.INTEGRATEDACCOUNT ,operation=LanguageParam.IMPORT_USER , type = LogType.importData)
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model,String dictCode) {
		FileModel fileModel = fileService.upload(getUploadPath(request) + "import", "/upload/import", file, null, false,
				FileModel.DICTVALUE_IMPORT);

		if (fileModel != null) {
			List<Map<Integer, String>> excelList = ExcelUtil.readExcelData(fileModel.getSavedPath());
			// 判断数据条数
			if (excelList == null || excelList.size() < 2) {
				return new BaseApi().put("code",7);
				/*String msg = PropertiesUtil.getLocaleMessage(getLocale(), "validation.excel_check_content");
				return BaseApi.error(msg)*/
			}
			/*
			 * 示例Excel 
			 * 字典项名称	|字典项值	|父级编码	|排序	|备注	|
			 * ------------------------------------------------------- 
			 * 	功能菜单		|1			| 			|1		|		|
			 * 
			 */
			List<Dict> listRecord = Lists.newArrayList();// 原始数据列表
			for (int i = 1; i < excelList.size(); i++) {
				Map<Integer, String> rowMap = excelList.get(i);
				if (StringUtils.isNotEmpty(rowMap.get(0)) && StringUtils.isNotEmpty(rowMap.get(1))&& StringUtils.isNotEmpty(rowMap.get(3))) {
					Dict record = new Dict();
					if (!StringUtils.isEmpty(rowMap.get(1))) {
						// 校验字典项名称或字典项值是否存在
						EntityWrapper<Dict> ew = new EntityWrapper<>();
						ew.eq("dict_code", dictCode)
						  .eq("item_name", rowMap.get(0))
						  .eq("item_value", rowMap.get(1));
						Dict rec = service.selectOne(ew);
						if (rec!=null) {
							return new BaseApi().put("code",1);
							//return BaseApi.error("字典项名称已经存在！");
						}
					}
					record.setDictName(null);
					record.setDictCode(dictCode);
					record.setItemName(rowMap.get(0));
					record.setItemValue(rowMap.get(1));
					if(rowMap.get(2)!=null) {
						record.setParentDictCode(rowMap.get(2));
					}
					record.setItemOrder(Integer.parseInt(rowMap.get(3)));
					if(rowMap.get(4)!=null) {
						record.setRemark(rowMap.get(4));
					}
					listRecord.add(record);
				}
			}
			boolean result = service.insertBatch(listRecord);
			return result ? BaseApi.success(listRecord.size()+""):BaseApi.error();//导入成功！共 " + listRecord.size() + " 条数据"
		}
		return BaseApi.error();
	}
		
}
