package com.xin.portal.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
import com.xin.portal.system.model.ResourceType;
import com.xin.portal.system.service.ResourceTypeService;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.portal.system.controller.ResourceTypeController 
* @Description: 资源类型
* @author zhoujun  
* @date 2018-10-31
* @version V1.0  
*/ 
@Controller
@RequestMapping("/resourceType")
public class ResourceTypeController extends BaseController {

	private static final String PATH = "resourceType/";
	
	@Autowired
	private ResourceTypeService service;
	
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
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.RESOURCE_TYPE ,operation=LanguageParam.PAGING_QUERY,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(ResourceType query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param ResourceType
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.RESOURCE_TYPE ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(ResourceType query){
		EntityWrapper<ResourceType> wrapper = new EntityWrapper<ResourceType>(query);
		wrapper.orderBy("sort", true);
		return BaseApi.list(service.selectList(wrapper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param ResourceType
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.RESOURCE_TYPE ,sort="tfPToROut1",tfPToROut1="id,id,name,com.xin.portal.system.mapper.ResourceTypeMapper,selectById",operation=LanguageParam.DETAILSQUERY)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		ResourceType result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.RESOURCE_TYPE ,sort="name",name="name",operation=LanguageParam.ACTIONADD ,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(ResourceType record){
		boolean result = record.insert();
		return result ? BaseApi.data(record) : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	@SystemLog(module=LanguageParam.RESOURCE_TYPE ,sort="name",name="name",operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(ResourceType record){
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
	@SystemLog(module=LanguageParam.RESOURCE_TYPE ,operation=LanguageParam.ACTIONDELETE,sort="tfPToROut1",before="tfPToROut1",
			tfPToROut1="id,id,name,com.xin.portal.system.mapper.ResourceTypeMapper,selectById",type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	
}

