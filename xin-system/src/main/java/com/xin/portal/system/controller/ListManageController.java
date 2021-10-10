package com.xin.portal.system.controller;


import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.ListManageService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.ListManage;
import com.xin.portal.system.model.ListManageResource;
import com.xin.portal.system.model.MoContent;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.SystemLog;

/**  
* @Title: com.xin.portal.system.controller.ListManageController 
* @Description: 列表管理
* @author xue  
* @date 2018-12-10
* @version V1.0  
*/ 
@Controller
@RequestMapping("/listManage")
public class ListManageController extends BaseController {

	private static final String PATH = "listManage/";
	
	@Autowired
	private ListManageService service;
	@Autowired
	private ConfigService configService;
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.HOME_PAGE)
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return PATH + "index";
	}
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.ACTIONADD)
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return PATH + "add";
	}
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.ACTIONUPDATE)
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(){
		return PATH + "edit";
	}
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.EDIT_LIST_RESOURCE)
	@RequestMapping(value="/editList",method=RequestMethod.GET)
	public String editList(){
		return PATH + "edit_list";
	}
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-10 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.PAGING_QUERY)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(ListManageResource query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param ListManage
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-10 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.ACTIONLISTSEE)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(ListManage query){
		EntityWrapper<ListManage> warpper = new EntityWrapper<ListManage>(query);
		warpper.orderBy("sort",true);
		return BaseApi.list(service.selectList(warpper));
	}
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param ListManage
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.DETAILSQUERY)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		ListManage result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-10 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.ADD_AND_SAVE)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(ListManage record){
		record.setCreater(SessionUtil.getUserId());
		return record.insert() ? BaseApi.data(record): BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-10 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.ACTION_RENEWAL)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(ListManage record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-10 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.ACTION_RENEWAL)
	@RequestMapping(value="/updateList",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi updateList(ListManage record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author xue
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.ACTIONDELETE)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		ListManageResource a=new ListManageResource();
		Wrapper<ListManageResource> wrapper =new EntityWrapper<ListManageResource>();
		wrapper.eq("list_id", id);
		a.delete(wrapper);
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-10 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.LIST_BOUND_RESOURCE)
	@PostMapping("/saveList")
	@ResponseBody
	public BaseApi saveList(String listId,@RequestParam(value = "resourceId[]")String[] resourceId){
		ListManageResource a=new ListManageResource();
		for(int i=0;i<resourceId.length;i++){
			a.setListId(listId);
			a.setResourceId(resourceId[i]);
			a.insert();
		}
		return BaseApi.success();
	}
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author xue
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.DELETES_LIST_RESOURCE_RELATIONSHIPS)
	@RequestMapping(value="/deleteListResource",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi deleteListResource(ListManageResource query){
		ListManageResource a=new ListManageResource();
		Wrapper<ListManageResource> wrapper =new EntityWrapper<ListManageResource>();
		wrapper.eq("list_id", query.getListId());
		wrapper.eq("resource_id", query.getResourceId());
		return a.delete(wrapper) ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-12-10 
	 */
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.UPDATE_DOCUMENT_ORDER)
	@RequestMapping(value="/updateListManageResource",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi updateListManageResource(ListManageResource record){
		Wrapper<ListManageResource> wrapper=new EntityWrapper<ListManageResource>();
		wrapper.eq("list_id", record.getListId());
		wrapper.eq("resource_id", record.getResourceId());
		return record.update(wrapper) ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module=LanguageParam.LIST_MANAGER ,operation=LanguageParam.OPEN_LIST)
	@RequestMapping(value="/lists/{id}",method=RequestMethod.GET)
	public String lists(@PathVariable String id,Model model){
		List<Resource> resource =service.selectByListId(id,SessionUtil.getUserId());
		MoContent moContent=new MoContent();
		model.addAttribute("file", resource);
		moContent.setxNum(2);
		model.addAttribute("moContent", moContent);
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		model.addAttribute("sys_theme", config.getValue());
		return PATH+"/lists";
	}
}

