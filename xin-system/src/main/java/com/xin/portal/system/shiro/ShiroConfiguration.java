package com.xin.portal.system.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.google.common.collect.Maps;
import com.xin.portal.system.filter.ForceLogoutFilter;
import com.xin.portal.system.filter.LicenseFilter;
import com.xin.portal.system.filter.XssFilter;

import net.sf.ehcache.CacheManager;

@Configuration
public class ShiroConfiguration {
	
//	@Autowired
//    private CacheManager cacheManager;


	@Bean
	@Order(Integer.MIN_VALUE)
	public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {
		FilterRegistrationBean<DelegatingFilterProxy> filterRegistration = new FilterRegistrationBean<DelegatingFilterProxy>();
		DelegatingFilterProxy proxy = new DelegatingFilterProxy();
		proxy.setTargetFilterLifecycle(true);
		proxy.setTargetBeanName("shiroFilter");
		filterRegistration.setFilter(proxy);
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);

		return filterRegistration;
	}
	
	/**
     * 配置过滤器
     * @return
     */
//    @Bean
//    public FilterRegistrationBean someFilterRegistration() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(licenseFilter());
//        registration.addUrlPatterns("/*");
//        registration.setName("licenseFilter");
//        registration.setDispatcherTypes(DispatcherType.REQUEST);
//        return registration;
//    }
//    
//    @Bean(name = "licenseFilter")
//	public LicenseFilter licenseFilter() {
//		return new LicenseFilter();
//	}

	@Bean(name = "forceLogout")
	public ForceLogoutFilter forceLogoutFilter() {
		return new ForceLogoutFilter();
	}
	@Bean(name = "xssFilter")
	public XssFilter xssFilter() {
		return new XssFilter();
	}
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		shiroFilterFactoryBean.setLoginUrl("/login/");
		shiroFilterFactoryBean.setSuccessUrl("/main.html");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		// Map<String, Filter> filters = Maps.newHashMap();
		// filters.put("roles",new RolesAuthorizationFilter());
		// filters.put("perms",new PermissionsAuthorizationFilter());
		// filters.put("anon", new AnonymousFilter());
		// filters.put("anon", new ShiroSessionFilter());

		// shiroFilterFactoryBean.setFilters(filters);
		Map<String, String> filterChainDefinitionManager = Maps.newLinkedHashMap();
		/*RSA算法生成秘钥，取消拦截*/
		filterChainDefinitionManager.put("/encrypt/publicKey", "anon");
		filterChainDefinitionManager.put("/static/**", "anon");
		filterChainDefinitionManager.put("/plugins/**", "anon");
		filterChainDefinitionManager.put("/css/**", "anon");
		filterChainDefinitionManager.put("/images/**", "anon");
		filterChainDefinitionManager.put("/js/**", "anon");
		filterChainDefinitionManager.put("/fonts/**", "anon");
		filterChainDefinitionManager.put("/sso/**", "anon");
		filterChainDefinitionManager.put("/mstr/auth", "anon");// mstr sso auth
		filterChainDefinitionManager.put("/login/**", "anon");
		//文件
		filterChainDefinitionManager.put("/upload/**", "anon");
		filterChainDefinitionManager.put("/wxPushFile/**", "anon");
		//check code 
		filterChainDefinitionManager.put("/kaptcha/**", "anon");
		filterChainDefinitionManager.put("/activate/**", "anon");
		filterChainDefinitionManager.put("/license/new/**", "anon");
		filterChainDefinitionManager.put("/license/apply*", "anon");
		
		// result api doc
		filterChainDefinitionManager.put("/swagger-ui.html", "anon");
		filterChainDefinitionManager.put("/docs.html", "anon");
		filterChainDefinitionManager.put("/webjars/**", "anon");
		filterChainDefinitionManager.put("/v2/api-docs", "anon");
		
		filterChainDefinitionManager.put("/userInfo/forget/**", "anon");
		//weixin 
		filterChainDefinitionManager.put("/wx/**", "anon");
		//微信账号绑定
		filterChainDefinitionManager.put("/userWxWork/bind", "anon");
		//line
		filterChainDefinitionManager.put("/linebot/**/**/callback", "anon");
		//Dossier
		filterChainDefinitionManager.put("/dossier/demo/*/instances", "anon");
		
		//mobile
		filterChainDefinitionManager.put("/mobile/login", "anon");
		filterChainDefinitionManager.put("/mobile/sso/login", "anon");
		//不需要登录验证，客户就可以查看最新产品的发布情况
		//http://m.imobilebi.com:8081/dataWindow/sysRelease/timeline
		filterChainDefinitionManager.put("/sysRelease/timeline", "anon");
		
//		filterChainDefinitionManager.put("/authorize", "anon");
//		filterChainDefinitionManager.put("/accessToken", "anon");
//		filterChainDefinitionManager.put("/oauth2Failure", "anon");
//		filterChainDefinitionManager.put("/oauth2-login","oauth2Authc");
		filterChainDefinitionManager.put("/authLogin", "anon");
		
		filterChainDefinitionManager.put("/logout", "logout");
		filterChainDefinitionManager.put("/**", "forceLogout,authc");
//		filterChainDefinitionManager.put("/**", "anon");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionManager);
		return shiroFilterFactoryBean;

	}

	@Bean("securityManager")
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setCacheManager(ehCacheManager());
		securityManager.setRealm(systemShiroRealm());
		securityManager.setSessionManager(defaultWebSessionManager());
		// 注入记住我管理器
		return securityManager;
	}

	@Bean(name = "lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}


	@Bean(name = "systemShiroRealm")
	public SystemShiroRealm systemShiroRealm() {
		SystemShiroRealm realm = new SystemShiroRealm();
//		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
//		credentialsMatcher.setStoredCredentialsHexEncoded(false);
//		realm.setCredentialsMatcher(credentialsMatcher);
		realm.setCredentialsMatcher(new CustomCredentialsMatcher());
		realm.setCacheManager(ehCacheManager());
		return realm;
	}
	
	@Bean
    public EhCacheManager ehCacheManager(){
        EhCacheManager ehcacheManager = new EhCacheManager();
        ehcacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ehcacheManager;
    }

	@Bean(name = "sessionManager")
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		Collection<SessionListener> listeners = new ArrayList<>();
	    listeners.add(new ShiroSessionListener());
		sessionManager.setSessionListeners(listeners);
		sessionManager.setSessionDAO(sessionDao());// 
		// sessionManager.setSessionValidationInterval(3600000*12);
		// sessionManager.setGlobalSessionTimeout(3600000*12);
		sessionManager.setDeleteInvalidSessions(true);
		sessionManager.setSessionValidationSchedulerEnabled(true);
		Cookie cookie = new SimpleCookie("sid");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(-1);
		sessionManager.setSessionIdCookie(cookie);
		
		return sessionManager;
	}

	// @Bean(name = "sessionManager")
	// public SessionManager defaultWebSessionManager() {
	// DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
	//// //url中是否显示session Id
	//// sessionManager.setSessionIdUrlRewritingEnabled(false);
	// // 删除失效的session
	// sessionManager.setDeleteInvalidSessions(true);
	// List<SessionListener> listeners = new ArrayList<SessionListener>();
	//// listeners.add(new ShiroSessionListener());
	// sessionManager.setSessionListeners(listeners);
	// return sessionManager;
	// }

	@Bean("sessionDAO")
	public SessionDAO sessionDao() {
		return new EnterpriseCacheSessionDAO();
	}

}
