package com.xin.portal.system.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.ServerComputer;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.model.SysLog;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.ServiceStateService;
import com.xin.portal.system.service.SysLogService;
import com.xin.portal.system.util.SigarUtils;

/**
 * @ClassPath: com.xin.portal.system.controller.SysLogController 
 * @Description: 日志记录
 * @author zhoujun
 * @date 2018年3月7日 上午11:13:19
 */
@Controller
@RequestMapping("/sysLog")
public class SysLogController {
	
	private static final String DIR = "sysLog/";
	private static final Logger logger = LoggerFactory.getLogger(MstrController.class);
	@Autowired
	private SysLogService service;
	@Autowired
	private DictService dictService;
	@Autowired
	private DictService dicterService;
	@Autowired
	private ServiceStateService serviceStateService;
	@RequestMapping("/index")
	public String index(Model model) {
		List<Dict> sysLogType = dictService.listItem(new Dict("sys_log_type"));
		model.addAttribute("sysLogType", JSONArray.toJSONString(sysLogType));
		return DIR + "index";
	}

	@RequestMapping("/sysLog")
	public String sysLog(Model model) {
		List<Dict> sysLogType = dictService.listItem(new Dict("sys_log_type"));
		model.addAttribute("sysLogType", JSONArray.toJSONString(sysLogType));
		
		List<Dict> recordType = dicterService.listItem(new Dict("record_type"));
		List<ServiceState> list = serviceStateService.selectList(null);
		for(ServiceState s:list) {
			model.addAttribute("serviceState", s);
			break;
		}
		model.addAttribute("recordType", recordType);
		
		ServerComputer serverComputer = SigarUtils.getSysInfo();
		model.addAttribute("serverComputer", serverComputer);
		return DIR + "sysLog";
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<SysLog> page(SysLog query) {
		return service.page(query);
	}
	@RequestMapping("/webLog")
	public String webLog() {
		return DIR + "webLog";
	}
	  /**
     * 模拟日志输出好让页面显示
     */
    @RequestMapping("/mockLog")
    public void mockLog() {
    	/*logger.info("测试日志输出:");
        logger.debug("测试日志输出:" );
        logger.error("测试日志输出:" );
        logger.warn("测试日志输出:" );*/
    }
}
