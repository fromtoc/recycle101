package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.bi.tableau.RestApiUtils;
import com.xin.portal.bi.tableau.TableauRestApi;
import com.xin.portal.bi.tableau.api.*;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.util.SessionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/bi/tableau")
@Api(value="Tableau接口",tags={"Tableau报表集成"})
public class TableauController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(TableauController.class);
	
	private static final String DIR = "tableau/";
	
	@Autowired
	private BiProjectService biProjectService;
    @Autowired
    private BiServerService biServerService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
	private BiUserService biUserService;


    @RequestMapping("/report/{id:.+}")
    public String report(@PathVariable("id")String id, Model model) {
        Resource resource = resourceService.selectById(id);
        BiServer biServer = biServerService.selectById(resource.getServerId());
        String userId = SessionUtil.getUserId();
		EntityWrapper<BiUser> ew = new EntityWrapper<>();
		ew.where("id in (select bi_user_id from t_bi_mapping where user_id ={0} and type='Tableau')",userId);
		BiUser biUser = biUserService.selectOne(ew);
		JSONObject result = TableauRestApi.getTicket(biServer.getUrl(),biUser==null?biServer.getDefaultUid():biUser.getUsername());
		model.addAttribute("ticket",result.getString("content"));
        model.addAttribute("biServer",biServer);
        model.addAttribute("resource",resource);
        return DIR + "container";
    }
	
	@ResponseBody
	@RequestMapping(value="/container/{projectId}/{id}")
	public String container(@PathVariable("projectId")String projectId,@PathVariable("id")String id, Model model) {
		BiProject biProject = biProjectService.findById(projectId);
		String ticket = null;
		try {
			JSONObject result = TableauRestApi.getTicket("",null);
			ticket = result.getString("content");
			String url = biProject.getUrl()+"/trusted/"+ticket+"/"+id;
			model.addAttribute("url", url);
		} catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return DIR + "container";
	}
	
	@ApiOperation(value="获取ticket信息", notes="根据portal的用户id关联映射用户来获取Tableau ticket信息,若userId为空，将使用默认Tableau用户")
	@ResponseBody
	@RequestMapping(value="/ticket", method=RequestMethod.GET)
	public BaseApi ticket(@ApiParam(name="id",value="用户id",required=false)Integer userId) {
		String ticket = null;
		try {
			if (userId == null) {
				JSONObject result = TableauRestApi.getTicket("",null);
				ticket = result.getString("content");
			} else {
				
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return BaseApi.error("获取ticket失败");
		}
		return BaseApi.data(ticket);
	}


	@RequestMapping("/{serverId}/rest/{api}")
	@ResponseBody
	public BaseApi rest(@PathVariable("serverId")String serverId, @PathVariable("api")String api){
		BiServer biServer = biServerService.selectById(serverId);
		BaseApi result = BaseApi.success();
		switch (api){
			case "sites":
				TableauCredentialsType tableauCredentialsType = new TableauCredentialsType();
				tableauCredentialsType.setName(biServer.getDefaultUid());
				tableauCredentialsType.setPassword(biServer.getDefaultPwd());
				SiteListType siteListType = RestApiUtils.getInstance().invokeQuerySites(tableauCredentialsType);
				List<SiteType> list = siteListType.getSite();
				result.put("data",list);
				list.forEach(item -> System.out.println(item.getName()));
				break;
			default:
				break;
		}
		return result;
	}

	@RequestMapping("/{serverId}/sites")
	@ResponseBody
	public BaseApi reports(@PathVariable("serverId")String serverId){
		BiServer biServer = biServerService.selectById(serverId);
		TableauCredentialsType tableauCredentialsType = new TableauCredentialsType();

		tableauCredentialsType.setName(biServer.getDefaultUid());
		tableauCredentialsType.setPassword(biServer.getDefaultPwd());
		SiteListType siteListType = RestApiUtils.getInstance().invokeQuerySites(tableauCredentialsType);
		List<SiteType> list = siteListType.getSite();
		list.forEach(item -> System.out.println(item.getName()));
		return BaseApi.data(list);
	}

	@RequestMapping("/{serverId}/sites/{siteId}/workbooks")
	@ResponseBody
	public BaseApi workbooks(@PathVariable("serverId")String serverId,@PathVariable("siteId")String siteId){
		BiServer biServer = biServerService.selectById(serverId);
		TableauCredentialsType tableauCredentialsType = new TableauCredentialsType();
		tableauCredentialsType.setName(biServer.getDefaultUid());
		tableauCredentialsType.setPassword(biServer.getDefaultPwd());
		WorkbookListType workbookListType = RestApiUtils.getInstance().invokeQueryWorkbooksForSite(tableauCredentialsType,siteId);
		List<WorkbookType> list = workbookListType.getWorkbook();
		list.forEach(item -> System.out.println(item.getName()));
		return BaseApi.data(list);
	}

}
