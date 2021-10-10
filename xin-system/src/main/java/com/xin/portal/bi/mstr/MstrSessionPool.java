package com.xin.portal.bi.mstr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebIServerSessionList;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.util.AESUtil;
import com.xin.portal.system.util.Constant;

public class MstrSessionPool {
	
	private static final Logger logger = LoggerFactory.getLogger(MstrSessionPool.class);
	
	private final WebObjectsFactory factory = WebObjectsFactory.getInstance();
	private WebIServerSessionList iServerSessionsList = factory.getIServerSessionList();
	private static Map<String,List<WebIServerSession>> sessionMap = new HashMap<String, List<WebIServerSession>>();
	private static final int SESSION_COUNT = 10;
	private static int currentSessionIndex = 0;
	private static final Object OBJ_LOCK = new Object();
	private static final MstrSessionPool SESSION_POOL = new MstrSessionPool();
	
	public static MstrSessionPool getInstance() {
		return SESSION_POOL;
	}
	
	
	
	public void initSessionPool(List<BiProject> projects) {
		Iterator<BiProject> iterator = projects.iterator();
		String mapKey = "";
		while (iterator.hasNext()) {
			BiProject project = iterator.next();
			List<WebIServerSession> sessionList = new ArrayList<WebIServerSession>();
			for (int i = 0; i < SESSION_COUNT; i++) {
				WebIServerSession localWebIServerSession = initSession(project);
				try {
					if (!localWebIServerSession.isAlive()) {
						break;
					}
					sessionList.add(localWebIServerSession);
				} catch (WebObjectsException e) {
					e.printStackTrace();
				}
			}
			mapKey = project.getIp() + "_"+project.getProject();
			sessionMap.put(mapKey, sessionList);
			logger.info(">>>>>>>>>>>>>>>initMstrSessionPoll<<<<<<<<<<< {}:{}",new Object[]{mapKey,sessionList.size()});
		}
	}
	
	public WebIServerSession initSession(BiProject project) {
		WebIServerSession webIServerSession = getFactory().getIServerSession();
		webIServerSession.setServerName(project.getIp());
		webIServerSession.setProjectName(project.getProject());
		webIServerSession.setServerPort(Integer.parseInt(project.getPort()));
//		webIServerSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		webIServerSession.setLogin(project.getDefaultUid());
		webIServerSession.setPassword(AESUtil.decrypt(project.getDefaultPwd(),Constant.DEFAULT_SALT));
		try {
			webIServerSession.getSessionID();
			iServerSessionsList.newSession(webIServerSession,
					project.getProject(), project.getIp(),
					Integer.parseInt(project.getPort()));
		} catch (WebObjectsException e) {
			e.printStackTrace();
			logger.error("======MSTR项目初始化链接错误！！！");
			logger.error("{}	{}		{}", new Object[]{project.getProject(),project.getIp(),project.getPort()});
			logger.error("==============================");
		}
		return webIServerSession;
	}
	
	/**
	 * 获取mstr session，默认使用第一个项目配置
	 * @return
	 */
	public WebIServerSession getFreeWebIServerSession() {
		return getFreeWebIServerSession(null);
	}
	
	/**
	 * 
	 * @param project 配置的项目信息
	 * @return
	 */
	public WebIServerSession getFreeWebIServerSession(BiProject project) {
		currentSessionIndex = getNextSessionIndex();
		List<WebIServerSession> list = null;
		if (project!=null) {
			list = sessionMap.get(project.getIp()+"_"+project.getProject());
		} else {
	        for (Entry<String, List<WebIServerSession>> entry : sessionMap.entrySet()) {  
	        	list = entry.getValue();  
	            if (list != null) {  
	                break;  
	            }  
	        }  
		}
		if (list == null || list.size()==0) {
			logger.error("===无法获取MSTR session 请检查配置==");
			return null;
		} else {
			WebIServerSession webIServerSession = list.get(currentSessionIndex);
			int i=0;
			synchronized (OBJ_LOCK)
		    {
		      while (!isSessionAlive(webIServerSession))
		      {
		    	currentSessionIndex = getNextSessionIndex();
		        i++;
		        webIServerSession = list.get(currentSessionIndex);
		        if (i >= 10)
		        {
		          updateSessionPool(project);
		          i = 0;
		        }
		      }
		    }
			webIServerSession.setActive();
		    return webIServerSession;
		}
	  }
	
	private synchronized int getNextSessionIndex() {
		if (currentSessionIndex == SESSION_COUNT-1) {
			currentSessionIndex = 0;
		} else {
			currentSessionIndex += 1;
		}
		return currentSessionIndex;
	}

	private boolean isSessionAlive(WebIServerSession paramWebIServerSession) {
		try {
			if (paramWebIServerSession.isAlive()) {
				return true;
			}
		} catch (WebObjectsException e) {
			e.printStackTrace();
		}
		return false;
	}	
	
	private void updateSessionPool(BiProject project) {
		List<WebIServerSession> list = sessionMap.get(project.getIp()+"_"+project.getProject());
		if (list != null) {
			for (int i = 0; i < SESSION_COUNT; i++) {
				updateSession(i, list.get(i), project);
			}
		}
	}
	
	private void updateSession(int index,WebIServerSession paramWebIServerSession, 
			BiProject project) {
		removeDeadSession(paramWebIServerSession, project);
		paramWebIServerSession = initSession(project);
		sessionMap.get(project.getIp()+"_"+project.getProject()).add(index, paramWebIServerSession);
	}
	
	
	private void removeDeadSession(WebIServerSession webIServerSession,
			BiProject project) {
		try {
			iServerSessionsList.remove(webIServerSession);
			sessionMap.get(project.getIp()+"_"+project.getProject()).remove(webIServerSession);
			return;
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public WebObjectsFactory getFactory() {
		return this.factory;
	}

}
