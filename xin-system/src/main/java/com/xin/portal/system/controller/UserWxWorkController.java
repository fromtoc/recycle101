package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Maps;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.mstr.RestApiUtil;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.WebUtil;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**  
* @Title: com.xin.portal.system.controller.UserWxWorkController 
* @Description: 微信企业用户
* @author zhoujun  
* @date 2019-01-07
* @version V1.0  
*/ 
@Controller
@RequestMapping("/userWxWork")
public class UserWxWorkController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(UserWxWorkController.class);

	private static final String PATH = "userWxWork/";
	
	@Autowired
	private UserWxWorkService service;
	@Autowired
	private UserService userService;
	@Autowired
	private ResourceLogService resourceLogService;
	@Autowired
	private ResourceTypeService resourceTypeService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private PromptRelService promptRelService;
	@Autowired
	private BiProjectService biProjectService;
	@Autowired
	private CollectService userCollectService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ConfigService configService;

	@RequestMapping(value="/index",method= RequestMethod.GET)
	public String index(String appId, Model model){
		model.addAttribute("appId",appId);
		return PATH + "index";
	}

	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return PATH + "add";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(){
		return PATH + "edit";
	}

	@RequestMapping(value="/search",method=RequestMethod.GET)
	public String search(){

		return "cp/search";
	}

	@RequestMapping(value="/history",method=RequestMethod.GET)
	public String history(Model model){
		ResourceLog query = new ResourceLog();
		query.setCreater(SessionUtil.getUserId());
		query.setType(ResourceLog.TYPE_VIEW);
		query.setResourceTypeIn("3,4,5,6,7");
		List<ResourceLog> resourceLogs = resourceLogService.findList(query);
		model.addAttribute("resourceLogs",resourceLogs);
		return "cp/history";
	}

	@RequestMapping(value="/collection",method=RequestMethod.GET)
	public String collection(Model model){
		List<ResourceType> userCollects = resourceTypeService.findResourceTypeList(SessionUtil.getUserId());
		model.addAttribute("userCollects",userCollects);
		return "cp/collection";
	}

	@RequestMapping(value="/report/{resourceId:.+}",method=RequestMethod.GET)
	public String report(@PathVariable String resourceId,Model model){
		Resource resource = resourceService.selectById(resourceId);
		if (resource==null) {
			return "error";
		}



		PromptRel query = new PromptRel();
		query.setResourceId(resource.getId());
		List<PromptRel> promptRelList = promptRelService.findList(query);

		BiProject project = biProjectService.findById(resource.getProjectId());

		StringBuilder sb = new StringBuilder();
		if (project!=null) {
			sb.append(project.getUrl()).append("?");
			sb.append("Server=").append(project.getServer());
			sb.append("&Project=").append(project.getProject());
			sb.append("&Port=").append(project.getPort());
			BiUser mstrUser = (BiUser) SessionUtil.getSession(SessionKeys.USER_MAPPING_MSTR);
			if (mstrUser!=null && StringUtils.isNotEmpty(mstrUser.getUsername())) {
				sb.append("&uid=").append(mstrUser.getUsername());
				if (StringUtils.isNotEmpty(mstrUser.getPassword())) {
					sb.append("&pwd=").append(mstrUser.getPassword());
				} else {
					sb.append("&pwd=");
				}
			} else {
				sb.append("&uid=").append(project.getDefaultUid());
				if (StringUtils.isNotEmpty(project.getDefaultPwd())) {
					sb.append("&pwd=").append(project.getDefaultPwd());
				} else {
					sb.append("&pwd=");
				}
			}

			if (StringUtils.isNotEmpty(resource.getHiddenSections())) {
				sb.append("&hiddensections=").append(resource.getHiddenSections());
			}

			if (EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition==resource.getTypeValue()) {
				if (ResourceType.MSTR_DOSSIER.equals(resource.getResourceType1())) {
					sb.append(project.getParamDossier());
				} else {
					sb.append(project.getParamDoc());
				}
				sb.append("&documentID=").append(resource.getReportId());
			} else if (EnumDSSXMLObjectTypes.DssXmlTypeFolder==resource.getTypeValue()) {
				sb.append(project.getParamFolder());
				sb.append("&folderID=").append(resource.getReportId());
			} else {
				sb.append(project.getParamReport());
				sb.append("&reportId=").append(resource.getReportId());
			}
		}

		StringBuilder promptContent = new StringBuilder();
		if (promptRelList!=null && promptRelList.size()>0) {
			sb.append("&valuePromptAnswers=");
			for (PromptRel rec : promptRelList) {
				if (StringUtils.isNotEmpty(rec.getDefaultValue1())) {
					//日期
					if (rec.getType().equals(3)) {
						sb.append(rec.getDefaultValue1().replace("-", "/"));
					} else {
						sb.append(rec.getDefaultValue1());
					}
					promptContent.append(rec.getName()).append(":").append(rec.getDefaultValue1());
				}
				sb.append("%5e");
			}
		}
		logger.info("report url : [{}]",sb);
		model.addAttribute("url",sb);
		model.addAttribute("resource",resource);

		//收藏信息
		EntityWrapper<UserCollect> ewUserCollect = new EntityWrapper<>();
		ewUserCollect.eq("resource_id", resourceId);
		ewUserCollect.eq("user_id", SessionUtil.getUserId());
		UserCollect userCollect= userCollectService.selectOne(ewUserCollect);
		model.addAttribute("collected", userCollect==null?false:true);


		List<Comment> comments = commentService.selectByResourceId(resourceId);
		//如果是会话中的本人，将username set为“我”1068339153872150529
		for(Comment a:comments){
			if(SessionUtil.getUserId().equals(a.getUserId())){
				a.setUserName("我");
			}
		}
		model.addAttribute("userInfo",SessionUtil.getUserInfo());
		model.addAttribute("comments",comments);
		model.addAttribute("resourceId",resourceId);

		return "cp/report";
	}

	@RequestMapping(value="/library/{resourceId:.+}",method=RequestMethod.GET)
	public String library(@PathVariable String resourceId){
		Resource resource = resourceService.selectById(resourceId);
		if (resource==null) {
			return "error";
		}


		switch (resource.getResourceType1()){
			case ResourceType.MSTR_DOSSIER :

				break;
			default:
				break;
		}

		Map<String, Object> params = Maps.newHashMap();
		params.put("username","administrator");
		params.put("password","dod1805");
		params.put("loginMode","1");

		String token = RestApiUtil.getToken("",params);
		//System.out.println("--------"+token);

		return "cp/report";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-01-07 
	 */
	@SystemLog(module= LanguageParam.WECHAT_ENTERPRISE_USERS ,operation= LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(UserWxWork query){
		query.setTenantId(getTenantId());
		return BaseApi.page(service.page(query));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-01-07 
	 */
	@SystemLog(module=LanguageParam.WECHAT_ENTERPRISE_USERS ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(UserWxWork query){
		EntityWrapper<UserWxWork> warpper = new EntityWrapper<UserWxWork>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.WECHAT_ENTERPRISE_USERS ,operation=LanguageParam.DETAILSQUERY ,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		UserWxWork result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-01-07 
	 */
	@SystemLog(module=LanguageParam.WECHAT_ENTERPRISE_USERS ,operation=LanguageParam.ACTIONADD ,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(UserWxWork record){
		record.setCreateTime(new Date());
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-01-07 
	 */
	@SystemLog(module=LanguageParam.WECHAT_ENTERPRISE_USERS ,operation=LanguageParam.ACTIONDELETE ,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(UserWxWork record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.WECHAT_ENTERPRISE_USERS ,operation=LanguageParam.ACTIONDELETE ,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/bind")
    @ResponseBody
    public BaseApi bind(String username, String password,UserWxWork userWxWork,Model model,HttpServletRequest request,HttpServletResponse response) {
    	User user = userService.findByUsername(username);
    	if (user == null) {
    		return BaseApi.error("用户名或密码错误");
    	}
    	
		Subject currentUser = SecurityUtils.getSubject();
		String lang = "zh_CN";
		Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, this.getTenantId());

		if(config!=null){
			lang = config.getValue();
		}
        try {  
        	UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword(),false,this.getTenantId());
			String start= LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+user.getUsername()+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_11);
			String by=LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+user.getUsername()+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_12);
			logger.info("[{}]",start);
			currentUser.login(token);
			logger.info("[{}]",by);
          /* logger.info("对用户[" + user.getUsername() + "]进行登录验证..验证开始");
            currentUser.login(token);  
            logger.info("对用户[" + user.getUsername() + "]进行登录验证..验证通过");  */
            
        }catch(Exception e){
			logger.info(LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_10)+"["+user.getUsername()+"]"+LangTransform.getLocaleLang(lang, LanguageParam.LOGGERINFO_20)+",[{}]",e.getMessage());
        	//logger.info("对用户[" + user.getUsername() + "]进行登录验证..验证未通过,{}",e.getMessage());
            //model.addAttribute("message_login", "未知账户");  
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
    		
    		//保存微信表
    		userWxWork.setId(user.getId());
    		userWxWork.insert();
    		
        	logger.info("redirect to main...");
        	//更新最后登录时间
        	user.setLastLoginTime(new Date());
			user.setLastLoginIp(WebUtil.getIpAddr(request));
			user.setLoginCount(user.getLoginCount()+1);
			user.updateById();
			
			
        	return BaseApi.success();
        } 
        
        return BaseApi.error("用户名或密码错误");
    }
	
	
}

