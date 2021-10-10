/**
 * Copyright (C), 2008-2019, dod123456
 * FileName: RestApiUtil
 * Author:   dod123456
 * Date:     2019/2/18 17:57
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xin.portal.bi.mstr;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xin.portal.system.util.HttpClientUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br> 
 * 〈〉
 *
 * @author dod123456
 * @create 2019/2/18
 * @since 1.0.0
 */
public class RestApiUtil {
    static String HEADER_AUTH_TOKEN = "X-MSTR-AuthToken";
    static String HEADER_AUTH_IDENTITYTOKEN = "X-MSTR-IdentityToken";
    public static String HEADER_PROJECTID = "X-MSTR-ProjectID";
    static String LIBRARY_PATH = "http://52.83.123.6:81/MicroStrategyLibrary";
    static String AUTH_LOGIN = "/api/auth/login";
    static String AUTH_IDENTITYTOKEN = "/api/auth/identityToken";
    static String PROJECTS = "/api/projects";
    static String FOLDERS = "/api/folders";
    static String INSTANCE_PROMPTS = "/api/documents/%/instances/*/prompts";
    static String PROMPTS = "/api/documents/%/prompts";
    static String PROMPTS_ANSWER = "/api/documents/%/instances/*/prompts/answers";
    static String DOSSIERS_INSTANCES = "/api/dossiers/%/instances";
    static String DOCUMENTS_REPROMPT = "/api/documents/%/instances/*/rePrompt";
    static String REPORTS_INSTANTS = "/api/reports/%/instances";

    static Map<String,String> HEADERS = new HashMap<String,String>(){{
        put("Accept", "application/json");
        put("Content-Type", "application/x-www-form-urlencoded");
        //put("X-Requested-With", "XMLHttpRequest");
        put("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
    }};

    private static Logger logger = LoggerFactory.getLogger(RestApiUtil.class);

    public static String token(String url, Map<String, Object> params,CloseableHttpClient httpClient,CloseableHttpResponse httpResponse) throws Exception{
        // 创建http对象
        HttpPost httpPost = new HttpPost(url + AUTH_LOGIN);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HttpClientUtils.CONNECT_TIMEOUT).setSocketTimeout(HttpClientUtils.SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);

        HttpClientUtils.packageHeader(HEADERS, httpPost);
        HttpClientUtils.packageParam(params,httpPost);


        // 执行请求并获得响应结果
        httpResponse = httpClient.execute(httpPost);

        //setCookieStore(httpResponse);

        Header[] headersResponse = (Header[]) httpResponse.getAllHeaders();
        String token = "";
        for (int i = 0; i < headersResponse.length; i++){
            Header header = headersResponse[i];
            if (header.getName().equalsIgnoreCase(HEADER_AUTH_TOKEN)){
                //System.out.println(" Token : " + header.getValue());
                return header.getValue();
            }
        }
        return null;
    }

    public static JSONObject authIdentityToken(String url, String token) {
        JSONObject result = new JSONObject();
        try {
            Map<String,String> headers = Maps.newHashMap();
            headers.put(HEADER_AUTH_TOKEN,token);
            headers.putAll(HEADERS);
            result = HttpClientUtils.doGet(url+AUTH_IDENTITYTOKEN,headers,Maps.newHashMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getToken(String url, Map<String, Object> params) {
        return doGet(url,null,params).get("token").toString();
    }

    public static JSONObject projects(String url, Map<String, Object> params) {
        return doGet(url,PROJECTS,params);
    }

    public static JSONObject folderGet(String url, Map<String, Object> params) {
        return doGet(url,FOLDERS+"/" + ("0".equals(params.get("id"))?"D3C7D461F69C4610AA6BAA5EF51F4125":params.get("id")),params);
    }

    public static JSONObject identityTokenGet(String url, Map<String, Object> params) {
        return doGet(url,AUTH_IDENTITYTOKEN,params);
    }

    public static String identityTokenPost(String url, Map<String, Object> params) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            String token = token(url,params,httpClient,httpResponse);

            HttpPost httpPost = new HttpPost(url + AUTH_IDENTITYTOKEN);
            httpPost.addHeader(HEADER_AUTH_TOKEN,token);

            // 执行请求并获得响应结果
            httpResponse = httpClient.execute(httpPost);
            Header[] headersResponse = (Header[]) httpResponse.getAllHeaders();
            String identityToken = "";
            for (int i = 0; i < headersResponse.length; i++){
                Header header = headersResponse[i];
                if (header.getName().equalsIgnoreCase(HEADER_AUTH_IDENTITYTOKEN)){
                    //System.out.println(" identityToken : " + header.getValue());
                    return header.getValue();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }  finally {
            try {
                // 释放资源
                HttpClientUtils.release(httpResponse, httpClient);
            } catch (Exception ex) {

            }
        }

        return null;
    }

    public static JSONObject doGet(String url,String api, Map<String, Object> params){
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        JSONObject result = new JSONObject();
        try {
            String token = token(url,params,httpClient,httpResponse);
            if (StringUtils.isEmpty(api)) {
                result.put("token",token);
                return result;
            }

            // 创建访问的地址
            URIBuilder uriBuilder = new URIBuilder(url + api);
            if (params!=null){
                for (String key : params.keySet()) {
                    uriBuilder.setParameter(key,params.get(key).toString());
                }
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            httpGet.addHeader(HEADER_AUTH_TOKEN,token);
            if (params!=null && !StringUtils.isEmpty(params.get(HEADER_PROJECTID))) {
                httpGet.addHeader(HEADER_PROJECTID,params.get(HEADER_PROJECTID).toString());
            }

            result = HttpClientUtils.getHttpClientResult(httpResponse,httpClient,httpGet);
            //System.out.println(result.toString());
        } catch (Exception e){
            e.printStackTrace();
        }  finally {
            try {
                // 释放资源
                HttpClientUtils.release(httpResponse, httpClient);
            } catch (Exception ex) {

            }
        }

        return result;

    }

    public static JSONObject doPost(String url,String api, Map<String, Object> params){
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        JSONObject result = new JSONObject();
        try {
            String token = token(url,params,httpClient,httpResponse);
            if (StringUtils.isEmpty(api)) {
                result.put("token",token);
                return result;
            }

            HttpPost httpPost = new HttpPost(url + api.replace("%",params.get("id").toString()));
            httpPost.addHeader(HEADER_AUTH_TOKEN,token);
            if (params!=null && !StringUtils.isEmpty(params.get(HEADER_PROJECTID))) {
                httpPost.addHeader(HEADER_PROJECTID,params.get(HEADER_PROJECTID).toString());
            }

            HttpClientUtils.packageParam(params, httpPost);
            result = HttpClientUtils.getHttpClientResult(httpResponse,httpClient,httpPost);
            //System.out.println(result.toString());
        } catch (Exception e){
            e.printStackTrace();
        }  finally {
            try {
                // 释放资源
                HttpClientUtils.release(httpResponse, httpClient);
            } catch (Exception ex) {

            }
        }

        return result;

    }

    public static JSONObject instancePrompts(String url, Map<String, Object> params) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        JSONObject result = new JSONObject();
        try {
            String token = token(url,params,httpClient,httpResponse);
            result.put("token",token);


            //instances
            HttpPost httpPost = new HttpPost(url + DOSSIERS_INSTANCES.replace("%",params.get("id").toString()));
            httpPost.addHeader(HEADER_AUTH_TOKEN,token);
            httpPost.addHeader(HEADER_PROJECTID,params.get(HEADER_PROJECTID).toString());

            //HttpClientUtils.packageParam(params, httpPost);
            result = HttpClientUtils.getHttpClientResult(httpResponse,httpClient,httpPost);
            String content = result.get("content").toString();
            JSONObject instantObj = JSONObject.parseObject(content);
            String mid = instantObj.getString(RestApiParam.MID);

            String instancePromptsUrl = INSTANCE_PROMPTS.replace("%",params.get("id").toString())
                    .replace("*",mid);
            //instance/prompts
            HttpGet httpGet = new HttpGet(url + instancePromptsUrl);
            httpGet.addHeader(HEADER_AUTH_TOKEN,token);
            httpGet.addHeader(HEADER_PROJECTID,params.get(HEADER_PROJECTID).toString());

            result = HttpClientUtils.getHttpClientResult(httpResponse,httpClient,httpGet);

            //System.out.println(result.toString());
        } catch (Exception e){
            e.printStackTrace();
        }  finally {
            try {
                // 释放资源
                HttpClientUtils.release(httpResponse, httpClient);
            } catch (Exception ex) {

            }
        }

        return result;
    }

    public static JSONObject prompts(String url, Map<String, Object> params) {
        return doGet(url,PROMPTS.replace("%",params.get("id").toString()),params);
    }

    public static JSONObject promptsAnswers(String url, Map<String, Object> params,String answers) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
//        CloseableHttpResponse instantResponse = null;
//        CloseableHttpResponse rePromptResponse = null;
//        CloseableHttpResponse answersResponse = null;

        JSONObject result = new JSONObject();
        try {
            String token = token(url,params,httpClient,httpResponse);
            result.put("token",token);

            logger.info("token: {}",token);

            //instances
            HttpPost httpPost = new HttpPost(url + DOSSIERS_INSTANCES.replace("%",params.get("dossierId").toString()));
            httpPost.addHeader(HEADER_AUTH_TOKEN,token);
            httpPost.addHeader(HEADER_PROJECTID,params.get(RestApiUtil.HEADER_PROJECTID).toString());
            httpPost.addHeader(RestApiParam.ContentType,RestApiParam.APPLICATIONJSON);
            httpPost.addHeader(RestApiParam.ACCEPT,RestApiParam.APPLICATIONJSON);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(RestApiParam.PERSISTVIEWSTATE,true);

            httpPost.setEntity(new StringEntity(jsonObject.toString(),HttpClientUtils.ENCODING));

            result = HttpClientUtils.getHttpClientResult(httpResponse,httpClient,httpPost);
            logger.info("Create Instance: {}",httpPost.getURI());
            logger.info("Instance: {}",result.toString());

            String content = result.get("content").toString();
            JSONObject instantObj = JSONObject.parseObject(content);
            String mid = instantObj.getString(RestApiParam.MID);
            params.put(RestApiParam.PERSISTVIEWSTATE,true);

            //rePrompt
            HttpPost rePromptPost = new HttpPost(url + DOCUMENTS_REPROMPT.replace("%",params.get("dossierId").toString())
                    .replace("*",mid));
            rePromptPost.addHeader(HEADER_AUTH_TOKEN,token);
            rePromptPost.addHeader(HEADER_PROJECTID,params.get(HEADER_PROJECTID).toString());

            HttpClientUtils.packageParam(params, rePromptPost);
            JSONObject rePromptResult = HttpClientUtils.getHttpClientResult(httpResponse,httpClient,rePromptPost);
            String rePromptContent = rePromptResult.get("content").toString();
            JSONObject instantObjNew = JSONObject.parseObject(rePromptContent);
            String midNew = instantObjNew.getString(RestApiParam.MID);

            logger.info("Reprompt Dossier: {}",rePromptPost.getURI());
            logger.info("Reprompt: {}",rePromptResult.toString());

            //prompts answers
            HttpPut httpPut = new HttpPut(url + PROMPTS_ANSWER.replace("%",params.get("dossierId").toString())
                    .replace("*",midNew));
            httpPut.addHeader(HEADER_AUTH_TOKEN,token);
            httpPut.addHeader(HEADER_PROJECTID,params.get(HEADER_PROJECTID).toString());
            httpPut.addHeader(RestApiParam.ContentType, RestApiParam.APPLICATIONJSON);

            List<NameValuePair> nvps = Lists.newArrayList(new BasicNameValuePair(RestApiParam.PROMPTS, answers));

            // 设置到请求的http对象中
//            httpPut.setEntity(new UrlEncodedFormEntity(nvps, HttpClientUtils.ENCODING));
            httpPut.setEntity(new StringEntity(answers,HttpClientUtils.ENCODING));

            result = HttpClientUtils.getHttpClientResult(httpResponse,httpClient,httpPut);
            logger.info("prompts answers: {}",httpPut.getURI());
            logger.info("result : {}",result.toString());
        } catch (Exception e){
            e.printStackTrace();
        }  finally {
            try {

                // 释放资源
                HttpClientUtils.release(httpResponse, httpClient);

            } catch (Exception ex) {
                    ex.printStackTrace();
            }
        }

        return result;
    }

    public static JSONObject reportInstances(String url, Map<String, Object> params) {
        return doPost(url,REPORTS_INSTANTS,params);
    }

    static CookieStore cookieStore = null;
    public static void setCookieStore(HttpResponse httpResponse){
        cookieStore = new BasicCookieStore();
        //JSSIONID
        String setCookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
        String JESSIONID = setCookie.substring("JESSIONID=".length(),setCookie.indexOf(";"));
        //新建一个COOKIE
        BasicClientCookie cookie = new BasicClientCookie("JESSIONID",JESSIONID);
        cookie.setVersion(0);
        cookie.setDomain("52.83.123.6");
        cookie.setPath("/MicroStrategyLibrary");
        // cookie.setAttribute(ClientCookie.VERSION_ATTR, "0");
        // cookie.setAttribute(ClientCookie.DOMAIN_ATTR, "127.0.0.1");
        // cookie.setAttribute(ClientCookie.PORT_ATTR, "8080");
         cookie.setAttribute(ClientCookie.PATH_ATTR, "/CwlProWeb");
        cookieStore.addCookie(cookie);
    }


}
