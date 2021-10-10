package com.xin.portal.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.bi.FineReport.FineReportHttpClient;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.util.HttpClientUtils;
import com.xin.portal.system.util.SessionUtil;


@Controller
@RequestMapping("/bi/FineReport")
public class FineReportController {
	
	private static final String DIR = "fineReport/";
	
    @Autowired
    private BiServerService biServerService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
	private BiUserService biUserService;
	
	@RequestMapping("/getToken/{id:.+}")
	public String loginGetToken(@PathVariable("id")String id, Model model) {
		Resource resource = resourceService.selectById(id);
        BiServer biServer = biServerService.selectById(resource.getServerId());
		String userId = SessionUtil.getUserId();
		EntityWrapper<BiUser> ew = new EntityWrapper<>();
		ew.where("id in (select bi_user_id from t_bi_mapping where user_id ={0} and type='FineReport')",userId);
		BiUser biUser = biUserService.selectOne(ew);
		//String token = FineReportHttpClient.getToken(biServer.getUrl(), biUser);
		//model.addAttribute("fine_auth_token", token);
		model.addAttribute("fine_auth_token", JSON.toJSONString(biUser));
		model.addAttribute("linkUrl", resource.getLinkUrl());
		model.addAttribute("baseUrl", biServer.getUrl());
		model.addAttribute("resource", resource);
		return DIR+"container";
	}
	
	
}
