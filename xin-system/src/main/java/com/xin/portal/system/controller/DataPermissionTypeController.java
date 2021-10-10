package com.xin.portal.system.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.model.DataPermission;
import com.xin.portal.system.model.DataPermissionType;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.OrgDataPermission;
import com.xin.portal.system.model.UserDataPermission;
import com.xin.portal.system.service.DataPermissionService;
import com.xin.portal.system.service.DataPermissionTypeService;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.OrgDataPermissionService;
import com.xin.portal.system.service.UserDataPermissionService;
import com.xin.portal.system.util.SessionUtil;

/**
 * @ClassPath: com.xin.portal.system.controller.DataPermissionController 
 * @Description: 数据权限
 * @author zhoujun
 * @date 2017-8-2 下午3:37:27
 */
@Controller
@RequestMapping("/dataPermissionType")
public class DataPermissionTypeController extends BaseController {

	private static final String DIR = "dataPermissionType/";
	
	@Autowired
	private DataPermissionTypeService service;
	@Autowired
	private DataPermissionService dataPermissionService;
	
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
		DataPermissionType dpt = service.selectById(id);
		model.addAttribute("dataPT", dpt);
		return DIR + "edit";
	}
	@RequestMapping("/list")
	@ResponseBody
	public List<DataPermissionType> list(){
		EntityWrapper<DataPermissionType> ew = new EntityWrapper<>();
		ew.orderBy("sort", true);
		return service.selectList(ew);
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(DataPermissionType query){
		//检测 code 重复
		EntityWrapper<DataPermissionType> ew = new EntityWrapper<>();
		ew.eq("extcode", query.getExtcode());
		List<DataPermissionType> moreList = service.selectList(ew);
		if(moreList != null && moreList.size() > 0){
			return BaseApi.error(-2, "To more code");
		}
		query.setCreateTime(new Date());
		query.setCreater(SessionUtil.getUserId());
		return service.insert(query) ? BaseApi.success() : BaseApi.error();
	}
	
	@Transactional(rollbackFor=Exception.class)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(DataPermissionType query){
		//检测 code 重复
		EntityWrapper<DataPermissionType> ew = new EntityWrapper<>();
		ew.eq("extcode", query.getExtcode());
		ew.ne("id", query.getId());
		List<DataPermissionType> moreList = service.selectList(ew);
		if(moreList != null && moreList.size() > 0){
			return BaseApi.error(-2, "To more code");
		}
		query.setUpdateTime(new Date());
		query.setUpdater(SessionUtil.getUserId());
		return service.updateById(query) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		EntityWrapper<DataPermission> ew = new EntityWrapper<>();
		ew.eq("type_id", id);
		List<DataPermission> selectList = dataPermissionService.selectList(ew);
		if(selectList != null && selectList.size() > 0){
			return new BaseApi().put("code", -2);//存在内容，先删除内容。
		}
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}

}
