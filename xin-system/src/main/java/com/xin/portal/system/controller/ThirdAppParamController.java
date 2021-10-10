package com.xin.portal.system.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.ThirdAppParam;
import com.xin.portal.system.model.UserWxWork;
import com.xin.portal.system.model.WxPush;
import com.xin.portal.system.service.ThirdAppParamService;
import com.xin.portal.system.service.UserWxWorkService;
import com.xin.portal.system.service.WxPushService;
import com.xin.portal.system.util.SessionUtil;

@Controller
@RequestMapping("/thirdAppParam")
public class ThirdAppParamController extends BaseController {
	
	private static final String PATH = "thirdAppParam/";
	
	@Autowired
	private ThirdAppParamService service;
	@Autowired
	private UserWxWorkService userWxWorkService;
	@Autowired
	private WxPushService wxPushService;
	
	@RequestMapping("/index")
	public String index(){
		return PATH + "index";
	}
	
	@RequestMapping("/add/{type}")
	public String add(@PathVariable String type){
		return PATH + "add_"+type;
	}
	
	@RequestMapping("/edit/{type}")
	public String edit(@PathVariable String type){
		return PATH + "edit_"+type;
	}
	
	@RequestMapping("/info")
	@ResponseBody
	public BaseApi info(ThirdAppParam record){
		record.setTenantId(getTenantId());
		List<ThirdAppParam> list = service.findeListByParam(record);
		return BaseApi.data(list.get(0));
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi page(ThirdAppParam record){
		record.setTenantId(getTenantId());
		PageModel<ThirdAppParam> page = service.page(record);
		return BaseApi.page(page);
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(ThirdAppParam record){
		record.setTenantId(getTenantId());
		record.setCreater(SessionUtil.getUserId());
		record.setIsEnable("1");
		record.setCreateTime(new Date());
		//企业微信需要验证corpId和agentId是否重复
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
		ew.eq("app_corpid", record.getAppCorpid());
		ew.eq("app_id", record.getAppId());
		ew.eq("tenant_id", record.getTenantId());
		List<ThirdAppParam> selectList = service.selectList(ew);
		if(selectList !=null  && selectList.size() > 0){
			return BaseApi.error(-2,"");
		}
		return service.save(record)?BaseApi.success():BaseApi.error();
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(HttpServletRequest request, HttpServletResponse response, ThirdAppParam record){
		record.setTenantId(getTenantId());
		record.setUpdater(SessionUtil.getUserId());
		record.setUpdateTime(new Date());
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
		ew.eq("app_corpid", record.getAppCorpid());
		ew.eq("app_id", record.getAppId());
		ew.eq("tenant_id", record.getTenantId());
		ew.ne("id", record.getId());
		List<ThirdAppParam> selectList = service.selectList(ew);
		if(selectList !=null  && selectList.size() > 0){
			return BaseApi.error(-2,"");
		}
		boolean update = service.update(record);
		if(update && ThirdAppParam.APP_TYPE_WECHAT_CP.equals(record.getAppType())){
			//修改微信的缓存配置
			try {
				request.getRequestDispatcher("/wx/cp/" + record.getAppCorpid() + 
						"/" + record.getAppId() + "/update").forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return update?BaseApi.success():BaseApi.error();
	}
	
	@RequestMapping("/synchRelation")
	@ResponseBody
	public BaseApi synchRelation(HttpServletRequest request, HttpServletResponse response, ThirdAppParam record){
		//通过id查询到应用参数
		record.setTenantId(getTenantId());
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<ThirdAppParam>(record);
		ThirdAppParam app = service.selectOne(ew);
		if(app != null){
			//判断参数类型，根据类型同步用户
			if(ThirdAppParam.APP_TYPE_WECHAT_CP.equals(app.getAppType())) {
				try {
					request.setAttribute("app", app);
					request.getRequestDispatcher("/wx/cp/" + app.getAppCorpid() + 
							"/" + app.getAppId() + "/synchRelation").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
					return BaseApi.error(-6, "Request forwarding exception");
				} catch (IOException e) {
					e.printStackTrace();
					return BaseApi.error(-5, "IO Exception");
				}
			}else if(ThirdAppParam.APP_TYPE_DINGDING.equals(app.getAppType())){
			
			}else if(ThirdAppParam.APP_TYPE_LINE.equals(app.getAppType())){
					
			}
			return BaseApi.success();
		}
		return BaseApi.error(-2, "No such parameter");
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@RequestBody ThirdAppParam record, HttpServletRequest request, HttpServletResponse response){
		//查看该应用是否有推送存在，如果存在则不能删除。
		EntityWrapper<WxPush> ewp = new EntityWrapper<>();
		ewp.eq("app_id", record.getId());
		ewp.eq("tenant_id", getTenantId());
		List<WxPush> selectList = wxPushService.selectList(ewp);
		if(selectList != null && selectList.size() > 0){
			return BaseApi.error(-2, "");///该应用下存在定时调度
		}
		//查询到record的信息
		ThirdAppParam thirdAppParam = service.selectById(record.getId());
		boolean delete = service.delete(record);
		if(delete){
			//删除关联用户信息
			EntityWrapper<UserWxWork> ew = new EntityWrapper<>();
			ew.eq("app_id", thirdAppParam.getId());
			userWxWorkService.delete(ew);
			if(ThirdAppParam.APP_TYPE_WECHAT_CP.equals(thirdAppParam.getAppType())){
				//删除微信的缓存配置
				try {
					request.getRequestDispatcher("/wx/cp/" + thirdAppParam.getAppCorpid() + 
							"/" + thirdAppParam.getAppId() + "/delete").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return delete?BaseApi.success():BaseApi.error();
		
	}
	
}
