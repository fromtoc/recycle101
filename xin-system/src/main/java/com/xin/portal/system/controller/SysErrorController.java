package com.xin.portal.system.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SysErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	/**  
	 * @Title: handleError 
	 * @Description:  TODO
	 * @return String
	 * @author zhoujun
	 * @Date 2018年12月12日 上午11:41:16 
	 */
	@RequestMapping(value = ERROR_PATH)
	public String handleError() {
		return "errors/404";
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}
	
	@GetMapping(value = "/404")
	  public String error404() {
	    return "error";
	    //return "actuator/index";
	  }

	  @GetMapping(value = "/500")
	  public String error500() {
	    return "error";
	  }

}
