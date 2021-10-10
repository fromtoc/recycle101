package com.xin.portal.system.bean;

import java.io.File;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import com.xin.portal.system.config.SystemInit;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.WebUtil;

/**
 * @ClassPath: com.xin.portal.system.bean.BaseController 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月5日 上午10:35:15
 */
public class BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	protected static String DEFAULT_TENANT = "1";
	
	@Autowired
    protected CacheManager cacheManager;
	
	protected String getCache(String cacheKey) {
		return getCache(Constant.CACHE_DEFAULT,cacheKey,String.class);
	}
	
	protected <T> T getCache(String cacheKey,@Nullable Class<T> type) {
		return getCache(Constant.CACHE_DEFAULT,cacheKey,type);
	}
	
	protected <T> T getCache(String cacheName, String cacheKey,@Nullable Class<T> type) {
		logger.info("getCache : {} | {} | {} ",cacheName,cacheKey,type.getName());
		return (T) cacheManager.getCache(cacheName).get(cacheKey, type);
	}
	
	protected void setCache(String cacheKey, Object value) {
		setCache(Constant.CACHE_DEFAULT,cacheKey, value);
	}
	
	protected void setCache(String cacheName, String cacheKey, Object value) {
		cacheManager.getCache(cacheName).put(cacheKey, value);
	}
	
	protected void setCache(HttpServletRequest request){
		request.setAttribute(ConfigKeys.SYS_NAME, getCache(ConfigKeys.SYS_NAME));
		request.setAttribute(ConfigKeys.SYS_LOGO, getCache(ConfigKeys.SYS_LOGO));
		request.setAttribute(ConfigKeys.SYS_COPYRIGHT, getCache(ConfigKeys.SYS_COPYRIGHT));
	}
	
	protected String getTenantId() {
		Object tenantId = SessionUtil.getSession(SessionKeys.TENANT_ID);
		return StringUtils.isEmpty(tenantId) ? DEFAULT_TENANT : tenantId.toString();
	}
	
	protected Locale getLocale() {
		Object locale = SessionUtil.getSession(ConfigKeys.SYS_LOCALE);
		return StringUtils.isEmpty(locale) ? Locale.getDefault() : (Locale) locale;
	}
	
	protected String getIp(HttpServletRequest request) {
		return WebUtil.getIpAddr(request);
	}
	
	protected String getUploadPath(HttpServletRequest request) {
		String con = request.getSession().getServletContext().getRealPath("");
		String webappPath = con.substring(0, con.lastIndexOf(File.separator));
		return webappPath.substring(0, webappPath.lastIndexOf(File.separator))+ File.separator + "upload" + File.separator;
	}
	
	
	
	
	
}
