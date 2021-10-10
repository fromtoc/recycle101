package com.xin.portal.bi.bo;

import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.security.ILogonTokenMgr;

/**
 * @ClassPath: com.xin.portal.bi.bo.BoSessionUtil 
 * @Description: bo 会话工具类
 * @author zhoujun
 * @date 2017年12月12日 下午1:36:08
 */
public class BoSessionUtil {
	
	/**
	 * 
	 * @Title: getSession 
	 * @Description:  创建bo 会话
	 * @param poUsername
	 * @param poPassword
	 * @param cms
	 * @param authentication
	 * @return IEnterpriseSession
	 * @author zhoujun
	 * @Date 2018年12月29日 下午1:35:18
	 */
	public static IEnterpriseSession getSession(String poUsername, String poPassword, String cms,String authentication){
        System.out.println("params : "+poUsername+" | "+poPassword+" | "+cms+" | "+authentication);
		try {
			ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
			return sessionMgr.logon(poUsername, poPassword, cms,authentication);
			
		} catch (SDKException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: getToken 
	 * @Description:  获取BO token
	 * @param username bo 用户用户名
	 * @param password bo 用户密码
	 * @param cms bo server的ip地址
	 * @param authentication 认证方式
	 * @return String bo 登录后的token
	 * @author zhoujun
	 * @Date 2017年12月12日 下午1:33:42
	 */
	public static String getToken(String username, String password, String cms,String authentication){

        //登录并获得TOKEN，并命名用OpenDocument方式打开一个文件
        String tokenStr = "";
		try {
			IEnterpriseSession enterpriseSession = getSession(username, password, cms,authentication);
			ILogonTokenMgr mgr = enterpriseSession.getLogonTokenMgr();
			/**
			 *
			 * String  token=logonTokenMgr.createLogonToken("",10,5);
            其中createLogonToken(java.lang.String clientComputerName, int validMinutes, int validNumOfLogons)
            clientComputerName为使用这个token的客户端计算机名，空字符串表示该token可被任何客户端使用；
            validMinutes为token的有效时间（分钟）；
            validNumOfLogons 表示该token允许被使用的最大次数。
			 *
			 */
			tokenStr = mgr.createLogonToken("",24*60,Integer.MAX_VALUE);
		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tokenStr;
	}
	
	public static void main(String[] args) {
		/**
		 * step 1: 获取token
		 * 	
		 */
		String token = getToken("username", "password", "server ip", "secEnterprise");
		System.out.println(token);
		/**
		 * 
		 
		step 2: 浏览器访问报表 http://[ip]:[port]/BOE/OpenDocument/opendoc/openDocument.jsp?sIDType=CUID&iDocID=[iDocID]&token=[token]
		【参数说明】
			ip:port：BO web服务的ip 和端口
			iDocID ：BO中报表的iDocID
			tokn： 上述 getToken 获取的token 
			示例：http://[ip]:[port]/BOE/OpenDocument/opendoc/openDocument.jsp?sIDType=CUID&iDocID=[iDocID]&token=[token]
		*/
		
	}

}
