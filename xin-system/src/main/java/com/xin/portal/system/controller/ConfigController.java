package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.mail.MailModel;
import com.xin.portal.system.mail.MailService;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.User;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.AdUtils.LdapFactory;
import com.xin.portal.system.util.AdUtils.entity.AdEntity;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.FileUtil;
import com.xin.portal.system.util.LangTransform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.List;

/**
 * @ClassPath: com.xin.portal.system.controller.ConfigController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-12-8 上午10:00:12
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends BaseController {
	
	private static final String DIR = "config/";

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private ConfigService service;

	@Autowired
	private FileService fileService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private MailService mailService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private UserService userService;
	
	/**
	 * @Title: index 
	 * @Description:  TODO
	 * @return String
	 * @author zhoujun
	 * @Date 2017-11-27 下午6:26:14
	 */
	@RequestMapping("/index")
	public String index(Model model){
		EntityWrapper<Config> ew = new EntityWrapper<>();
		List<Config> list= service.selectList(ew);
		for(Config cf:list){
			if(cf.getCode()!=null&&!"".equals(cf.getCode())&&cf.getValue()!=null&&!"".equals(cf.getValue())){
				model.addAttribute(cf.getCode(),cf.getValue());
				if("PREFERENCE_VALUE".equals(cf.getCode())&&(cf.getValue()!=null&&!"".equals(cf.getValue().trim()))){
					Integer  index  =  cf.getValue().lastIndexOf(",");
					model.addAttribute("PREFERENCE_NAME",cf.getValue().substring(0,index));
				}
			}
		}
		String tenantId=SessionUtil.getUserInfo().getTenantId();
		model.addAttribute("tenantId",tenantId);
		return DIR + "index";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:53:38
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}
	
	/**
	 * @Title: select 
	 * @Description:  TODO
	 * @param query
	 * @return String
	 * @author zhoujun
	 * @Date 2017-11-27 下午6:28:17
	 */
	@SystemLog(module = LanguageParam.SYSTEM_SETTINGS , operation=LanguageParam.ACTIVE_SEE, type=LogType.query)
	@RequestMapping("/findList")
	@ResponseBody
	public List<Config> findList(Config query){
		EntityWrapper<Config> ew = new EntityWrapper<>(query);
		ew.eq("is_display", "1");
		return service.selectList(ew);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:53:47
	 */
	@RequestMapping("/edit")
	public String edit(Config query, Model model){
		Config record = query.selectById();
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:54:41
	 */
	@SystemLog(module = LanguageParam.SYSTEM_SETTINGS , operation=LanguageParam.ACTIONADD, type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Config query){
		query.setCode(query.getCode().toUpperCase());
		Config rec = service.findByCode(query.getCode(), getTenantId());
		if (rec!=null) {
			return BaseApi.error("配置项已经存在！");
		}
		return query.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-11-29 上午10:08:08
	 */
	@SystemLog(module = LanguageParam.SYSTEM_SETTINGS , operation=LanguageParam.ACTIONUPDATE, type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(Config record){
		cacheManager.getCache(Constant.CACHE_DEFAULT).put(record.getCode(),record.getValue());
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}



	@SystemLog(module = LanguageParam.SYSTEM_SETTINGS , operation=LanguageParam.ACTIONUPDATE, type=LogType.update)
	@RequestMapping("/updateByCode")
	@ResponseBody
	public BaseApi updateByCode(String code,String value){
		return service.updateByCode(code,value,this.getTenantId())?BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.SYSTEM_SETTINGS , operation=LanguageParam.ACTIONDELETE, type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(Config record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/refresh")
	@ResponseBody
	public BaseApi refresh(String code){
		//service.refresh(code);
		return BaseApi.success();
	}

	@SystemLog(module = LanguageParam.SYSTEM_SETTINGS , operation=LanguageParam.UPLOAD_LOGO, type=LogType.upload)
	@RequestMapping(value="/upload")
	@ResponseBody
	public BaseApi upload(@RequestParam("file") MultipartFile file,
						  HttpServletRequest request, Model model,@RequestParam("type")String type){
		BaseApi result = new BaseApi();
		String fileName = file.getOriginalFilename();
		// 获得文件后缀名
		String tmpName = fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
		if (!"png".equals(tmpName)) {
			return new BaseApi().put("code",2); 
		}
		long size = file.getSize();
		int flag = FileUtil.checkFileSizeOrType(fileName,size,"image");
		if(flag==1){
			return new BaseApi().put("code",1);
			//return BaseApi.error("上传的文件大小超出上限");
		}else if(flag == 2){
			return new BaseApi().put("code",2);
			//return BaseApi.error("不符合上传要求的文件格式");
		}else{
			String uploadPath = getUploadPath(request) + "logo";
			/*FileModel f = null;
			if(type=="SYS_LOGO"){
				 f = fileService.uploadConfig(uploadPath,"/upload/logo", file,null,true,"logo",348,84);
			}else{
				 f = fileService.uploadConfig(uploadPath,"/upload/logo", file,null,true,"logo",122,34);
			}*/
			FileModel f = fileService.uploadImg(uploadPath,"/upload/logo", file,null,true,FileModel.LOGO);
			if (f.getId()!=null && f.getFilePathView()!=null) {
				result.put("data", f);
				result.put("code", 0);
				return result;
			}else{
				result.put("code", -1);
				return result;
			}
		}
	}
	
	@SystemLog(module = LanguageParam.SYSTEM_SETTINGS , operation=LanguageParam.CHECKING_SEND_MAIL, type=LogType.query)
	@RequestMapping(value="/checkEmailSend")
	@ResponseBody
	public BaseApi checkEmailSend(HttpServletRequest request, Model model){
		MailModel mailModel=new MailModel();
		Config config = service.findByCode(ConfigKeys.MAIL_FROM, this.getTenantId());
		String lang = "zh_CN";
		Config langConfig = service.findByCode(ConfigKeys.CONFIG_LOCALE, this.getTenantId());
		if(langConfig!=null){
			lang = langConfig.getValue();
		}
		mailModel.setToAddress(config.getValue());
		String content=LangTransform.getLocaleLang(lang, LanguageParam.USERINFO_1)+".<br/>"+LangTransform.getLocaleLang(lang, LanguageParam.TEST_EMAIL)+"<br/>" +
				LangTransform.getLocaleLang(lang, LanguageParam.EMAIL_IS_SEND_SUCCESSFULLY);
		mailModel.setContent(content);
		mailModel.setSubject(LangTransform.getLocaleLang(lang, LanguageParam.TEST_EMAIL));
		try {
			if(mailService.send(mailModel, true)){
				return new BaseApi().put("code",0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new BaseApi().put("code",1);
	}


	@RequestMapping("/checkLdapConnect")
	@ResponseBody
	public BaseApi checkLdapConnect(){
		String status  = "2";//configService.findByCode(ConfigKeys.LOGIN_OPTION, getTenantId()).getValue();
		//系统默认语言
		String CONFIG_LOCALE  = configService.findByCode(ConfigKeys.CONFIG_LOCALE, getTenantId()).getValue();
		String DOMAIN_IP  = configService.findByCode("DOMAIN_IP",this.getTenantId()).getValue();
		String DOMAIN_PASSWORD  = configService.findByCode("DOMAIN_PASSWORD",this.getTenantId()).getValue();
		String DOMAIN_NAME  = configService.findByCode("DOMAIN_NAME",this.getTenantId()).getValue();
		String DOMAIN_ACCOUNT  = configService.findByCode("DOMAIN_ACCOUNT",this.getTenantId()).getValue();
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(DOMAIN_ACCOUNT);
		userInfo.setPassword(DOMAIN_PASSWORD);
		AdEntity adEntity = new AdEntity();
		adEntity.setUsername(DOMAIN_ACCOUNT);
		adEntity.setPassword(DOMAIN_PASSWORD);
		adEntity.setDomain(DOMAIN_NAME);
		adEntity.setLdapIP(DOMAIN_IP);
		LdapContext ldapContext = LdapFactory.initLdapConnection(adEntity.getLdapURL(),adEntity.getUserPrincipalName(),adEntity.getPassword());
		if(ldapContext!=null){
			LdapFactory.closeLdapConnection(ldapContext);
			try{
				User user = userService.findByUsernameAndTenantIds(DOMAIN_ACCOUNT,getTenantId());
				//更新AD组织架构到数窗
				organizationService.importOrgForAD(this.getTenantId());
				if(user == null) {
					userInfoService.saveAdministrator(userInfo, CONFIG_LOCALE, status, getTenantId(), "admin_system");
				}
				service.updateByCode(ConfigKeys.LOGIN_OPTION,"2",this.getTenantId());

			}catch(Exception e){
				return BaseApi.error();
			}
			return BaseApi.success();
		}else{
			service.updateByCode(ConfigKeys.LOGIN_OPTION,"1",this.getTenantId());
			return BaseApi.error();
		}
	}
	
}
