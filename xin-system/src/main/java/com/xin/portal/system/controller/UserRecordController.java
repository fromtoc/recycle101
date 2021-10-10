package com.xin.portal.system.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.service.UserRecordService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.model.UserRecord;

import com.xin.portal.system.bean.BaseController;

/**  
* @Title: com.xin.portal.system.controller.UserRecordController 
* @Description: 
* @author zhoujun  
* @date 2018-03-12
* @version V1.0  
*/ 
@Controller
@RequestMapping("/userRecord")
public class UserRecordController extends BaseController {

	private static final String DIR = "userRecord/";
	
	@Autowired
	private UserRecordService service;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-03-12 
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
	 * @Date 2018-03-12 
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}

	/**
	 * @Title: page 
	 * @Description:  TODO
	 * @param query
	 * @return PageModel<UserRecord>
	 * @author zhoujun
	 * @Date 2018-03-12 
	 */
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<UserRecord> page(UserRecord query){
		return service.page(query);
	}
	
	
	/**
	 * @Title: select 
	 * @Description:  TODO
	 * @param id
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-03-12 
	 */
	@RequestMapping("/findList")
	@ResponseBody
	public List<UserRecord> findList(UserRecord query, Model model){
		EntityWrapper<UserRecord> warpper = new EntityWrapper<UserRecord>(query);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-03-12 
	 */
	@RequestMapping("/edit")
	public String edit(UserRecord query, Model model){
		EntityWrapper<UserRecord> warpper = new EntityWrapper<UserRecord>(query);
		UserRecord record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-03-12 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(UserRecord record){
		EntityWrapper<UserRecord> ew = new EntityWrapper<UserRecord>();
		ew.eq("type", record.getType()).eq("resource_id", record.getResourceId());
		UserRecord rec = record.selectOne(ew);
		if (rec==null) {
			UserInfo user = SessionUtil.getUserInfo();
			record.setOwner(user.getId());
			record.setCreateTime(new Date());
			return record.insert() ? BaseApi.success() : BaseApi.error();
		} else {
			rec.setCreateTime(new Date());
			return rec.updateById() ? BaseApi.success() : BaseApi.error();
		}
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-03-12 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(UserRecord record){
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
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(UserRecord record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:46:55
	 */
	@SystemLog(module = "我的报表 - 设计说明编辑", name="resourceId" ,type=LogType.update)
	@RequestMapping("/updateIntroduce")
	@ResponseBody
	public BaseApi updateIntroduce(HttpServletRequest request, Integer resourceId, Integer userId,String introduce, Integer resourceType){
		int num = 0 ;
		if (resourceType!=null && resourceType==2) {
			Map map = new HashMap<>();
			map.put("resourceId", resourceId);
			map.put("userId", userId);
			map.put("introduce", introduce);
			num = service.updateIntroduce(map);
		}
		return num > 0 ? BaseApi.success() : BaseApi.error();
	}
	
	
}
