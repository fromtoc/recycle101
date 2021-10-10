package com.xin.portal.system.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

public class WebUtil {

	private static Logger logger = LoggerFactory.getLogger(WebUtil.class);
	/** 
     * 获取当前网络ip 
     * @param request 
     * @return 
     */  
    public static String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {  
                        e.printStackTrace();  
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
            return ipAddress;   
    }
    
    public static String getMacAddress() {
	    try {
		    Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		    byte[] mac = null;
		    while (allNetInterfaces.hasMoreElements()) {
		    	NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
		    	if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
		    		continue;
			    } else {
			    	mac = netInterface.getHardwareAddress();
			    	if (mac != null) {
			    		StringBuilder sb = new StringBuilder();
			    		for (int i = 0; i < mac.length; i++) {
			    			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			    		}
			    		if (sb.length() > 0) {
			    			return sb.toString();
			    		}
			    	}
			    }
		    }
	    } catch (Exception e) {
	    	// _logger.error("MAC地址获取失败", e);
	    	e.printStackTrace();
	    }
	    return "";
    }

	/**  @Title: getBrower 
	 * @Description:  TODO
	 * @param request
	 * @return Object
	 * @author zhoujun
	 * @Date 2018年12月17日 下午6:01:02 
	 */
	public static String getBrower(HttpServletRequest request) {
		//获取浏览器信息
		Browser browser = UserAgent.parseUserAgentString(request.getHeader("User-Agent")).getBrowser();
		//获取浏览器版本号
		Version version = browser.getVersion(request.getHeader("User-Agent"));
		if (version!=null) {
			return browser.getName() + "/" + version.getVersion();
			
		} else {
			return browser.getName();
		}
	}
	
	/**
	 * 通过IP获取IP的国省市地址
	 * @param ip
	 * @return
	 */
	public static String getAddressByIp(String ip){
		//淘宝IP地址库：http://ip.taobao.com/instructions.php
        String add = null;
        try {
            //URL U = new URL("http://ip.taobao.com/service/getIpInfo.php?ip=114.111.166.72");
            URL U = new URL("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);
            URLConnection connection = U.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));  
            String result = "";
            String line;
            while ((line = in.readLine())!= null){  
                result += line;  
            }
            in.close(); 
            JSONObject jsonObject = JSONObject.parseObject(result); 
            Map<String, Object> map = (Map<String, Object>) jsonObject;
            String code = String.valueOf(map.get("code"));//0：成功，1：失败。
            if("1".equals(code)){//失败
                String errorInfo = String.valueOf(map.get("data"));//错误信息
                logger.error(errorInfo);
            }else if("0".equals(code)){//成功
                Map<String, Object> data = (Map<String, Object>) map.get("data");

                //String country = String.valueOf(data.get("country"));//国家
                //String area = String.valueOf(data.get("area"));//地区
                String city = String.valueOf(data.get("city"));//省（自治区或直辖市）
                String region = String.valueOf(data.get("region"));//市（县）
                add =city==region?city+region:city;
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return "未知";
        } catch (IOException e) {
            e.printStackTrace();
            return "未知";
        }  
        return add;
	}
	
	/**
	 * 获取URL中参数值
	 * @param URL
	 * @param params
	 * @return
	 */
	public static String urlParamValue(String URL, String params) {
		String result = null;
		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit = null;
		String strUrlParam = TruncateUrlPage(URL);
		if (strUrlParam == null) {
			return null;
		}
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");
			// 解析出键值
			if (arrSplitEqual.length > 1) {
				// 正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			} else {
				if (arrSplitEqual[0] != "") {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		Iterator<Map.Entry<String, String>> it = mapRequest.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (entry.getKey().equals(params)) {
				result = entry.getValue();
			}
		}
		return result;
	}

	private static String TruncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;
		if(strURL != null && strURL.trim().length() > 0){
			arrSplit = strURL.split("[?]");
			if (strURL.length() > 1) {
				if (arrSplit.length > 1) {
					for (int i = 1; i < arrSplit.length; i++) {
						strAllParam = arrSplit[i];
					}
				}
			}
		}
		return strAllParam;
	}
	
	public static void main(String[] args) {
		//String a = getAddressByIp("114.111.166.72");
		//System.out.println(a);
	}
	
}
