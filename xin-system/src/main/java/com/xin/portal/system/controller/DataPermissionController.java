package com.xin.portal.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.model.DataPermission;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.OrgDataPermission;
import com.xin.portal.system.model.UserDataPermission;
import com.xin.portal.system.service.DataPermissionService;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.OrgDataPermissionService;
import com.xin.portal.system.service.UserDataPermissionService;

/**
 * @ClassPath: com.xin.portal.system.controller.DataPermissionController 
 * @Description: 数据权限
 * @author zhoujun
 * @date 2017-8-2 下午3:37:27
 */
@Controller
@RequestMapping("/dataPermission")
public class DataPermissionController extends BaseController {

	private static final String DIR = "dataPermission/";
	
	@Autowired
	private DataPermissionService service;
	@Autowired
	private OrgDataPermissionService orgDataPermissionService;
	@Autowired
	private DictService dictService;
	@Autowired
	private UserDataPermissionService userDataPermissionService;
	
	@RequestMapping("/index")
	public String index(Model model){
		return DIR + "index";
	}
	/**
	 * 数据权限新增页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String add(Model model){
		return DIR + "add";
	}
	/**
	 * 数据权限编辑页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		DataPermission dataPermission = service.selectById(id);
		model.addAttribute("dataPer", dataPermission);
		return DIR + "edit";
	}
	/**
	 * 组织-数据权限选择页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/orgSelect")
	public String orgSelect(String orgId, Model model){
		model.addAttribute("orgId", orgId);
		return DIR + "orgSelect";
	}
	
	/**
	 * 用户-数据权限选择页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/userSelect")
	public String userSelect(String userId, String orgId, Model model){
		model.addAttribute("userId", userId);
		model.addAttribute("orgId", orgId);
		return DIR + "userSelect";
	}
	
	@RequestMapping("/userDataPermission")
	public String userDataPermission(String userId, String orgId, Model model){
		model.addAttribute("userId", userId);
		model.addAttribute("orgId", orgId);
		return "userInfo/" + "userDataPermission";
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi selpageect(DataPermission query){
		return BaseApi.page(service.page(query));
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(DataPermission query){
		EntityWrapper<DataPermission> ew = new EntityWrapper<>();
		ew.eq("extcode", query.getExtcode());
		List<DataPermission> list = service.selectList(ew);
		if(list != null && list.size() > 0){
			return BaseApi.error(-2, "To more code");
		}
		return service.insert(query) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(DataPermission query){
		EntityWrapper<DataPermission> ew = new EntityWrapper<>();
		ew.eq("extcode", query.getExtcode());
		ew.ne("id", query.getId());
		List<DataPermission> list = service.selectList(ew);
		if(list != null && list.size() > 0){
			return BaseApi.error(-2, "To more code");
		}
		return service.updateById(query) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(DataPermission query){
		EntityWrapper<OrgDataPermission> ew = new EntityWrapper<>();
		ew.eq("dp_id", query.getId());
		orgDataPermissionService.delete(ew);
		EntityWrapper<UserDataPermission> ewu = new EntityWrapper<>();
		ewu.eq("dp_id", query.getId());
		userDataPermissionService.delete(ewu);
		return service.deleteById(query.getId()) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/dptype")
	@ResponseBody
	public List<Dict> dptype(){
		Dict dict = new Dict();
		dict.setDictCode("data_permission_type");
		return dictService.listItem(dict);
	}
	
	/**
	 * 查询组织的数据权限
	 * @param query
	 * @return
	 */
	@RequestMapping("/orgDataPage")
	@ResponseBody
	public BaseApi orgDataPage(OrgDataPermission query){
		return BaseApi.page(service.pageByOrg(query));
	}
	
	@RequestMapping("/userDataPage")
	@ResponseBody
	public BaseApi userDataPage(UserDataPermission query){
		return BaseApi.page(userDataPermissionService.pageUserDataPermission(query));
	}
	/**
	 * 保存组织数据权限关系
	 * @param query
	 * @return
	 */
	@RequestMapping("/saveOrgDataPermission")
	@ResponseBody
	public BaseApi saveOrgDataPermission(String orgId, @RequestParam(value = "dataPermissions[]",required=false)String[] dataPermissions){
		//防止用户权限重复，先删除新组织权限中用户已经有的数据权限（让用户使用组织权限）
		userDataPermissionService.deleteUserPerOfOrg(orgId,dataPermissions);
		//保存组织数据权限
		return orgDataPermissionService.saveOrgDataPermission(orgId,dataPermissions) ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * 删除组织关系表
	 * @param query
	 * @return
	 */
	@RequestMapping("/deleteByOrg")
	@ResponseBody
	public BaseApi deleteByOrg(OrgDataPermission query){
		return service.deleteByOrg(query) > 0 ? BaseApi.success():BaseApi.error();
	}
	/**
	 * 保存用户数据权限关系
	 * @param query
	 * @return
	 */
	@RequestMapping("/saveUserDataPermission")
	@ResponseBody
	public BaseApi saveUserDataPermission(String userId, @RequestParam(value = "dataPermissions[]",required=false)String[] dataPermissions){
		return userDataPermissionService.saveUserDataPermission(userId,dataPermissions) ? BaseApi.success() : BaseApi.error();
	}
	/**
	 * 删除用户关系表
	 * @param query
	 * @return
	 */
	@RequestMapping("/deleteByUser")
	@ResponseBody
	public BaseApi deleteByUser(UserDataPermission query){
		return userDataPermissionService.deleteByUser(query) > 0 ? BaseApi.success():BaseApi.error();
	}

}
