package com.xin.portal.system.controller;

import javax.servlet.http.HttpServletRequest;

import com.xin.portal.system.model.Config;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.util.StringUtils;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.util.AESUtil;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;

@Controller
@RequestMapping("/sso")
public class SsoController extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(SsoController.class);
	@Autowired
	private ConfigService configService;

	@SystemLog(module = LanguageParam.MSTR_SSO_INFO_AUTH, type=LogType.login)
	@RequestMapping("/login")
	public String login(String url, HttpServletRequest request, Model model,RedirectAttributes redirectModel){
		//setCache(request);
		String lang = "zh_CN";
		Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, this.getTenantId());

		if(config!=null){
			lang = config.getValue();
		}
		String paramStr = AESUtil.decrypt(new String(Base64Utils.decodeFromUrlSafeString(url)),Constant.DEFAULT_SALT);
		if (StringUtils.isEmpty(paramStr)) {
			return getCache(ConfigKeys.SYS_LOGIN_PAGE);
		}
		String[] params = paramStr.split("&");
		if (params.length<2) {
			return getCache(ConfigKeys.SYS_LOGIN_PAGE);
		}
		UsernamePasswordToken token = new UsernamePasswordToken(params[0], params[1]); 
        Subject currentUser = SecurityUtils.getSubject();
        try {
			String start= LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+params[0]+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_11);
			String by=LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+params[0]+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_12);
			logger.info("[{}]",start);
			currentUser.login(token);
			logger.info("[{}]",by);
          /* logger.info("对用户[" + params[0] + "]进行登录验证..验证开始");
            currentUser.login(token);
            System.out.println("对用户[" + params[0] + "]进行登录验证..验证通过");  */
            request.setAttribute("forward", params[2]);
            redirectModel.addFlashAttribute("forwardParams", params[2]);
            return "redirect:/main";
        } catch(Exception e) {
        	e.printStackTrace();
        }
        if(!currentUser.isAuthenticated()){  
        	model.addAttribute("username", params[0]);
        	token.clear();  
        } 
		return getCache(ConfigKeys.SYS_LOGIN_PAGE);
	}
	

}
