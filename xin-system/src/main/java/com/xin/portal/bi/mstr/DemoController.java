package com.xin.portal.bi.mstr;

import com.google.common.collect.Maps;
import com.xin.portal.system.bean.BaseApi;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/mstr/demo")
public class DemoController {

    @RequestMapping("index")
    public String index() {
        System.out.println("demo");
        return "mstr/dossier_demo";
    }

    @RequestMapping("token")
    @ResponseBody
    public BaseApi token() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("username","administrator");
        params.put("password","dod1805");
        params.put("loginMode","1");


        String token = RestApiUtil.getToken(RestApiUtil.LIBRARY_PATH, params);
        System.out.println("--------"+token);
        return BaseApi.data(token);
    }
	

}
