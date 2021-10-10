package com.xin.portal.bi.mstr.http.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xin.portal.bi.mstr.http.Entity.BookmarkInfo;
import com.xin.portal.bi.mstr.http.Entity.FolderCreationBody;
import com.xin.portal.bi.mstr.http.Entity.MstrAnswer;
import com.xin.portal.bi.mstr.http.service.MstrDossierPromptService;
import com.xin.portal.bi.mstr.http.util.RestClient;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.cache.CacheManager;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/**
 * 目前的方法仅仅适用于11.1版本
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/dossier/demo")
public class DossierDemo {
	
	@Autowired
	private BiServerService biServerService;
	@Autowired
	private BiUserService biUserService;
	
	private final static Logger LOGGER = LoggerFactory.getLogger(DossierDemo.class);
	
	private final static String LOGINAPI = "/api/auth/login";
    private final static String INSTANCES_PART_API = "/api/dossiers/";
    private final static String PROMPT_ANSWER_API = "/api/documents/{1}/instances/{2}/prompts/answers";
    private final static String GET_PROMPT_API = "/api/documents/{1}/instances/{2}/prompts"; 
    
    @RequestMapping(value="/{dossierId}/instances",method = {RequestMethod.POST})
    @ResponseBody
    public JSONObject postInstances(@PathVariable String dossierId, 
    		HttpServletRequest request, HttpServletResponse response) {
    	JSONObject result = new JSONObject();
    	LOGGER.info("the dossierid is {} ,custom get instance...", dossierId);
    	// 1.设置公用的请求头header
        StringBuffer headCookie = new StringBuffer();
        String headToken = null;
        String answerKey = "";
        String MSTR_API_BASE_PATH = "";
        Cookie[] cookies = request.getCookies();
        for (Cookie temp : cookies) {
        	if ("report_query_api".equals(temp.getName())) {//获取回答提示的key
        		try {
					MSTR_API_BASE_PATH = URLDecoder.decode(temp.getValue(),"UTF-8") ;
				} catch (UnsupportedEncodingException e) {
					MSTR_API_BASE_PATH = "";
					e.printStackTrace();
				}
        		LOGGER.info("answer 接口地址值为：{}", temp.getValue());	
        	}
        	if ("report_answer_key".equals(temp.getName())) {//获取回答提示的key
        		answerKey = temp.getValue();
        		LOGGER.info("answer key 值为：{}", temp.getValue());	
        	}
            if ("JSESSIONID".equals(temp.getName())) {//获取mstr的jsessionid
                headCookie.append("JSESSIONID=").append(temp.getValue()).append(";");
                LOGGER.info("headCookie的值为{}", headCookie);
            }
            if ("iSession".equals(temp.getName())) {//获取library的isession
            	headCookie.append("iSession=").append(temp.getValue()).append(";");
                headToken = temp.getValue();
                LOGGER.info("headToken的值为{}", headToken);
            }
        }
        if (MSTR_API_BASE_PATH == null || MSTR_API_BASE_PATH.trim().length() == 0) {
            LOGGER.error("cookie参数异常");
            result.put("code", "0005");
            result.put("iServerCode", "datawindow-0005");
            result.put("message", "cookie参数异常无法获取请求api链接");
            return result;
        }
        if (answerKey == null || answerKey.trim().length() == 0) {
            LOGGER.error("cookie参数异常");
            result.put("code", "0004");
            result.put("iServerCode", "datawindow-0004");
            result.put("message", "cookie参数异常无法获取key");
            return result;
        }
        if (headCookie == null || headCookie.length() == 0) {
            LOGGER.error("cookie认证异常");
            result.put("code", "0001");
            result.put("iServerCode", "datawindow-0001");
            result.put("message", "cookie认证异常");
            return result;
        }
        if (headToken == null || headToken.trim().length() == 0) {
            LOGGER.error("token认证异常");
            result.put("code", "0002");
            result.put("iServerCode", "datawindow-0002");
            result.put("message", "token认证异常");
            return result;
        }
        String headProjectId = request.getHeader("X-MSTR-ProjectID");
        LOGGER.info("post 方法的 JSESSIONID：{},iSession：{},headProjectId：{},dossierId：{}", headCookie, headToken, headProjectId, dossierId);
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> accept = new ArrayList<MediaType>(1);
        accept.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accept);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-MSTR-AuthToken", headToken);
        headers.set("Cookie", headCookie.toString());
        headers.set("X-MSTR-ProjectID", headProjectId);
        //获取配置的回答DTO
        try {
            //2.生成报表的默认实例
            BookmarkInfo bookmarkInfo = new BookmarkInfo(true);
            FolderCreationBody requsetBodyForPost = new FolderCreationBody(bookmarkInfo, true);
            HttpEntity<FolderCreationBody> requestBody = new HttpEntity<FolderCreationBody>(requsetBodyForPost, headers);
            ResponseEntity<JSONObject> responseEntity = RestClient.postJson(MSTR_API_BASE_PATH 
            		+ INSTANCES_PART_API + dossierId + "/instances", requestBody, JSONObject.class);
            JSONObject postInstancesApiResult = responseEntity.getBody();
            LOGGER.info("POST_INSTANCES_API返回结果是：{}", JSON.toJSONString(responseEntity));
            String instanceId = (String) postInstancesApiResult.get("mid");//实例id
            Integer instanceStatus = (Integer) postInstancesApiResult.get("status");//状态
            LOGGER.info("实例id是{},实例状态是{},是否为2:{}", instanceId, instanceStatus, (2 == instanceStatus));
            //Status "1" indicates that the instance has been created, 
            //and status "2" indicates that the dossier, document or report is waiting for a prompt answer.
            //If the status is "1", the response also returns the ID of the object.
            if (instanceStatus == 2) {
            	Map<String, List<MstrAnswer>> promptAnswerMap = new HashMap<String, List<MstrAnswer>>();
                //3.获取报表上的提示
                HttpEntity<JSONObject> requestBodyPrompt = new HttpEntity<JSONObject>(null, headers);
                String[] objUrl = {dossierId, instanceId};
                ResponseEntity<JSONArray> responseEntityPrompt = RestClient.exchange(MSTR_API_BASE_PATH 
                		+ GET_PROMPT_API, HttpMethod.GET, requestBodyPrompt, JSONArray.class, objUrl);
                LOGGER.info("报表{}实例{}获取的回答列表的响应是{}", dossierId, instanceId, JSON.toJSONString(responseEntityPrompt));
                JSONArray mstrPromptList = responseEntityPrompt.getBody();
                //4.组装提示与回答
                //从缓存中取出参数
            	String params = URLDecoder.decode((String)CacheManager.get(answerKey),"UTF-8");
            	LOGGER.info("页面中选择的筛选项内容为：{}", params);
            	Map<String, List<String>> mstrPromptMap = null;//标志是否有提示内容
            	if(params != null && params.trim().length() > 0){
            		//转化参数值 map  id,  list<value>
            		mstrPromptMap = MstrDossierPromptService.transformParamsToMap(params);
            		LOGGER.info("页面筛选切割为：{}", JSONObject.toJSON(mstrPromptMap).toString());
            	}
                List<MstrAnswer> mstrPromptAnswerList = MstrDossierPromptService.getMstrDossierPromptAnswer(mstrPromptList, mstrPromptMap);
                promptAnswerMap.put("prompts", mstrPromptAnswerList);
                LOGGER.info("页面筛选 转化为MstrAnswer对象为：{}", JSONObject.toJSON(mstrPromptAnswerList).toString());
                HttpEntity<Map<String, List<MstrAnswer>>> requestBodyForPut = new HttpEntity<Map<String, List<MstrAnswer>>>(promptAnswerMap, headers);
                //5.回答提示 不配置回答的使用默认答案回答
                RestClient.put(MSTR_API_BASE_PATH + PROMPT_ANSWER_API, requestBodyForPut, String.class , objUrl);
                postInstancesApiResult.put("status", 1);//将status置为1
                //6.respose 删除key
                Cookie cookie = new Cookie("report_answer_key", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            return postInstancesApiResult;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            result.put("code", "0003");
            result.put("iServerCode", "datawindow-0003");
            result.put("message", e.getMessage());
            return result;
        }
    }
    
    @RequestMapping(value="/library/login")
    @ResponseBody
    public void libraryLogin(HttpServletRequest request, HttpServletResponse response,
    		@RequestBody String postData) {
    	//获取biuser 和 server
    	LOGGER.info("Begin to get the MstrLibrary Session，the postDate: {} ;", postData);
    	JSONObject jsonObj = JSON.parseObject(postData);
    	String serverId = jsonObj.getString("serverId");
    	String biUserId = jsonObj.getString("biUserId");
    	BiServer biServer = biServerService.selectById(serverId);
    	BiUser biUser = biUserService.selectById(biUserId);
    	String MSTR_API_BASE_PATH = biServer.getParam();//dossier 使用param字段存放请求接口的链接
    	String mstrPaht = "";
    	String url = biServer.getUrl();
    	if (url !=null && url.trim().length() > 0 && !"/".equals(url.substring(url.length()-1))){
    		mstrPaht = url.substring(url.lastIndexOf("/"),url.length());
    	} else if(url !=null && url.trim().length() > 0 && "/".equals(url.substring(url.length()-1))){
			mstrPaht = url.substring(0, url.length() - 1).substring(
					url.substring(0, url.length() - 1).lastIndexOf("/"),
					url.substring(0, url.length() - 1).length());
    	} else{
    		response.setStatus(401);
    	}
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> accept = new ArrayList<MediaType>(1);
        accept.add(MediaType.APPLICATION_JSON);
        headers.setAccept(accept);
        headers.setContentType(MediaType.APPLICATION_JSON);
        //发送Login请求
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", biUser.getUsername());
        requestBody.put("password", biUser.getPassword());
        requestBody.put("loginMode", 1);
        requestBody.put("applicationType", "35");
        HttpEntity<JSONObject> EntityBody = new HttpEntity<JSONObject>(requestBody, headers);
        ResponseEntity<String> responseEntity = RestClient.exchange(MSTR_API_BASE_PATH 
        		+ LOGINAPI, HttpMethod.POST, EntityBody, String.class);
        LOGGER.info("have session for MstrLibrary: {} ;", responseEntity.toString());
        HttpHeaders responseHeaders = responseEntity.getHeaders();
        List<String> tokenList = responseHeaders.get("X-MSTR-AuthToken");
        List<String> cookiesList = responseHeaders.get("Set-Cookie");
        if((tokenList != null && tokenList.size() > 0) && (cookiesList != null && cookiesList.size() > 0)){
        	String libCookies = cookiesList.get(0);
        	String libToken = tokenList.get(0);
        	response.setHeader("x-mstr-authtoken", libToken);
            //设置jsessionid
        	Cookie cookie1 = new Cookie("JSESSIONID", libCookies.substring(11, 43));
        	String path = RestClient.getCookieBySet("Path", libCookies)!=null?RestClient.getCookieBySet("Path", libCookies):mstrPaht;
            cookie1.setMaxAge(24 * 60 * 60);
            cookie1.setHttpOnly(true);
            cookie1.setPath(path);//获取set-cookie中的path或者使用项目名称
            response.addCookie(cookie1);
            //设置iSession
            Cookie cookie2 = new Cookie("iSession", libToken);
            cookie2.setMaxAge(24 * 60 * 60);
            cookie2.setPath(mstrPaht);//截取server url中的最后一个
            response.addCookie(cookie2);
            
        }else{
        	response.setStatus(401);
        }
    }
    
    @RequestMapping(value="/library/setParam")
    @ResponseBody
    public BaseApi setParamToCookie(HttpServletRequest request, HttpServletResponse response,
    		String reportId, String serverId, String params) {
    	//如果params 太大是不能放入cookie的，所以把参数名称放到cookie中，将参数值放到系统缓存中（key 为cookie中的参数）
    	//最后通过key 取到 param
    	if((reportId != null && reportId.trim().length() != 0) && 
    			(serverId != null && serverId.trim().length() != 0)){
    		//获取request中传入的参数
    		String sessionId = "";
    		HttpSession session = request.getSession(false);
        	if(session != null){//you
        		sessionId = session.getId();
        	}else{
        		sessionId = UUID.randomUUID().toString();
        	}
        	//获取server的api url
        	BiServer selectById = biServerService.selectById(serverId);
    		CacheManager.put(reportId + "_" + sessionId, params);
    		try {
    			Cookie cookieKey = new Cookie("report_answer_key", reportId + "_" + sessionId);
            	cookieKey.setMaxAge(24 * 60 * 60);
            	cookieKey.setPath("/");
                response.addCookie(cookieKey);
                Cookie cookieApi = new Cookie("report_query_api", URLEncoder.encode(selectById.getParam(),"UTF-8"));
                cookieApi.setMaxAge(24 * 60 * 60);
                cookieApi.setPath("/");
                response.addCookie(cookieApi);
    		} catch (Exception e) {
    			e.printStackTrace();
    			return BaseApi.error(-3, "");//内部方法错误
    		}
            return BaseApi.success();
    	}else{
    		return BaseApi.error(-2, "");//参数不正确
    	}
    }
    
    
    public static void main(String[] args) {
		String a = "sfs/werw/wer";
		String aa = a.substring(a.length()-1);
		String cc = "";
		if(!aa.equals("/")){
			cc = a.substring(a.lastIndexOf("/")+1,a.length());
		}else{
			cc = a.substring(0,a.length()-1).substring(a.substring(0,a.length()-1).lastIndexOf("/")+1,a.substring(0,a.length()-1).length());
		}
    	System.out.println(cc);
	}
    
}	