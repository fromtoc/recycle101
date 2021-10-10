package com.xin.portal.bi.mstr.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.i18n.LanguageParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.microstrategy.web.objects.WebConstantPrompt;
import com.microstrategy.web.objects.WebDisplayHelper;
import com.microstrategy.web.objects.WebDisplayUnits;
import com.microstrategy.web.objects.WebElementSource;
import com.microstrategy.web.objects.WebElements;
import com.microstrategy.web.objects.WebElementsPrompt;
import com.microstrategy.web.objects.WebExpressionPrompt;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebObjectsPrompt;
import com.microstrategy.web.objects.WebPrompt;
import com.microstrategy.web.objects.WebPrompts;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.Prompt;

public class PromptApi {
	
	private static Logger logger = LoggerFactory.getLogger(PromptApi.class);
	
	public static List<Prompt> prompts(BiProject mstrProject,String mstrid,int type) throws WebObjectsException, InterruptedException, IOException{
		List<Prompt> list = Lists.newArrayList();
		WebObjectsFactory m_factory = WebObjectsFactory.getInstance();
		WebIServerSession m_serverSession = m_factory.getIServerSession();
		m_serverSession.setServerName(mstrProject.getIp());
		m_serverSession.setServerPort(Integer.parseInt(mstrProject.getPort().toString()));
		m_serverSession.setProjectName(mstrProject.getProject());
		m_serverSession.setLogin(mstrProject.getDefaultUid());
		m_serverSession.setPassword(mstrProject.getDefaultPwd());
		m_serverSession.setDisplayLocale(Locale.CHINESE);// 设置语言
		if (EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition == type) {
			getReport(m_serverSession,mstrid);
		} else if (EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition == type){
			getDocument(m_serverSession,mstrid);
		}
		return list;
		
	}
	
	public static void getReport(WebIServerSession session,String mstrId) throws WebObjectsException, InterruptedException, IOException{
		WebReportSource reportSource = session.getFactory().getReportSource();
	    WebReportInstance report = reportSource.getNewInstance(mstrId);
	    WebPrompts prompts = report.getPrompts();
	    setParams(prompts);
		
		
	}
	
	public static void getDocument(WebIServerSession session,String mstrId) throws WebObjectsException, InterruptedException, IOException{
		RWInstance rwi = session.getFactory().getRWSource().getNewInstance(mstrId);
		WebPrompts prompts = rwi.getPrompts();
		setParams(prompts);
		
	}


	public static void setParams(WebPrompts prompts) throws WebObjectsException{
		logger.info("---"+ LangTransform.getLocaleLang( LanguageParam.LOGGERINFO_40)+"---");
		//logger.info("---设置参数---");
		int size = prompts.size();	//参数个数
		    for(int i=0;i<size;i++){
		    	WebPrompt webprompt = prompts.get(i);
		    	logger.info("----getName:---"+webprompt.getName());
		    	logger.info("----getID:---"+webprompt.getID());
		    	logger.info("----getTitle:---"+webprompt.getTitle());
		    	logger.info("----getDisplayName:---"+webprompt.getDisplayName());
		    	logger.info("----getDescription:---"+webprompt.getDescription());
		    	
		    	if(webprompt instanceof WebElementsPrompt){
					logger.info("---"+ LangTransform.getLocaleLang( LanguageParam.LOGGERINFO_41)+"---");
		    		//logger.info("----元素应答---");
		    		WebElementsPrompt webElementsPrompt = (WebElementsPrompt) webprompt;
		    		WebElementSource eltSrc=webElementsPrompt.getOrigin().getElementSource();
		    		WebElements elements = eltSrc.getElements();
			    	WebElements we = webElementsPrompt.getAnswer(); 
			    	String key = "";
			    	we.clear();
			    	
			    	
			    	for (int e = 0; e < elements.size(); e++) {
						String originName = webElementsPrompt.getOrigin().getName();
						String elemName = elements.get(e).getDisplayName();
						logger.info("--set value--{}, {}---",originName,elemName);
						logger.info(" add Value {}",elements.get(e).getElementID());
					}

			    	
		    	}
		    	
		    	if(webprompt instanceof WebConstantPrompt ){
		    		logger.info("----WebConstantPrompt----");
		    		WebConstantPrompt webConstantPrompt = (WebConstantPrompt) webprompt;   
		    		if(webConstantPrompt.getDefaultAnswer()!=null){//如果默认值不为null
						logger.info( LangTransform.getLocaleLang( LanguageParam.LOGGERINFO_42)+"---[{}]",webConstantPrompt.getDefaultAnswer());
		    			//logger.info(" 默认参数---" + webConstantPrompt.getDefaultAnswer());
		    		}
		    	}
		    	
		    	if(webprompt instanceof WebObjectsPrompt){
		 
		    		logger.info("----WebObjectsPrompt----");
		    		/*WebObjectsPrompt po = (WebObjectsPrompt)webprompt;
		    		logger.info("ShortAnswerXML="+	po.getShortAnswerXML());	
		    		logger.info(" title： {}, {}",po.getDisplayName());	
		    		WebFolder answer = po.getAnswer();
		    		answer.clear();
		    		 String arr[] = map.get(po.getDisplayName());
		    		 for(int j =0;j<arr.length;j++){
		    			 logger.info("参数ID==="+(arr[j].split("~")[0]));
		    			 logger.info("参数类型==="+Integer.parseInt((arr[j].split("~")[1])));
		    			 WebObjectInfo object = WebObjectsFactory.getInstance().getObjectSource().getObject((arr[j].split("~")[0]), Integer.parseInt((arr[j].split("~")[1])));
		    			 
		    			 answer.add(object);	 
		    		 }    		 
		    		 po.setAnswer(answer);
		    		 po.validate();
		             po.answerPrompt(); */
		             
		    		
		             
		            WebObjectsPrompt webObjectsPrompt = (WebObjectsPrompt) webprompt;                    // get answers of this prompt                    
		            logger.info(" title： {}, {}",webObjectsPrompt.getDisplayName());	
		            WebFolder defaultWebFolder = webObjectsPrompt.getAnswer();                                            // clear
		     		defaultWebFolder.clear();                    // print all answers                         
		     		WebFolder allWebFolder = webObjectsPrompt.getSuggestedAnswers();    //                    
		     		WebDisplayHelper webDisplayHelper = webObjectsPrompt.getDisplayHelper();//    
		     		WebDisplayUnits webDisplayUnits = webDisplayHelper.getAvailableDisplayUnits();//    
//		     		for (int k = 0; k < webDisplayUnits.size(); k++) {//                     
//		     			System.out.println(webDisplayUnits.get(k).getDisplayUnitType() + " " + webDisplayUnits.get(k).getDisplayName());//        
//		     		}                    //             
		     		webDisplayHelper.applyDefaultDisplaySettings(); 
		     		
		     		for(int j = 0; j < allWebFolder.size(); j++) {//                       
		     			System.out.println(allWebFolder.get(j).getType() + " " +allWebFolder.get(j).getName());    //   
		     			
		     		}                        // another method print all answers//                   
		             
		             
		             
		             
		    	}
		    	
		    	if(webprompt instanceof WebExpressionPrompt ){
		    		logger.info("----WebExpressionPrompt----");
		    	}		    	
		    }
	};

}
