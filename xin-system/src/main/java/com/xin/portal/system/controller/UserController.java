package com.xin.portal.system.controller;

import java.util.Collection;
import java.util.Date;

import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.util.PasswordUtils;
import com.xin.portal.system.model.User;
import com.xin.portal.system.service.UserService;

/**
 * @ClassPath: com.xin.portal.system.controller.UserController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-8-2 上午10:51:11
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
	
	private static final String DIR = "user/";
	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	private UserService service;
	@Value("${system.default_password}")
	private String defaultPassword;
	
	/**
	 * @Title: index 
	 * @Description:  用户管理
	 * @return String
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:52:43
	 */
	@RequestMapping("/index")
	public String index(){
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
	 * @Title: list 
	 * @Description:  列表查询
	 * @param query
	 * @return List<User>
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:54:14
	 */
	@SystemLog(module = LanguageParam.USER_MANAGEMENT , operation=LanguageParam.ACTIONLISTSEE , type=LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<User> page(User query){
		return service.page(query);
	}
	
	@SystemLog(module = LanguageParam.USER_MANAGEMENT , operation=LanguageParam.UPDATE_PASSWORD , sort="name",name="username", type=LogType.update)
	@RequestMapping("/updatePwd")
	@ResponseBody
	public BaseApi resetPwd(User query){
		if (query.getPassword()==null) {
			//query.setPassword(PwdEncoder.encodePassword(defaultPassword));
			query.setPassword(PasswordUtils.hash(defaultPassword));
			query.setResetKeyLasttime(new Date());
			query.setResetKey("1");
		} else {
			//query.setPassword(PwdEncoder.encodePassword(query.getPassword()));
			query.setPassword(PasswordUtils.hash(query.getPassword()));
			query.setResetKeyLasttime(new Date());
			query.setResetKey("2");
		}
		//在允许重复登录情况下，实现修改密码后踢掉所有重复登录的用户
		String tenantId = this.getTenantId();
		String userId = query.getId();//获取的是重置密码用户的id
		if(query.updateById()){
			Collection<Session> sessions = sessionDAO.getActiveSessions();
			for(Session  session :sessions){
				UserInfo user = (UserInfo)session.getAttribute("user");
				if(user!=null && user.getTenantId().equals(tenantId) && user.getId().equals(userId)){
					session.setTimeout(0);
				}
			}

			return  BaseApi.success();
		}else{
			return BaseApi.error();
		}

	}
	
	@SystemLog(module = LanguageParam.USER_MANAGEMENT , operation=LanguageParam.ACTIONUPDATE, sort="name",name="username", type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(User query){
		return query.updateById() ? BaseApi.success() : BaseApi.error();
	}
	@SystemLog(module = LanguageParam.USER_MANAGEMENT , operation=LanguageParam.ACTIONDELETE, type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(User query){
		query.setIsDelete(1);
		return query.updateById() ? BaseApi.success() : BaseApi.error();
	}
	


}
