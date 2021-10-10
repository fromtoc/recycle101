package com.xin.portal.system.controller;

import com.xin.portal.system.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.mapper.EntityWrapper;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.TenantService;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.model.Tenant;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;

/**  
* @Title: com.xin.portal.system.controller.TenantController 
* @Description: 租户管理
* @author zhoujun  
* @date 2018-09-27
* @version V1.0  
*/ 
@Controller
@RequestMapping("/tenant")
public class TenantController extends BaseController {

	private static final String PATH = "tenant/";
	
	@Autowired
	private TenantService service;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
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
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-09-27 
	 */
	@SystemLog(module=LanguageParam.TENANT_MANAGEMENT ,operation=LanguageParam.PAGING_QUERY ,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(Tenant query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-09-27 
	 */
	@SystemLog(module=LanguageParam.TENANT_MANAGEMENT ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(Tenant query){
		query.setIsDelete(1);
		EntityWrapper<Tenant> warpper = new EntityWrapper<Tenant>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.TENANT_MANAGEMENT ,operation=LanguageParam.DETAILSQUERY ,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		Tenant result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-09-27 
	 */
	@SystemLog(module=LanguageParam.TENANT_MANAGEMENT ,sort="name",name="name",operation=LanguageParam.ACTIONADD,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(Tenant record){
		return service.save(record) ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-09-27 
	 */
	@SystemLog(module=LanguageParam.TENANT_MANAGEMENT ,sort="name",name="name",operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(Tenant record){
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
	@SystemLog(module=LanguageParam.TENANT_MANAGEMENT ,operation=LanguageParam.ACTIONDELETE ,sort="tfPToROut1",before="tfPToROut1",
			tfPToROut1="id,id,name,com.xin.portal.system.mapper.TenantMapper,selectById",type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		Tenant t = new Tenant();
		t.setId(id);
		Tenant tenant = t.selectById();
		tenant.setIsDelete(0);
		//将删除的租户下的所有用户，置为无效。
		userService.updateIsDeleteForTenantDelete(id);
		return tenant.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
}

