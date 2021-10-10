package com.xin.portal.system.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class XssFilter implements Filter {
    FilterConfig filterConfig = null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
    	//处理部分无需防御Xss攻击的请求
    	HttpServletRequest req = (HttpServletRequest) request;
    	if(isFilter(req.getRequestURL().toString())){
    		//不拦截
    		chain.doFilter(new XssHttpServletRequestWrapperNo(
                    (HttpServletRequest) request), response);
    	}else{
    		chain.doFilter(new XssHttpServletRequestWrapper(
    				(HttpServletRequest) request), response);
    	}
    		
    }
    /* 这里设置不被拦截的请求路径 */
    private static final List<String> unFilterUrlList = Arrays.asList(
    		"/notice/save",
    		"/notice/update",
    		"/notice/publish",
            "/userInfo/save",
    		"/user/skinCss",
    		"/userInfo/forget/**",
    		"/moContent/save",
    		"/moContent/update",
    		"/issue/save",
            "/issue/updateData",
    		"/issueRecord/save",
            "/wxPush/save",
            "userSetting/update",
            "/sysLog/*",
            "/comment/save",
            "/dict/save",
            "/dict/update",
            "/datasource/search/data", 
            "/sysRelease/save",
            "/sysRelease/update"
    		);
    /* 判断请求路径是否为不拦截的请求路径 */
    private boolean isFilter(String url){
    	//     "/userInfo/forget/**"这种形式不生效
       for(String s: unFilterUrlList) {
          if(url.indexOf(s) > -1) {
             return true;
          }
       }
       return false;
    }
}