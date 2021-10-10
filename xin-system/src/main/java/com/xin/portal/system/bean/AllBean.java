package com.xin.portal.system.bean;

import org.springframework.beans.BeansException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AllBean implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext; 

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		AllBean.applicationContext = context;
		
	}
	
	public static Object getBean(String name){
        return applicationContext.getBean(name);
   }
   
    public static ApplicationContext getApplicationContext() {  
         return applicationContext;  
   }  


}
