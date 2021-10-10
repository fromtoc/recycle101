package com.xin.portal.system.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xin.portal.system.model.*;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.cache.CacheManager;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.mail.MailModel;
import com.xin.portal.system.mail.MailService;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.i18n.LanguageParam;

/**
 * @ClassPath: com.xin.portal.system.controller.UserInfoController 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月28日 下午3:46:34
 */
@Controller
@RequestMapping("/userInfo")
public class UserInfoController extends BaseController {

	private static final String DIR = "userInfo/";
	
	@Value("${maxUserCount:0}")
	private Integer maxUserCount;
	@Value("${system.default_password:123456}")
	private String defaultPassword;
	
	
	@Autowired
	private UserInfoService service;
	@Autowired
	private UserService userService;
	@Autowired
	private ResourceLogService resourceLogService;
	@Autowired
	private MailService mailService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private FileService fileService;
	@Autowired
	private DataImportService dataImportService;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private BiServerService biServer;
	@Autowired
	private BiProjectService biProject;
	@Autowired
	private UserSettingService userSettingService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private CollectService collectService;
	@Autowired
	private UserOrganizationService userOrganizationService;
	
	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22 
	 */
	@RequestMapping("/index")
	public String index(){
		return DIR + "index";
	}
	@RequestMapping("/test")
	public String test(){
		return DIR + "test";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22 
	 */
	@RequestMapping("/add")
	public String add(Model model){
		model.addAttribute("defaultPassword", defaultPassword);
		Config configUserEmail = configService.findByCode(ConfigKeys.SYS_USER_EMAIL, getTenantId());
		Config syncCreateMstrUser = configService.findByCode(ConfigKeys.SYS_SYNC_CREATE_MSTR_USER, getTenantId());
		if(String.valueOf(BiServer.TYPE_MSTR).equals(syncCreateMstrUser.getValue())){//同步创建mstr用户
			//mstr-server-list type=1
			EntityWrapper<BiServer> ews = new EntityWrapper<BiServer>();
			ews.eq("type", 1);
			ews.orderBy("sort", false);
			List<BiServer> list = biServer.selectList(ews);
			model.addAttribute("biServer", list);
			//获取自主分析项目（即默认添加到给项目中）
			EntityWrapper<BiProject> ewp = new EntityWrapper<BiProject>();
			ewp.eq("is_indepen_pro", 1);
			List<BiProject> listProject = biProject.selectList(ewp);
			BiProject pro = (listProject!=null&&listProject.size()>0)?listProject.get(0):null;
			model.addAttribute("indepenProject", JSON.toJSONString(pro));
		}
		//获取系统名
		Config configSystemName = configService.findByCode(ConfigKeys.SYS_NAME, getTenantId());
		model.addAttribute("SYS_NAME", configSystemName.getValue());
		model.addAttribute("SYS_USER_EMAIL", configUserEmail.getValue());
		model.addAttribute("SYS_SYNC_CREATE_MSTR_USER", syncCreateMstrUser.getValue());

		return DIR + "add";
	}

	/**
	 * @Title: page 
	 * @Description: 分页查询用户列表
	 * @param query
	 * @return PageModel<MstrUser>
	 * @author zhoujun
	 * @Date 2018-01-22 
	 */
	@SystemLog(module = LanguageParam.USERINFO , operation=LanguageParam.ACTION_SAVE, type=LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<UserInfo> page(UserInfo query){
        query.setUsername(query.getUsername()!=null?query.getUsername().replaceAll(" ", ""):null);
		String status  = configService.findByCode(ConfigKeys.LOGIN_OPTION, getTenantId()).getValue();
		return service.page(query,status);
	}
	
	@SystemLog(module = LanguageParam.USERINFO , operation=LanguageParam.ACTIVE_SEE, type=LogType.query)
	@RequestMapping("/pageRoleUser")
	@ResponseBody
	public PageModel<UserInfo> pageRoleUser(UserInfo query){
		return service.pageRoleUser(query);
	}
	
	/**
	 * @Title: select 
	 * @Description: 不分页查询用户信息列表
	 * @param
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22 
	 */
	@SystemLog(module = LanguageParam.USERINFO , operation=LanguageParam.ACTIVE_SEE,type=LogType.query)
	@RequestMapping("/findList")
	@ResponseBody
	public List<UserInfo> findList(UserInfo query, Model model){
		EntityWrapper<UserInfo> warpper = new EntityWrapper<UserInfo>(query);
		List<UserInfo> list=service.selectList(warpper);
		return list;
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22 
	 */
	@RequestMapping("/edit")
	public String edit(UserInfo query, Model model){
		UserInfo record = service.findUserInfo(query);
		model.addAttribute("record", record);
		Config configUserEmail = configService.findByCode(ConfigKeys.SYS_USER_EMAIL, getTenantId());
		Config syncCreateMstrUser = configService.findByCode(ConfigKeys.SYS_SYNC_CREATE_MSTR_USER, getTenantId());
		model.addAttribute("SYS_USER_EMAIL", configUserEmail.getValue());
		model.addAttribute("SYS_SYNC_CREATE_MSTR_USER", syncCreateMstrUser.getValue());
		return DIR + "edit";
	}
	
	/**
	 * @Title: select 
	 * @Description:  选择
	 * @return String
	 * @author zhoujun
	 * @Date 2017-8-17 下午4:16:01
	 */
	@RequestMapping("/select/{type}")
	public String select(String id, String appId, @PathVariable("type")String type, Model model){
		model.addAttribute("id", id);
		model.addAttribute("appId",appId);
		return DIR + "select_"+type;
	}

	/**
	 * @Title: personRecord
	 * @Description:  个人信息
	 * @return String
	 * @author dingchuanheng
	 * @Date 2019-7-11 下午4:16:01
	 */
	@SystemLog(module = LanguageParam.PERSONAL_INFORMATION , operation = LanguageParam.ACTIVE_SEE, type = LogType.query)
	@RequestMapping("/personalRecord/{userId}")
	public String personRecord(@PathVariable("userId")String userId, Model model){
		try{
			UserInfo record = service.selectById(SessionUtil.getUserId());
			List<Organization> userOrgs = userOrganizationService.selectOrgbyUserId(userId);
			model.addAttribute("userOrgs", userOrgs);
			model.addAttribute("record", record);
			User user = userService.selectById(userId);
			model.addAttribute("user",user);

			User userResetKey = userService.selectById(SessionUtil.getUserId());
			String lastLoginip = userResetKey.getLastLoginIp();
			String lastLoginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(userResetKey.getLastLoginTime());
			String lastLoginAdress = WebUtil.getAddressByIp(lastLoginip);
			model.addAttribute("lastLoginip", lastLoginip);
			model.addAttribute("lastLoginTime", lastLoginTime);
			model.addAttribute("lastLoginAdress", lastLoginAdress);

			List<ResourceLog> list = resourceLogService.findEchartData(SessionUtil.getUserId());
			Map map = new HashMap();
			List<String> xAxis =  new ArrayList<>();
			List<Integer> yAxis =  new ArrayList<>();
			for(ResourceLog resourceLog :list){
				xAxis.add(resourceLog.getName());
				yAxis.add(resourceLog.getNum());
			}
			map.put("xAxis",xAxis);
			map.put("yAxis",yAxis);
			String EchartData = JSONObject.toJSONString(map);
			SessionUtil.setSession("EchartData", EchartData);
		}catch(Exception e){
			e.printStackTrace();
		}

		return DIR + "personalRecord";
	}


	/**
	 * @Title: personRecord
	 * @Description:  修改个人信息
	 * @return String
	 * @author dingchuanheng
	 * @Date 2019-7-11 下午4:16:01
	 */
	@SystemLog(module = LanguageParam.PERSONAL_INFORMATION , operation=LanguageParam.ACTIONUPDATE , type=LogType.update)
	@RequestMapping("/updatePersonalRecord")
	@ResponseBody
	public BaseApi updatePersonalRecord(UserInfo userInfo){
		if(userInfo.getId() ==null || userInfo.getId()!=""){
			String userId  = SessionUtil.getUserId();
			userInfo.setId(userId);
		}
		boolean result = service.updatePersonnalRecordById(userInfo);
		return result?BaseApi.success():BaseApi.error();
	}




	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-22 
	 */
	@SystemLog(module = LanguageParam.USERINFO , operation=LanguageParam.ACTIONADD , type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(UserInfo record){
		//User user = userService.findByUsername(record.getUsername());
		User user = userService.findByUsernameAndTenantIds(record.getUsername(),getTenantId());
		Integer maxUserCount=(Integer) CacheManager.get("maxUserCount");
		if (user!=null) {
			return new BaseApi().put("code","3");//用户名已存在！
		}
		try {
			EntityWrapper<User> ew = new EntityWrapper<>();
			ew.eq("is_delete", 0);
			int count = userService.selectCount(ew);
			boolean newEnable = (maxUserCount==null || maxUserCount==0 || maxUserCount>count ) ? true : false;
			if (newEnable) {
				record.setMstrUser(record.getMstrUser()!=null?record.getMstrUser():0);
				record.setPassword(PasswordUtils.hash(defaultPassword));
				//record.setTenantId(getTenantId());
				String configLocale = configService.findByCode(ConfigKeys.CONFIG_LOCALE,this.getTenantId()).getValue();
				boolean isSaved = service.save(record,configLocale,"1",getTenantId(),"EVERYONE");//登录选项为数窗用户：创建用户
				//如果getUserEmail不为空，需要推送邮件提醒
				if(record.getUserEmail()!=null&&!"".equals(record.getUserEmail())&&isSaved){
					MailModel mailModel = new MailModel();
					mailModel.setToAddress(record.getEmail());
					mailModel.setContent(record.getUserEmail());
//					mailModel.setSubject("系统用户创建成功");
					mailModel.setSubject(LangTransform.getLocaleLang(LanguageParam.SYSTEM_USER_CREATION_SUCCESS));
					mailService.send(mailModel, true);
				}
				return isSaved ? BaseApi.success() : BaseApi.error();
			}
			return new BaseApi().put("code","4");//超出最大用户数！
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-22 
	 */
	@SystemLog(module = LanguageParam.USERINFO , operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(UserInfo record){//该方法仅能用与在组织管理中修改用户信息中。
		record.setMstrUser(record.getMstrUser()!=null?record.getMstrUser():0);
		User u = new User();
		u.setId(record.getId());
		u.setActivateStartTime(record.getActivateStartTime());
		u.setActivateEndTime(record.getActivateEndTime());
		userService.updateTimeById(u);
		//查询用户组织，看主组织是否有变化
		List<Organization> userOrgs = userOrganizationService.selectOrgbyUserId(record.getId());
		if(record.getOrgId()!=null && record.getOrgId().length()>0){
			//新组织与主组织相同不修改，新组织不是主组织也不是兼容组织 直接修改主组织
			//兼职组织与修改后的组织相同，需要将原主组织换成兼职组织，兼职组织换成主
			boolean isSameMainOrg = false;
			boolean isSameDeputyOrg = false;
			String oldOrgId = "";
			for (Organization org : userOrgs) {
				if(org.getIsDeputy() == 0){
					oldOrgId = org.getId();
				}
				if(org.getIsDeputy() == 0 && org.getId().equals(record.getOrgId())){
					isSameMainOrg = true;
				}
				if(org.getIsDeputy() != 0 && org.getId().equals(record.getOrgId())){
					isSameDeputyOrg = true;
				}
			}
			if(!isSameMainOrg && !isSameDeputyOrg){//新组织既不是原主组织也不是原兼职组织
				//修改主组织关联表信息
				EntityWrapper<UserOrganization> ew = new EntityWrapper<>();
				ew.eq("user_id", record.getId());
				ew.eq("is_deputy", 0);
				UserOrganization userOrg = new UserOrganization();
				userOrg.setOrgId(record.getOrgId());
				userOrganizationService.update(userOrg, ew);
			}else if(!isSameMainOrg && isSameDeputyOrg){//新组织不是原主组织但是原兼职组织之一
				//修改兼职组织关联为主
				EntityWrapper<UserOrganization> ewNew = new EntityWrapper<>();
				ewNew.eq("user_id", record.getId());
				ewNew.eq("org_id", record.getOrgId());
				UserOrganization userOrgNew = new UserOrganization();
				userOrgNew.setIsDeputy(0);
				userOrganizationService.update(userOrgNew, ewNew);
				//修改主组织关联为兼职
				EntityWrapper<UserOrganization> ewOld = new EntityWrapper<>();
				ewOld.eq("user_id", record.getId());
				ewOld.eq("org_id", oldOrgId);
				UserOrganization userOrgOld = new UserOrganization();
				userOrgOld.setIsDeputy(1);
				userOrganizationService.update(userOrgOld, ewOld);
			}
			Organization org = organizationService.selectById(record.getOrgId());
			record.setOrgCode(org.getCode());
			record.setExtCode(org.getExtCode());
		}
		return service.updateById(record) ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/info")
	public String info(UserInfo query, Model model){
		UserInfo record = service.findUserInfo(query);
		model.addAttribute("record", record);
		return DIR + "info";
	}
	
	/**
	 * 用户数
	 * @param query
	 * @return
	 */
	@RequestMapping("/count")
	@ResponseBody
	public BaseApi count(UserInfo query){
		EntityWrapper<User> ew = new EntityWrapper<>();
		ew.eq("is_delete", 0);
		int count = userService.selectCount(ew);
		JSONObject jobj = new JSONObject();
		jobj.put("count", count);
		Integer maxUserCount=(Integer) CacheManager.get("maxUserCount");
		jobj.put("maxUserCount", maxUserCount);
		boolean newEnable = (maxUserCount==null || maxUserCount==0 || maxUserCount>count ) ? true : false;
		jobj.put("newEnable", newEnable);
		return BaseApi.data(jobj);
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.USERINFO , operation=LanguageParam.ACTIONDELETE, type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(UserInfo record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}

	@SystemLog(module = LanguageParam.PERSONAL_INFORMATION, operation=LanguageParam.UPDATE_PASSWORD, type=LogType.update)
	@RequestMapping("/setPassword")
	public String setPassword(HttpServletRequest request, Model model){
		model.addAttribute("id", SessionUtil.getUserId());
		//密码强度规则
		Config passwordRule = configService.findByCode(ConfigKeys.PASSWORD_STRENGTH_REQUIRE, getTenantId());
		model.addAttribute("PASSWORD_STRENGTH_REQUIRE", passwordRule.getValue());
		if("1".equals(passwordRule.getValue())){//开启，表示提示密码规则
			//开启密码强度规则具体内容
			Config leastLength = configService.findByCode(ConfigKeys.LEAST_LENGTH, getTenantId());//长度至少
			Config lengthValue = configService.findByCode(ConfigKeys.LENGTH_VALUE, getTenantId());//长度
			Config containNumber = configService.findByCode(ConfigKeys.CONTAIN_NUMBER, getTenantId());//包含数字
			Config containLetter = configService.findByCode(ConfigKeys.CONTAINT_LETTERS, getTenantId());//包含字母
			Config containSymbol = configService.findByCode(ConfigKeys.CONTAINT_SYMBOLS, getTenantId());//包含符号
			model.addAttribute("leastLength", leastLength.getValue());
			model.addAttribute("lengthValue", lengthValue.getValue());
			model.addAttribute("containNumber", containNumber.getValue());
			model.addAttribute("containLetter", containLetter.getValue());
			model.addAttribute("containSymbol", containSymbol.getValue());
		}

		return DIR + "setPassword";
	}
	@RequestMapping("/forget/forgetPwd")
	public String forget(User user){
		return DIR + "forget_password";
	}
	@Transactional(rollbackFor=Exception.class)
	@RequestMapping("/forget/sendEmail")
	@ResponseBody
	public BaseApi sendEmail(HttpServletRequest request,String email){
		Long saveTime = (Long) CacheManager.get(email);
		Long currTime = System.currentTimeMillis();
		if(saveTime!=null && currTime-currTime<5*60*1000){
			return new BaseApi().put("code", -2);//return BaseApi.error("邮件已经发送成功，请不要重复发送！");
		}
		Wrapper<UserInfo> wrapper=new EntityWrapper<UserInfo>();
		wrapper.eq("email", email);
		int i= service.selectCount(wrapper);
		if(i<=0){
			return new BaseApi().put("code", -3);//return BaseApi.error("该邮箱系统不存在，请重新输入");
		}else{
			MailModel mailModel=new MailModel();
			mailModel.setToAddress(email);
			
			UserInfo useInfo=new UserInfo();
			String secretKey= UUID.randomUUID().toString();  //密钥
	        Timestamp outDate = new Timestamp(System.currentTimeMillis()+30*60*1000);//30分钟后过期
	        long date = outDate.getTime()/1000*1000;                  //忽略毫秒数
	        useInfo.setSecretKey(secretKey);
	        useInfo.setOutDate(outDate);
	        
	        Wrapper<UserInfo> useInfoModel=new EntityWrapper<UserInfo>();
	        useInfoModel.where("email={0}",email );
	        service.update(useInfo, useInfoModel);   //保存到数据库
	        
	        String key = email+"$"+date+"$"+secretKey;
	        String url = DigestUtils.md5Hex(key);                //数字签名
	        String lang = "zh_CN";
	        Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, this.getTenantId());
	        if(config!=null){
	        	lang = config.getValue();
	        }
	        String path = (String) request.getAttribute("basePath")+"/userInfo/forget/resetPwd?sid="+url+"&email="+email;
			
//	        String content="请勿回复本邮件.<br/>点击下面的链接,重设密码<br/><a href="+path +" target='_BLANK'>点击我重新设置密码</a>" +
//                    "<br/>提醒:本邮件超过30分钟,链接将会失效，需要重新申请";
	        String content=LangTransform.getLocaleLang(lang, LanguageParam.USERINFO_1)+".<br/>"+LangTransform.getLocaleLang(lang, LanguageParam.USERINFO_2)+"<br/><a href="+path +" target='_BLANK'>"+LangTransform.getLocaleLang(lang, LanguageParam.USERINFO_3)+"</a>" +
                    "<br/>"+LangTransform.getLocaleLang(lang, LanguageParam.USERINFO_4);
			mailModel.setContent(content);
			mailModel.setSubject(LangTransform.getLocaleLang(lang, LanguageParam.USERINFO_5));
			try {
				if(mailService.send(mailModel, true)){
					CacheManager.put(email, System.currentTimeMillis());
					return BaseApi.success();//return BaseApi.success("邮件发送成功！");
				};
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//return BaseApi.error("邮箱服务器故障");
		return new BaseApi().put("code", -4);
	}
	@RequestMapping("/forget/sendSuccess")
	public String sendSuccess(){
		return DIR + "send_success";
	}
	@RequestMapping("/forget/resetPwd")
	public String resetPwd(String sid,String email,Model model,HttpServletRequest request){
		String path = (String) request.getAttribute("basePath");
		if(null==sid||"".equals(sid) || null==email||"".equals(email) ){
            String msg="链接不完整,请重新生成";
            model.addAttribute("msg", msg);
            return DIR+"/forget_password";
        }
		Wrapper<UserInfo> wrapper=new EntityWrapper<UserInfo>();
		wrapper.eq("email", email);
		UserInfo u= service.selectOne(wrapper);
		if(u==null){
			String msg="链接错误,无法找到匹配用户,请重新申请找回密码";
            model.addAttribute("msg", msg);
            return DIR+"/forget_password";
		}
		
		Date outDate = u.getOutDate();
        if(outDate.getTime() <= System.currentTimeMillis()){         //表示已经过期
            String msg = "链接已经过期,请重新申请找回密码.";
            model.addAttribute("msg", msg);
            return DIR +"/forget_password";
        }
        String key = u.getEmail()+"$"+outDate.getTime()/1000*1000+"$"+u.getSecretKey();//数字签名
        String url = DigestUtils.md5Hex(key); 
//        System.out.println(key+"\t"+url);
        if(!url.equals(sid)) {
        	 String msg = "链接不正确,是否已经过期了?重新申请吧";
             model.addAttribute("msg", msg);
             return DIR+"forget_password";
        }
        model.addAttribute("id", u.getId());
//        System.out.println(u.getId()+"忘记密码-成功跳转到修改密码页面");
      	return DIR + "/reset_password"; 
		
	}
	@RequestMapping("/forget/updatePwd")
	@ResponseBody
	public BaseApi resetPwd(User query){
		if (query.getPassword()==null) {
			//query.setPassword(PwdEncoder.encodePassword(defaultPassword));
			query.setPassword(PasswordUtils.hash(defaultPassword));
		} else {
			//query.setPassword(PwdEncoder.encodePassword(query.getPassword()));
			query.setPassword(PasswordUtils.hash(query.getPassword()));
		}
		//成功修改密码之后，需要将secret_key删除，只能修改一次,修改过一次之后将secret_key初始化为0
	    UserInfo userInfo=new UserInfo();
	    userInfo.setSecretKey("0");
	    Wrapper<UserInfo> userInfoModel=new EntityWrapper<UserInfo>();
	    userInfoModel.where("id={0}", query.getId());
	    service.update(userInfo, userInfoModel);
		return query.updateById() ? BaseApi.success() : BaseApi.error();
	}
	@SystemLog(module =LanguageParam.USERINFO_6, operation=LanguageParam.ACTIVE_SEE, type=LogType.query)
	@RequestMapping("/preference/{id}")
	public String preference(@PathVariable("id")String id,Model model){
		UserInfo info = service.selectById(id);
		if("catalogue".equals(info.getPreferenceType()) || "docObject".equals(info.getPreferenceType())){
			Menu menu = menuService.selectById(info.getPreferenceValue());
			model.addAttribute("menu", menu);
		}
		//获取个人设置内容（导航、主题、消息通知）
		UserSetting userSetting = userSettingService.selectByUserIdAndInsert(id);
		model.addAttribute("info", info);
		model.addAttribute("userSetting", userSetting);
		return DIR+"personalSetting";
	}
	
	@RequestMapping("/importUser")
	public String importUser(){
		return DIR+"/importUser";
	}
	@SystemLog(module = LanguageParam.USERINFO ,operation = LanguageParam.USER_IMPORT_TEMPLATE_DOWNLOAD , type=LogType.download)
	@RequestMapping(value = "/downExcel", method = RequestMethod.GET)
	public void downLoad(HttpServletRequest request, HttpServletResponse response) {
			String fileName = null;
		String lang = "zh_CN";
//		Config config = configService.findByCode(SessionUtil, SessionUtil.getUserInfo().getTenantId());
        Object config = SessionUtil.getSession(ConfigKeys.SYS_LOCALE);
        if(config!=null){
			lang = config.toString();
		}
		String user=LangTransform.getLocaleLang(lang,LanguageParam.USER_TEMPLATE);
			try {
				fileName = new String((user+".xlsx").getBytes("UTF-8"), "ISO-8859-1");
				
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			String filePath=null;
			try {
				filePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath()
						 + "doc";
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
			//File file = new File(filePath +"/" + (user+".xlsx"));
			File file = new File(filePath +File.separator + (user+".xlsx"));
			//System.out.println("--------------------------------------"+filePath +"/" + "user.xlsx");;
			// 判断文件父目录是否存在
			if (file.exists()) {
				response.setHeader("conent-type", "application/octet-stream");
				response.setContentType("application/force-download");
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
				
				byte[] buffer = new byte[1024];
				// 文件输入流
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				// 输出流
				OutputStream os = null;
				try {
					os = response.getOutputStream();
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer);
						i = bis.read(buffer);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						bis.close();
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
//			System.out.println("没有找到文件");
			
	}

	@SystemLog(module = LanguageParam.USERINFO ,operation = LanguageParam.UPLOAD_USER ,  type=LogType.upload)
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi excelUserImport(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, Model model) {
		/*String tableName="t_user_info";
		String templateName = request.getParameter("templateName");
		String dataTime = request.getParameter("dataTime");*/
		// 上传目录为 系统部署路径
		//System.out.println("开始------------------------");
		String lang = "zh_CN";//设置语言，用户语言。
        Object configs = SessionUtil.getSession(ConfigKeys.SYS_LOCALE);
        if(configs!=null){
            lang = configs.toString();
        }
		//导入用户功能使用了系统设置中的地址，需要修改为upload路径
		String uploadPath = getUploadPath(request) + "import";
		FileModel f = fileService.upload(uploadPath,"/upload/excel", file, null,false, FileModel.IMPORT);
		//System.out.println("开始读EXCEL数据");
		//System.out.println(f.getSavedPath());
		List<Map<Integer, String>> list = ExcelUtil.readExcelData(f.getSavedPath());
		//System.out.println("读EXCEL完毕...");
		//ImportLog importLog = new ImportLog();//没有使用到，库中无表
		//importLog.setTableName("t_user_info");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//List<UserInfo> userInfoList = new ArrayList<UserInfo>();
		if (list!=null && list.size()>1) {
			list.remove(0);
			BaseApi result =checkExcel(list);
			EntityWrapper<User> ew = new EntityWrapper<>();
			ew.eq("is_delete", 0);
			int count = userService.selectCount(ew);
			Integer maxUserCount=(Integer) CacheManager.get("maxUserCount");
			boolean newEnable = (maxUserCount==null || maxUserCount==0 || maxUserCount>(count+list.size()-1) ) ? true : false;
			if (newEnable) {
				if((Integer)result.get("code")==0){
					//System.out.println("EXCEL格式没问题");
					UserInfo insertUserInfo = null;
					for(int i=0;i<list.size();i++){
						//System.out.println("开始插入第----"+i+1+"----行");
						//验证合法性
						Map<Integer, String> map = new HashMap<Integer, String>(list.get(i));
						String startTime = null;
						insertUserInfo = new UserInfo();
						for (Entry<Integer, String> entry : map.entrySet()) { 
							 // System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
							switch(entry.getKey() ) {
							  	case 0://验证组织不存在 org_code    案例：YFB
							  		String orgCode=entry.getValue().toString();
							  		Organization organization=new Organization();
							  		Wrapper<Organization> o=new EntityWrapper<Organization>(organization);
							  		o.eq("ext_code",orgCode);
							  		organization=organization.selectOne(o);
								  	insertUserInfo.setOrgCode(organization.getCode());
								  	insertUserInfo.setOrgId(organization.getId());
							  		break;
							  	case 1://兼容组织 验证组织是否存在    案例：YFB1,YFB2
							  		String orgCodes=entry.getValue();
							  		if(orgCodes != null && orgCodes.trim().length() > 0){
							  			List<String> orgCodeList = Arrays.asList(orgCodes.split(","));
							  			Organization organizationDeputy = new Organization();
								  		Wrapper<Organization> wo = new EntityWrapper<Organization>(organizationDeputy);
								  		wo.in("ext_code", orgCodeList);
								  		List<Organization> orgList = organizationDeputy.selectList(wo);
								  		if(orgList != null && orgCodeList.size() == orgList.size()){
								  			insertUserInfo.setUserOrgs(orgList);
								  		}
							  		}
							  		break;
							  	case 2://工号，也就是code,不能为空，不能重复
							  		insertUserInfo.setCode(entry.getValue().toString().trim());
							  		break;
							  	case 3://用户名 t_user 表中username,不能重复，不能为空
							  		insertUserInfo.setUsername(entry.getValue().toString().trim());
							  		break;
							  	case 4://姓名realname,暂时不判断
							  		insertUserInfo.setRealname(entry.getValue().toString().trim());
							  		break;
							  	case 5://性别，只能是男女或者空,暂时不判断
							  		String gender=entry.getValue();
							  		String man = LangTransform.getLocaleLang(lang,LanguageParam.USERINFO_GENDER_MALE);
							  		String woman = LangTransform.getLocaleLang(lang,LanguageParam.USERINFO_GENDER_FEMALE);
							  		if(gender!=null && gender.equals(man)){
							  			insertUserInfo.setGender(1);
							  		}else if(gender!=null && gender.equals(woman)){
							  			insertUserInfo.setGender(2);
							  		}else{//默认是男
							  			insertUserInfo.setGender(1);
							  		}
							  		break;
							  	case 6://邮箱可为空，不为空的不能重复且邮件格式校验
							  		insertUserInfo.setEmail(entry.getValue().toString());
							  		break;
							  	case 7://电话可为空，不为空的不能重复且电话格式验证
							  		insertUserInfo.setPhone(entry.getValue().toString());
							  		break;
							  	case 8://有效期开始时间
							  		if(entry.getValue() != null && entry.getValue().trim().length() > 0){
							  			startTime = entry.getValue();
							  		}
							  		break;
							  	case 9://有效期结束时间，两个都为空表示永久有效，一个不为空还是算作都为空。只有两个都不为空才有效
									String endTime = entry.getValue();
							  		if(startTime != null && endTime != null && endTime.trim().length() > 0){
							  			Date startDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(startTime));
							  			Date endDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(endTime));
							  			insertUserInfo.setActivateStartTime(startDate);
							  			insertUserInfo.setActivateEndTime(endDate);
									}
							  		break;
							  	default:
								break;
							}
						}
						try {
							String configLocale = configService.findByCode(ConfigKeys.CONFIG_LOCALE,getTenantId()).getValue();
							service.save(insertUserInfo,configLocale,"1",getTenantId(),"EVERYONE");//登录选项为数窗用户：数窗用户批量导入账号
						} catch (Exception e) {
							e.printStackTrace();
							return new BaseApi().put("code", -15) ;
						}
					}
				}else{
					return  result ;
				}
			}
			return new BaseApi().put("code","4");//超出最大用户数！
		}
		return  BaseApi.success() ;
	}
	public BaseApi checkExcel(List<Map<Integer, String>> list){
		//比对excel数据中用户名，员工号，邮箱是否重复
		BaseApi result = new BaseApi();
		List<String> list_username=new ArrayList<String>();
		List<String> list_name=new ArrayList<String>();
		List<String> list_email=new ArrayList<String>();
		List<String> list_phone=new ArrayList<String>();
		for(int i=0;i<list.size();i++){
			Map<Integer, String> map = new HashMap<Integer, String>(list.get(i));
			for (Entry<Integer, String> entry : map.entrySet()) { 
				// System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				  switch(entry.getKey() ) {
				  	case 2:
				  		list_username.add(entry.getValue().toString());
				  		break;
				  	case 3:
				  		list_name.add(entry.getValue().toString());
				  		break;
				  	case 6:
				  		if(entry.getValue() != null && entry.getValue().trim().length() > 0){
				  			list_email.add(entry.getValue().toString());
				  		}
				  		break;
				  	case 7:
				  		if(entry.getValue() != null && entry.getValue().trim().length() > 0){
				  			list_phone.add(entry.getValue().toString());
				  		}
				  		break;
				  }
			}
		}
		if(!hasSame(list_username)){
			return result.put("code", -2);
//			return BaseApi.error("excel中员工号有重复，请检查");	
		}
		if(!hasSame(list_name)){
			return result.put("code", -3);
//			return BaseApi.error("excel中用户名有重复，请检查");	
		}
		if(!hasSame(list_email)){
			return result.put("code", -4);
//			return BaseApi.error("excel中邮箱有重复，请检查");	
		}
		if(!hasSame(list_phone)){
			return result.put("code", -16);
//			return BaseApi.error("excel中电话号码有重复，请检查");	
		}
		//比对数据库，校验规则等
		//System.out.println("开始验证EXCEL格式");
		UserInfo query = null;
		for(int i=0;i<list.size();i++){
			//System.out.println("开始检查第----"+i+"----行");
			//验证合法性
			Map<Integer, String> map = new HashMap<Integer, String>(list.get(i));
			String startTime = null;
			String mainOrgCode = null;
			for (Entry<Integer, String> entry : map.entrySet()) { 
				//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				switch(entry.getKey() ) {
				  	case 0://验证组织不存在 org_code    案例：信息部- XXB
				  		if(entry.getValue().toString()!=null){
					  		String orgCode = entry.getValue().toString();
					  		Organization organization=new Organization();
					  		Wrapper<Organization> o=new EntityWrapper<Organization>(organization);
					  		o.eq("ext_code",orgCode);
					  		if(organization.selectCount(o)<=0){
					  			result.put("code", -5);
					  			result.put("msg", i+2);
					  			return result;
//					  			return BaseApi.error("导入失败，未查到对应的组织，请检查第"+(i+2)+"行数据");
					  		}
					  		mainOrgCode = orgCode;
					  	} else{
				  			result.put("code", -6);
				  			result.put("msg", i+2);
				  			return result;
//				  			return BaseApi.error("组织结构数据格式错误，请检查第"+(i+2)+"行");
				  		}
				  		break;
				  	case 1://兼职组织。 主组织不能与兼职组织一样，需要判断一下。
				  		String orgCodes=entry.getValue();
				  		if(orgCodes != null && orgCodes.trim().length() > 0){
				  			List<String> orgCodeList = Arrays.asList(orgCodes.split(","));
				  			if(orgCodeList.contains(mainOrgCode)){
				  				result.put("code", -19);//导入失败，主组织不能与兼职组织相同;
					  			result.put("msg", i+2);
					  			return result;
				  			}else{
				  				Organization organizationDeputy = new Organization();
						  		Wrapper<Organization> wo = new EntityWrapper<Organization>(organizationDeputy);
						  		wo.in("ext_code", orgCodeList);
						  		List<Organization> orgList = organizationDeputy.selectList(wo);
						  		if(orgList == null || orgCodeList.size() != orgList.size()){
						  			result.put("code", -5);//"导入失败，未查到对应的组织，请检查第"+(i+2)+"行数据");
						  			result.put("msg", i+2);
						  			return result;
						  		}
				  			}
				  		}
				  		break;
				  	case 2://工号，也就是code,不能为空，不能重复
				  		String username=entry.getValue().toString();
				  		if(null==username||username.length()==0){
				  			result.put("code", -7);
				  			result.put("msg", i+2);
				  			return result;
//				  			return BaseApi.error("员工号不能为空，请检查第"+(i+2)+"行");	
				  		}
				  		query = new UserInfo();
				  		query.setCode(entry.getValue());
				  		List<UserInfo> userinfo=service.selectUser(query);
				  		if(userinfo.size()>0){
				  			result.put("code", -8);
				  			result.put("msg", i+2);
				  			return result;
//				  			return BaseApi.error("员工号重复了，请检查第"+(i+2)+"行");
				  		}
				  		break;
				  	case 3://用户名 t_user 表中username,不能重复，不能为空
				  		String name=entry.getValue().toString();
				  		if(null==name||name.length()==0){
				  			result.put("code", -9);
				  			result.put("msg", i+2);
				  			return result;
//				  			return BaseApi.error("用户名不能为空，请检查第"+(i+2)+"行");	
				  		}
				  		User user=new User();
				  		Wrapper<User> u=new EntityWrapper<User>(user);
				  		u.eq("username",entry.getValue());
				  		u.eq("is_delete",0);
				  		if(user.selectCount(u)>0){
				  			result.put("code", -10);
				  			result.put("msg", i+2);
				  			return result;
//				  			return BaseApi.error("用户名重复了，请检查第"+(i+2)+"行");
				  		}
				  		break;
				  	case 4://姓名realname,暂时不判断
				  		break;
				  	case 5://性别，只能是男女或者空,默认是男。暂时不判断
				  		break;
				  	case 6://邮箱可为空，但是不能重复
				  		String email=entry.getValue().toString();
				  		if(null==email || email.trim().length()==0){
				  			/*result.put("code", -11);
				  			result.put("msg", i+2);
				  			return result;*/
//				  			return BaseApi.error("邮箱不能为空，请检查第"+(i+2)+"行");	
				  			//验证邮箱规则
				  		}else if(!checkEmaile(email)){
				  			result.put("code", -12);
				  			result.put("msg", i+2);
				  			return result;
//				  			return BaseApi.error("邮箱格式错误，请检查第"+(i+2)+"行");	
				  		}else{
				  			query = new UserInfo();
				  			query.setEmail(entry.getValue());
					  		List<UserInfo> userinfo2 = service.selectUser(query);
					  		if(userinfo2.size()>0){
					  			result.put("code", -13);
					  			result.put("msg", i+2);
					  			return result;
//					  			return BaseApi.error("邮箱与已有邮箱重复，请检查第"+(i+2)+"行");
					  		}
				  		}
				  		break;
				  	case 7://电话格式验证， 可为空。验证规则，不可重复。
				  		String phone = entry.getValue().toString();
				  		if(null != phone && phone.length() != 0 ){
				  			if(!checkPhone(phone)){
				  				//验证电话规则
					  			result.put("code", -14);
					  			result.put("msg", i+2);
					  			return result;
//					  			return BaseApi.error("电话号格错误，请检查第"+(i+2)+"行");
				  			}else{
				  				query = new UserInfo();
				  				query.setPhone(entry.getValue());
						  		List<UserInfo> userinfo2 = service.selectUser(query);
						  		if(userinfo2.size()>0){
						  			result.put("code", -17);//电话与已有电话重复，请检查第"+(i+2)+"行
						  			result.put("msg", i+2);
						  			return result;
						  		}
				  			}
				  		}
				  		break;
				  	case 8://有效期开始时间,可为空。不为空校验开始时间是否小于结束时间。
				  		if(entry.getValue() != null && entry.getValue().trim().length() > 0){
				  			startTime = entry.getValue();
				  		}
				  		break;
				  	case 9://有效期结束时间，可为空。都不为空，校验开始时间是否小于结束时间。
				  		String endTime = entry.getValue();
				  		Date endDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(endTime));
				  		Date startDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.valueOf(startTime));
				  		if(startTime != null && endTime != null && endTime.trim().length() > 0){
				  			boolean endTimeisgreaterThanStartTime = DateUtil.greaterThanStartTime(startDate, endDate);
					  		if(!endTimeisgreaterThanStartTime){
					  			result.put("code", -18);//结束日期小于开始日期 请检查第"+(i+2)+"行
					  			result.put("msg", i+2);
					  			return result;
					  		}
				  		}
				  		break;
				  	default:
					break;
				}
			}
		}
		return  BaseApi.success() ;
		
		
	}
	private static boolean hasSame(List<? extends Object> list)  
    {  
        if(null == list)  
            return false;  
        return list.size() == new HashSet<Object>(list).size();  
    }  
	public static boolean checkEmaile(String emaile){
         String rule_email = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
         //正则表达式的模式 编译正则表达式
         Pattern p = Pattern.compile(rule_email);
         //正则表达式的匹配器
         Matcher m = p.matcher(emaile);
         //进行正则匹配\
         return m.matches();  
     }  
	public boolean checkPhone(String phone) {
		 boolean flag = false;
		 //Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		 Pattern regex = Pattern.compile("^(((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		 Matcher matcher = regex.matcher(phone);
		 flag = matcher.matches();
		 return flag;
	}
	@SystemLog(module = LanguageParam.USERINFO_6, operation= LanguageParam.REPLACE_THE_PICTURE , type=LogType.update)
	@RequestMapping("/userMessage")
	public String getUserMessage(Model model){
		UserInfo record = service.selectById(SessionUtil.getUserId());
		String orgId = record.getOrgId();
		String orgName = organizationService.selectById(orgId).getName();
		model.addAttribute("orgName", orgName);
		model.addAttribute("record", record);
		String userId = SessionUtil.getUserId();
		User user = userService.selectById(userId);
		model.addAttribute("user",user);
		Config config = configService.findByCode("SYS_THEME",this.getTenantId());
		String configValue = config.getValue();
		model.addAttribute("SYS_THEME",configValue);
		return DIR+"/userMessage";
	}
	
	//把用户的头像路径返回给前端
	@ResponseBody
	@RequestMapping("/userAvatar")
	public String userAvatar(Model model){
		UserInfo userinfo = new UserInfo();
		String userinfoId = SessionUtil.getUserId();
		userinfo.setId(userinfoId);
		userinfo = userinfo.selectById();
		String AvatarPath = userinfo.getAvatar();
		return AvatarPath ;
	}
	
	//保存用户上传的头像路径到数据库
	@ResponseBody
	@RequestMapping("/saveAvatar")
	public BaseApi saveAvatar(String Avatarpath){ 
		UserInfo userInfo = SessionUtil.getUserInfo();
		userInfo.setAvatar(Avatarpath);
		boolean flag = userInfo.updateById();
		if(flag){
			SessionUtil.setSession(SessionKeys.USER, userInfo);
		}
		return flag ? BaseApi.success() : BaseApi.error();
	}
	
	//保存个人设置到数据库
	@ResponseBody
	@RequestMapping("/savePersonalSetting")
	public BaseApi savePersonalSetting(UserSetting userSetting){ 
		String userId = SessionUtil.getUserId();
		userSetting.setUserId(userId);
		int num = userSettingService.updateUserSetting(userSetting);
		return num>0 ? BaseApi.success() : BaseApi.error();
	}
	//个人主页
	@RequestMapping("/personalPage")
	public String personalPage(Model model){
		String userId = SessionUtil.getUserId();//获取当前用户id
		Config config = configService.findByCode("SYS_THEME",this.getTenantId());
		String configValue = config.getValue();
		UserSetting setting = userSettingService.selectByUserId(SessionUtil.getUserId());
		if(setting!=null && (setting.getNavigaStyle() != null && setting.getNavigaStyle().trim().length() > 0)){
			configValue = setting.getNavigaStyle();
		}
		//分页查询前十条自己的资源日志  去重、时间逆序  （最近访问）
		List<ResourceLog> recentsAccess = resourceLogService.findRecentsResourceLog(userId, 1, 10);
		//分页查询资源日志全部的资源前十  访问最多、权限、时间逆序、排除系统资源  （最热）
		List<ResourceLog> hotAccess = resourceLogService.findHotAccessResourceLog(userId, 1, 10);
		//分页查询公告前十   时间逆序、有效、未读排前？？  （公告）
		Map noticeMap = noticeService.findListForTheme();
		//分页查询最近收藏的资源前十   时间逆序  （收藏）
		UserCollect collect = new UserCollect();
		collect.setPageNumber(1);
		collect.setPageSize(10);
		collect.setUserId(userId);
		List<UserCollect> collectList = collectService.findCollectListPage(collect);
		model.addAttribute("personHistory", recentsAccess);//最近访问
		model.addAttribute("personHotResource", hotAccess);//最热
		model.addAttribute("personReadNotice", noticeMap.get("readList"));//公告
		model.addAttribute("personUnreadNotice", noticeMap.get("unreadList"));//公告
		model.addAttribute("personCollect", collectList);//收藏
		model.addAttribute("sysTheme", configValue);
		return DIR + "personalPage";
	}
	
}
