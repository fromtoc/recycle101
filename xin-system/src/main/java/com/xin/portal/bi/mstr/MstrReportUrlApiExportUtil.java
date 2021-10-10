package com.xin.portal.bi.mstr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.util.DateUtil;
import com.xin.portal.system.util.HttpRequestUtils;
import com.xin.portal.system.util.datasource.DataSourceUtils;

public class MstrReportUrlApiExportUtil {
	
	private static Logger logger = LoggerFactory.getLogger(MstrReportUrlApiExportUtil.class);
	
	/**
	 * 使用URL的方式通过http请求获取document的PDF档。
	 * @param documentUrl
	 * @param baseUrl
	 * @param filePath
	 * @return
	 */
	public static String getPdfFileByUrl(String url,String baseUrl,String filePath){
		logger.info("报表导出url：{}", url);
		//保持session 
		HttpClientContext clientContext = HttpClientContext.create();
		CookieStore cookieStore = new BasicCookieStore();
		clientContext.setCookieStore(cookieStore);
		String fileName = "";
		String baseUrlUp = baseUrl.substring(0,baseUrl.indexOf("/mstrWeb"));//根据mstr的导出特征进行处理
		try {
			String doGet = HttpRequestUtils.httpGet(url, null, null, 3000, false, null, clientContext);
			logger.info("the result httpget is ：{}", doGet);//第一次请求，完成登录和回答提示等
			Document doc = Jsoup.parse(doGet);
			Element firstFrom = doc.select("FORM").first();
			logger.info("the result httpget acion is: {}", firstFrom.toString());
			String action = firstFrom.attr("ACTION");
			if(action.indexOf("export") > -1 || action.indexOf("mstrWeb?rn") > -1){//如果有缓存则会直接获取到导出页面。直接导出即可
				logger.info("first request have export ：{}", action);
				String baseUrlEnd = getBaseUrlWithExport(baseUrlUp, action);
				String actionEnd = action.replaceAll("\\.\\./", "");//去除../
				String urlExport = baseUrlEnd + "/" +actionEnd;
				logger.info("first export Url is ：{}", urlExport);
				Map<String, Object> formParam = getForm(firstFrom);
				logger.info("first request have export acionparam is: {}", formParam.toString());
				fileName = HttpRequestUtils.httpPost(urlExport, null, formParam, 3000, true, filePath, clientContext);
			}else{//如果没有缓存就需要检查刷新（第一个form就是检查刷新的请求）
				logger.info("first request have no export ：{}", action);//第一次重请求
				String post = HttpPostRequeset(baseUrlUp, action, firstFrom, clientContext);//递归方法检查刷新直到获取export页面
				logger.info("get export page ：{}", post);
				Document doc2 = Jsoup.parse(post);
				Element firstFrom2 = doc2.select("FORM").first();
				String actionPost = firstFrom2.attr("ACTION");
				if(actionPost.indexOf("export") > -1 || actionPost.indexOf("mstrWeb?rn") > -1 ){
					logger.info(" export from is ：{}", actionPost);
					String baseUrlEnd1 = getBaseUrlWithExport(baseUrlUp, actionPost);
					String actionEnd1 = actionPost.replaceAll("\\.\\./", "");
					String urlExport1 = baseUrlEnd1 + "/" +actionEnd1;
					logger.info(" export url is ：{}", urlExport1);
					Map<String, Object> formParam1 = getForm(firstFrom2);
					fileName = HttpRequestUtils.httpPost(urlExport1, null, formParam1, 3000, true, filePath, clientContext);
				}else{
					return null;
				}
			}
			logger.info("报表导出的PDF名称为：{}", fileName);
		} catch (URISyntaxException e) {
			logger.error("执行get请求出错");
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			logger.error("执行post请求出错");
			e.printStackTrace();
		} 
		return fileName;
	}
	
	private static String HttpPostRequeset(String baseUrlUp, String action, Element element, HttpClientContext clientContext) throws UnsupportedEncodingException{
		String url = baseUrlUp + "/" + action;
		logger.info("递归查询 URL  ：{}", url);
		Map<String, Object> formList = getForm(element);
		String post =  HttpRequestUtils.httpPost(url, null, formList, 3000, false, "null", clientContext);
		logger.info("递归查询直到 找到export 请求后的页面 ：{}", post);
		Document doc = Jsoup.parse(post);
		Element firstFrom = doc.select("FORM").first();
		String actionPost = firstFrom.attr("ACTION");
		logger.info("递归请求后的 action ： {}", actionPost);
		if(actionPost.indexOf("export") == -1 && actionPost.indexOf("mstrWeb?rn") == -1 ){
			post = HttpPostRequeset(baseUrlUp, actionPost, firstFrom, clientContext);
		}
		return post;
	}
	
	private static String getBaseUrlWithExport(String baseUrl, String exportUrl){
		String result = baseUrl;
		if(exportUrl.startsWith("../")){
			//截去../后，也截去baseUrl最后一个/ 然后 递归调用查看是否有../
			String subExportUrl = exportUrl.substring(exportUrl.indexOf("../")+3,exportUrl.length());
			String subBaseUrl = baseUrl.substring(0, baseUrl.lastIndexOf("/"));
			//递归调用
			result = getBaseUrlWithExport(subBaseUrl, subExportUrl);
		}
		return result;
	}
	
	public static Map<String, Object> getForm(Element from){
		Map<String, Object> map = new HashMap<>();
		for (Element etr : from.select("input")) {
			String name = etr.attr("name");
			String value= etr.attr("value");
			map.put(name, value);
		}
		return map;
	}
	
	public static String buildMstrExportUrlWithTenantId(Resource resource, List<PromptRel> promptRelList, BiProject project, BiMapping mapping ,String tenantId) throws UnsupportedEncodingException{
		String url = "";
		StringBuilder sb = new StringBuilder();
		if (project != null) {//判断项目是否为空，为空则不能拼接链接
			//访问报表链接拼接开始
			sb.append(project.getUrl()).append("?");
			sb.append("Server=").append(project.getServer());
			sb.append("&Project=").append(project.getProject());
			sb.append("&Port=").append(project.getPort());
			if(mapping != null && mapping.getBiUserName() != null && mapping.getBiUserName().trim().length() > 0){
				sb.append("&uid=").append(mapping.getBiUserName());
				sb.append("&pwd=").append((mapping.getBiPassword()!=null&&mapping.getBiPassword().trim().length()>0)?mapping.getBiPassword():"");
			}else{
				sb.append("&uid=").append(project.getDefaultUid());
				sb.append("&pwd=").append((project.getDefaultPwd()!=null&&project.getDefaultPwd().trim().length() > 0)?project.getDefaultPwd():"");
			}
			if (StringUtils.isNotEmpty(resource.getHiddenSections())) {
				sb.append("&hiddensections=").append(resource.getHiddenSections());
			}
			if (EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition==resource.getTypeValue()) {
				sb.append("&evt=3069&src=mstrWeb.3069");//document|达西 导出参数
				sb.append("&documentID=").append(resource.getReportId());
			}else if(EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition == resource.getTypeValue()){
				sb.append("&evt=3062&src=mstrWeb.3062");//report 导出参数
				sb.append("&showOptionsPage=false");
				sb.append("&reportID=").append(resource.getReportId());
			}
			//回答提示链接拼接开始
			promptRelList = DateUtil.timeFunctionWithTenantId(promptRelList, tenantId);//时间默认值设置根据选择生成具体数字
			promptRelList = DataSourceUtils.DatasourceValueFunctionWithTenantId(promptRelList, tenantId);//设置组织管理的默认值
			if (promptRelList != null && promptRelList.size() > 0) {
				StringBuilder valuePrompt = new StringBuilder();
				StringBuilder elementPrompt = new StringBuilder();
				StringBuilder objPrompt = new StringBuilder();
				StringBuilder otherParams = new StringBuilder();
				for (PromptRel rec : promptRelList) {//暂时只回答只提示
					if("1".equals(rec.getPromptType())){//值提示
						if (3 == rec.getType() || 9 == rec.getType()) {//时间
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								valuePrompt.append(valuePrompt.toString().trim().length()>0?"^":"" 
										+ rec.getDefaultValue1());
							}else{
								valuePrompt.append("^");
							}
						} else if(8 == rec.getType() || 15 == rec.getType()){//时间区间
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								valuePrompt.append(valuePrompt.toString().trim().length()>0?"^":"" 
										+ rec.getDefaultValue1());
							}else{
								valuePrompt.append("^");
							}
							if(rec.getDefaultValue3() != null && rec.getDefaultValue3().trim().length() > 0){
								valuePrompt.append(valuePrompt.toString().trim().length()>0?"^":"" 
										+ rec.getDefaultValue3());
							}else{
								valuePrompt.append("^");
							}
						} else {
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								valuePrompt.append(valuePrompt.toString().trim().length()>0?"^":"" 
										+ rec.getDefaultValue1());
							}else{
								valuePrompt.append("^");
							}
						}
					} else if("2".equals(rec.getPromptType())){//元素筛选
						elementPrompt.append(rec.getCode());
						if(8 == rec.getType() || 15 == rec.getType()){
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								elementPrompt.append(";").append(rec.getCode()).append(":").append(rec.getDefaultValue1());
							}
							if(rec.getDefaultValue3() != null && rec.getDefaultValue3().trim().length() > 0){
								elementPrompt.append(";").append(rec.getCode()).append(":").append(rec.getDefaultValue3());
							}
						} else if(10 == rec.getType()){
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								String[] defaultValue = rec.getDefaultValue1().split(",");
								for (int i = 0;i < defaultValue.length; i++) {
									elementPrompt.append(";").append(rec.getCode()).append(":").append(defaultValue[i]);
								}
							}
						} else{
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								elementPrompt.append(";").append(rec.getCode()).append(":").append(rec.getDefaultValue1());
							}
						}
						elementPrompt.append(";,");
					} else if("3".equals(rec.getPromptType())){//对象
						String objectType = (rec.getObjectType()!=null&&rec.getObjectType().trim().length()>0)?rec.getObjectType():"12";
						if(10 == rec.getType()){
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								String[] defaultValue = rec.getDefaultValue1().split(",");
								for (int i = 0;i < defaultValue.length; i++) {
									if(i == defaultValue.length-1){
										objPrompt.append(defaultValue[i]).append("~").append(objectType);
									}else{
										objPrompt.append(defaultValue[i]).append("~").append(objectType).append("");
									}
								}
							}
						} else{
							if(rec.getDefaultValue1() != null && rec.getDefaultValue1().trim().length() > 0){
								objPrompt.append(rec.getDefaultValue1()).append("~").append(objectType);
							}
						}
						objPrompt.append("^");
					}
				}
				//整合提示
				otherParams.append("&promptAnswerMode=2");
				sb.append("&valuePromptAnswers=").append(valuePrompt.toString()).append("&elementsPromptAnswers=")
					.append(elementPrompt.toString()).append("&objectsPromptAnswers=").append(objPrompt.toString())
					.append(otherParams.toString());
			}
			try {
				url = EncodeUrl(sb.toString());
			} catch (MalformedURLException | URISyntaxException e) {
				logger.error("url encode is error");
				e.printStackTrace();
				return null;
			}
			logger.info("the mstr export URL is : {}",url);
		}else{
			logger.error("check the biProject is null");
			return null;
		}
		return url; 
	}
	
	public static String EncodeUrl(String url) throws MalformedURLException, URISyntaxException{
		URL newUrl = new URL(url);
		URI uri = new URI(newUrl.getProtocol(), newUrl.getUserInfo(), newUrl.getHost(), newUrl.getPort(), newUrl.getPath(), newUrl.getQuery(), newUrl.getRef());
		return uri.toURL().toString();
	}
	
	public static String getWebPath(String randomLevel1,String randomLevel2, String extraPath, String type) {//E:tomcat/webapps
		String WebappPaht = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		WebappPaht = WebappPaht.substring(0,WebappPaht.indexOf("/WEB-INF"));
		WebappPaht = WebappPaht.substring(0, WebappPaht.lastIndexOf("/"));
		String filePaht = WebappPaht + File.separator + type + File.separator + randomLevel1 + File.separator +  randomLevel2;
		if(extraPath != null && extraPath.trim().length() >0){
			filePaht += File.separator + extraPath;
		}
		File fileDir = new File(filePaht);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		return filePaht;
	}
	
}
