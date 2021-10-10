package com.xin.portal.system.controller;

import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.i18n.LanguageParam;


@Controller
@RequestMapping("/session")
public class SessionController extends BaseController{
	
	private static final String DIR = "session/";
	
	@Autowired
	private SessionDAO sessionDao;
	
	@RequestMapping("/index")
	public String index() {
		return DIR + "index";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public JSONObject list() {
		Collection<Session> list = sessionDao.getActiveSessions();
		JSONArray jArr = new JSONArray();
		for (Session session : list) {
			String username = session.getAttribute(DefaultWebSubjectContext.PRINCIPALS_SESSION_KEY).toString();
			JSONObject jobj = new JSONObject();
			jobj.put("ip", session.getAttribute(SessionKeys.IP));
			jobj.put("user", username);
			jobj.put("sessionId", session.getId());
			jobj.put("startTimestamp", session.getStartTimestamp());
			jobj.put("lastAccessTime", session.getLastAccessTime());
			jobj.put("timeOut", session.getTimeout());
			jArr.add(jobj);
		}
		JSONObject result = new JSONObject();
		result.put("list", jArr);
		result.put("total", list.size());
		return result;
	}
	
	@SystemLog(module = LanguageParam.USER_LOGOUT, type=LogType.logout)
	@RequestMapping("/logout")
	@ResponseBody
	public BaseApi logout(String sessionId) {
		Session session = sessionDao.readSession(sessionId);
		if(session != null) {  
            session.setAttribute(
            		SessionKeys.SESSION_FORCE_LOGOUT_KEY, Boolean.TRUE);  
        } 
		return BaseApi.success();
	}

}
