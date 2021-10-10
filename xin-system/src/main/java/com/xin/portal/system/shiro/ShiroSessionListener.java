package com.xin.portal.system.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ShiroSessionListener implements SessionListener {

	private Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);
    @Override  
    public void onStart(Session session) {//会话创建触发 已进入shiro的过滤连就触发这个方法  
    	logger.info("The session to create：" + session.getId());
    }  
  
    @Override  
    public void onStop(Session session) {//退出
    	logger.info("Out of the session：" + session.getId());
    }  
  
    @Override  
    public void onExpiration(Session session) {//会话过期时触发  
    	logger.info("Session expiration：" + session.getId());
    }  
  
}  

