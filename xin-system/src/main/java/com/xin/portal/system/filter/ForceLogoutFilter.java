package com.xin.portal.system.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xin.portal.system.util.Constant.SessionKeys;


/**
 * 
 */
public class ForceLogoutFilter extends AccessControlFilter {
	
	private final Logger logger = LoggerFactory.getLogger(ForceLogoutFilter.class);
	
	private static final String[] IGNORE_URI = {"/kaptcha","/system","/login","/license","/apply","/activate","/plugins/","/js/","/css/","/images/","/fonts/","/error",".html"};

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		try {
			if (isIgnore((HttpServletRequest)request)) {
				logger.debug("isAccessAllowed : true");
				return true;
			}
			Session session = getSubject(request, response).getSession(false);
			if (session == null) {
				logger.debug("isAccessAllowed : true");
				return true;
			}
//			return true;
			return session.getAttribute(SessionKeys.SESSION_FORCE_LOGOUT_KEY) == null;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return true;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		try {
			getSubject(request, response).logout();// 强制退出
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		String loginUrl = getLoginUrl() + (getLoginUrl().contains("?") ? "&" : "?") + "forceLogout=1";
		WebUtils.issueRedirect(request, response, loginUrl);
		return false;
	}
	
		
	private boolean isIgnore(HttpServletRequest request) {
		boolean flag = false;
        String requestUrl = request.getRequestURL().toString();
        logger.debug("request url : {}",requestUrl);
        for (String reg : IGNORE_URI) {
            if (requestUrl.contains(reg)) {
                flag = true;
                break;
            }
        }
        return flag;
	}
}
