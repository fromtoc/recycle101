package com.xin.portal.bi.mstr;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;

@Component
public class MstrSessionFactory {

	private static final Logger logger = LoggerFactory.getLogger(MstrSessionFactory.class);
	
	@Autowired
    protected CacheManager cacheManager;
	
	public static WebIServerSession getSession(BiServer server) {
		return getSession(server,null,null);
	}
	
	public static WebIServerSession getSession(BiServer server,String uid, String pwd) {
		WebObjectsFactory factory = WebObjectsFactory.getInstance();
		WebIServerSession webIServerSession = factory.getIServerSession();
		webIServerSession.setServerName(server.getIp());
		webIServerSession.setServerPort(Integer.parseInt(server.getPort()));
		webIServerSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		webIServerSession.setLogin(uid==null?server.getDefaultUid():uid);
		webIServerSession.setPassword(pwd==null?server.getDefaultPwd():pwd);
		webIServerSession.setLocale(Locale.ENGLISH);
		
		
		try {
			webIServerSession.getSessionID();
		} catch (WebObjectsException e) {
			e.printStackTrace();
			logger.error("Error creating mstr sesion: {}	{}	 {}   {}   {}",
					new Object[] { server.getName(), server.getIp(), server.getPort(),server.getDefaultPwd(),server.getDefaultUid() });
		}
		return webIServerSession;
	}

	public static WebIServerSession getSession(BiProject project) {
		return getSession(project,null,null);
	}
	
	public static WebIServerSession getSession(BiProject project,String uid, String pwd) {
		WebObjectsFactory factory = WebObjectsFactory.getInstance();
		WebIServerSession webIServerSession = factory.getIServerSession();
		webIServerSession.setServerName(project.getIp());
		webIServerSession.setProjectName(project.getProject());
		webIServerSession.setServerPort(Integer.parseInt(project.getPort()));
		webIServerSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		webIServerSession.setLogin(uid==null?project.getDefaultUid():uid);
		webIServerSession.setPassword(pwd==null?project.getDefaultPwd():pwd);
		webIServerSession.setLocale(Locale.CHINESE);
		
		
		try {
			webIServerSession.getSessionID();
		} catch (WebObjectsException e) {
			e.printStackTrace();
			logger.error("Error creating a sesion！！！");
			logger.error("{}	{}		{}",
					new Object[] { project.getProject(), project.getIp(), project.getPort() });
		}
		return webIServerSession;
	}

	
	
	/**
	 * Close Intelligence Server Session
	 * 
	 * @param serverSession
	 *            WebIServerSession
	 */
	public static void closeSession(WebIServerSession serverSession) {
		try {
			serverSession.closeSession();
		} catch (WebObjectsException ex) {
			System.out.println("Error closing session:" + ex.getMessage());
			return;
		}
		System.out.println("Session closed.");
	}
	
	public static void main(String[] args){
		BiServer mstrServer = new BiServer();
		mstrServer.setIp("192.168.1.175");
		mstrServer.setPort("0");
		mstrServer.setDefaultUid("administrator");
		mstrServer.setDefaultPwd("");
		WebIServerSession session = getSession(mstrServer);
		try {
			logger.info("{}",session.getSessionID());
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**  
	 * @Title: check 
	 * @Description:  TODO
	 * @param record void
	 * @author zhoujun
	 * @Date 2018年12月20日 下午5:48:24 
	 */
	public static void check(BiProject server) throws Exception{
		WebObjectsFactory factory = WebObjectsFactory.getInstance();
		WebIServerSession webIServerSession = factory.getIServerSession();
		webIServerSession.setServerName(server.getIp());
		webIServerSession.setProjectName(server.getProject());
		webIServerSession.setServerPort(Integer.parseInt(server.getPort()));
		webIServerSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		webIServerSession.setLogin(server.getDefaultUid());
		webIServerSession.setPassword(server.getDefaultPwd());
		webIServerSession.setLocale(Locale.getDefault());
		webIServerSession.getSessionID();
		logger.info("{}",webIServerSession.getSessionID());
	}
	
	public static void checkServer(BiServer server) throws Exception{
		WebObjectsFactory factory = WebObjectsFactory.getInstance();
		WebIServerSession webIServerSession = factory.getIServerSession();
		webIServerSession.setServerName(server.getIp());
		webIServerSession.setServerPort(Integer.parseInt(server.getPort()));
		webIServerSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		webIServerSession.setLogin(server.getDefaultUid());
		webIServerSession.setPassword(server.getDefaultPwd());
		webIServerSession.setLocale(Locale.getDefault());
		webIServerSession.getSessionID();
		logger.info("{}",webIServerSession.getSessionID());
	}

}
