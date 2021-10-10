package com.xin.portal.system.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;

/**
 * @ClassPath: com.xin.portal.HomeController 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年3月27日 下午4:57:31
 */
@Controller
@RequestMapping("/home")
public class HomeController  extends BaseController {
	
	protected static final String DIR = "home/";
	protected static final String NO_ROLE_PAGE = "about";
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, Model model) {
		String forward = request.getParameter("forward");
		if (!hasRole()) {
			return DIR + NO_ROLE_PAGE ;
		}
		if (StringUtils.isNotEmpty(forward)) {
			return "forward:"+forward;
		} else {
//			return DIR + CacheManager.get(ConfigKeys.SYS_HOME_INDEX);
			return DIR + getCache(ConfigKeys.SYS_HOME_INDEX);
		}
	}
	
	protected boolean hasRole() {
		Subject currentUser = SecurityUtils.getSubject();    
        if(null != currentUser){  
            Session session = currentUser.getSession();   
            List<String> userRoles = (List<String>) session.getAttribute(SessionKeys.USER_ROLES);
            if (userRoles==null || userRoles.size()<1) {
            	return false ;
            } else {
            	return true;
            }
        }
        return false;
	}
}
