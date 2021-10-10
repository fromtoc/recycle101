package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.crystaldecisions.thirdparty.com.ooc.OB.Logger;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.model.User;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.model.UserOrganization;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.OrganizationService;
import com.xin.portal.system.service.UserInfoService;
import com.xin.portal.system.service.UserOrganizationService;
import com.xin.portal.system.service.UserService;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassPath: com.xin.portal.system.controller.OrganizationController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-8-2 下午3:37:27
 */
@Controller
@RequestMapping("/organization")
public class OrganizationController extends BaseController {

	private static final String DIR = "organization/";
	
	@Autowired
	private ConfigService configService;

	@Autowired
	private OrganizationService service;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserOrganizationService userOrganizationService;
	
	
	
	@RequestMapping("/index")
	public String index() {
		return DIR + "index";
	}
	
	@RequestMapping("/add")
	public String add() {
		return DIR + "add";
	}

	@RequestMapping("/select/{type}")
	public String select(@PathVariable("type") String type) {
		return DIR + "select_"+type;
	}
	
	@RequestMapping("/edit")
	public String edit(Organization query, Model model) {
		Organization record = query.selectById();
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public List<Organization> list(Organization query) {
		EntityWrapper<Organization> ew = new EntityWrapper<>();
		ew.orderBy("sort",true);
		String LOGIN_OPTION = configService.findByCode(Constant.ConfigKeys.LOGIN_OPTION, getTenantId()).getValue();
		if("2".equals(LOGIN_OPTION)){
			ew.eq("status","2");
		}else{
			ew.notIn("status","2");
		}
		return service.selectList(ew);
	}
	
	@SystemLog(module = LanguageParam.ORGANIZATIONAL_MANAGEMENT , operation=LanguageParam.ACTIONLISTSEE, type=LogType.query)
	@RequestMapping("/listAll")
	@ResponseBody
	public List<Organization> listAll(Organization query) {
		EntityWrapper<Organization> ew = new EntityWrapper<>(query);
		return service.selectList(ew);
	}
	
	@RequestMapping("/selectTree")
	@ResponseBody
	public List<JSONObject> selectTree(Organization query) {
		EntityWrapper<Organization> ew = new EntityWrapper<>(query);
		List<Organization> list = service.selectList(ew);
		List<JSONObject> objList = new ArrayList<>();
		for (Organization org : list) {
			JSONObject obj = new JSONObject();
			obj.put("id", org.getCode());
			obj.put("name", org.getName());
			objList.add(obj);
		}
		return objList;
	}
	
	@SystemLog(module = LanguageParam.ORGANIZATIONAL_MANAGEMENT ,sort="name",name="name", operation=LanguageParam.ACTIONADD, type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Organization query) {
		Organization a=new Organization();
		a.setExtCode(query.getExtCode());
		EntityWrapper<Organization> ew = new EntityWrapper<>(a);
		Organization rec = service.selectOne(ew);
		if(rec!=null){
			return new BaseApi().put("code","3");//组织编码不能重复
			/*return BaseApi.error("组织编码不能重复！");*/
		}
		EntityWrapper<Organization> ewCode = new EntityWrapper<>();
		ewCode.eq("parent_id", query.getParentId());
		ewCode.orderBy("code", false);
		Organization maxCodeRec = service.selectOne(ewCode);
		if (maxCodeRec!=null) {
			String code = maxCodeRec.getCode().substring(0,maxCodeRec.getCode().length()-3);
			Integer codeEnd = Integer.valueOf(maxCodeRec.getCode().substring(maxCodeRec.getCode().length()-3)) + 1;
//			String code = "";
			if (codeEnd>999){
				query.setCode(null);
			} else if (codeEnd>99) {
				query.setCode(code.concat(codeEnd.toString()));
			} else if (codeEnd>9) {
				query.setCode(code.concat("0").concat(codeEnd.toString()));
			} else {
				query.setCode(code.concat("00").concat(codeEnd.toString()));
			}
		}else {
			Organization parentInfo = service.selectById(query.getParentId());
			String code = parentInfo.getCode();
			query.setCode(code.concat("001"));
		}
		query.setStatus(1);//状态，可编辑
		boolean result = query.insert();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ORGANIZATIONAL_MANAGEMENT , sort="name",name="name", operation=LanguageParam.ACTIONUPDATE, type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(Organization query) {
		Organization a=new Organization();
		a.setExtCode(query.getExtCode());
		EntityWrapper<Organization> ew = new EntityWrapper<>(a);
		ew.ne("id", query.getId());
		Organization rec = service.selectOne(ew);
		if(rec!=null){
			return new BaseApi().put("code","3");
		}
		boolean result = query.updateById();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.ORGANIZATIONAL_MANAGEMENT , sort="tfToName1",before="tfToName1", operation=LanguageParam.ACTIONDELETE, 
			tfToName1="query,id,name,com.xin.portal.system.mapper.OrganizationMapper,selectById", type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(Organization query) {
		String orgId = query.getId();				
		UserInfo userInfo=new UserInfo();
		userInfo.setOrgId(orgId);
		List<UserInfo> userList = userInfoService.selectUser(userInfo);
		if (!userList.isEmpty()) {
			return BaseApi.error("");
		}
		//删除组织与用户关系
		EntityWrapper<UserOrganization> ew = new EntityWrapper<>();
		ew.eq("org_id", orgId);
		userOrganizationService.delete(ew);
		return service.deleteById(orgId) ? BaseApi.success() : BaseApi.error();
	}
	@RequestMapping("/selectOrganizationById")
	@ResponseBody
	public Organization selectOrganizationById(Organization query) {
		Organization o=new Organization();
		o.setId(query.getId());
		return o.selectById();
	}
	
	@RequestMapping("/findOrgByUser")
	@ResponseBody
	public BaseApi findOrgByUser(String userId) {
		List<Organization> list = userOrganizationService.selectOrgbyUserId(userId);
		return BaseApi.data(list);
	}
	
	/**
	 * @Title: updateBatch 
	 * @Description:  数据迁移时批量更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018年3月9日 下午6:23:14
	 */
	/*@RequestMapping("/updateBatch")
	@ResponseBody
	public BaseApi updateBatch(Organization query) {
		
		int s = 0;
		for (int j=518;j<628;j++) {
			Organization parentQuery = new Organization();
			parentQuery.setId(j);
			Organization parent = service.find(parentQuery);
			query.setParentId(j);
			List<Organization> listc = service.findList(query);
			
			for (int i=1;i<=listc.size();i++) {
				Organization o = listc.get(i-1);
				String co = parent.getCode();
				if (i>9) {
					co += "0"+i;
				} else {
					co += "00"+i;
				}
				s++;
				o.setCode(co);
				o.setParentId(j);
				o.setStatus(0);
				service.update(o);
				
			}
			
		}
		System.out.println("count:"+s);
		return BaseApi.success();
	}*/

}
