package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.code.kaptcha.Constants;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.AdUtils.ADUtils;
import com.xin.portal.system.util.AdUtils.entity.AdEntity;
import com.xin.portal.system.util.AdUtils.entity.AdUser;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.*;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassPath: com.xin.portal.system.controller.MainController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-7-14 下午6:20:20
 */
@Controller
public class MainController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(MainController.class);
	
	private static final String DIR = "main/";
	
	private int start = 0;
	
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserService userService;
    @Value("${tableau.server}")
    private String tableauServer;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private ConfigService configService;
	@Autowired
	private ResourceService resourceService;
    @Autowired
    private OrganizationModuleService organizationModuleService;
    @Autowired
    private ServiceStateService	 serviceStateService;
    @Value("${system.version:1.0.0}")
	private String version;
	@Autowired
	private IssueService issueService;
	@Autowired
	private TenantService tenantService;
	@Autowired
	private MessageCenterService messageCenterService;
	@Autowired
	private UserSettingService userSettingService;
	
	@RequestMapping("/")
	public String index(HttpServletRequest request,Model model){
		Subject currentUser = SecurityUtils.getSubject();
//		setCache(request);
		if (currentUser.isAuthenticated()) {
			return "forward:/main.html";
		}
		Config config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, getTenantId());
		setConfigAttr(model,getTenantId());
		logger.info("login page: {}",config.getValue());
		return config.getValue();
		
	}
	
	@SystemLog(module = LanguageParam.USERS_LOGIN, type=LogType.login)
	@RequestMapping("/main")
	public String main(HttpServletRequest request,HttpServletResponse response,Model model,@ModelAttribute("forwardParams") String forward){
		
		startServiceMonitor();
		model.addAttribute("tenantId",getTenantId());
		model.addAttribute("forwardParams", forward);
		try {
			//根据系统参数设置当前会话有效期
			Subject currentUser = SecurityUtils.getSubject();
			if(null != currentUser){
				Long sessionTime = Long.parseLong(configService.findByCode("SESSION_TIME",this.getTenantId()).getValue());
				Session	currentSession = currentUser.getSession();
				currentSession.setTimeout(sessionTime*1000*60);
			}

			//加载最新个人动态
			//暂取前100条
			List<Resource> log=resourceService.findReourceLog(SessionUtil.getUserId());
			model.addAttribute("log", log);
			//setCache(request);
			//访问历史纪录和收藏
			UserInfo userInfo = SessionUtil.getUserInfo();
			UserRecord userRecord = new UserRecord();
			userRecord.setOwner(userInfo.getId());
			
			//获取个人设置
    		UserSetting setting = userSettingService.selectByUserId(userInfo.getId());
			
			//setConfigAttr(model,getTenantId());
			EntityWrapper<Config> ewConfig = new EntityWrapper<>();
			ewConfig.eq("tenant_id", getTenantId());
			List<Config> list = configService.selectList(ewConfig);
			Map<String, String> configMap = new HashMap(); 
			for (Config	config : list) {
				//logger.info("tenantId {} {} {}",tenantId,config.getCode(),config.getValue());
				model.addAttribute(config.getCode(), config.getValue());
				configMap.put(config.getCode(),config.getValue());
			}
			//Config config = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());//获取主题
			
			//用户第一次登录是否提示修改密码
			//Config firstUpadatePwd = configService.findByCode(ConfigKeys.SYS_FIRST_UPDATE_PWD, getTenantId());
			String firstUpadatePwd = configMap.get(ConfigKeys.SYS_FIRST_UPDATE_PWD);
			User userResetKey = userService.selectById(userInfo.getId());
			if(userResetKey!=null && firstUpadatePwd!=null && "0".equals(userResetKey.getResetKey()) && "1".equals(firstUpadatePwd)) {
				model.addAttribute("changePWD", userResetKey.getResetKey());
			} else if ("1".equals(userResetKey.getResetKey())) {
				model.addAttribute("changePWD", userResetKey.getResetKey());
			}
			//定期更新密码提示
			//Config updateRegular = configService.findByCode(ConfigKeys.PASSWORD_UPDATE_REGULARLY, getTenantId());
			String updateRegular = configMap.get(ConfigKeys.PASSWORD_UPDATE_REGULARLY);
			if(userResetKey!=null && updateRegular!=null && "1".equals(updateRegular)){//开启提示
				//获取提示时间，和用户上次修改密码时间
				//Config regularTime = configService.findByCode(ConfigKeys.PASSWORD_UPDATE_REGULARLY_TIME, getTenantId());
				String regularTime = configMap.get(ConfigKeys.PASSWORD_UPDATE_REGULARLY_TIME);
				if(DateUtil.ParamDateGreaterCurrentParamMonth(userResetKey.getResetKeyLasttime(), Integer.valueOf(regularTime))){
					//说明超过了指定月数,需要提示
					model.addAttribute("UPDATE_REGULARLY_PROMPT", "1");
				}
			}
			//登录提示
			//Config loginPrompt = configService.findByCode(ConfigKeys.LOGIN_PROMPT, getTenantId());
			String loginPrompt = configMap.get(ConfigKeys.LOGIN_PROMPT);
			if(userResetKey!=null && loginPrompt!=null && "1".equals(loginPrompt)) {
				String lastLoginip = userResetKey.getLastLoginIp();
				String lastLoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userResetKey.getLastLoginTime());
				String lastLoginAdress = WebUtil.getAddressByIp(lastLoginip);
				model.addAttribute("lastLoginip", lastLoginip);
				model.addAttribute("lastLoginTime", lastLoginTime);
				model.addAttribute("lastLoginAdress", lastLoginAdress);
			}

			Integer noticeMsg = setting!=null?setting.getNoticeMsg():1;//公告通知
			model.addAttribute("noticeMsg",noticeMsg);
			Integer issueMsg = setting!=null?setting.getIssueMsg():1;//问题管理通知  因为问题管理目前还没有重构所以暂时在main中判断
			model.addAttribute("issueMsg",issueMsg);
			Integer systemMsg = setting!=null?setting.getSystemMsg():1;//系统消息
			model.addAttribute("systemMsg",systemMsg);
			Integer commentMsg = setting!=null?setting.getCommentMsg():1;//评论消息
			model.addAttribute("commentMsg",commentMsg);







			//获取前5条消息(更多中包含已读的消息)
			Map<String, Object> unReadMessage = messageCenterService.findUnReadMessage();
			model.addAttribute("messageList",(List<MessageCenter>)unReadMessage.get("messageList"));
			model.addAttribute("messageCount", (Integer)unReadMessage.get("unReadCount"));//未读消息条数
			/*Integer messageCount = (Integer) unReadMessage.get("unReadCount");*/


			//判断是否要显示通知
			if(noticeMsg==null || (noticeMsg!=null && noticeMsg!=0)){
				//加载消息中心  最多加载10条
				Map map = noticeService.findListForTheme();
				List<Notice> readNotices= (List<Notice>)map.get("readList");
				List<Notice> unreadNotices = (List<Notice>)map.get("unreadList");
				model.addAttribute("readNotices", readNotices);
				model.addAttribute("unreadNotices", unreadNotices);
				model.addAttribute("noticeCount",(Integer)map.get("realSize"));//未读公告条数
				/*Integer noticeCount = (Integer) map.get("realSize");
				WebSocketServer.sendInfo();*/
			}
			//问题管理
			if(issueMsg==null || (issueMsg!=null && issueMsg!=0)){
				EntityWrapper<Issue> ew = new EntityWrapper();
				ew.eq("creater",SessionUtil.getUserId());
				ew.eq("is_reply","1");
				List<Issue> issueList = issueService.selectList(ew);
				model.addAttribute("issueCount",issueList.size());
				model.addAttribute("issueList",issueList);
			}
			
			//系统版本
			model.addAttribute("version", version);
			
			//先获取到用户自定义的首选项
			UserInfo user = userInfoService.selectById(userInfo.getId());
			String preferType = user.getPreferenceType();
			String preferValue = user.getPreferenceValue();

			//加载主页面头像
			model.addAttribute("AvatarPath",userInfo.getAvatar());
			
			//加载是否要显示菜单图标
			//Config menuIcon = configService.findByCode(ConfigKeys.SYS_MENU_ICON, getTenantId());
			String menuIcon = configMap.get(ConfigKeys.SYS_MENU_ICON);
			model.addAttribute("menuIcon", menuIcon);
			
			//获取用户定制化主页(默认读取系统设置中的主页)
			String moduleId=getModuleId(userInfo.getOrgId());
//			String SYS_HOME_PAGE_NAME=/*"首页"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_HOME_PAGE);
			//String SYS_HOME_PAGE_NAME=LangTransform.getLocaleLang(configMap.get(ConfigKeys.SYS_LOCALE)!=null?configMap.get(ConfigKeys.SYS_LOCALE):"zh_CN", LanguageParam.HOME_PAGE);
			String SYS_HOME_PAGE_NAME=configService.findByCode(ConfigKeys.SYS_HOME_PAGE_NAME, getTenantId()).getValue();
			if("".equals(preferType)||preferType==null){
//				String prefereceType = configService.findByCode(ConfigKeys.PREFERENCE_TYPE, getTenantId()).getValue();
//				String preferenceValue = configService.findByCode(ConfigKeys.PREFERENCE_VALUE, getTenantId()).getValue();
//				String sysHomePageName = configService.findByCode(ConfigKeys.SYS_HOME_PAGE_NAME, getTenantId()).getValue();
				String prefereceType = configMap.get(ConfigKeys.PREFERENCE_TYPE);
				String preferenceValue = configMap.get(ConfigKeys.PREFERENCE_VALUE);
				String sysHomePageName = configMap.get(ConfigKeys.SYS_HOME_PAGE_NAME);
				if(!"".equals(preferenceValue)&&preferenceValue!=null){
					Integer index = (preferenceValue).lastIndexOf(",");
					preferValue=preferenceValue.substring(index+1);
				}
				SYS_HOME_PAGE_NAME=sysHomePageName;
				preferType=prefereceType;
			}
			model.addAttribute("SYS_HOME_PAGE_NAME",SYS_HOME_PAGE_NAME);
			model.addAttribute("preferType", preferType);
			model.addAttribute("preferValue", preferValue);
			model.addAttribute("moduleId", moduleId);
			//多语言
        	Locale locale = Locale.getDefault();	
    		LocaleResolver localeResolver =RequestContextUtils.getLocaleResolver(request);
    		UserInfo u = userInfoService.selectById(userInfo.getId());
    		if (Locale.SIMPLIFIED_CHINESE.toString().equals(u.getLocale())) {
    			locale = Locale.SIMPLIFIED_CHINESE;
    	    } else if (Locale.TRADITIONAL_CHINESE.toString().equals(u.getLocale())) {
    	    	locale = Locale.TRADITIONAL_CHINESE;
    	    } else if (Locale.US.toString().equals(u.getLocale())) {
    	    	locale = Locale.US;
    	    }
    		localeResolver.setLocale(request, response, locale);
    		SessionUtil.setSession(ConfigKeys.SYS_LOCALE, locale);
			//系统设置的导航
    		String sysTheme = configMap.get(ConfigKeys.SYS_THEME);
    		if(setting!=null){
    			String theme = setting.getNavigaStyle();
    			sysTheme = (theme!=null&&theme.length()>0)?theme:sysTheme;
    		}
			logger.info("theme page : {}",sysTheme);
			return DIR + sysTheme;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return DIR + "theme_default";

	}


	@RequestMapping("/reloadHead")
	public String mainHead(HttpServletRequest request,HttpServletResponse response,Model model){
		String configValue = "";
		try {
			UserInfo userInfo = SessionUtil.getUserInfo();
			//加载主页面头像
			model.addAttribute("AvatarPath",userInfo.getAvatar());

			//获取个人设置
			UserSetting setting = userSettingService.selectByUserId(userInfo.getId());
			Integer noticeMsg = setting!=null?setting.getNoticeMsg():null;//公告通知
			model.addAttribute("noticeMsg",noticeMsg);
			Integer issueMsg = setting!=null?setting.getIssueMsg():null;//问题管理通知  因为问题管理目前还没有重构所以暂时在main中判断
			model.addAttribute("issueMsg",issueMsg);
			Integer systemMsg = setting!=null?setting.getSystemMsg():null;//系统消息
			model.addAttribute("systemMsg",systemMsg);
			Integer commentMsg = setting!=null?setting.getCommentMsg():null;//评论消息
			model.addAttribute("commentMsg",commentMsg);
			//加载消息中心  获取前5条消息(包含已读的消息)
			Map<String, Object> unReadMessage = messageCenterService.findUnReadMessage();
			model.addAttribute("messageList",(List<MessageCenter>)unReadMessage.get("messageList"));
			model.addAttribute("messageCount", (Integer)unReadMessage.get("unReadCount"));//未读消息条数

			//判断是否要显示通知
			if(noticeMsg==null || (noticeMsg!=null && noticeMsg!=0)) {
				//加载通知 最多加载10条 优先加载未读公告，如果未读公告不够10条，再加载已读公告凑够10条。

				Map map = noticeService.findListForTheme();
				List<Notice> readNotices = (List<Notice>) map.get("readList");
				List<Notice> unreadNotices = (List<Notice>) map.get("unreadList");
				model.addAttribute("readNotices", readNotices);
				model.addAttribute("unreadNotices", unreadNotices);
				model.addAttribute("noticeCount", (Integer) map.get("realSize"));//未读公告条数
				int numt = (Integer) unReadMessage.get("unReadCount") + (Integer) map.get("realSize");
				model.addAttribute("numt", numt);
			}else{
				int numt = (Integer) unReadMessage.get("unReadCount") ;
				model.addAttribute("numt", numt);
			}

//问题管理
			if(issueMsg==null || (issueMsg!=null && issueMsg!=0)) {
				EntityWrapper<Issue> ew = new EntityWrapper();
				ew.eq("creater", SessionUtil.getUserId());
				ew.eq("is_reply", "1");
				List<Issue> issueList = issueService.selectList(ew);
				model.addAttribute("issueCount", issueList.size());
				model.addAttribute("issueList", issueList);
			}
			model.addAttribute("version", version);
			Config config = configService.findByCode("SYS_THEME",this.getTenantId());
			configValue = config.getValue();
			//获取个人设置的导航
    		/*UserSetting setting = userSettingService.selectByUserId(userInfo.getId());*/
    		if(setting!=null){
    			String theme = setting.getNavigaStyle();
    			configValue = (theme!=null&&theme.length()>0)?theme:configValue;
    		}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return DIR + configValue+"_head";

	}

	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(String tenantId,String lang, Model model,HttpServletRequest request,HttpServletResponse response){
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {//验证当前是否已经是登录状态，如果是登录状态就不需要再次登录
			return "redirect:/main";
		}
		/*Locale locale = Locale.getDefault();
		LocaleResolver localeResolver =RequestContextUtils.getLocaleResolver(request);
		if (Locale.SIMPLIFIED_CHINESE.toString().equals(lang)) {
			locale = Locale.SIMPLIFIED_CHINESE;
	    } else if (Locale.TRADITIONAL_CHINESE.toString().equals(lang)) {
	    	locale = Locale.TRADITIONAL_CHINESE;
	    } else if (Locale.US.toString().equals(lang)) {
	    	locale = Locale.US;
	    }*/
		LocaleResolver localeResolver =RequestContextUtils.getLocaleResolver(request);
		Locale locale = null;
		Config localeConfig = null;
		Config config = null;
		if(tenantId!=null&&!"".equals(tenantId)){
			if("1".equals(tenantId)){
				localeConfig = configService.findByCode("CONFIG_LOCALE",tenantId);
				setConfigAttr(model,tenantId);
				config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE,tenantId);
			}else{
				Tenant tenant = tenantService.selectById(tenantId);
				if(tenant!=null){
					if(tenant.getIsDelete()==0){
						localeConfig = configService.findByCode("CONFIG_LOCALE","1");
						setConfigAttr(model,"1");
						model.addAttribute("message_login",LangTransform.getLocaleLang(LanguageParam.TENANT_INFORMATION_ERROR));
						config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, "1");
					}else{
						localeConfig = configService.findByCode("CONFIG_LOCALE",tenantId);
						setConfigAttr(model,tenantId);
						config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE,tenantId);
					}

				}else{
					localeConfig = configService.findByCode("CONFIG_LOCALE","1");
					setConfigAttr(model,"1");
					model.addAttribute("message_login",LangTransform.getLocaleLang(LanguageParam.TENANT_INFORMATION_ERROR));
					config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, "1");
				}
			}

		}else{
			localeConfig = configService.findByCode("CONFIG_LOCALE","1");
			setConfigAttr(model,"1");
			config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, "1");
		}

		if (Locale.SIMPLIFIED_CHINESE.toString().equals(localeConfig.getValue())) {
			locale = Locale.SIMPLIFIED_CHINESE;
		} else if (Locale.TRADITIONAL_CHINESE.toString().equals(localeConfig.getValue())) {
			locale = Locale.TRADITIONAL_CHINESE;
		} else if (Locale.US.toString().equals(localeConfig.getValue())) {
			locale = Locale.US;
		}

		localeResolver.setLocale(request, response, locale);
		SessionUtil.setSession(ConfigKeys.SYS_LOCALE, locale);

		List<Tenant> tenantList = tenantService.selectLists();
		model.addAttribute("tenantList",tenantList);
		model.addAttribute("tenantId",tenantId);
		logger.info("login page: {}",config.getValue());
		return config.getValue();
	}

	@RequestMapping(value="/sso/login",method={RequestMethod.GET,RequestMethod.POST})
	public String SSOLogin(@RequestParam(required=false,defaultValue="1") String tenantId, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value="empNo",required=true)String empNo){
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser.isAuthenticated()){
			return "redirect:/main";
		}
		User user = userService.findByUsernameAndTenantIds(empNo, tenantId);
		if(user != null){
			UsernamePasswordToken token = new UsernamePasswordToken(empNo, user.getPassword(),true,tenantId);
			currentUser.login(token);
			return "redirect:/main";
		}
		Config config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, "1");
		return config.getValue();
	}

	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String doLogin(String tenantId,String username,String password,String verifyCode,
			HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		User user = null;
		//判断登录类型 ： 数窗用户登录   or    AD域用户登录
		String status  = configService.findByCode(ConfigKeys.LOGIN_OPTION, tenantId).getValue();
		if("1".equals(status)){//数窗用户登录
			//查出是否有此用户
			user = userService.findByUsernameAndTenantIds(username,tenantId);
		}else if("2".equals(status)){//AD域用户登录
			//查出是否有此用户
			user = userService.findByUsernameAndTenantIds(username,tenantId);
			if(user == null){
				//根据AD域用户登录的信息创建对应数窗账号
				try{
					//系统默认语言
					String CONFIG_LOCALE  = configService.findByCode(ConfigKeys.CONFIG_LOCALE, tenantId).getValue();
					UserInfo userInfo = new UserInfo();
					userInfo.setPassword(password);
					userInfo.setUsername(username);
					userInfoService.save(userInfo,CONFIG_LOCALE,status,tenantId,"EVERYONE");//创建账号赋予“所有人”权限
				}catch (Exception e){
					logger.info(e.getMessage());
				}
				user = userService.findByUsernameAndTenantIds(username,tenantId);
			}else{
				//根据AD域用户登录的信息修改对应数窗账号。
				userInfoService.updateADUser(username,password,getTenantId());
				user = userService.findByUsernameAndTenantIds(username,tenantId);
			}
		}


		if(user!=null && user.getStatus().equals(status)){
			//获取HttpSession中的验证码
			String sessionCode = (String)request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
			String lang = "zh_CN";
			Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, tenantId);
			if(config!=null){
				lang = config.getValue();
			}
			String uvs=LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_1)+"["+username+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_2)
					+"["+verifyCode+"],"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_3)+"["+sessionCode+"]";
			//获取用户请求表单中输入的验证码
			logger.info("[{}]",uvs);
			//获取用户请求表单中输入的验证码
			//logger.info("用户[" + username + "]登錄時輸入的驗證碼爲[" + verifyCode + "],HttpSession中的驗證碼爲[" + sessionCode + "]");
			//获取config表中的验证码是否开启
			Config configCheckCode = configService.findByCode(ConfigKeys.SYS_CHECK_CODE, tenantId);
			//获取初始的语言配置值
			Config configLoginPage = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, tenantId);
			Config sysName = configService.findByCode(ConfigKeys.SYS_NAME, getTenantId());
			if ("1".equals(configCheckCode.getValue())) {
				if (StringUtils.isEmpty(verifyCode) || StringUtils.isEmpty(sessionCode) || !verifyCode.equals(sessionCode.toLowerCase())){
					model.addAttribute("message_login", LangTransform.getLocaleLang(lang, LanguageParam.MAIN_VERIFICATION_CODE_INCORRECT));/*"驗證碼不正確"*/
					model.addAttribute("checkCodeEnable", "1".equals(configCheckCode.getValue())?true:false);
					model.addAttribute("SYS_NAME",sysName.getValue());
					model.addAttribute("SYS_CHECK_CODE",configCheckCode.getValue());
					return configLoginPage.getValue();
				}

			}
//        UsernamePasswordToken token = new UsernamePasswordToken(username, PwdEncoder.encodePassword(password));
			//解密密码
			password=Encrypt(password);
			
			UsernamePasswordToken token = new UsernamePasswordToken(username, PasswordUtils.hash(password),true,tenantId);
			//token.setRememberMe(rememberMe);
			//获取当前的Subject
			Subject currentUser = SecurityUtils.getSubject();
			//在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
			//每个Realm都能在必要时对提交的AuthenticationTokens作出反应
			//所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
			String start=LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+username+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_11);
			String by=LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+username+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_12);
			logger.info("[{}]",start);
			//查询是否开启密码错误用户锁定功能和密码错误次数
			int PASSWORD_ERROR_LOCK  = Integer.parseInt(configService.findByCode(ConfigKeys.PASSWORD_ERROR_LOCK, getTenantId()).getValue());
			int PASSWORD_ERROR_COUNT  =Integer.parseInt(configService.findByCode(ConfigKeys.PASSWORD_ERROR_COUNT, getTenantId()).getValue());
			// 登录前查询该用户是否被锁定
			int num;
			if (lock(username,tenantId)) {
				System.out.println("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
				model.addAttribute("message_login", "账户已锁定,请联系管理员");
			} else {
				String str = String.valueOf(SessionUtil.getSession(username + "_num"));
				if (str == null || str.length() <= 0 || "null".equals(str)) {
					num = 0;
					SessionUtil.setSession(username + "_num", num);
				} else {
					num = Integer.parseInt(str);
				}
				int kk = num + 1;
				System.out.println("第" + kk + "次进行登录验证...");
				try {
					currentUser.login(token);
					logger.info("[{}]",by);
					/*logger.info("對用戶[" + username + "]進行登錄驗證..驗證開始");
            currentUser.login(token);
            logger.info("對用戶[" + username + "]進行登錄驗證..驗證通過");*/

				}catch(UnknownAccountException uae){
					//System.out.println("對用戶[" + username + "]進行登錄驗證..驗證未通過,未知賬戶");
					logger.error(LangTransform.getLocaleLang(lang, LanguageParam.MAIN_UNKNOWN_ACCOUNT));/*"未知賬戶"*/
					model.addAttribute("message_login", LangTransform.getLocaleLang(lang, LanguageParam.MAIN_USERNAME_OR_PASSWORD_INCORRECT));
				}catch(IncorrectCredentialsException ice){
					if (PASSWORD_ERROR_LOCK==1) {
						num++;
						SessionUtil.setSession(username + "_num", num);
						if (user != null && (Integer) SessionUtil.getSession(username + "_num") > PASSWORD_ERROR_COUNT) {
		                    model.addAttribute("message_login", "登陆错误超过" + PASSWORD_ERROR_COUNT + "次，账户已锁定");
		                    SessionUtil.setSession(username + "_num", 0);
		                    User u = new User();
		                    // State=1表示锁定(禁用即锁定)
		                    u.setState(1);
		                    u.setUsername(username);
		                    userService.updateByName(u);
		                } else {
		                    model.addAttribute("message_login", "登陆错误，剩余" + (PASSWORD_ERROR_COUNT - num) + "次将锁定该账户");
		                }
					} else {
						logger.error(LangTransform.getLocaleLang(lang, LanguageParam.MAIN_INCORRECT_PASSWORD));/*"密碼不正確"*/
						model.addAttribute("message_login", LangTransform.getLocaleLang(lang, LanguageParam.MAIN_USERNAME_OR_PASSWORD_INCORRECT));
					}
					
				}catch(LockedAccountException lae){
					//System.out.println("對用戶[" + username + "]進行登錄驗證..驗證未通過,賬戶已鎖定");
					model.addAttribute("message_login", /*"賬戶已鎖定"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_ACCOUNT_LOCKED));
				}catch(ExcessiveAttemptsException eae){
					//System.out.println("對用戶[" + username + "]進行登錄驗證..驗證未通過,錯誤次數過多");
					model.addAttribute("message_login", /*"用戶名或密碼錯誤次數過多"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_TOO_MANY_USERNAME_OR_PASSWORD_ERRORS));
				}catch(ConcurrentAccessException cae){
					//System.out.println("用户[" + username + "]進行登錄驗證..已經在處於登錄狀態，無法重複登錄");
					model.addAttribute("message_login", /*"該用戶已經在其他地方登錄無法重複登錄"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_THE_USER_HAS_LOGGED_IN_ELSEWHERE_AND_CANNOT_LOG_IN_AGAIN));
				}catch (ExpiredCredentialsException e) {//账号过期
					//System.out.println("用户[" + username + "]進行登錄驗證..已經在處於登錄狀態，無法重複登錄");
					model.addAttribute("message_login", /*"該用戶已過期，無法登入"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_USERNAME_IS_EXPIRED));
				}catch(AuthenticationException ae){
					//通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
					//System.out.println("對用戶[" + username + "]進行登錄驗證..驗證未通過,堆棧軌跡如下");
					ae.printStackTrace();
					ae.getMessage();
					model.addAttribute("message_login", /*"用戶名或密碼不正確"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_USERNAME_OR_PASSWORD_INCORRECT));
				}
			}
			if(currentUser.isAuthenticated()){
				SessionUtil.setSession(SessionKeys.IP, WebUtil.getIpAddr(request));
				SessionUtil.setSession("num", 0);
				//对语言
				Locale locale = Locale.getDefault();
				LocaleResolver localeResolver =RequestContextUtils.getLocaleResolver(request);
				String langg = SessionUtil.getUserInfo().getLocale();
				if (Locale.SIMPLIFIED_CHINESE.toString().equals(langg)) {
					locale = Locale.SIMPLIFIED_CHINESE;
				} else if (Locale.TRADITIONAL_CHINESE.toString().equals(langg)) {
					locale = Locale.TRADITIONAL_CHINESE;
				} else if (Locale.US.toString().equals(langg)) {
					locale = Locale.US;
				}
				localeResolver.setLocale(request, response, locale);
				SessionUtil.setSession(ConfigKeys.SYS_LOCALE, locale);

				logger.info("redirect to main...");
				//更新最后登录时间
				user = userService.selectById(SessionUtil.getUserId());
				user.setLastLoginTime(new Date());
				user.setLastLoginIp(WebUtil.getIpAddr(request));
				user.setLoginCount(user.getLoginCount()!=null?user.getLoginCount()+1:1);
				user.updateById();
				return "redirect:main";
			}else{
				//获取登录页LOGO
				setConfigAttr(model,tenantId);
				model.addAttribute("username", username);
				token.clear();
				List<Tenant> tenantList = tenantService.selectLists();
				model.addAttribute("tenantList",tenantList);
				model.addAttribute("tenantId",tenantId);
				return configLoginPage.getValue();
			}
		}else{
			Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, tenantId);
			String lang = "zh_CN";
			setConfigAttr(model,tenantId);
			if(config!=null){
				lang = config.getValue();
			}
			List<Tenant> tenantList = tenantService.selectLists();
			model.addAttribute("tenantList",tenantList);
			//获取初始的语言配置值
			Config configLoginPage = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, tenantId);
			model.addAttribute("message_login", /*"密碼不正確"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_USERNAME_OR_PASSWORD_INCORRECT));
			return configLoginPage.getValue();
		}


	}
	@SystemLog(module="用户登出",operation="用户登出",type=LogType.logout)
	@RequestMapping("/logout")
	public String logout(Model model,String tenantId) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			String exit= LangTransform.getLocaleLang(LanguageParam.LOGGERINFO_1)+subject.getPrincipal()+LangTransform.getLocaleLang(LanguageParam.LOGGERINFO_19);
			logger.info("[{}]",exit);
			//logger.info("用戶[{}]退出登錄",subject.getPrincipal());
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
			//SessionUtil.clear();
		}
		if(tenantId==null && "".equals(tenantId))
			tenantId = "1";
		setConfigAttr(model,tenantId);
		List<Tenant> tenantList = tenantService.selectLists();
		model.addAttribute("tenantList",tenantList);
		model.addAttribute("tenantId",tenantId);
		return configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE,tenantId).getValue();
		//return getCache(ConfigKeys.SYS_LOGIN_PAGE);
}

	@RequestMapping("status")
	public String status(){
		return "main/status";
	}

	private void setConfigAttr(Model model, String tenantId){
		List<Config> list = configService.findListByCode(tenantId);
		for (Config	config : list) {
			//logger.info("tenantId {} {} {}",tenantId,config.getCode(),config.getValue());
			model.addAttribute(config.getCode(), config.getValue());
		}
	}
	private String Encrypt(String password) throws Exception {
		// TODO Auto-generated method stub
		String pwd ; 
		byte[] en_result = hexStringToBytes(password);
		byte[] de_result = RSAUtils.decrypt(RSAUtils.getKeyPair().getPrivate(),  
                en_result);
        StringBuffer sb = new StringBuffer();  
        sb.append(new String(de_result));  
        pwd = sb.reverse().toString();
        password = URLDecoder.decode(pwd,"UTF-8");
        return password;
	}
	public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();

        int length = hexString.length() / 2;

        char[] hexChars = hexString.toCharArray();

        byte[] d = new byte[length];

        for (int i = 0; i < length; i++) {

            int pos = i * 2;

            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));

        }
        return d;
    }
	private static byte charToByte(char c) {
	 return (byte) "0123456789ABCDEF".indexOf(c);
	}
	private String getModuleId(String orgId) {
		Organization organization=new Organization();
		organization=organization.selectById(orgId);
		
		List<OrganizationModule> list=organizationModuleService.getModuleByOrgId(organization.getId());
		OrganizationModule  organizationModule=new OrganizationModule();
		if(list!=null&&list.size()>0){
			organizationModule=list.get(0);
		}
		
		if(organizationModule!=null&&organizationModule.getModuleId()!=null){
			return organizationModule.getModuleId();
		}else{
			if(organization!=null&&organization.getParentId()!=null){
				Wrapper<Organization> wrapper3=new EntityWrapper<Organization>();
				wrapper3.eq("id", organization.getParentId());
				Organization organization2=new Organization();
				organization2=organization2.selectOne(wrapper3);
				if(organization2!=null&&organization2.getCode()!=null){
					return getModuleId(organization2.getId());
				}else{
					return "noHome";
				}
				
			}else{
				return "noHome";
			}
		}
	}
	
	class MyThread extends Thread{
        @Override
        public void run() {
        	startServiceMonitor();
        }
    }
	
	private void startServiceMonitor() {
		if(start==0) {
			int time = 0;
			List<ServiceState> list = serviceStateService.selectList(null);
			if(list.size()>0) {
				for(ServiceState serviceState:list) {
					time = serviceState.getRefreshTime();
					break;
				}
				Timer timer = new Timer();
		        timer.schedule(new ServiceStaticTaskTest(), 0, time*60*1000);
				start = 1;
			}
		}
	}
	@SystemLog(module = LanguageParam.HISTORICAL_ACCESS, operation=LanguageParam.ACTIVE_SEE, type=LogType.query)
	@RequestMapping("/historyLog")
	public String historyLog(Model model){
		String configValue="";
		try {
			Config config = configService.findByCode("SYS_THEME",this.getTenantId());
			configValue = config.getValue();
			UserSetting setting = userSettingService.selectByUserId(SessionUtil.getUserId());
			if(setting!=null){
				configValue = setting.getNavigaStyle();
			}
			//加载最新个人动态
			//暂取前100条
			List<Resource> log = resourceService.findReourceLog(SessionUtil.getUserId());
			model.addAttribute("log", log);

		}catch (Exception e){
		e.printStackTrace();
		}

		return DIR +configValue+"_history";
	}
	
	public boolean lock(String username,String tenantId) {
         User user = userService.findByUsernameAndTenantIds(username,tenantId);
        if (user != null && user.getState() == 2) {
            return true;
        }
        return false;
    }
	
}
