package com.xin.portal.system.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.UserLikesService;
import com.xin.portal.system.model.UserLikes;

import com.xin.portal.system.bean.BaseController;

/**  
* @Title: com.xin.portal.system.controller.UserLikesController 
* @Description: 
* @author zhoujun  
* @date 2018-06-14
* @version V1.0  
*/ 
@Controller
@RequestMapping("/userLikes")
public class UserLikesController extends BaseController {

	private static final String DIR = "userLikes/";
	
	@Autowired
	private UserLikesService service;

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
	 * @param UserLikes
	 * @param Model
	 * @return List<UserLikes>
	 * @author zhoujun
	 * @Date 2018-06-14 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<UserLikes> list(UserLikes query, Model model){
		EntityWrapper<UserLikes> warpper = new EntityWrapper<UserLikes>(query);
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
	public String edit(UserLikes query, Model model){
		EntityWrapper<UserLikes> warpper = new EntityWrapper<UserLikes>(query);
		UserLikes record = service.selectOne(warpper);
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
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(UserLikes record){
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-06-14 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(UserLikes record){
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
	public BaseApi delete(UserLikes record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
}
