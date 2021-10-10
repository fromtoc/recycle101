package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.model.Role;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.service.RoleService;
import com.xin.portal.system.service.RoleUserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Issue;
import com.xin.portal.system.service.IssueService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

/**  
* @Title: com.xin.portal.controller.IssueController 
* @Description: 模块名称
* @author zhoujun  
* @date 2018-12-03
* @version V1.0  
*/ 
@Controller
@RequestMapping("/issue")
public class IssueController extends BaseController {

	private static final String PATH = "issue/";
	
	@Autowired
	private IssueService service;

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleUserService roleUserService;
	
	@RequestMapping(value="/index")
	public String index(Model model){//问题管理
		model.addAttribute("userId",SessionUtil.getUserId());
		return PATH + "index";
	}
	
	@RequestMapping(value="/singleIndex")
	public String singleIndex(Model model){//个人的问题
		String id = SessionUtil.getUserId();
		model.addAttribute("userId", id);
		return PATH + "single_index";
	}
	
	@RequestMapping(value="/add")
	public String add(HttpServletRequest request, Model model){
		//获取系统授权、系统版本、创建人、创建组织、ip
		Issue record = service.getEnvironmentInfo(request);
		model.addAttribute("record", record);
		return PATH + "add";
	}

	@RequestMapping(value="/edit/{id}")
	public String edit(@PathVariable("id")String id , Model model){
		Issue record =service.selectIssueById(id);
		model.addAttribute("record", record);
		return PATH + "edit";
	}
	
	@RequestMapping(value="/info")
	public String info(String id,Model model){
		Issue record = service.selectIssueById(id);
		model.addAttribute("record", record);

		String code = "admin";
		if(!this.getTenantId().equals("1")){
			code  = "admin_system";
		}
		EntityWrapper<Role> ew = new EntityWrapper<>();
		ew.eq("code",code);
		String parentId= roleService.selectOne(ew).getId();
		EntityWrapper<Role> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("parent_id",parentId);
		//得到所有管理员的ID
		List<Role> roleList = roleService.selectList(entityWrapper);
		List<String> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toList());
		roleIds.add(parentId);
		EntityWrapper<RoleUser> roleUserEntityWrapper = new EntityWrapper<>();
		roleUserEntityWrapper.eq("user_id", SessionUtil.getUserId());
		roleUserEntityWrapper.in("role_id",roleIds);
		List<RoleUser> roleUserList = roleUserService.selectList(roleUserEntityWrapper);
		if(roleUserList.size()>0){
			model.addAttribute("is_admin","1");
		}else{
			model.addAttribute("is_admin","0");
		}
		return PATH + "info";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-03 
	 */
	@SystemLog(module=LanguageParam.PROBLEM_MANAGEMENT ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi page(@RequestBody Issue query){
		return BaseApi.page(service.page(query));
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  新增保存
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-03 
	 */
	@SystemLog(module=LanguageParam.PROBLEM_MANAGEMENT ,operation=LanguageParam.ACTIONADD ,type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Issue record){
		//指定code 规则 w+时间+4位随机数
		String code = "W"+System.currentTimeMillis()+getFixLenthString(4);
		record.setCode(code);
		//指定createtime
		record.setCreateTime(new Date());
		//指定state=1 新增
		record.setState(1);
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  关闭问题
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-03 
	 */
	@SystemLog(module=LanguageParam.PROBLEM_MANAGEMENT ,operation=LanguageParam.CLOSED_QUESTIONS ,type=LogType.update)
	@RequestMapping(value="/update")
	@ResponseBody
	public BaseApi update(Issue record){
		Issue issue = service.updatebyQuery(record);
		return BaseApi.data(issue) ;
	}

	/**
	 * @Title: update
	 * @Description:  关闭问题
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-03
	 */
	@SystemLog(module=LanguageParam.PROBLEM_MANAGEMENT ,operation=LanguageParam.ACTIONUPDATE ,type=LogType.update)
	@RequestMapping(value="/updateData")
	@ResponseBody
	public BaseApi updateData(Issue record){
		return service.updateByIssue(record)?BaseApi.success():BaseApi.error();
	}

	@SystemLog(module=LanguageParam.PROBLEM_MANAGEMENT ,operation=LanguageParam.ACTIONDELETE ,type=LogType.delete,tfToName1="record,id,title,com.xin.portal.system.mapper.IssueMapper,selectById",before="tfToName1",sort="tfToName1")
	@RequestMapping(value="/delete")
	@ResponseBody
	public BaseApi delete(Issue record){
		return record.deleteById()?BaseApi.success():BaseApi.success();
	}

	/**
	 * @Title: updateIsReply
	 * @Description:  修改回复状态值
	 * @return Boolean
	 * @author dch
	 * @Date 2019-05-05
	 */
	@SystemLog(module=LanguageParam.PROBLEM_MANAGEMENT ,operation=LanguageParam.ACTIONUPDATE ,type=LogType.update)
	@RequestMapping(value="/updateIsReply")
	@ResponseBody
	public Boolean updateIsReply(String id){
		Issue issue = new Issue();
		issue.setId(id);
		issue.setIsReply("0");
		return service.updateById(issue);
	}
	
	private static String getFixLenthString(int strLength) {  
	    Random rm = new Random();  
	    // 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(1, strLength + 1);  
	} 
	
}

