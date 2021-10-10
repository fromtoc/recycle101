package com.xin.portal.bi.mstr;

import com.microstrategy.utils.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetSessionState {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void main(String[] args)  {
		//return "";
		try {
			//System.out.println(URLEncoder.encode("&project=MicroStrategy Tutorial&uid=administrator&pwd=","GBK"));
			
			test2();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void test2()throws ClientProtocolException, IOException{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		try {
			//获取sessionState url
			String url = "http://192.168.1.175:8080/MicroStrategy/servlet/taskAdmin?taskId=login&taskEnv=xhr&taskContentType=json&server=192.168.1.175&project=MicroStrategy+Tutorial&userid=administrator&password=";
			HttpPost post = new HttpPost(url);
			StringBuilder soapRequestData = new StringBuilder();
			
			StringEntity entiy = new StringEntity(url.toString(),
					"GBK");
			//entiy.setContentEncoding("UTF-8");

			post.setEntity(entiy);
			//http请求头 xml格式
			post.setHeader("Content-Type", "text/xml; charset=GBK");
			//tomcat 用户名密码，防止401错误
			String username = "admin";
			String password = "admin175";
			String auth = "Basic " + Base64.encode((username + ":" + password).getBytes());
			post.addHeader("Authorization", auth);
			//http 请求获取sessionState
			CloseableHttpResponse  response = closeableHttpClient.execute(post);
			//System.out.println("===httpclient state==="+response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String sessionState = "";
			if (entity != null) {
                String results = EntityUtils.toString(entity);
                //System.out.println("======httpclient response======");
                //System.out.println(results);
               // System.out.println("======httpclient response end ======");
                //解析返回xml获取sessionState
                sessionState = getValue(results, "min-state"); 
               // System.out.println("===sessionState==="+sessionState);
                //return sessionState;
            }
			
		} finally {
			// 释放资源  
            closeableHttpClient.close();
		}
	}
	
	
	public void test1() throws ClientProtocolException, IOException{
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();  
        CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		try {
			//获取sessionState url
			String url = "http://192.168.1.175:8080/MicroStrategy/servlet/taskAdmin?taskId=getSessionState&taskEnv=xml&taskContentType=xml&server=192.168.1.175&project=数据服务智能平台&uid=administrator&pwd=password";
			HttpPost post = new HttpPost(url);
			StringBuilder soapRequestData = new StringBuilder();
			StringEntity entiy = new StringEntity(url.toString(),
					"GBK");
			//entiy.setContentEncoding("UTF-8");

			post.setEntity(entiy);
			//http请求头 xml格式
			post.setHeader("Content-Type", "text/xml; charset=GBK");
			//tomcat 用户名密码，防止401错误
			String username = "admin";
			String password = "admin";
			String auth = "Basic " + Base64.encode((username + ":" + password).getBytes());
			post.addHeader("Authorization", auth);
			//http 请求获取sessionState
			CloseableHttpResponse  response = closeableHttpClient.execute(post);
			//System.out.println("===httpclient state==="+response.getStatusLine());
			HttpEntity entity = response.getEntity();
			String sessionState = "";
			if (entity != null) {
                String results = EntityUtils.toString(entity);
                //System.out.println("======httpclient response======");
               // System.out.println(results);
                //System.out.println("======httpclient response end ======");
                //解析返回xml获取sessionState
                sessionState = getValue(results, "min-state"); 
                //System.out.println("===sessionState==="+sessionState);
                //return sessionState;
            }
			
		    String durl = "http://192.168.1.175:8080/MicroStrategy/servlet/taskAdmin?taskId=exportReport&taskEnv=xml&taskContentType=xml&sessionState=";
			//String durl = "http://192.168.1.175:8080/MicroStrategy/servlet/taskAdmin?taskId=exportDocument&taskEnv=xml&taskContentType=xml&sessionState=";
			durl = durl + sessionState;
		    durl = durl + "&reportID=EC35ED1E4512811AFC2F2F9AD5EFB7D3&executionMode=3&elementsPromptAnswers=3144D3D3427319E5DF48C185039BFF1B%3B3144D3D3427319E5DF48C185039BFF1B%3A1%5E%E4%B8%AD%E5%9B%BD%E4%BA%BA%E5%AF%BF%E5%86%8D%E4%BF%9D%E9%99%A9%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8%3B%2C6D26F2094BBBD3CFABCEDAAA58758DF3%3B6D26F2094BBBD3CFABCEDAAA58758DF3%3A%5E%3B%2CA13F80AF45F5876014D43CA23D3FE407%3BA13F80AF45F5876014D43CA23D3FE407%3A%5E&valuePromptAnswers=2016%2F12%2F31";
			//durl = durl + "&objectID=235C65E74351C169B8824D972B86CC33&executionMode=4&elementsPromptAnswers=3144D3D3427319E5DF48C185039BFF1B%3B3144D3D3427319E5DF48C185039BFF1B%3A1%5E%E4%B8%AD%E5%9B%BD%E4%BA%BA%E5%AF%BF%E5%86%8D%E4%BF%9D%E9%99%A9%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8%3B%2C6D26F2094BBBD3CFABCEDAAA58758DF3%3B6D26F2094BBBD3CFABCEDAAA58758DF3%3A%5E%3B%2CA13F80AF45F5876014D43CA23D3FE407%3BA13F80AF45F5876014D43CA23D3FE407%3A%5E&valuePromptAnswers=2016%2F09%2F30";
			//durl = durl + "reportID=EC35ED1E4512811AFC2F2F9AD5EFB7D3&valuePromptAnswers=2014%2F12%2F31&executionMode=3&elementsPromptAnswers=3144D3D3427319E5DF48C185039BFF1B%3B3144D3D3427319E5DF48C185039BFF1B%3A1%5E%E4%B8%AD%E5%9B%BD%E4%BA%BA%E5%AF%BF%E5%86%8D%E4%BF%9D%E9%99%A9%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8%3B";
			//System.out.println("===durl==="+durl);
			HttpGet dget = new HttpGet(durl);
			StringBuilder dsoapRequestData = new StringBuilder();
			StringEntity dentiy = new StringEntity(url.toString(),
					"GBK");
			//entiy.setContentEncoding("UTF-8");

			//dget.setEntity(entiy);
			//http请求头 xml格式
			dget.setHeader("Content-Type", "text/xml; charset=GBK");
			//tomcat 用户名密码，防止401错误
			String dusername = "admin";
			String dpassword = "admin";
			String dauth = "Basic " + Base64.encode((dusername + ":" + dpassword).getBytes());
			dget.addHeader("Authorization", dauth);
			//http 请求获取sessionState
			CloseableHttpResponse  dresponse = closeableHttpClient.execute(dget);
			//System.out.println("===httpclient state==="+dresponse.getStatusLine());
			
			try {  
                //System.out.println(dresponse.getStatusLine());
                HttpEntity httpEntity = dresponse.getEntity();  
                long contentLength = httpEntity.getContentLength();  
                InputStream is = httpEntity.getContent();  
                // 根据InputStream 下载文件  
                ByteArrayOutputStream output = new ByteArrayOutputStream();  
                byte[] buffer = new byte[4096];  
                int r = 0;  
                long totalRead = 0;  
                while ((r = is.read(buffer)) > 0) {  
                    output.write(buffer, 0, r);  
                    totalRead += r;  
                }  
                FileOutputStream fos = new FileOutputStream("D:\\333.xls");  
                output.writeTo(fos);  
                output.flush();  
                output.close();  
                fos.close();  
                EntityUtils.consume(httpEntity);  
            } finally {  
            	dresponse.close();  
            }  
		} finally {
			// 释放资源  
            closeableHttpClient.close();
		}
	}

	/**
	 * @Title: getValue 
	 * @Description:  解析xml
	 * @param xml
	 * @param tagName
	 * @return String
	 * @author zhoujun
	 * @Date 2017-9-19 下午3:25:22
	 */
	private static String getValue(String xml, String tagName){
		if (xml.lastIndexOf("<"+tagName)>-1){
			xml = xml.substring(xml.lastIndexOf("<"+tagName));
			return xml.substring(xml.indexOf(">") + 1, xml.lastIndexOf("</"+tagName+">"));
		} else {
			return "";
		}
	}
}
