package com.xin.portal.bi.mstr.http.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestClient {
	
	public static RestTemplate getRestTemplate(){
		HttpsClientRequestFactory factory = new HttpsClientRequestFactory();
		factory.setReadTimeout(6000);
		factory.setConnectTimeout(6000);
        return new RestTemplate(factory);
    }
	
    public static <T> ResponseEntity<T> postJson(String reqUrl, HttpEntity<?> requestEntity, 
    		Class<T> responseType) {
        ResponseEntity<T> resp = getRestTemplate()
                .exchange(reqUrl, HttpMethod.POST, requestEntity, responseType);
        //返回请求返回值
        return resp;
    }
    
    public static String postForm(String reqUrl, String reqFormPara) {
        //设置 Header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //设置参数
        HttpEntity<String> requestEntity = new HttpEntity<>(reqFormPara, httpHeaders);
        //执行请求
        ResponseEntity<String> resp = getRestTemplate()
                .exchange(reqUrl, HttpMethod.POST, requestEntity, String.class);
        //返回请求返回值
        return resp.getBody();
    }
    
    public static <T> ResponseEntity<T> exchange(String reqUrl, HttpMethod method,
    		HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
		return getRestTemplate()
				.exchange(reqUrl, method, requestEntity, responseType, uriVariables);
    }
    
    public static <T> ResponseEntity<T> put(String reqUrl, HttpEntity<?> requestEntity, 
    		Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> resp = getRestTemplate()
                .exchange(reqUrl, HttpMethod.PUT, requestEntity, responseType, uriVariables);
        //返回请求返回值
        return resp;
    }
    
    public static String getCookieBySet(String name,String set){
        String regex=name+"=(.*?);";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher =pattern.matcher(set);
        if(matcher.find()){
        	String nameValue = matcher.group();
        	String replaceValue = nameValue.replaceAll(";", "").replaceAll(" ", "");
        	String[] splitValue = replaceValue.split("=");
        	if(splitValue.length == 2){
        		return splitValue[1];
        	}
        }
        return null;
    }
    
}

