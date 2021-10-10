package com.xin.portal.system.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.code.kaptcha.Constants;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;


import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.Issue;
import com.xin.portal.system.model.Menu;
import com.xin.portal.system.model.MessageCenter;
import com.xin.portal.system.model.Notice;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.model.OrganizationModule;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.model.User;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.model.UserRecord;
import com.xin.portal.system.model.UserSetting;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.MenuService;
import com.xin.portal.system.service.OrganizationModuleService;
import com.xin.portal.system.service.ServiceStateService;
import com.xin.portal.system.service.UserService;

/**
 * @ClassPath: com.xin.portal.system.controller.MainController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-7-14 下午6:20:20
 */
@Controller
@RequestMapping("/mobile")
public class MobileController extends BaseController{
	
	private Logger logger = LoggerFactory.getLogger(MobileController.class);
	
	private static final String DIR = "mobile/main/";
	
	private int start = 0;

    @Autowired
    private UserService userService;
    @Value("${tableau.server}")
    private String tableauServer;
    @Autowired
    private ConfigService configService;
    @Autowired
    private OrganizationModuleService organizationModuleService;
    @Autowired
    private ServiceStateService	 serviceStateService;
    @Value("${system.version:1.0.0}")
	private String version;
    @Autowired
    private MenuService menuService;
	
	@RequestMapping("/")
	public String index(HttpServletRequest request,Model model){
		Subject currentUser = SecurityUtils.getSubject();
//		setCache(request);
		if (currentUser.isAuthenticated()) {
			return "forward:mobile/main";
		}
		Config config = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, DEFAULT_TENANT);
		setConfigAttr(model,getTenantId());
		logger.info("login page: {}",config.getValue());
		return config.getValue();
		
	}
	
	@RequestMapping("/main")
    public String greetUser(Model model
    		,HttpServletRequest request, HttpServletResponse response) {
		//进入移动端菜单导航页
		String userId = SessionUtil.getUserId();
    	List<Menu> list = menuService.findUserMobileMenus(userId);
    	List<Menu> uMenus = buildByRecursive(list);
    	uMenus.sort((a, b) -> a.getSort() - b.getSort());
    	List<Menu> userMenus  = new ArrayList<>();
		for(Menu menu : uMenus){
			String  linkUrl = menu.getLinkUrl();
			if(linkUrl!=null && !"".equals(linkUrl)){
				userMenus.add(menu);
			}else{
				List<Menu> isHave = menu.getChildren();
				if(isHave != null && isHave.size()>0){
					userMenus.add(menu);
				}
			}
		}
		Integer colorNum=0;
		for (Menu menu : userMenus) {
			List<Menu> children = menu.getChildren();
			if (children!=null && children.size()>0) {
				children.sort((a, b) -> a.getSort() - b.getSort());
			}
			menu.setChildren(children);
			colorNum+=1;
			if(colorNum>4){
				colorNum=1;
			}
			menu.setColorNum(colorNum);

			String title = menu.getName();
			if(title.length()>4){
				menu.setShortTitle(title.substring(0,4));
			}else{
				menu.setShortTitle(title);
			}
		}
		SessionUtil.setSession(SessionKeys.USER_MENUS, userMenus);
		String userMenusJson = JSONObject.toJSONString(userMenus);
		//SessionUtil.setSession("userMobileMenus", userMenusJson);
		model.addAttribute("userMobileMenus", userMenus);
		model.addAttribute("userId", userId);
		logger.info("用户 {} 跳转到移动端首页面", userId);
		return "mobile/index";
    }
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(@PathVariable(required=false) String tenantId,String lang, Model model,HttpServletRequest request,HttpServletResponse response){
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser.isAuthenticated()){
			return "redirect:/mobile/main";
		}
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
		setConfigAttr(model,DEFAULT_TENANT);
		return "/mobile/login";
	}
	
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String doLogin(String username,String password,
			@RequestParam(value="tenantId", required=false, defaultValue="1") String tenantId,
			HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		String lang = "zh_CN";
		Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, tenantId);
		if(config!=null){
			lang = config.getValue();
		}
        Config configLoginPage = configService.findByCode(ConfigKeys.SYS_LOGIN_PAGE, tenantId);
        Config sysName = configService.findByCode(ConfigKeys.SYS_NAME, tenantId);
        //解密密码
        password=Encrypt(password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, PasswordUtils.hash(password),true,tenantId);
        //获取当前的Subject  
        Subject currentUser = SecurityUtils.getSubject();
        try {  
            //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查  
            //每个Realm都能在必要时对提交的AuthenticationTokens作出反应  
            //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法  
			String start=LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+username+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_11);
			String by=LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+username+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_12);
			logger.info("[{}]",start);
			currentUser.login(token);
			logger.info("[{}]",by);
        }catch(UnknownAccountException uae){
			//System.out.println("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
			model.addAttribute("message_login", /*"未知账户"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_UNKNOWN_ACCOUNT));
		}catch(IncorrectCredentialsException ice){
			//System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
			model.addAttribute("message_login", /*"密码不正确"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_INCORRECT_PASSWORD));
		}catch(LockedAccountException lae){
			//System.out.println("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
			model.addAttribute("message_login", /*"账户已锁定"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_ACCOUNT_LOCKED));
		}catch(ExcessiveAttemptsException eae){
			//System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
			model.addAttribute("message_login", /*"用户名或密码错误次数过多"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_TOO_MANY_USERNAME_OR_PASSWORD_ERRORS));
		}catch(ConcurrentAccessException cae){
			//System.out.println("用户[" + username + "]進行登錄驗證..已經在處於登錄狀態，無法重複登錄");
			model.addAttribute("message_login", /*"該用戶已經在其他地方登錄無法重複登錄"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_THE_USER_HAS_LOGGED_IN_ELSEWHERE_AND_CANNOT_LOG_IN_AGAIN));
		}catch(AuthenticationException ae){
			//通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
			//System.out.println("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
			ae.printStackTrace();
			model.addAttribute("message_login", /*"用户名或密码不正确"*/LangTransform.getLocaleLang(lang, LanguageParam.MAIN_USERNAME_OR_PASSWORD_INCORRECT));
		}
		if(currentUser.isAuthenticated()){
        	SessionUtil.setSession(SessionKeys.IP, WebUtil.getIpAddr(request));
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
        	User user = userService.selectById(SessionUtil.getUserId());
        	user.setLastLoginTime(new Date());
			user.setLastLoginIp(WebUtil.getIpAddr(request));
			user.setLoginCount(user.getLoginCount()+1);
			user.updateById();
        	return "redirect:/mobile/main";
        }else{  
        	model.addAttribute("username", username);
            token.clear();  
            return "/mobile/login";
        }  
	}
	
	@RequestMapping(value="/sso/login",method={RequestMethod.GET,RequestMethod.POST})
	public String SSOLogin(@RequestParam(required=false,defaultValue="1") String tenantId, HttpServletRequest request,
			HttpServletResponse response, @RequestParam(value="j_username",required=true)String j_username, Model model){
		Subject currentUser = SecurityUtils.getSubject();
		if(currentUser.isAuthenticated()){
			return "redirect:/mobile/main";
		}
		String lang = "zh_CN";
		Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, tenantId);
		if(config!=null){
			lang = config.getValue();
		}
		User user = userService.findByUsernameAndTenantIds(j_username, tenantId);
		if(user != null){
			try {
				UsernamePasswordToken token = new UsernamePasswordToken(j_username, user.getPassword(),false,tenantId);
				currentUser.login(token);
			} catch (Exception e) {
				model.addAttribute("msg", e.getMessage());
				return "mobile/error";
			}
			return "redirect:/mobile/main";
		}else{
			model.addAttribute("msg", "未找到用户名为【"+j_username+"】的用户，请联系管理员！");
		}
		return "mobile/error";
	}
	
	@RequestMapping("/logout")
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			String exit= LangTransform.getLocaleLang(LanguageParam.LOGGERINFO_1)+subject.getPrincipal()+LangTransform.getLocaleLang(LanguageParam.LOGGERINFO_19);
			logger.info("[{}]",exit);
			//logger.info("用户[{}]退出登录",subject.getPrincipal());
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
			//SessionUtil.clear();
		}
//		return (String)CacheManager.get(ConfigKeys.SYS_LOGIN_PAGE);
		return getCache(ConfigKeys.SYS_LOGIN_PAGE);
	}
	private void setConfigAttr(Model model, String tenantId){
		EntityWrapper<Config> ew = new EntityWrapper<>();
		ew.eq("tenant_id", StringUtils.isEmpty(tenantId)?DEFAULT_TENANT:tenantId);
		List<Config> list = configService.selectList(ew);
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
	
	/** * 使用递归方法建树 * @param treeNodes * @return */
	public static List<Menu> buildByRecursive(List<Menu> treeNodes) {
		List<Menu> trees = new ArrayList<Menu>();
		for (Menu treeNode : treeNodes) {
			if ("0".equals(treeNode.getParentId())) {
                trees.add(findChildren(treeNode, treeNodes));
            }

		}
		return trees;
	}
	/**     * 递归查找子节点     * @param treeNodes     * @return     */    
	public static Menu findChildren(Menu treeNode,List<Menu> treeNodes) {        
		for (Menu it : treeNodes) {            
			if(treeNode.getId().equals(it.getParentId())) {                
				if (treeNode.getChildren() == null) {                    
					treeNode.setChildren(new ArrayList<Menu>());                
					}                
				treeNode.getChildren().add(findChildren(it,treeNodes));  
				}        
			}        
		return treeNode;
	}

}
