package com.xin.portal.system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.service.UserOrganizationService;

@Controller
@RequestMapping("/userOrg")
public class UserOrganizationController extends BaseController {
	
	@Autowired
	private UserOrganizationService service;
	
	@RequestMapping("/saveDeputyOrg")
	@ResponseBody
	public BaseApi insertOrUpdateDeputyOrg(String userId, @RequestParam(value="orgIds[]",required=false)List<String> orgIds){
		try {
			return service.insertOrUpdateDeputyOrg(userId, orgIds);
		} catch (Exception e) {
			e.printStackTrace();
			return BaseApi.error(e.getMessage());
		}
	}

}
