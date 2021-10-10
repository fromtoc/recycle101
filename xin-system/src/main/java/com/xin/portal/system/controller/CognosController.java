package com.xin.portal.system.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.bi.bo.BoController;
import com.xin.portal.bi.tableau.TableauRestApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ResourceType;
import com.xin.portal.system.service.BiMappingService;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.PromptRelService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.service.ResourceTypeService;
import com.xin.portal.system.util.DateUtil;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.datasource.DataSourceUtils;

/**
 * 
 * @author xue
 *
 */
@Controller
@RequestMapping("/bi/cognos")
@Api(value="Cognos接口",tags={"Cognos报表集成"})
public class CognosController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(TableauController.class);
	
	private static final String DIR = "cognos/";;
	@Autowired
	private BiProjectService biProjectService;
    @Autowired
    private BiServerService biServerService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
	private BiUserService biUserService;
    @Autowired
	private ResourceTypeService resourceTypeService;
    @Autowired
	private PromptRelService promptRelService;
	@Autowired
	private BiMappingService biMappingService;
    @RequestMapping("/report/{id:.+}")
    public String report(@PathVariable("id")String id, Model model) {
        Resource resource = resourceService.selectById(id);
        BiServer biServer = biServerService.selectById(resource.getServerId());
        String userId = SessionUtil.getUserId();
		EntityWrapper<BiUser> ew = new EntityWrapper<>();
	/*	ew.where("id in (select bi_user_id from t_bi_mapping where user_id ={0} and type='Tableau')",userId);
		BiUser biUser = biUserService.selectOne(ew);
		JSONObject result = TableauRestApi.getTicket(biServer.getUrl(),biUser==null?biServer.getDefaultUid():biUser.getUsername());
		model.addAttribute("ticket",result.getString("content"));*/
		
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		PromptRel query = new PromptRel();
		query.setResourceId(resource.getId());
		List<PromptRel> promptRelList = promptRelService.findList(query);
		promptRelList = DateUtil.timeFunction(promptRelList);//设置时间提示的默认值
		//promptRelList = setOrgValue(promptRelList);//设置下拉，复选，等默认值
		//下拉单选，下拉多选等，m.bi_user_id,
		promptRelList = DataSourceUtils.DatasourceValueFunction(promptRelList);
		//通过iserver获取当前访问用户的映射用户，如果没有使用默认用户。
		BiMapping bm = new BiMapping();
		bm.setUserId(userId);
		bm.setBiServerId(biServer.getId());
		List<BiMapping> biList = biMappingService.selectBiUserBySysUserAndServerId(bm);
		if(biList != null && biList.size() > 0){
			model.addAttribute("biUserId", biList.get(0).getBiUserId());//bi userID
			model.addAttribute("biPassWord", biList.get(0).getBiPassword());//bi userID
			model.addAttribute("hasMapping", "true");//是否已经映射用户
		}else{
			model.addAttribute("biUserId", null);//bi userID
			model.addAttribute("hasMapping", "false");
		}
		model.addAttribute("resource",resource);
		model.addAttribute("biServer",biServer);
		model.addAttribute("promptRelList",promptRelList);
		model.addAttribute("resourceTypeCode", resourceType.getCode());
		model.addAttribute("userId", userId);//userID
		//提示是否显示
		List<PromptRel> shwoPrompt = promptRelList.stream().filter(item-> 0 == item.getHidden()).collect(Collectors.toList());
		model.addAttribute("showPrompt", shwoPrompt.size() > 0 ? true : false);
        model.addAttribute("biServer",biServer);
        model.addAttribute("ip",biServer.getIp());
        model.addAttribute("port",biServer.getPort());
        model.addAttribute("url",biServer.getUrl());
        model.addAttribute("server",biServer.getServer());
        model.addAttribute("default_uid",biServer.getDefaultUid());
        model.addAttribute("default_pwd",biServer.getDefaultPwd());
        model.addAttribute("report_id",resource.getReportId());
        model.addAttribute("resource",resource);
        
        return DIR + "container";
    }
	
	
}
