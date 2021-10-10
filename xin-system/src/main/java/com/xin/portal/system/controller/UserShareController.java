package com.xin.portal.system.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.UserShareService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.model.UserRecord;
import com.xin.portal.system.model.UserShare;

import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;

/**  
* @Title: com.xin.portal.system.controller.UserShareController 
* @Description: 
* @author zhoujun  
* @date 2018-06-14
* @version V1.0  
*/ 
@Controller
@RequestMapping("/userShare")
public class UserShareController extends BaseController {

	private static final String DIR = "userShare/";
	
	@Autowired
	private UserShareService service;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-06-14 
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
	 * @Date 2018-06-14 
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}

	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param UserShare
	 * @param Model
	 * @return List<UserShare>
	 * @author zhoujun
	 * @Date 2018-06-14 
	 */
	@SystemLog(module = "报表分享查看", type=LogType.query)
	@RequestMapping("/list")
	@ResponseBody
	public List<UserShare> list(UserShare query, Model model){
		EntityWrapper<UserShare> warpper = new EntityWrapper<UserShare>(query);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-06-14 
	 */
	@RequestMapping("/edit")
	public String edit(UserShare query, Model model){
		EntityWrapper<UserShare> warpper = new EntityWrapper<UserShare>(query);
		UserShare record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-06-14 
	 */
	@SystemLog(module = "分享报表添加", type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(UserShare record){
		String[] userIds = record.getUserIds();
		if (userIds!=null && userIds.length>0) {
			List<UserShare> list = Lists.newArrayList();
			for (String userId : userIds ) {
				UserShare rec = new UserShare();
				rec.setResourceId(record.getResourceId());
				rec.setResourceType(record.getResourceType());
				rec.setUserId(userId);
				rec.setShareType(record.getShareType());
				rec.setRemark(record.getRemark());
				rec.setCreater(SessionUtil.getUserId());
				rec.setCreateTime(new Date());
				list.add(rec);
			}
			return service.insertBatch(list) ? BaseApi.success() : BaseApi.error();
		} else {
			return record.insert() ? BaseApi.success() : BaseApi.error();
		}
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-06-14 
	 */
	@SystemLog(module = "分享报表编辑", type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(UserShare record){
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
	@SystemLog(module = "分享报表删除", type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(UserShare record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
}
