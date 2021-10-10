package com.xin.portal.system.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.model.UserInfo;

/**
 * @ClassPath: com.xin.portal.system.util.SessionUtil 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-7-14 下午4:58:47
 */
public class SessionUtil{
	
	public static UserInfo getUserInfo(){
		Subject currentUser = SecurityUtils.getSubject();    
        if(null != currentUser){  
            Session session = currentUser.getSession();   
            return (UserInfo) session.getAttribute(SessionKeys.USER);
        }
		return null;
	}
	
	public static Object getSession(String sessionKey){
		Subject currentUser = SecurityUtils.getSubject();    
        if(null != currentUser){  
            Session session = currentUser.getSession();   
            return  session.getAttribute(sessionKey);
        }
		return null;
	}
	
	/**  
     * 将一些数据放到ShiroSession中,以便于其它地方使用  
     */    
    public static void setSession(Object key, Object value){  
        Subject currentUser = SecurityUtils.getSubject();    
        if(null != currentUser){  
            Session session = currentUser.getSession();   
            if(null != session){    
                session.setAttribute(key, value);
            }    
        }    
    }

	public static String getUserId() {
		return getUserInfo().getId();
	}

}
