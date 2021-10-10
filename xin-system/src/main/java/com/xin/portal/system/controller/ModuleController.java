package com.xin.portal.system.controller;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.BannerService;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.MoContentService;
import com.xin.portal.system.service.ModuleService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.model.Banner;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.MoContent;
import com.xin.portal.system.model.Module;
import com.xin.portal.system.model.ModuleContent;
import com.xin.portal.system.model.OrganizationModule;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;

/**  
* @Title: com.xin.portal.system.controller.ModuleController 
* @Description: 模板管理
* @author xue  
* @date 2018-11-08
* @version V1.0  
*/ 
@Controller
@RequestMapping("/module")
public class ModuleController extends BaseController {

	private static final String PATH = "module/";
	@Autowired
	private ModuleService service;
	@Autowired
	private MoContentService moContentService;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private ConfigService configService;
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.HOMEPAGE_TEMPLATE ,type=LogType.query)
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return PATH + "index";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ADD_TEMPLATE ,type=LogType.add)
	@RequestMapping(value="/add/{id}",method=RequestMethod.GET)
	public String add(@PathVariable String id,Model model){
		model.addAttribute("orgId", id);
		return PATH + "add";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.EDITOR_TEMPLATE ,type=LogType.update)
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(){
		return PATH + "edit";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.HOME_PAGE ,type=LogType.query)
	@RequestMapping(value="/moduleHome/{id}",method=RequestMethod.GET)
	public String moduleHome(@PathVariable String id,Model model){
		model.addAttribute("moduleId", id);
		model.addAttribute("moContentList",moContentService.selectByModuleId(id));	
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		model.addAttribute("sys_theme", config.getValue());
		return PATH + "module_home";
	}
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.TEMPLATE_BACKSTAGE_MANAGEMENT ,type=LogType.query)
	@RequestMapping(value="/editModule/{id}",method=RequestMethod.GET)
	public String editModule(@PathVariable String id,Model model){
		model.addAttribute("moduleId", id);
		model.addAttribute("moContentList",moContentService.selectByModuleId(id));	
		Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		model.addAttribute("sys_theme", config.getValue());
		return PATH + "edit_module";
	}
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(Module query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param Module
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(Module query){
		EntityWrapper<Module> warpper = new EntityWrapper<Module>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param Module
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.DETAILSQUERY ,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		Module result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ADD_AND_SAVE ,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(Module record){
		boolean result=false;
		String creater=SessionUtil.getUserId();
		record.setState("0");
		record.setCreater(creater);
		record.setIsDelete(0);
		record.setIsEdit(1);
		record.setCreateTime(new Date());
		result=record.insert(); 
		OrganizationModule organizationModule=new OrganizationModule();
		organizationModule.setModuleId(record.getId());
		organizationModule.setOrganizationId(record.getOrganizationId());
		result=organizationModule.insert();
		return result ?BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTION_RENEWAL ,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(Module record){
		record.setUpdateTime(new Date());
		//解决layui 开关组件bug
		if(record.getState()==null||record.getState()==""){
			record.setState("1");
		}
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
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.ACTIONDELETE ,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		moContentService.deleteByModuleId(id);
		ModuleContent a=new ModuleContent();
		Wrapper<ModuleContent> wrapper1=new EntityWrapper<ModuleContent>();
		wrapper1.eq("module_id",id);
		a.delete(wrapper1);
		
		OrganizationModule  organizationModule=new OrganizationModule();
		Wrapper<OrganizationModule> wrapper2=new EntityWrapper<OrganizationModule>();
		wrapper2.eq("module_id",id);
		organizationModule.delete(wrapper1);
		
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * @Title: delete 
	 * @Description:  模板组织关联页
	 * @param id
	 * @return BaseApi
	 * @author xue
	 * @Date 2017-7-25 下午3:47:04
	 */
	@RequestMapping(value="/orgModuleIndex/{id}",method=RequestMethod.GET)
	public String orgModuleIndex(@PathVariable String id,Model model){
		model.addAttribute("moduleId", id);
		return PATH + "org_module";
	}
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@Transactional(rollbackFor=Exception.class)
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.TEMPLATE_ORGANIZATION_SAVE ,type=LogType.add)
	@PostMapping("/orgModuleSave")
	@ResponseBody
	public BaseApi orgModuleSave(String moduleId,@RequestParam(value = "roleIds[]",required=false) String[] roleIds){
		OrganizationModule a=new OrganizationModule();
		EntityWrapper<OrganizationModule> wrapper=new EntityWrapper<OrganizationModule>();
		wrapper.eq("module_id",moduleId);
		a.delete(wrapper);
		for (int i = 0; i < roleIds.length; i++) {
            System.out.println(roleIds[i]);
            a.setModuleId(moduleId);
            a.setOrganizationId(roleIds[i]);
            a.insert();
        }
		return BaseApi.success();
		
	}
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author xue
	 * @Date 2018-11-08 
	 */
	@SystemLog(module=LanguageParam.TEMPLATE_MANAGEMENT ,operation=LanguageParam.VIEW_TEMPLATES_AND_ORG , type=LogType.query)
	@PostMapping("/findOrgByModule")
	@ResponseBody
	public List<String> findOrgByModule(OrganizationModule record){
		OrganizationModule a=new OrganizationModule();
		EntityWrapper<OrganizationModule> wrapper=new EntityWrapper<OrganizationModule>();
		wrapper.eq("module_id", record.getModuleId());
		List<OrganizationModule> list=a.selectList(wrapper);
		if (list!=null) {
			return list.stream().map(OrganizationModule::getOrganizationId).collect(Collectors.toList());
		}
		return Lists.newArrayList();
	}
}

