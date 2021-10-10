package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Permission;
import com.xin.portal.system.model.ResourceType;
import com.xin.portal.system.model.Role;
import com.xin.portal.system.model.RolePermission;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**  
* @Title: com.xin.portal.system.controller.RolePermissionController 
* @Description: 角色-权限
* @author zhoujun  
* @date 2018-11-15
* @version V1.0  
*/ 
@Controller
@RequestMapping("/rolePermission")
public class RolePermissionController extends BaseController {

	private static final String PATH = "rolePermission/";
	
	@Autowired
	private RolePermissionService service;
	@Autowired
	private ResourceTypeService resourceTypeService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private PermissionService permissionService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(Model model){
		EntityWrapper<ResourceType> ew = new EntityWrapper<>();
		ew.eq("parent_id", 0);
		ew.orderBy("sort",true);
		List<ResourceType> resourceTypeList = resourceTypeService.selectList(ew);
		model.addAttribute("resourceTypeList", resourceTypeList);
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

	@SystemLog(module=LanguageParam.PERMISSIONS_USERS,operation=LanguageParam.ACTIVE_SEE, type=LogType.query)
	@RequestMapping(value="/rolePermissionUser",method=RequestMethod.GET)
	public String rolePermissionUser(String resourceId,Model model){
		//获取拥有当前菜单权限的角色
		List<Role> roleList = roleService.selectRoleByResourceId(resourceId);
		model.addAttribute("resourceRole", JSONArray.toJSONString(roleList));
		model.addAttribute("resourceId", resourceId);
		return PATH + "rolePermissionUser";
	}
	
	@RequestMapping("/pagePermissionRoleUser")
	@ResponseBody
	public PageModel<RolePermission> pagePermissionRoleUser(RolePermission query){
		query.setCode("view");
		return service.pagePermissionUser(query);
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-11-15 
	 */
	@SystemLog(module=LanguageParam.ROLE_AUTHORIZATION,operation=LanguageParam.PAGING_QUERY, type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(RolePermission query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-11-15 
	 */
	@SystemLog(module=LanguageParam.ROLE_AUTHORIZATION,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(RolePermission query){
		EntityWrapper<RolePermission> warpper = new EntityWrapper<RolePermission>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.ROLE_AUTHORIZATION,operation=LanguageParam.DETAILSQUERY ,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		RolePermission result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-11-15 
	 */
	@SystemLog(module=LanguageParam.ROLE_AUTHORIZATION,operation=LanguageParam.ACTIONADD ,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(RolePermission record){
		record.setCreater(SessionUtil.getUserId());
		record.setCreateTime(new Date());
		service.insertRolePserssionToMsgCenter(SessionUtil.getUserId(),record,"save");
		String  resourceId = record.getResourceId();
		String roleId = record.getRoleId();
		String permissionId = record.getPermissionId();
		if(resourceId!=null && !"".equals(resourceId)&&roleId!=null&&!"".equals(roleId)&&permissionId!=null&&!"".equals(permissionId)){
			Role role = new Role();
			role.setId(roleId);
			EntityWrapper<Role> ew = new EntityWrapper<>(role);
			role = roleService.selectOne(ew);
			Permission permission = permissionService.selectById(permissionId);
			if(role.getCode().equals("EVERYONE")&& permission.getCode().equals("view")){
				//修改资源编辑中显示的所有人可见字段
				resourceService.updateEveryoneById(resourceId,"on");
			}
		}
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-11-15 
	 */
	@SystemLog(module=LanguageParam.ROLE_AUTHORIZATION,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(RolePermission record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module=LanguageParam.ROLE_AUTHORIZATION,operation=LanguageParam.ACTIONUPDATE ,type=LogType.update)
	@RequestMapping(value="/saveAll",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi saveAll(RolePermission record){
		service.insertRolePserssionToMsgCenter(SessionUtil.getUserId(),record,"saveAll");
		String  resourceId = record.getResourceId();
		String roleId = record.getRoleId();
		Role role = new Role();
		role.setId(roleId);
		EntityWrapper<Role> ew = new EntityWrapper<>(role);
		role = roleService.selectOne(ew);
		if(role.getCode().equals("EVERYONE")){
			if(StringUtils.isNoneEmpty(record.getPermissionId())){
				resourceService.updateEveryoneById(resourceId,"on");
			}else{
				resourceService.updateEveryoneById(resourceId,"off");
			}
		}
		return service.saveAll(record) ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param rolePermission
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.ROLE_AUTHORIZATION,operation=LanguageParam.ACTIONDELETE,type=LogType.delete)
	@RequestMapping(value="/delete")
	@ResponseBody
	public BaseApi delete(RolePermission rolePermission){
		if (StringUtils.isEmpty(rolePermission.getResourceId()) || StringUtils.isEmpty(rolePermission.getRoleId()) || StringUtils.isEmpty(rolePermission.getPermissionId())) {
			return new BaseApi().put("code", -2);//return BaseApi.error("参数错误");
		}
		service.insertRolePserssionToMsgCenter(SessionUtil.getUserId(),rolePermission,"delete");
		String  resourceId = rolePermission.getResourceId();
		String roleId = rolePermission.getRoleId();
		String permissionId = rolePermission.getPermissionId();
		if(resourceId!=null && !"".equals(resourceId)&&roleId!=null&&!"".equals(roleId)&&permissionId!=null&&!"".equals(permissionId)){
			Role role = new Role();
			role.setId(roleId);
			EntityWrapper<Role> ew = new EntityWrapper<>(role);
			role = roleService.selectOne(ew);
			Permission permission = permissionService.selectById(permissionId);
			if(role.getCode().equals("EVERYONE")&& permission.getCode().equals("view")){
				//修改资源编辑中显示的所有人可见字段
				resourceService.updateEveryoneById(resourceId,"off");
			}
		}
		EntityWrapper<RolePermission> ew = new EntityWrapper<>(rolePermission);
		return service.delete(ew) ? BaseApi.success() : BaseApi.error();
	}

}

