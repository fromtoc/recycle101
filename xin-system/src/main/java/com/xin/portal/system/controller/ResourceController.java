package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.EchartData;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.Series;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * @ClassPath: com.xin.portal.system.controller.ResourceController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-7-19 上午11:05:40
 */
@Controller
@RequestMapping("/resource")
public class ResourceController extends BaseController {
	
	private static final String DIR = "resource/"; 
	
	@Autowired
	private ResourceService service;

	@Autowired
	private ResourceLogService resourceLogservice;
	@Autowired
	private FileService fileService;
	@Autowired
	private ResourceTypeService	resourceTypeService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private CollectService userCollectService;
	@Autowired
	private RolePermissionService rolePermissionService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private BiServerService biServerService;

	@Autowired
	private ResourceDictService resourceDictService;

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private PromptRelService promptRelService;
	
	/**
	 * @Title: index 
	 * @Description:  菜单管理
	 * @return String
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:45:36
	 */
	@RequestMapping("/index")
	public String index(Model model){
		return DIR + "index";
	}
	
	@RequestMapping("/introduce")
	public String introduce(Model model){
		return DIR + "introduce";
	}
	
	/**
	 * 
	 * @Title: permission 
	 * @Description:  资源权限列表
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018年12月29日 下午2:02:32
	 */
	@RequestMapping("/permissions")
	public String permission(Model model){
		return DIR + "permissions";
	}
	
	@SystemLog(module=LanguageParam.RESOURCE_MANAGEMENT ,operation=LanguageParam.RESOURCE_PERMISSION_VIEW ,sort="tfPToROut1",tfPToROut1="id,id,name,com.xin.portal.system.mapper.ResourceMapper,selectById",type=LogType.query)
	@GetMapping("/{id:.+}/permissions")
	@ResponseBody
	public BaseApi permissions(@PathVariable("id")String id){
		Resource resource = service.selectById(id);
		if (resource==null) {
			return BaseApi.error();
		}
		List<Role> roleList = roleService.selectList(null);
		EntityWrapper<Permission> ewPermission = new EntityWrapper<>();
		ewPermission.eq("resource_type_id", resource.getResourceType1());
		ewPermission.orderBy("sort",true);
		//查询该资源对应的大类别所拥有的所有权限permission
		List<Permission> permissions = permissionService.selectList(ewPermission);
		EntityWrapper<RolePermission> ewRolePermission = new EntityWrapper<>();
		ewRolePermission.eq("resource_id", resource.getId());
		//获取该资源所有的角色权限（如 管理员：可见、评论、分享、下载等）
		List<RolePermission> rolePermissions = rolePermissionService.selectList(ewRolePermission);
		List<ResourcePerm> list = Lists.newArrayList();
		for (Role role : roleList) {
			//判断该角色是否是“角色”根节点，如果是跳过循环
			if(role.getCode().equals("role"))
			continue;
			ResourcePerm resourcePerm = new ResourcePerm();
			resourcePerm.setRoleName(role.getName());
			resourcePerm.setRoleId(role.getId());
			
			List<Permission> permissionsAfter = Lists.newArrayList();
			
			Iterator<Permission> iterator = permissions.iterator(); 
			while(iterator.hasNext()){ 
				Permission item = iterator.next();
				Permission permissionNew = item.clone();
				for (RolePermission rolePermission : rolePermissions) {
					if (rolePermission.getPermissionId().equals(item.getId()) && rolePermission.getRoleId().equals(role.getId())) {
						permissionNew.setRolePermissionId(rolePermission.getId());
					}
				}
				permissionsAfter.add(permissionNew); 
			}
			resourcePerm.setPermissions(permissionsAfter);
			list.add(resourcePerm);
		}
		
		return BaseApi.list(list);
	}
	
	@RequestMapping(value="/resource",method=RequestMethod.GET)
	public String resource(String listId,Model model){
		model.addAttribute("listId", listId);
		return DIR + "resource";
	}
	public List<Menu> getParent(Menu menu, List<Menu> list){
		List<Menu> menus = (List<Menu>) SessionUtil.getSession(SessionKeys.USER_MENUS);
		Optional<Menu> opt = menus.stream().filter(item -> item.getId().equals(menu.getParentId())).findFirst();
		if(opt.isPresent()){
			list.add(opt.get());
			if (!"0".equals(opt.get().getParentId())) {
				getParent(opt.get(),list);
			}
		}
		return list;
	}

/*	@SystemLog(module=LanguageParam.RESOURCE_MANAGEMENT ,operation="查看",sort="tfPToROut1",tfPToROut1="id,id,name,com.xin.portal.system.mapper.ResourceMapper,selectById",type=LogType.query)*/
	@RequestMapping("/view/{id:.+}")
	public String view(@PathVariable("id")String id,String from,Model model,HttpServletRequest request){
		Resource resource = service.selectById(id);
		if (resource==null) {
			return DIR + "missing";
		}

		// 预览不验证权限
		if (!"review".equalsIgnoreCase(from)) {
			Subject currentUser = SecurityUtils.getSubject();
			if (!currentUser.isPermitted(resource.getId()+":view")) {
				return "errors/403";
			}
		}
		EntityWrapper<Permission> ew = new EntityWrapper<>();
		ew.eq("resource_type_id", resource.getResourceType1());
		List<Permission> permissions = permissionService.selectList(ew);
		model.addAttribute("permissions", permissions);
		model.addAttribute("resource", resource);
		List<ResourceDict> resourceDictlist= resourceDictService.findList(id);
		if(resourceDictlist.size()>0){
			model.addAttribute("modifyTime",resourceDictlist.get(0).getModifyTimeTran());
			String modifier = resourceDictlist.get(0).getModifier();
			UserInfo userInfo = userInfoService.selectById(modifier);
			modifier=userInfo.getRealname();
			model.addAttribute("modifier",modifier);
		}
		Object menuId = request.getAttribute("menuId");
		if (menuId!=null) {
			//model.addAttribute("menuId", menuId.toString());
		} else {
			List<String> path = Lists.newArrayList();
			if ("home".equals(from)) {
				path.add(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.HOME_PAGE));
			} else if ("collect".equals(from)) {
				path.add(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.MY_COLLECTION));
			} else if ("search".equals(from)) {
				path.add(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.ACTION_SEARCH));
			}
			path.add(resource.getName());
			model.addAttribute("pathList", path);
		}
		
		model.addAttribute("introduce", resource.getIntroduce());
		
		//收藏信息
		EntityWrapper<UserCollect> ewUserCollect = new EntityWrapper<>();
		ewUserCollect.eq("resource_id", id);
		ewUserCollect.eq("user_id", SessionUtil.getUserId());
		UserCollect userCollect= userCollectService.selectOne(ewUserCollect);
		model.addAttribute("collected", userCollect==null?false:true);
		
		
		//资源访问记录
		ResourceLog resourceLog = new ResourceLog();
		resourceLog.setCreater(SessionUtil.getUserId());
		resourceLog.setCreateTime(new Date());
		resourceLog.setType(ResourceLog.TYPE_VIEW);
		resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
		resourceLog.setResourceId(id);
		resourceLog.setIp(WebUtil.getIpAddr(request));
		resourceLog.setBrowser(WebUtil.getBrower(request));
		resourceLogservice.save(resourceLog);
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		switch(resourceType.getCode()+""){
		case ResourceType.FUNCTION:
			return "forward:"+resource.getLinkUrl();
		case ResourceType.LINK:
			model.addAttribute("url", resource.getLinkUrl());
			return DIR + "container";
		case ResourceType.MSTR_DOSSIER:
			return "forward:/bi/mstr/dossier/"+id;
		case ResourceType.MSTR_REPORT:
		case ResourceType.MSTR_DOCUMENT:
			return "forward:/bi/mstr/container/"+id;
		case ResourceType.TABLEAU://tableau
			return "forward:/bi/tableau/report/"+id;
		case ResourceType.FINEREPORT://finereport
			//return "redirect:"+resource.getLinkUrl();
			return "forward:/bi/FineReport/getToken/"+id;
		case ResourceType.BO://bo
			//return "forward:/bi/bo/container/"+resource.getProjectId()+"/"+resource.getLinkUrl();//sdk获取session
			from = "review".equals(from)?"review":"view";
			return "forward:/bi/bo/boCntainer/"+from+"/"+id;
		case ResourceType.DOCUMENT://文档
			return "forward:/file/view/"+resource.getFileId();
		case ResourceType.SMARTBI://smartBi报表
			return "forward:/smartBI/view/"+id;
		case ResourceType.QLIK://QLIK报表
			return "forward:/qlik/view/"+id;
		case ResourceType.COGNOS://COGNOS报表
			return "forward:/bi/cognos/report/"+id;
		}
		return "forward:"+resource.getLinkUrl();
	}
	/**
	 * 移动端查看报表
	 * @param id
	 * @param from
	 * @param model
	 * @param request
	 * @param userId
	 * @return
	 */
	@RequestMapping("/mobile/view/{id:.+}")
	public String mobileView(@PathVariable("id")String id,String from,Model model,HttpServletRequest request,@ModelAttribute("userId")String userId){
		Resource resource = service.selectById(id);
		if (resource==null) {
			model.addAttribute("Msg","Sorry,资源丢失");
			return DIR + "mobile_error_msg";
		}
		if(userId==null||"".equals(userId)){
			model.addAttribute("Msg","Sorry,报表访问失败");
			return DIR + "mobile_error_msg";
		}

		EntityWrapper<Permission> ew = new EntityWrapper<>();
		ew.eq("resource_type_id", resource.getResourceType1());
		List<Permission> permissions = permissionService.selectList(ew);
		model.addAttribute("permissions", permissions);
		model.addAttribute("resource", resource);
		List<ResourceDict> resourceDictlist= resourceDictService.findList(id);
		if(resourceDictlist.size()>0){
			model.addAttribute("modifyTime",resourceDictlist.get(0).getModifyTimeTran());
			String modifier = resourceDictlist.get(0).getModifier();
			UserInfo userInfo = userInfoService.selectById(modifier);
			modifier=userInfo.getRealname();
			model.addAttribute("modifier",modifier);
		}
		Object menuId = request.getAttribute("menuId");
		if (menuId!=null) {
			//model.addAttribute("menuId", menuId.toString());
		} else {
			List<String> path = Lists.newArrayList();
			if ("home".equals(from)) {
				path.add(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.HOME_PAGE));
			} else if ("collect".equals(from)) {
				path.add(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.MY_COLLECTION));
			} else if ("search".equals(from)) {
				path.add(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(),LanguageParam.ACTION_SEARCH));
			}
			path.add(resource.getName());
			model.addAttribute("pathList", path);
		}
		
		model.addAttribute("introduce", resource.getIntroduce());
		//model.addAttribute("isMobile", resource.getIntroduce());
		
		UserInfo userInfo=userInfoService.selectById(userId);
		//资源访问记录
		ResourceLog resourceLog = new ResourceLog();
		resourceLog.setCreater(userInfo.getId());
		resourceLog.setCreateTime(new Date());
		resourceLog.setType(ResourceLog.TYPE_VIEW);
		resourceLog.setCreaterName(userInfo.getRealname());
		resourceLog.setResourceId(id);
		resourceLog.setIp(WebUtil.getIpAddr(request));
		resourceLog.setBrowser(WebUtil.getBrower(request));
		resourceLogservice.save(resourceLog);
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		switch(resourceType.getCode()+""){
		case ResourceType.FUNCTION:
			return "forward:"+resource.getLinkUrl();
		case ResourceType.LINK:
			model.addAttribute("url", resource.getLinkUrl());
			return DIR + "container";
		case ResourceType.MSTR_DOSSIER:
			return "forward:/bi/mstr/mobile/dossier/"+id+"/"+userId;
		case ResourceType.MSTR_REPORT:
		case ResourceType.MSTR_DOCUMENT:
			return "forward:/bi/mstr/mobile/container/"+id;
		case ResourceType.TABLEAU://tableau
			return "forward:/bi/tableau/report/"+id;
		case ResourceType.FINEREPORT://finereport
			//return "redirect:"+resource.getLinkUrl();
			return "forward:/bi/FineReport/getToken/"+id;
		case ResourceType.BO://bo
			//return "forward:/bi/bo/container/"+resource.getProjectId()+"/"+resource.getLinkUrl();//sdk获取session
			from = "review".equals(from)?"review":"view";
			return "forward:/bi/bo/boCntainer/"+from+"/"+id;
		case ResourceType.DOCUMENT://文档
			return "forward:/file/view/"+resource.getFileId()+"/"+resource.getName();
		case ResourceType.SMARTBI://smartBi报表
			return "forward:/smartBI/view/"+id;
		case ResourceType.QLIK://QLIK报表
			return "forward:/qlik/view/"+id;
		case ResourceType.COGNOS://COGNOS报表
			return "forward:/bi/cognos/report/"+id;
		}
		return "forward:"+resource.getLinkUrl();
	}
	
	@RequestMapping("/download/{id}")
	public String download(@PathVariable("id")String id,String from,Model model,HttpServletRequest request){
		Resource resource = service.selectById(id);
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		if (resource==null || !ResourceType.DOCUMENT.equals(String.valueOf(resourceType.getCode()))
				|| StringUtils.isEmpty(resource.getFileId())) {
			return DIR + "missing";
		}
		//资源访问记录
		ResourceLog resourceLog = new ResourceLog();
		resourceLog.setCreater(SessionUtil.getUserId());
		resourceLog.setCreateTime(new Date());
		resourceLog.setType(ResourceLog.TYPE_DOWNLOAD);
		resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
		resourceLog.setResourceId(id);
		resourceLog.setIp(WebUtil.getIpAddr(request));
		resourceLog.setBrowser(WebUtil.getBrower(request));
		resourceLogservice.save(resourceLog);
		
		return "forward:/file/download/"+resource.getFileId();
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:46:03
	 */
	@RequestMapping("/add/{type}")
	public String add(@PathVariable("type")String type,String resourceType1,Model model){
		EntityWrapper<ResourceType> ew = new EntityWrapper<>();
		ew.eq("parent_id", resourceType1);
		List<ResourceType> list = resourceTypeService.selectList(ew);
		model.addAttribute("resourceType2List", list);
		if ("dossier".equals(type)){
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_MSTR_LIBRARY);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("bo".equals(type)){
			//没有项目概念，但是不使用sdk获取服务的。如果使用sdk获取服务就将此段判断代码删去，且使用bo_sdk
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_BO);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("tableau".equals(type)) {//没有项目的概念，且不能获取到服务器中的列表的。
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_TABLEAU);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("finereport".equals(type)) {//没有项目的概念，且不能获取到服务器中的列表的。
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_FIREREPORT);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("smartbi".equals(type)) {//没有项目的概念，且不能获取到服务器中的列表的。
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_SMARTBI);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		} 
		if ("cognos".equals(type)) {//没有项目的概念，且不能获取到服务器中的列表的。
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_COGNOS);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		return DIR + type;
	}
	
	@RequestMapping("/select")
	public String select(Resource resource,Model model){
		List<ResourceType> resourceType = resourceTypeService.selectList(null);
		model.addAttribute("resourceTypes", JSONObject.toJSONString(resourceType));
		model.addAttribute("query", resource);
		return DIR + "select";
	}
	
	@RequestMapping("/iconSelect")
	public String iconSelect(Model model){
		return DIR + "icon_select";
	}
	
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @return String
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:46:15
	 */
	@RequestMapping("/edit/{type}/{id}")
	public String edit(@PathVariable("type")String type,@PathVariable("id")String id,String resourceType1, Model model){
		Resource resource = service.selectById(id);
		model.addAttribute("record", resource);
		EntityWrapper<ResourceType> ew = new EntityWrapper<>();
		ew.eq("parent_id", resourceType1);
		List<ResourceType> list = resourceTypeService.selectList(ew);
		model.addAttribute("resourceType2List", list);
		if ("dossier".equals(type)){
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_MSTR_LIBRARY);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("bo".equals(type)){
			//没有项目概念，但是不使用sdk获取服务的。如果使用sdk获取服务就将此段判断代码删去，且使用bo_sdk
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_BO);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("tableau".equals(type)) {
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_TABLEAU);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("finereport".equals(type)) {//没有项目的概念，且不能获取到服务器中的列表的。
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_FIREREPORT);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("smartbi".equals(type)) {
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_SMARTBI);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		if ("cognos".equals(type)) {//没有项目的概念，且不能获取到服务器中的列表的。
			EntityWrapper<BiServer> serverEw = new EntityWrapper<>();
			serverEw.eq("type",BiServer.TYPE_COGNOS);
			List<BiServer> servers = biServerService.selectList(serverEw);
			model.addAttribute("servers",servers);
		}
		return DIR + type;
	}
	
	@SystemLog(module=LanguageParam.RESOURCE_MANAGEMENT ,operation=LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(Resource query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	@GetMapping("/listPage")
	@ResponseBody
	public BaseApi listPage(Resource query, Integer pageNumber, Integer pageSize,String listId){
		ListManageResource a=new ListManageResource();
		Wrapper<ListManageResource> wrapper =new EntityWrapper<ListManageResource>();
		wrapper.eq("list_id", listId);
		List<ListManageResource> list=a.selectList(wrapper);
		List<String> resourceId=new ArrayList<String>();
		for(ListManageResource i:list){
			resourceId.add(i.getResourceId());
		}
		return BaseApi.page(service.listPage(query,pageNumber,pageSize,resourceId));
	}
	@SystemLog(module=LanguageParam.RESOURCE_MANAGEMENT ,operation=LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/pageRoleResource")
	@ResponseBody
	public BaseApi pageRoleResource(Resource query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.pageRoleResource(query,pageNumber,pageSize));
	}
	
	@SystemLog(module=LanguageParam.RESOURCE_MANAGEMENT ,operation=LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/pageResourcePerm")
	@ResponseBody
	public BaseApi page(ResourcePerm query, Integer pageNumber, Integer pageSize){
		query.setTenantId(getTenantId());
		if(query.getResourceType1()!=null && !"".equals(query.getResourceType1())){
			String [] arr = query.getResourceType1().split(",");
			query.setResourceType1(arr[0]);
		}
		return BaseApi.page(service.pageResourcePerm(query,pageNumber,pageSize));
	}
	
	
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param query
	 * @return List<Resource>
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:46:30
	 */
	@SystemLog(module = LanguageParam.RESOURCE_MANAGEMENT , operation=LanguageParam.ACTIONLISTSEE , type=LogType.query)
	@RequestMapping("/list")
	@ResponseBody
	public List<Resource> list(Resource query){
		EntityWrapper<Resource> ew = new EntityWrapper<>(query);
		ew.orderBy("sort",true);
		return service.selectList(ew);
	}
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:46:45
	 */
	@SystemLog(module = LanguageParam.RESOURCE_MANAGEMENT ,sort="name", name="1,name" ,type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(HttpServletRequest request,Resource query, 
			@RequestParam(value="thumbFile",required=false) MultipartFile thumbFile,
			@RequestParam(value="docFile",required=false) MultipartFile docFile,String reports){
		if (StringUtils.isNotEmpty(query.getLinkUrl())) {
			query.setLinkUrl(query.getLinkUrl().trim());
		}
		query.setCreater(SessionUtil.getUserId());
		ResourceType resourceType = resourceTypeService.selectById(query.getResourceType1());
		//快捷码校验唯一
		if (StringUtils.isNotEmpty(query.getCode())) {
			EntityWrapper<Resource> checkEw = new EntityWrapper<>();
			checkEw.eq("code", query.getCode());
			Resource unique = service.selectOne(checkEw);
			if (unique!=null && !unique.getId().equals(query.getId())) {
				return new BaseApi().put("code","3");//快捷码重复
			}
		}
//		if (StringUtils.isNotEmpty(reports)) {
//			List<Resource> list = JSON.parseArray(reports, Resource.class);
//			for (Resource res : list ) {
//				res.setParentId(query.getParentId());
//				res.setLinkType(2);
//				res.setHiddenSections(query.getHiddenSections());
//				res.setType(ResourceReport.TYPE_REPORT);
//			}
//			boolean result = service.saveBatch(list);
//			return result ? BaseApi.success() : BaseApi.error();
//		} else {
		if ((thumbFile != null && !thumbFile.isEmpty()) 
				|| (docFile!=null && !docFile.isEmpty())) {
				String con = request.getSession().getServletContext().getRealPath("");
				String webappPath,uploadPath;
		    	if (con.endsWith("/")) {
		    		webappPath=con.substring(0, con.lastIndexOf("/"));
		    		uploadPath = webappPath.substring(0, webappPath.lastIndexOf("/"))+ File.separator + "upload" + File.separator + "resource";
		        }else{
		        	webappPath = con.substring(0, con.lastIndexOf("\\"));
		        	uploadPath = webappPath.substring(0, webappPath.lastIndexOf("\\"))+ File.separator + "upload" + File.separator + "resource";
		        }
				
				if (ResourceType.DOCUMENT.equals(resourceType.getCode()+"")) {
					if (docFile != null && docFile.getSize() > 0) {
						FileModel resourceFile = fileService.upload(uploadPath, "/upload/resource", docFile, null,false, FileModel.RESOURCE);
						//转化为pdf
						fileService.transformToPdf(resourceFile);
						query.setFileId(resourceFile.getId());
					}
				}
				if(thumbFile != null && !thumbFile.isEmpty()){
					if (thumbFile != null && thumbFile.getSize() > 0) {
						FileModel savedFile = fileService.upload(uploadPath, "/upload/resource", thumbFile, null, true, FileModel.THUMBNAIL);
						query.setThumbnail(savedFile.getFilePathView());
						query.setThumbnailId(savedFile.getId());
					}
				}
			}
			if (StringUtils.isNotEmpty(query.getResourceType2())) {
				query.setType(query.getResourceType2());
			} else {
				query.setType(query.getResourceType1());
			}
			query.setState(1);
			query.setViewNum("0");
			query.setDownloadNum("0");
			query.setCollectNum("0");
			query.setCommentNum("0");
			Resource result= service.save(query);
			return result!=null ? BaseApi.data(result) : BaseApi.error();
//		}
	}
	
	@SystemLog(module = LanguageParam.RESOURCE_MANAGEMENT ,operation = LanguageParam.ACTIONUPDATE ,sort="name", name="name" ,type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(HttpServletRequest request,Resource query,
			@RequestParam(value="thumbFile",required=false) MultipartFile thumbFile, 
			@RequestParam(value="docFile",required=false) MultipartFile docFile){
		if (StringUtils.isNotEmpty(query.getLinkUrl())) {
			query.setLinkUrl(query.getLinkUrl().trim());
		}
		query.setUpdateTime(new Date());
		ResourceType resourceType = resourceTypeService.selectById(query.getResourceType1());
		//快捷码校验唯一
		if (StringUtils.isNotEmpty(query.getCode())) {
			EntityWrapper<Resource> checkEw = new EntityWrapper<>();
			checkEw.eq("code", query.getCode());
			Resource unique = service.selectOne(checkEw);
			if (unique!=null && !unique.getId().equals(query.getId())) {
				return new BaseApi().put("code","3");
			}
		}
		if ((thumbFile != null && !thumbFile.isEmpty()) 
				|| (docFile!=null && !docFile.isEmpty())) {
			String con = request.getSession().getServletContext().getRealPath("");
			String webappPath,uploadPath;
	    	if (con.endsWith("/")) {
	    		webappPath=con.substring(0, con.lastIndexOf("/"));
	    		uploadPath = webappPath.substring(0, webappPath.lastIndexOf("/"))+ File.separator + "upload" + File.separator + "resource";
	        }else{
	        	webappPath = con.substring(0, con.lastIndexOf("\\"));
	        	uploadPath = webappPath.substring(0, webappPath.lastIndexOf("\\"))+ File.separator + "upload" + File.separator + "resource";
	        }
			if(resourceType.getCode() !=null && ResourceType.DOCUMENT.equals(String.valueOf(resourceType.getCode()))){
				if(docFile != null && !docFile.isEmpty()){
					FileModel resourceFile = fileService.upload(
							uploadPath + File.separator + "document" , "/upload/resource/document", docFile, null,false, FileModel.RESOURCE);
					//如果是文档的其他形式会转化为pdf（方便预览）
					fileService.transformToPdf(resourceFile);
					query.setFileId(resourceFile.getId());
				}
			}
			if(thumbFile != null && !thumbFile.isEmpty()){
				FileModel savedFile = fileService.upload(
						uploadPath + File.separator +"thumbnail", "/upload/resource/thumbnail", thumbFile, null,true, FileModel.THUMBNAIL);
				query.setThumbnail(savedFile.getFilePathView());
				query.setThumbnailId(savedFile.getId());
			}
		}
		if (StringUtils.isNotEmpty(query.getResourceType2())) {
			query.setType(query.getResourceType2());
		} else {
			query.setType(query.getResourceType1());
		}
		boolean result = query.updateById();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.RESOURCE_MANAGEMENT ,operation =LanguageParam.DELETE_RESOURCES ,sort="tfToName1", before="tfToName1", tfToName1="record,id,name,com.xin.portal.system.mapper.ResourceMapper,selectById" ,type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(Resource record){
		
		return service.delete(record) ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * 查询总资源数和当月活跃资源数
	 * */
	@RequestMapping("/overviewResource")
	@ResponseBody
	public Resource overviewResource(){
		 int resourceData=service.resourceData();
		  int resourceActive=service.resourceActive();
		  String  resourceActivity=resourceActive*100/resourceData+"%";
		Resource resource=new Resource();
		resource.setResourceData(resourceData);
		resource.setResourceActive(resourceActive);
		resource.setResourceActivity(resourceActivity);

		 return resource;
	}
/**
 * 查询当月资源大类访问占比
 * */
	@RequestMapping("chart")
	@ResponseBody
	public Map<String,List> chart(){
	    Map<String,List> map = new HashMap<>();
	List<Resource> resources=service.chart();
	List<Integer> states=new ArrayList<>();
	List<String> names=new ArrayList<>();
	for(Resource r:resources){
		states.add(r.getState());
		names.add(r.getUsername());
	}
	map.put("states",states);
        map.put("names",names);
        return map;
	}

	

}
