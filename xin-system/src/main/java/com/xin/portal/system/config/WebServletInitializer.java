package com.xin.portal.system.config;

import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas20ProxyReceivingTicketValidationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.xin.portal.system.filter.CasLoginFilter;
import com.xin.portal.system.util.PropertiesUtil;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Properties;

@Component
public class WebServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[0];
    }

    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	String ssoType = new PropertiesUtil("application.properties").getProperty("sso.type");
    	if("cas".equals(ssoType)){
    		 this.registerCasFilter(servletContext);
    	     this.registerCasListener(servletContext);
    	}
        super.onStartup(servletContext);
    }

    /**
     *  动态注册CAS过滤器
     */
    protected void registerCasFilter(ServletContext servletContext) {
    	Properties properties = new PropertiesUtil("application.properties").getProperties();
        /* CAS单点登录校验过滤器 */
        FilterRegistration casFilter = servletContext.addFilter("CAS Authentication Filter", AuthenticationFilter.class);
        casFilter.setInitParameter("casServerLoginUrl" , properties.getProperty("cas.server.loginUrl"));
        casFilter.setInitParameter("serverName" , properties.getProperty("cas.server.serverName"));
        casFilter.setInitParameter("ignorePattern" , "/static/*");
        casFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class) , true, "/*");
        /* CAS单点登录ticket校验过滤器 */
        FilterRegistration casValidationFilter = servletContext.addFilter("CAS Validation Filter", Cas20ProxyReceivingTicketValidationFilter.class);
        casValidationFilter.setInitParameter("casServerUrlPrefix" , properties.getProperty("cas.server.urlPrefix"));
        casValidationFilter.setInitParameter("serverName" , properties.getProperty("cas.server.serverName"));
        casValidationFilter.setInitParameter("redirectAfterValidation" , "true");
        casValidationFilter.setInitParameter("useSession" , "true");
        casValidationFilter.setInitParameter("encoding" , "UTF-8");
        casValidationFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class) , true, "/*");
        /* CAS单点登出过滤器 */
        FilterRegistration singleSignOutFilter = servletContext.addFilter("CAS Single Sign Out Filter", SingleSignOutFilter.class);
        singleSignOutFilter.setInitParameter("casServerUrlPrefix" , properties.getProperty("cas.server.urlPrefix"));
        singleSignOutFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class) , true, "/*");
        /* HttpServletRequestWrapper过滤器 */
        FilterRegistration httpServletRequestWrapperFilter = servletContext.addFilter("CAS HttpServletRequest Wrapper Filter", HttpServletRequestWrapperFilter.class);
        httpServletRequestWrapperFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class) , true, "/*");
        /* 验证登录 过滤器 */
        FilterRegistration assertionThreadLocalFilter = servletContext.addFilter("CAS Login Filter", CasLoginFilter.class);
        assertionThreadLocalFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class) , true, "/*");
    }

    /**
     * 注册CAS监听器
     */
    protected void registerCasListener(ServletContext servletContext){
        //注册监听器
        servletContext.addListener(SingleSignOutHttpSessionListener.class);
    }

    @Override
    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        return super.registerServletFilter(servletContext, filter);
    }

    @Override
    protected void registerContextLoaderListener(ServletContext servletContext) {
        super.registerContextLoaderListener(servletContext);
    }
}