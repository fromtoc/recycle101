package com.xin.portal.bi.tableau;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.xin.portal.system.util.HttpClientUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TableauRestApi {

	public static final String AUTH_SIGNIN = "/auth/signin";

	private static final String TICKET = "/trusted";


	static Map<String,String> HEADERS = new HashMap<String,String>(){{
		put("Accept", "application/json");
		put("Content-Type", "application/x-www-form-urlencoded");
		put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
	}};

	public static JSONObject signin(String url, JSONObject jsonParam){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = HttpClientUtils.doPost(url,HEADERS,jsonParam);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static JSONObject getTicket(String url,String username) {
		Map<String,Object> parameterMap = new HashMap<>();
		parameterMap.put("username", StringUtils.isEmpty(username)?"administrator":username);
		JSONObject result = new JSONObject();
		try {
			result = HttpClientUtils.doPost(url + TICKET, parameterMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
