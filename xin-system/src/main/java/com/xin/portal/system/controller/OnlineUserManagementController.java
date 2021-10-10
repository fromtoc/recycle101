package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.service.UserInfoService;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @ClassPath: com.xin.portal.system.controller.UserController 
 * @Description: TODO
 * @author dingch
 */
@Controller
@RequestMapping("/onlineUserManagement")
public class OnlineUserManagementController extends BaseController {
	
	private static final String DIR = "onlineUserManagement/";
	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	private UserInfoService service;

	/**
	 * @Title: index 
	 * @Description:  在线用户管理
	 * @return String
	 * @author dingch
	 */
	@RequestMapping("/index")
	public String index(){

		return DIR + "index";
	}

	/**
	 * @Title: page
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author dingch
	 */
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(UserInfo query, Integer pageNumber, Integer pageSize){
		List<UserInfo> userInfoList = new ArrayList();
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for(Session  session :sessions){
			UserInfo user = (UserInfo)session.getAttribute("user");
			if(user !=  null && user.getTenantId().equals(this.getTenantId())){
				user.setIp((String)session.getAttribute("ip"));
				user.setSessionId(session.getId().toString());
				userInfoList.add(user);
			}
		}
		Page<UserInfo> page = new Page<UserInfo>(pageNumber,pageSize);
		page.setRecords(userInfoList);
		PageModel<UserInfo> pageModel = new PageModel<UserInfo>(page);
		return  BaseApi.page(pageModel);
	}
	
	/**
	 * @Title: kickout
	 * @Description:  强制离线
	 * @return String
	 * @author dingch
	 */
	@SystemLog(module=LanguageParam.ONLINE_USER_MANAGEMENT ,operation=LanguageParam.FORCED_OFFLINE ,type=LogType.logout)
	@RequestMapping(value="/kickout/{sessionId}")
	@ResponseBody
	public BaseApi kickout(@PathVariable String sessionId){
		BaseApi baseApi = BaseApi.error();
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		for(Session  session :sessions){
			UserInfo user = (UserInfo)session.getAttribute("user");
			if(user !=  null && sessionId.equals(session.getId().toString())){
				session.setAttribute("user",null);
				session.setTimeout(0);
				baseApi=BaseApi.success();
			}
		}
		return baseApi;
	}
	
	

	


}
