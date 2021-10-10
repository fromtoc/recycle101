package com.xin.portal.bi.mstr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;

public class Demo {

	public static void main(String[] args) {
		String url = "http://52.83.123.6:81/MicroStrategyLibrary";
		Map<String,Object> params = Maps.newHashMap();
		params.put("username","administrator");
		params.put("password","dod1805");
		params.put("loginMode",1);

		JSONObject result = RestApiUtil.projects(url, params);

		JSONArray jsonArray = result.getJSONArray("content");
		//System.out.println(jsonArray.toString());
	}

	public static void test() {


	}
}