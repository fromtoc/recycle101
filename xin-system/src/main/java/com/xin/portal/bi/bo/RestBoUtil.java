package com.xin.portal.bi.bo;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.util.HttpClientUtils;

/**
 * 使用Restapi 获取token，拼接报表URL
 * @author Administrator
 *
 */
public class RestBoUtil {
	
	private static String LOGIN_RESTAPI_URL = "/biprws/logon/long";
	private static String AUTH_TYPE = "secEnterprise";//默认的认证类型（目前也只实现了这种类型。后续要实现其他类型需要在biserver表中加字段bo验证类型）
	
	public static JSONObject getSessionByRest(BiServer biServer) throws Exception{
		Map<String, String> map = new HashMap<>();
		map.put("Accept", "application/xml");
		map.put("Content-Type", "application/xml");
		String params = "<attrs>"
				+ "<attr name=\"userName\" type=\"string\" >%s</attr>"
				+ "<attr name=\"password\" type=\"string\" >%s</attr>"
				+ "<attr name=\"auth\" type=\"string\" possibilities=\"secEnterprise,secLDAP,secWinAD,secSAPR3\">%s</attr>"
				+ "</attrs>";
		String stringParams = String.format(params, biServer.getDefaultUid(), biServer.getDefaultPwd(), AUTH_TYPE);
		JSONObject doPostStringParam = HttpClientUtils.doPostStringParam(biServer.getParam() + LOGIN_RESTAPI_URL,map,stringParams);
		return doPostStringParam;
	}
	
	public static String getLoginTokenFromXML(String xmlStr){
		Document document = Jsoup.parse(xmlStr);
        Elements elementsByTag = document.getElementsByTag("attr");
        return elementsByTag.text();
	}
	
	public static void checkServer(BiServer biserver) throws Exception{
		JSONObject sessionObj = RestBoUtil.getSessionByRest(biserver);
		if("200".equals(sessionObj.getString("code")) 
				&& !StringUtils.isEmpty(sessionObj.getString("content"))){
			RestBoUtil.getLoginTokenFromXML(sessionObj.getString("content"));
		}else{
			throw new Exception(String.format("请求异常，code： %s ; 错误信息： %s", sessionObj.getString("code"),sessionObj.getString("content")));
		}
	}
	
	public static String getLoginToken(BiServer biServer){
		try {
			JSONObject sessionByRest = RestBoUtil.getSessionByRest(biServer);
			if("200".equals(sessionByRest.getString("code"))){
				return RestBoUtil.getLoginTokenFromXML(sessionByRest.getString("content"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "-1";
		}
		return "-1";
	}
	
	public static String getLoginToken(BiServer biServer, BiUser biUser){
		if(biUser != null && !biUser.getUsername().isEmpty()){
			//设置默认用户和密码为提供的用户和密码
			biServer.setDefaultUid(biUser.getUsername());
			biServer.setDefaultPwd(biUser.getPassword());
			try {
				JSONObject sessionByRest = RestBoUtil.getSessionByRest(biServer);
				if("200".equals(sessionByRest.getString("code"))){
					return RestBoUtil.getLoginTokenFromXML(sessionByRest.getString("content"));
				}
				return "-1";
			} catch (Exception e) {
				e.printStackTrace();
				return "-1";
			}
		}
		return "0";
	}
	
	public static void main(String[] args) {
		String token = "<entry xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                "    <author>\n" +
                "        <name>@JYJDBBOX01:6400</name>\n" +
                "    </author>\n" +
                "    <title type=\"text\">Logon Result</title>\n" +
                "    <updated>2020-10-23T05:33:27.976Z</updated>\n" +
                "    <content type=\"application/xml\">\n" +
                "        <attrs xmlns=\"http://www.sap.com/rws/bip\">\n" +
                "            <attr name=\"logonToken\" type=\"string\">JYJDBBOX01:6400@{3&amp;2=33964,U3&amp;2v=JYJDBBOX01:6400,UP&amp;66=40,U3&amp;68=secEnterprise:Administrator,UP&amp;S9=12,U3&amp;qe=100,U3&amp;vz=CKnkRIicfu.rmOY4uuNfqFs53faEcVyf7tWefyln26MPGHXEIWQZq7QfBgAJiM1B,UP}</attr>\n" +
                "        </attrs>\n" +
                "    </content>\n" +
                "</entry>";
		String loginTokenFromXML = getLoginTokenFromXML(token);
		System.out.println(loginTokenFromXML);
	}

}
