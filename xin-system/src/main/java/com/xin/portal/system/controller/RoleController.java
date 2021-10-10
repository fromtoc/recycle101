package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Role;
import com.xin.portal.system.model.RolePermission;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.service.RolePermissionService;
import com.xin.portal.system.service.RoleService;
import com.xin.portal.system.service.RoleUserService;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassPath: com.xin.portal.system.controller.RoleController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-8-1 上午9:37:57
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {

	private static final String DIR = "role/";
	
	@Autowired
	private RoleService service;
	@Autowired
	private RoleUserService roleUserService;
	@Autowired
	private RolePermissionService rolePermissionService;


	@RequestMapping("/index")
	public String index() {
		return DIR + "index";
	}
	
	@RequestMapping("/select")
	public String select() {
		return DIR + "select";
	}

	@RequestMapping("/select/{type}")
	public String selectRole(@PathVariable("type") String type) {
		return DIR + "select_"+type;
	}
	
	@RequestMapping("/add")
	public String add() {
		return DIR + "add";
	}
	
	@RequestMapping("/edit")
	public String edit(Role query, Model model) {
		EntityWrapper<Role> ew = new EntityWrapper<>(query);
		Role role = service.selectOne(ew);
		model.addAttribute("record", role);
		return DIR + "edit";
	}
	
	@SystemLog(module = LanguageParam.ROLE_MANAGEMENT , operation=LanguageParam.ACTIONLISTSEE , type=LogType.query)
	@RequestMapping("/list")
	@ResponseBody
	public List<Role> list(Role query) {
		EntityWrapper<Role> ew = new EntityWrapper<>(query);
		return service.selectList(ew);
	}
	
	@SystemLog(module = LanguageParam.ROLE_MANAGEMENT , operation=LanguageParam.ACTIONADD , sort="name", name="name", type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Role query) {
		boolean isUnique = service.checkUnique(query,"add");

		if(!isUnique){
			return (new BaseApi()).put("code",-2);
		}
		boolean result = query.insert();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_MANAGEMENT , operation=LanguageParam.ACTIONUPDATE , sort="name", name="name", type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(Role query) {
		boolean isUnique = service.checkUnique(query,"update");
		if(!isUnique){
			return new BaseApi().put("code", -2);
		}
		boolean result =query.updateById();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_MANAGEMENT , operation=LanguageParam.ACTIONDELETE , sort="tfToName1", before="tfToName1", 
			tfToName1="query,id,name,com.xin.portal.system.mapper.RoleMapper,selectById", type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(Role query) {
		EntityWrapper<RolePermission> ew = new EntityWrapper<>();
		ew.eq("role_id", query.getId());
		rolePermissionService.delete(ew);
		roleUserService.deleteByRoleId(query.getId());
		return query.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
	
}
