package com.xin.portal.system.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.xin.portal.system.model.Role;
import com.xin.portal.system.service.RoleService;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.webSocketLog.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.service.RoleUserService;

/**
 * @ClassPath: com.xin.portal.system.controller.RoleUserController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-8-4 上午11:10:09
 */
@Controller
@RequestMapping("/roleUser")
public class RoleUserController extends BaseController {
	
	@Autowired
	private RoleUserService service;

	@Autowired
	private RoleService roleService;
	
	@SystemLog(module = LanguageParam.ROLE_USER_RELATIONSHIP , operation=LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping("/findRoleByUser")
	@ResponseBody
	public List<String> findRole(RoleUser query){
		EntityWrapper<RoleUser> ew = new EntityWrapper<>(query);
		List<RoleUser> list = service.selectList(ew);

		if (list!=null) {
			EntityWrapper<Role> e = new EntityWrapper();
			e.eq("code","EVERYONE");
			//获得租户对应的所有人EVERYONE的roleId
			String everyOne = roleService.selectOne(e).getId();
			//选中的用户对应的所有roleId的list
			List<String> roleIdlist = list.stream().map(RoleUser::getRoleId).collect(Collectors.toList());
			//将所有人EVERYONE的roleId添加到list的最后一个
			roleIdlist.add(everyOne);
			return  roleIdlist;
		}
		return Lists.newArrayList();
	}
	
	@SystemLog(module = LanguageParam.ROLE_USER_RELATIONSHIP , sort="tfPToROut1,tfPToROut2",
			tfPToROut2="roleIds,id,name,com.xin.portal.system.mapper.RoleMapper,selectById",
			tfPToROut1="userId,id,username,com.xin.portal.system.mapper.UserMapper,selectById", 
			operation=LanguageParam.MODIFY_USER_ROLES , type=LogType.update)
	@RequestMapping("/saveRoleUsersByUserId")
	@ResponseBody
	public BaseApi saveRoleUsersByUserId(String userId,@RequestParam(value = "roleIds[]",required=false) String[] roleIds){
		service.compareAfterUpdateRoleMenuChangeAddMessage(userId,roleIds);
		return service.saveRoleUsersByUserId(userId,roleIds)? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_USER_RELATIONSHIP ,sort="tfPToROut1,tfPToROut2", 
			tfPToROut1="roleId,id,name,com.xin.portal.system.mapper.RoleMapper,selectById",
			tfPToROut2="userIds,id,username,com.xin.portal.system.mapper.UserMapper,selectById", 
			operation=LanguageParam.ADD_USERS , type=LogType.update)
	@RequestMapping("/saveRoleUsersByRoleId")
	@ResponseBody
	public BaseApi saveRoleUsersByRoleId(String roleId,String roleCode,@RequestParam(value = "userIds[]",required=false)String[] userIds){
		service.compareAfterUpdateUsersRoleMenuChangeAddMessage(roleId,userIds,false);
		return service.saveRoleUsersByRoleId(roleId,roleCode,userIds) ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_USER_RELATIONSHIP ,sort="tfToName2,tfToName1",
			tfToName1="query,userId,username,com.xin.portal.system.mapper.UserMapper,selectById",
			tfToName2="query,roleId,name,com.xin.portal.system.mapper.RoleMapper,selectById", 
			operation=LanguageParam.DELETE_USER , type=LogType.update)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(RoleUser query){
		service.compareAfterUpdateUsersRoleMenuChangeAddMessage(query.getRoleId(),new String[]{query.getUserId()},true);
		WebSocketServer web=new WebSocketServer();
		web.onMessage(null,"1");
		return service.delete(query) > 0 ? BaseApi.success() : BaseApi.error();
	}
	

}
