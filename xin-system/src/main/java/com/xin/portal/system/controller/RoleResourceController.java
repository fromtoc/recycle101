package com.xin.portal.system.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.RoleResource;
import com.xin.portal.system.service.RoleResourceService;
import com.xin.portal.system.util.i18n.LanguageParam;

/**
 * @ClassPath: com.xin.portal.system.controller.RoleResourceController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-8-2 上午9:47:15
 */
@Controller
@RequestMapping("/roleResource")
public class RoleResourceController extends BaseController {
	
	@Autowired
	private RoleResourceService service;
	
	/**
	 * @Title: findRole 
	 * @Description:  
	 * 		query的roleId为空时根据resourceId 查roleId
	 * 		query的resourceId为空时根据roleId 查resourceId
	 * @param query
	 * @return List<Integer>
	 * @author zhoujun
	 * @Date 2017-8-2 上午9:56:30
	 */
	@SystemLog(module = LanguageParam.ROLE_RESOURCE_RELATIONSHIP , operation=LanguageParam.RELATIONSHIP_VIEW , type=LogType.query)
	@RequestMapping("/findIds")
	@ResponseBody
	public List<String> findRole(RoleResource query){
		EntityWrapper<RoleResource> ew = new EntityWrapper<>(query);
		if (StringUtils.isNotEmpty(query.getResourceId())) {
			List<RoleResource> list = service.selectList(ew);
			return list.stream().map(RoleResource::getRoleId).collect(Collectors.toList());

		} else {
			List<RoleResource> list = service.selectList(ew);
			return list.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
		}
	}
	
	@SystemLog(module = LanguageParam.ROLE_RESOURCE_RELATIONSHIP , operation=LanguageParam.ADD_RESOURCES_TO_ROLE , type=LogType.add)
	@RequestMapping("/saveByRoleId")
	@ResponseBody
	public BaseApi saveByRoleId(String roleId,String[] ids){
		return service.saveByRoleId(roleId,ids) > 0 ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_RESOURCE_RELATIONSHIP , operation=LanguageParam.RESOURCES_ARE_ADDED_TO_ROLE , type=LogType.add)
	@RequestMapping("/saveByResId")
	@ResponseBody
	public BaseApi saveByResId(String resId, String[] resIds, String[] delRoleIds, String[] saveRoleIds){
		return service.saveByResId(resId,resIds,delRoleIds,saveRoleIds) > 0 ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_RESOURCE_RELATIONSHIP , operation=LanguageParam.ADD_RESOURCES , type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(RoleResource record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_RESOURCE_RELATIONSHIP , operation=LanguageParam.DELETE_RESOURCES, type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(RoleResource record){
		EntityWrapper<RoleResource> ew = new EntityWrapper<>(record);
		return record.delete(ew) ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ROLE_RESOURCE_RELATIONSHIP , operation=LanguageParam.BULK_DELETE_RELATIONSHIP , type=LogType.delete)
	@RequestMapping("/deleteBatch")
	@ResponseBody
	public BaseApi deleteBatch(String roleId,String[] ids){
		EntityWrapper<RoleResource> ew = new EntityWrapper<>();
		ew.eq("role_id", roleId);
		ew.in("resource_id", ids);
		return service.delete(ew) ? BaseApi.success() : BaseApi.error();
	}
	

}
