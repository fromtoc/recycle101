package com.xin.portal.system.util;

public class ExceptionUtil {
	
	public static String getStackLastMsg(Throwable e){
		StackTraceElement [] messages=e.getStackTrace();
		int length=messages.length;
		StringBuffer error = new StringBuffer();
		if(length > 1){
			error.append("Msg is:"+e.getMessage()+";");
			error.append("LastStackTrace : "+ messages[length-1].toString()+" .");
		}else{
			error.append("Msg is:"+e.getMessage()+" .");
		}
		return error.toString();
	}
	
}
