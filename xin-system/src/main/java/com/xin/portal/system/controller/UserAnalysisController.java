package com.xin.portal.system.controller;


import com.alibaba.fastjson.JSONObject;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.model.UserRecord;
import com.xin.portal.system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Date:18:072019/6/24
 * 描述:
 */
@Controller
@RequestMapping("/userAnalysis")
public class UserAnalysisController extends BaseController{
    private static Logger logger = LoggerFactory.getLogger(AnalysisController.class);
    private  static String DIR="userAnalysis/";
    @Autowired
    private UserService userService;

    @RequestMapping("/one")
    public String homePage(Model model){
        Integer count = userService.heabCount();
        Integer numberActive =userService.numberActive();
        String activeRate=numberActive*100/count+"%";
        Integer monthActive = userService.monthActive();
        String monthRate = monthActive * 100  / count+ "%";
        Integer dailyActive = userService.dailyActive();
        String dailyRate = dailyActive * 100/ count  + "%";


        model.addAttribute("count",count);
        model.addAttribute("numberActive",numberActive);
        model.addAttribute("monthActive",monthActive);
        model.addAttribute("dailyActive",dailyActive);
        model.addAttribute("activeRate",activeRate);
        model.addAttribute("monthRate",monthRate);
        model.addAttribute("dailyRate",dailyRate);

        return DIR +"index";
    }



}
