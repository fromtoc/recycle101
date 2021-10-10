package com.xin.portal.bi.FineReport;

import java.util.HashMap;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.util.HttpClientUtils;

public class FineReportHttpClient {
	
	public static String FRAUTHOKEN = "accessToken";
	private static String FRUSERNAME = "fine_username";
	private static String FRPASSWORD = "fine_password";
	private static String FRVALIDITY = "validity";
	private static String LOGINURL = "/login/cross/domain";

	public static String getToken(String baseUrl, BiUser user){
		Map<String, String> params = new HashMap<>();
		params.put(FRUSERNAME, user.getUsername());
		params.put(FRPASSWORD, user.getPassword());
		params.put(FRVALIDITY, "-2");
		
		JSONObject jsonObject = new JSONObject();
		try {
			String content = (String) HttpClientUtils.doGet(baseUrl+LOGINURL,params).get("content");
			//callback({"accessToken":"eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmYW5ydWFuIiwiaWF0IjoxNTU1NDA1NjE4
			//LCJleHAiOjE1NTU0MDkyMTgsInN1YiI6ImFkbWluIiwiZGVzY3JpcHRpb24iOiJhZG1pbihhZG1pbikiLCJqdGkiOi
			//Jqd3QifQ.7A7Dbu2mgFY_EEQ-USVeli1hrAqS88U-u9ouuTAgvxg","url":"http://52.80.85.123:8075/webroot
			///decision?fine_username=admin&validity=-1&fine_password=dod1805"})
			//去掉callback()
			if(content!=null && content!=""){
				content = content.substring(9, content.length()-1);
				jsonObject = JSONObject.parseObject(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject!=null?jsonObject.get(FRAUTHOKEN).toString():null;
	}
	
}
