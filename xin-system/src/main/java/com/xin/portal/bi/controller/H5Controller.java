package com.xin.portal.bi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xin.portal.system.bean.BaseController;
@Controller
@RequestMapping("/h5")
public class H5Controller extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(UserReportController.class);

	private static final String DIR = "h5/";

	@RequestMapping("/index")
	public String index(){
		return DIR + "index";
	}

	@RequestMapping("/echartDemo1")
	public String echartDemo1(){
		return DIR+"echartDemo1";
	}
	@RequestMapping("/echartDemo2")
	public String echartDemo2(){
		return DIR+"echartDemo2";
	}
}
