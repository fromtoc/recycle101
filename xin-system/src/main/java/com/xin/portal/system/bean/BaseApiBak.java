package com.xin.portal.system.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @ClassPath: com.xin.portal.system.bean.BaseApi 
 * @Description: 通用返回结果
 * @author zhoujun
 * @date 2018年1月5日 上午10:34:29
 */
@ApiModel(value="返回结果", description="规范返回结果")
public class BaseApiBak {
	@ApiModelProperty(value="状态码",name="code",required=true,allowableValues="0,-1")
	public String code;
	@ApiModelProperty(value="返回结果",name="msg",example="成功/失败")
	public String msg;
	@ApiModelProperty(value="返回数据",name="data")
	public Object data;
	
	public BaseApiBak(String code, String msg){
		this.code = code;
		this.msg = msg;
	}
	
	public BaseApiBak() {
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	

}
