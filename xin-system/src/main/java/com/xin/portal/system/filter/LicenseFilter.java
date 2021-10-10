package com.xin.portal.system.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xin.portal.system.license.ClientVerifyLicense;

/**
 * 
 */
public class LicenseFilter  implements Filter {
	
	private final Logger logger = LoggerFactory.getLogger(LicenseFilter.class);
	
	private static final String[] IGNORE_URI = {"/system","/license","/apply","/activate","/plugins/","/js/","/css/","/images/","/fonts/","/error",".html"};

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

//	private VerifyLicense vLicense ;
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
            if (isIgnore((HttpServletRequest)servletRequest)) {
//			if (true) {
            	chain.doFilter(servletRequest, servletResponse);
            	return;
            } else {
            	//验证证书
            	if (ClientVerifyLicense.verify()){
            		chain.doFilter(servletRequest, servletResponse);
            	} else {
					logger.info( LangTransform.getLocaleLang( LanguageParam.LOGGERINFO_35)+"...");
            		//logger.info("跳转激活页面...");
            		servletRequest.getRequestDispatcher("system/activate").forward(servletRequest, servletResponse);
            		return;
            	}
            }
		
		
				
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isIgnore(HttpServletRequest request) {
		boolean flag = false;
        String requestUrl = request.getRequestURL().toString();
        //logger.info(requestUrl);
        for (String reg : IGNORE_URI) {
            if (requestUrl.contains(reg)) {
                flag = true;
                break;
            }
        }
        return flag;
	}
	
	
}
