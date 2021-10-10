package com.xin.portal.system.controller;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.ServerComputer;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.model.ServiceStateRecord;
import com.xin.portal.system.service.ServiceStateRecordService;
import com.xin.portal.system.service.ServiceStateService;
import com.xin.portal.system.util.SigarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**  
* @Title: com.xin.portal.system.controller.ServiceStateController 
* @Description: 
* @author songyi  
* @date 2018-12-11
* @version V1.0  
*/ 
@Controller
@RequestMapping("/serviceStateRecord")
public class ServiceStateRecordController extends BaseController {

	private static final String DIR = "serviceStateRecord/";
	
	@Autowired
	private ServiceStateRecordService service;
	@Autowired
	private ServiceStateService serviceStateService;
	
	@RequestMapping("/index")
	public String index(Model model){
		ServerComputer serverComputer = SigarUtils.getSysInfo();
		model.addAttribute("serverComputer", serverComputer);
		return DIR + "index";
	}

	@RequestMapping("/monitor")
	@ResponseBody
	public BaseApi monitor(){
		return BaseApi.data(SigarUtils.getSysInfo());
	}
	
	/**
	 * @Title: page 
	 * @Description:  TODO
	 * @param query
	 * @return PageModel<ServiceStateRecord>
	 * @author songyi
	 * @Date 2018-12-11
	 */
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<ServiceState> page(ServiceState query){
		return serviceStateService.statePage(query);
	}
	
	@RequestMapping("/info")
	public String info(ServiceStateRecord serviceStateRecord,Model model){
		model.addAttribute("serviceId", serviceStateRecord.getServiceId());
		return DIR + "info";
	}
	
	@RequestMapping("/ServiceStatePage")
	@ResponseBody
	public PageModel<ServiceStateRecord> ServiceStatePage(ServiceStateRecord query){
		return service.findlist(query);
	}
		
}
