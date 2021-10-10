package com.xin.portal.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Datasource;
import com.xin.portal.system.service.DatasourceService;
import com.xin.portal.system.util.datasource.DataSourceDaoUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.cms.controller.DatasourceController 
* @Description: 数据源管理
* @author zhoujun  
* @date 2020-04-21
* @version V1.0  
*/ 
@Controller
@RequestMapping("/datasource")
public class DatasourceController extends BaseController {
	
	private static String path = "datasource/";

	@Autowired
	private DatasourceService service;
	
	@RequestMapping("/index")
	public String index(){
		return path + "index";
	}
	
	@RequestMapping("/edit")
	public String edit(){
		return path + "edit";
	}
	
	@RequestMapping("/add")
	public String add(){
		return path + "add";
	}
	
	@RequestMapping("/search")
	public String search(Model model){
		List<Datasource> source = service.selectList(null);
		model.addAttribute("dataSourceList", source);
		return path + "search";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2020-04-21 
	 */
	@SystemLog(module=LanguageParam.DATASOURCE_MANAGE, operation=LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping(value="/page",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi page(Datasource query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2020-04-21 
	 */
	@SystemLog(module=LanguageParam.DATASOURCE_MANAGE, operation=LanguageParam.ACTIONADD, type=LogType.add, sort="name", name="sourceName")
	@RequestMapping(value="/save",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi save(Datasource record){
		record.setIsActive(1);
		record.setIsDelete(0);
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2020-04-21 
	 */
	@RequestMapping(value="/list")
	@ResponseBody
	public List<Datasource> list(){
		EntityWrapper<Datasource> ew = new EntityWrapper<>();
		ew.eq("is_active", 1);
		ew.eq("is_delete", 0);
		return service.selectList(ew);
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2020-04-21 
	 */
	@SystemLog(module=LanguageParam.DATASOURCE_MANAGE, operation=LanguageParam.ACTIONUPDATE, 
			type=LogType.update, sort="tfToName1", tfToName1="record,id,sourceName,com.xin.portal.system.mapper.DatasourceMapper,selectById")
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(Datasource record){
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
	@SystemLog(module=LanguageParam.DATASOURCE_MANAGE,operation=LanguageParam.ACTIONDELETE,type=LogType.delete,
			before="tfPToROut1", sort="tfPToROut1", tfPToROut1="id,id,sourceName,com.xin.portal.system.mapper.DatasourceMapper,selectById")
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping(value="/checkDataSource",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi checkDataSource(Datasource record){
		//暂未实现
		String result = DataSourceDaoUtil.checkDatasourceConn(record);
		return "1".equals(result)? BaseApi.success() : BaseApi.error(result);
	}
	
	@RequestMapping(value="/search/sourceTable",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi sourceTable(Datasource record){//根据数据源信息查询表信息
		Datasource ds = service.selectById(record.getId());
		List<Map<String, Object>> list = DataSourceDaoUtil.getAllTablesOfSource(ds);
		return BaseApi.data(list);
	}
	
	@RequestMapping(value="/search/data",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi searchData(Datasource record){
		//根据id获取dataSource 数据源信息
		Datasource ds = service.selectById(record.getId());
		//通过SQL 查询到结果
		List<Map<String, Object>> list = DataSourceDaoUtil.getListBySql(ds, record.getSql());
		return BaseApi.data(list);
	}
	
}

