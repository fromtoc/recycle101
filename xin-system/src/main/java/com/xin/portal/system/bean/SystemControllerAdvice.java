package com.xin.portal.system.bean;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SystemControllerAdvice {
	/**
	 * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	}

	/**
	 * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
	 * 
	 * @param model
	 */
	@ModelAttribute
	public void addAttributes(Model model) {
		//model.addAttribute("author", "zhoujun");
	}

	/**
	 * 全局异常捕捉处理
	 * 
	 * @param ex
	 * @return 页面
	 */
	@ExceptionHandler(value = Exception.class)
	public String errorHandler(Exception ex, Model model) {
		model.addAttribute("code", -1);
		model.addAttribute("msg", ex.getMessage());
		ex.printStackTrace();
		return "errors/500";
		
//		ModelAndView modelAndView = new ModelAndView();
//	    modelAndView.setViewName("error");
//	    modelAndView.addObject("code", ex.getCode());
//	    modelAndView.addObject("msg", ex.getMsg());
//	    return modelAndView;
	}

	/**
	 * 返回json
	 */
	// @ResponseBody
	// @ExceptionHandler(value = Exception.class)
	// public BaseApi errorHandlerJson(Exception ex) {
	// return new BaseApi(BaseController.FAIL_CODE, ex.getMessage());
	// }
}
