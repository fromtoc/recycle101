package com.xin.portal.system.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.service.UserInfoService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;

@Controller
@RequestMapping("locale")
public class LocaleController {
	
	@Autowired
    private UserInfoService userInfoService;
	
	@GetMapping("{lang}")
	public String changeLanguage(@PathVariable(name="lang",required=false)String lang, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		Locale locale = Locale.getDefault();	
		LocaleResolver localeResolver =RequestContextUtils.getLocaleResolver(request);
		if (Locale.SIMPLIFIED_CHINESE.toString().equals(lang)) {
			locale = Locale.SIMPLIFIED_CHINESE;
	    } else if (Locale.TRADITIONAL_CHINESE.toString().equals(lang)) {
	    	locale = Locale.TRADITIONAL_CHINESE;
	    } else if (Locale.US.toString().equals(lang)) {
	    	locale = Locale.US;
	    }
		localeResolver.setLocale(request, response, locale);
		SessionUtil.setSession(ConfigKeys.SYS_LOCALE, locale);
    	String setSql = " locale= '" + locale+"'";
    	EntityWrapper<UserInfo> ew = new EntityWrapper<>();
    	ew.eq("id", SessionUtil.getUserId());
    	userInfoService.updateForSet(setSql, ew);
	    return "redirect:/main";
	}

}
