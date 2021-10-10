package com.xin.portal.bi.mstr.http.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xin.portal.bi.mstr.http.Entity.ElementAnswer;
import com.xin.portal.bi.mstr.http.Entity.ElementAnswers;
import com.xin.portal.bi.mstr.http.Entity.MstrAnswer;
import com.xin.portal.bi.mstr.http.Entity.ObjectAnswer;
import com.xin.portal.bi.mstr.http.Entity.ObjectAnswers;
import com.xin.portal.bi.mstr.http.Entity.ValueAnswer;

public class MstrDossierPromptService {

	//根据portal用户信息获取用户的权限以及回答提示的内容
	//--需要可以从cookie或者request的session中获取到portal的用户
	//--设置了 GUID - 在不同组织或者角色或者用户时的 回答内容：值提示-值;对象提示 id:对象id 或者 name(name可能会重复推荐id) ;
	//元素提示 id:实体id:元素id 或者 name(name可能会重复推荐id)
	
	
	//接口只能获取到默认提示值，所以接口只是用于获取key type。
	
	//模拟用户通过自己的权限获得回答提示的内容  
	//测试使用 B19DEDCC11D4E0EFC000EB9495D0F44F / AF52B3BF4E0713EBFF0434B254B770FA
	/**
	 * 
	 * @param mstrPromptList 通过接口获取的提示信息
	 * @param mstrPromptMap
	 * @return
	 */
	public static List<MstrAnswer> getMstrDossierPromptAnswerOld(List<LinkedHashMap<String,Object>> mstrPromptList,
			 Map<String, List<String>> mstrPromptMap) {
		List<MstrAnswer> list = new ArrayList<MstrAnswer>(); 
		//遍历 提示
		for (LinkedHashMap<String,Object> linkMap : mstrPromptList) {
			MstrAnswer mstrAnswer = null;
			String key = String.valueOf(linkMap.get("key"));
			switch (String.valueOf(linkMap.get("type"))) {
			case "VALUE":
				//值提示，获取值提示的值和 key 参数
				String valueAnswers = null;
				if("5ADE552742C51046F13811878AA4BE26".equals(String.valueOf(linkMap.get("id")))){
					valueAnswers = "2016-04-15";
				}
				if("3566E8C444544773334B329251C7F08F".equals(String.valueOf(linkMap.get("id")))){
					valueAnswers = "2017-03-15";
				}
				mstrAnswer = new ValueAnswer(key, "VALUE", false, valueAnswers);
				break;
			case "ELEMENTS":
				List<ElementAnswers> Ela = new ArrayList<>();
				if("7FABC609491F2A211AA363902F5C7B02".equals(String.valueOf(linkMap.get("id")))){
					ElementAnswers elementAnswers = new ElementAnswers("电器");
					Ela.add(elementAnswers);
				}
				mstrAnswer = new ElementAnswer(key, "ELEMENTS", false, Ela);			
				break;
			case "OBJECTS":
				List<ObjectAnswers> Ola = new ArrayList<>();
				if("834151F04ED55D780C06EF8460049750".equals(String.valueOf(linkMap.get("id")))){
					ObjectAnswers objectAnswer = new ObjectAnswers("7FD5B69611D5AC76C000D98A4CC5F24F");
					Ola.add(objectAnswer);
				}else if("86BC370048F62D0022E74EB822B7A695".equals(String.valueOf(linkMap.get("id")))){
					ObjectAnswers objectAnswer = new ObjectAnswers("8D679D3711D3E4981000E787EC6DE8A4");
					Ola.add(objectAnswer);
				}
				mstrAnswer = new ObjectAnswer(key, "OBJECTS", false, Ola);
				break;
			default:
				break;
			}
			list.add(mstrAnswer);
		}
		return list;
	}
	
	/**
	 * 
	 * @param mstrPromptList 通过接口获取的提示信息
	 * @param mstrPromptMap
	 * @return
	 */
	public static List<MstrAnswer> getMstrDossierPromptAnswer(JSONArray mstrPromptList,
			 Map<String, List<String>> mstrPromptMap) {
		List<MstrAnswer> list = new ArrayList<MstrAnswer>(); 
		//遍历 提示
		for(int k = 0; k < mstrPromptList.size(); k++){
			JSONObject linkMap = mstrPromptList.getJSONObject(k);
			MstrAnswer mstrAnswer = null;
			String key = String.valueOf(linkMap.get("key"));
			switch (String.valueOf(linkMap.get("type"))) {
			case "VALUE":
				//值提示，获取值提示的值和 key 参数
				List<String> valueList = mstrPromptMap.get(linkMap.get("id"));
				if(valueList != null && valueList.size() > 0){//有值
					mstrAnswer = new ValueAnswer(key, false, StringUtils.join(valueList.toArray(), ","));
				}else{
					mstrAnswer = new ValueAnswer(key, true, null);
				}
				break;
			case "ELEMENTS":
				//元素提示使用了元素ID作为key，所以需要通过source中的元素ID判断
				List<ElementAnswers> elementAnswer = new ArrayList<>();
				List<String> elementList = mstrPromptMap.get(linkMap.getJSONObject("source").getString("id"));
				if(elementList != null && elementList.size() > 0){//有值
					ElementAnswers elementAnswers = null;
					for (int i = 0; i < elementList.size(); i++) {
						elementAnswers = new ElementAnswers(elementList.get(i));
						elementAnswer.add(elementAnswers);
					}
					mstrAnswer = new ElementAnswer(key, false, elementAnswer);
				} else{
					mstrAnswer = new ElementAnswer(key, true, elementAnswer);
				}
				break;
			case "OBJECTS":
				List<ObjectAnswers> objectAnswer = new ArrayList<>();
				List<String> objectList = mstrPromptMap.get(linkMap.get("id"));
				if(objectList != null && objectList.size() > 0){//有值
					ObjectAnswers objectAnswers = null;
					for (int i = 0; i < objectList.size(); i++) {
						objectAnswers = new ObjectAnswers(objectList.get(i));
						objectAnswer.add(objectAnswers);
					}
					mstrAnswer = new ObjectAnswer(key, false, objectAnswer);
				} else{
					mstrAnswer = new ObjectAnswer(key, true, objectAnswer);
				}
				break;
			default:
				break;
			}
			list.add(mstrAnswer);
		}
		return list;
	}

	public static Map<String, List<String>> transformParamsToMap(String params) {
		//定义map
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		//先通过 |(是特殊符号需要转义) 将内入切割。分成一个个的 id=值,值 的形式
		String[] paramsSplit = params.split("\\|");
		List<String> list = null;
		String key = null;
		//遍历切割后的数组，将空值去掉。
		for (int i = 0; i < paramsSplit.length; i++) {
			list = new ArrayList<>();
			if(paramsSplit[i] != null && paramsSplit[i].trim().length() > 0){
				//获取的值通过= 切割，切割后就是 key value 格式的数据了。
				String[] keyValues = paramsSplit[i].split("=");
				key = keyValues[0];//key
				String value = keyValues[1];//value
				//判断值中是否有值（有值的用值回答，没有值的用默认值回答，）
				if(value != null && value.trim().length() > 0 && value.indexOf(",") > -1){
					//将value通过 , 切割。切割后为多个答案。存储到list中
					String[] values = value.split(",");
					for (int j = 0; j < values.length; j++) {
						list.add(values[j]);
					}
				}else if(value == null || value.trim().length() == 0){
				}else{
					list.add(value);
				}
			}
			result.put(key, list);
		}
		return result;
	}
	
}
