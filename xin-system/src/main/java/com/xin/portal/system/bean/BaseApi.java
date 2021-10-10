/*
 * @Description:  *
 * @Author: zhoujun 
 * @Date: 2018-06-22 11:45:17 
 * @Last Modified by: zhoujun
 * @Last Modified time: 2018-06-22 15:11:07
 */
package com.xin.portal.system.bean;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.xin.portal.system.util.PropertiesUtil;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "返回结果")
public class BaseApi extends HashMap<String, Object>{
    /** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	
    
    public static BaseApi success() {

        return success(PropertiesUtil.getLocaleMessage("result.success"));
    }
    
    public static BaseApi success(String msg) {
    	BaseApi result = new BaseApi();
    	result.put("code", 0);
    	result.put("msg",msg);
        return result;
    }
    
    public static BaseApi page(PageModel page) {
        return success().put("total", page.getTotal()).put("rows", page.getRows()).put("status", 200);
    }
    
    public static BaseApi list(List list) {
        return success().put("list", list);
    }
    
    public static BaseApi data(Object obj) {
        return success().put("data", obj);
    }
    
    public static BaseApi error() {
        return error(PropertiesUtil.getLocaleMessage("result.fail"));
    }
    
    public static BaseApi error(String msg) {
        return error(-1,msg);
    }
    
    public static BaseApi error(Integer code, String msg) {
    	BaseApi result = new BaseApi();
    	result.put("code", code);
    	result.put("msg", msg);
        return result;
    }

    @Override
	public BaseApi put(String key, Object value) {
		super.put(key, value);
		return this;
	}
    

}