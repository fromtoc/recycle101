package com.xin.portal.system.config;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.util.Constant.ConfigKeys;

@Configuration
public class NativeLocaleResolver implements LocaleResolver {
	

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
//		String lang = request.getParameter("lang");
//		if (StringUtils.isNotEmpty(lang)) {
//			LocaleResolver localeResolver =RequestContextUtils.getLocaleResolver(request);
//			localeResolver.setLocale(request, response, Locale.SIMPLIFIED_CHINESE);
//			
//		}
//		if ("zh_CN".equals(lang)) {
//			session.setAttribute(ConfigKeys.SYS_LOCALE, Locale.SIMPLIFIED_CHINESE);
//		} else if ("zh_TW".equals(lang)) {
//			localeResolver.setLocale(request, response, Locale.TRADITIONAL_CHINESE);
//			session.setAttribute(ConfigKeys.SYS_LOCALE, Locale.TRADITIONAL_CHINESE);
//		} else if ("en_US".equals(lang)) {
//			localeResolver.setLocale(request, response, Locale.US);
//			session.setAttribute(ConfigKeys.SYS_LOCALE, Locale.US);
//		}
		
		Object localeObj = request.getSession().getAttribute(ConfigKeys.SYS_LOCALE);
		Locale locale = localeObj == null ? Locale.SIMPLIFIED_CHINESE : (Locale) localeObj;
		request.getSession().setAttribute(ConfigKeys.SYS_LOCALE, locale);
		
		return locale;
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		
		
	}

}
