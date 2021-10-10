package com.xin.portal.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.xin.portal.system.license.ClientVerifyLicense;


/**
 * @ClassPath: com.xin.portal.system.interceptor.LicenseInterceptor 
 * @Description: TODO
 * @author zj
 * @date 2017-7-13 下午2:39:41
 */
public class LicenseInterceptor  implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(LicenseInterceptor.class);
	

	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView arg3)


			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		/*if (!ClientVerifyLicense.verify()) {
			String basePath = (String) request.getAttribute("basePath");
			response.sendRedirect(basePath+"/activate/");  
		}*/
		return true;
	}

}