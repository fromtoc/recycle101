package com.xin.portal.bi.mstr.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.microstrategy.web.beans.RWBean;
import com.microstrategy.web.beans.WebBeanFactory;
import com.microstrategy.web.objects.WebConstantPrompt;
import com.microstrategy.web.objects.WebDisplayHelper;
import com.microstrategy.web.objects.WebDisplayUnits;
import com.microstrategy.web.objects.WebElementSource;
import com.microstrategy.web.objects.WebElements;
import com.microstrategy.web.objects.WebElementsPrompt;
import com.microstrategy.web.objects.WebExpressionPrompt;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebObjectsPrompt;
import com.microstrategy.web.objects.WebPrompt;
import com.microstrategy.web.objects.WebPrompts;
import com.microstrategy.web.objects.WebReportExcelExportSettings;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.web.objects.WebReportPDFExportSettings;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.rw.EnumRWExecutionModes;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.system.model.BiProject;

public class ExportApi {
	
	private static Logger logger = LoggerFactory.getLogger(ExportApi.class);

	public static void exportDom(BiProject mstrProject,String mstrid,String path,Map<String,String[]>params,String type) throws WebObjectsException, InterruptedException, IOException{
		WebObjectsFactory m_factory = WebObjectsFactory.getInstance();
		WebIServerSession m_serverSession = m_factory.getIServerSession();
		m_serverSession.setServerName(mstrProject.getIp());
		m_serverSession.setServerPort(Integer.parseInt(mstrProject.getPort().toString()));
		m_serverSession.setProjectName(mstrProject.getProject());
		m_serverSession.setLogin(mstrProject.getDefaultUid());
		m_serverSession.setPassword(mstrProject.getDefaultPwd());
		m_serverSession.setDisplayLocale(Locale.CHINESE);// 设置语言
		
		byte[] data = null;
		RWInstance rwi = m_serverSession.getFactory().getRWSource().getNewInstance(mstrid);
		
		  setParams(rwi.getPrompts(), params);
		
		if(path.endsWith(".html")){
			data = "目前不支持html格式显示".getBytes();
		}
		
		if(path.endsWith(".pdf")){
			rwi.setExecutionMode(EnumRWExecutionModes.RW_MODE_PDF);
			
			while (true) {
				if (rwi.pollStatus() == 1)
					break;
				Thread.sleep(200);
			}
			
			data = rwi.getPDFData();
			
		}
		
        if(path.endsWith(".xls")||path.endsWith(".xlsx")){
        	rwi.setExecutionMode(EnumRWExecutionModes.RW_MODE_EXCEL);  
        	/*while (true) {
				if (rwi.pollStatus() == 1)
					break;
				Thread.sleep(200);
			}	
        	data = rwi.getExcelData();*/
        	rwi.setAsync(false);
        	
        	
        	logger.info(" =========导出报表DOM的XLS格式***开始！！！================== ");
        	int lStatus = 0 ;
        	while (true) {
        		lStatus = rwi.pollStatus();
    			logger.info(" =======**【"+lStatus+"】**======== ");
				if (lStatus == 1) {
					break;
				}
				Thread.sleep(300);
			}	
        
        	data = rwi.getExcelData();
        	logger.info(" =========导出报表DOM的XLS格式***结束！！！================== ");
        	
		}
        
        
//        if(path.endsWith(".xlsx")){
//        	rwi.setExecutionMode(EnumRWExecutionModes.RW_MODE_EXCEL);  
//        	while (true) {
//				if (rwi.pollStatus() == 1)
//					break;
//				Thread.sleep(200);
//			}			
//			data = rwi.getExportData();
//		}
        
        
        
        
        
        File file =new File(path);		
       if(!file.getParentFile().exists()) {          
       	 logger.info("目标文件所在目录不存在，准备创建它！");  
           if(!file.getParentFile().mkdirs()) {  
               logger.info("创建目标文件所在目录失败!");
               throw new IOException("创建目标文件所在目录失败!"+path);
           }  
       }
       //创建目标文件
       if (file.createNewFile()) {  
       	 logger.info("创建单个文件" + path + "成功！");          
       } else {  
       	 logger.info("创建单个文件" + path + "失败！");  
       	  throw new IOException("创建单个文件失败!"+path);
       }  
		
		FileOutputStream os = new FileOutputStream(file);
		    os.write(data);
		    os.flush();
		    os.close();
		    m_serverSession.closeSession();
		    
		
	}
	private static void outMapKey(Map<String, String[]> map)
	  {
	    for (String key : map.keySet())
	    {
	     // System.out.println("Key = " + key);
	    }

	    for (String[] value : map.values())
	    {
	    	//System.out.println("Value = " + Arrays.toString(value));
	    }
	  }

	
	public static void exportReport(BiProject mstrProject,String mstrid, String path, Map<String, String[]> params)
		    throws WebObjectsException, IOException
		  {
		WebObjectsFactory m_factory = WebObjectsFactory.getInstance();
		WebIServerSession m_serverSession = m_factory.getIServerSession();
		m_serverSession.setServerName(mstrProject.getIp());
		m_serverSession.setServerPort(Integer.parseInt(mstrProject.getPort().toString()));
		m_serverSession.setProjectName(mstrProject.getProject());
		m_serverSession.setLogin(mstrProject.getDefaultUid());
		m_serverSession.setPassword(mstrProject.getDefaultPwd());
		m_serverSession.setDisplayLocale(Locale.CHINESE);// 设置语言

		    String accessToken = m_serverSession.getAccessToken();
		    //System.out.println("token=" + accessToken);
		    outMapKey(params);

		    WebReportSource reportSource = m_serverSession.getFactory().getReportSource();
		    reportSource
		      .setExecutionFlags(2432);

		    WebReportInstance reportI = reportSource.getNewInstance(mstrid);
		    try
		    {
		      setParams(reportI.getPrompts(), params);
		    } catch (Exception e) {
		      e.printStackTrace();
		     // System.out.println(e.getMessage());
		      throw new RuntimeException("设置参数错误！", e);
		    }

		    byte[] data = null;

		    if (path.endsWith(".html")) {
		      data = "目前不支持html格式显示".getBytes();
		    }
		    if (path.endsWith(".pdf")) {
		      reportI.setExecutionMode(2);
		      WebReportPDFExportSettings exportSettings = (WebReportPDFExportSettings)reportI.getExportSettings();
		      exportSettings.setMode(0);
		      reportI.setAsync(false);
		      reportI.pollStatus();
		      data = reportI.getBinaryData();
		    }

		    if ((path.endsWith(".xls")) || (path.endsWith(".xlsx")))
		    {
		      reportI.setExecutionMode(3);
		      WebReportExcelExportSettings exportSettings = (WebReportExcelExportSettings)reportI.getExportSettings();
		      exportSettings.setMode(0);
		      exportSettings.setVersion(4);
		      reportI.setAsync(false);

		      int lStatus = reportI.pollStatus();

		      data = reportI.getExcelBinaryData();
		    }

		    File file = new File(path);

		    if (!file.getParentFile().exists())
		    {
		      //System.out.println("目标文件所在目录不存在，准备创建它！");
		      if (!file.getParentFile().mkdirs()) {
		        //System.out.println("创建目标文件所在目录失败!");
		        throw new IOException("创建目标文件所在目录失败!" + path);
		      }
		    }

		    if (file.createNewFile()) {
		      //System.out.println("创建单个文件" + path + "成功！");
		    } else {
		      //System.out.println("创建单个文件" + path + "失败！");
		      throw new IOException("创建单个文件失败!" + path);
		    }

		    FileOutputStream os = new FileOutputStream(file);
		    os.write(data);
		    os.flush();
		    os.close();
		    m_serverSession.closeSession();
		  }
	
	
	public static void exportDom2(BiProject mstrProject,String mstrid,String path,Map<String,String[]>params,String type) throws WebObjectsException, InterruptedException, IOException{
		WebObjectsFactory m_factory = WebObjectsFactory.getInstance();
		WebIServerSession m_serverSession = m_factory.getIServerSession();
		m_serverSession.setServerName(mstrProject.getIp());
		m_serverSession.setServerPort(Integer.parseInt(mstrProject.getPort().toString()));
		m_serverSession.setProjectName(mstrProject.getProject());
		m_serverSession.setLogin(mstrProject.getDefaultUid());
		m_serverSession.setPassword(mstrProject.getDefaultPwd());
		m_serverSession.setDisplayLocale(Locale.CHINESE);// 设置语言
		
		byte[] data = null;
		 RWBean rwb;
		 WebPrompts prompts;
		 WebConstantPrompt prompt;
         try {
            // create the RWBean for the document object
               WebBeanFactory factory = WebBeanFactory.getInstance();
               rwb = (RWBean) factory.newBean("RWBean");
               rwb.setSessionInfo(m_serverSession);
               rwb.setObjectID(mstrid);
               prompts = rwb.getRWInstance().getPrompts();
               prompt = (WebConstantPrompt)prompts.get(0);
               prompt.setAnswer("2018-05-22");
               prompt = (WebConstantPrompt)prompts.get(0);
               prompt.setAnswer("2018-05-22");
               prompt = (WebConstantPrompt)prompts.get(0);
               prompt.setAnswer("2018-05-22");
               
               rwb.getRWInstance().setAsync(false);
               rwb.collectData();
              
            // set the execution mode to Flash export
               rwb.setExecutionMode(EnumRWExecutionModes.RW_MODE_FLASH_EXPORT);
            // get the binary data for the MHT file
               data = rwb.getRWInstance().getExportData();
        
         } catch(Exception e) {
        	 e.printStackTrace();
         }
        
        
        
        File file =new File(path);		
       if(!file.getParentFile().exists()) {          
       	 logger.info("目标文件所在目录不存在，准备创建它！");  
           if(!file.getParentFile().mkdirs()) {  
               logger.info("创建目标文件所在目录失败!");
               throw new IOException("创建目标文件所在目录失败!"+path);
           }  
       }
       //创建目标文件
       if (file.createNewFile()) {  
       	 logger.info("创建单个文件" + path + "成功！");          
       } else {  
       	 logger.info("创建单个文件" + path + "失败！");  
       	  throw new IOException("创建单个文件失败!"+path);
       }  
		
		FileOutputStream os = new FileOutputStream(file);
		    os.write(data);
		    os.flush();
		    os.close();
		    m_serverSession.closeSession();
		    
		
	}
	
	/**
	 * 设置参数
	 * @param obj
	 * @param map
	 * @throws WebObjectsException
	 */
	public static void setParams(WebPrompts prompts,Map<String,String[]> map) throws WebObjectsException{
		logger.info("---设置参数---");
		int size = prompts.size();	//参数个数
		    for(int i=0;i<size;i++){
		    	WebPrompt webprompt = prompts.get(i);
		    	logger.info("----当前处理的参数名称:---"+webprompt.getName());
		    	logger.info("----当前处理的参数ID:---"+webprompt.getID());
		    	if(webprompt instanceof WebElementsPrompt){
		    		logger.info("----元素应答---");
		    		WebElementsPrompt webElementsPrompt = (WebElementsPrompt) webprompt;
		    		WebElementSource eltSrc=webElementsPrompt.getOrigin().getElementSource();
		    		WebElements elements = eltSrc.getElements();
			    	WebElements we = webElementsPrompt.getAnswer(); 
			    	String key = "";
			    	we.clear();
			    	
			    	if(!webElementsPrompt.getName().endsWith("的元素")){
			    		key = webElementsPrompt.getName();
			    	}else if(!webElementsPrompt.getTitle().endsWith("的元素")){
			    		key = webElementsPrompt.getTitle();
			    	}else if(!webElementsPrompt.getDisplayName().endsWith("的元素")){
			    		key = webElementsPrompt.getDisplayName();
			    	}
			    	if("".equals(key)){
			    		throw new RuntimeException("参数名有异常!");
			    	}
			    	
			    	List<String> vals = Lists.newArrayList();
			    	if(null!=map.get(key)){			    		    
			    		String [] arr = map.get(key);//获取选择的参数
			    		vals = Arrays.asList(arr);
			    	}
			    	
			    	for (int e = 0; e < elements.size(); e++) {
						String originName = webElementsPrompt.getOrigin().getName();
						String elemName = elements.get(e).getDisplayName();
						logger.info("--set value--{}, {}---",originName,elemName);
						if (originName.equals(key) && vals.contains(elemName)) {
							logger.info(" add Value {}",elements.get(e).getElementID());
							we.add(elements.get(e).getElementID());
						}
					}

			    	webElementsPrompt.setAnswer(we);
			    	
//					defaultWebElements.clear();
//			    	if(null!=map.get(key)){			    		    
//			    		String [] arr = map.get(key);//获取选择的参数
//			    		logger.info(""+arr.length);
//			    	    if(arr.length>1){			    	    	
//			    	    	for(int j=0;j<arr.length;j++){
//			    	    		logger.info("多个选项之一 =====  "+arr[j]);
//			    	    		logger.info("elementID = "+arr[j].substring(0, arr[j].indexOf("^")));
//			    	    			defaultWebElements.add(arr[j].substring(0, arr[j].indexOf("^"))/*turnValue(webElementsPrompt,arr[j])*/);
//			    	    	}
//			    	    }			    	    
//			    	    if(arr.length==1){
//			    	    	logger.info("只有一个选项 =====  "+arr[0]);
//			    	    		defaultWebElements.add(arr[0].substring(0, arr[0].indexOf("^")));		
//			    	    }			    	
//			    	 }
//			    	
//			    	webElementsPrompt.setAnswer(defaultWebElements);
//			    	webElementsPrompt.validate();
//				    webElementsPrompt.answerPrompt();
		    	}
		    	
		    	if(webprompt instanceof WebConstantPrompt ){
		    		logger.info("----WebConstantPrompt----");
		    		WebConstantPrompt webConstantPrompt = (WebConstantPrompt) webprompt;   
		    		if(webConstantPrompt.getDefaultAnswer()!=null){//如果默认值不为null
		    			logger.info(" 默认参数---" + webConstantPrompt.getDefaultAnswer()); 
		    			logger.info(" ShortAnswerXML---" + webConstantPrompt.getShortAnswerXML()); 
		    			String arr[] = map.get(webprompt.getTitle());
		    			logger.info("选择参数个数=="+arr.length);
		    			if(arr.length>0){
		    				 logger.info("参数="+arr[0]);
		    				 webConstantPrompt.setAnswer(arr[0]);
		    			}
		    			else{
		    				webConstantPrompt.setAnswer(webConstantPrompt.getDefaultAnswer());
		    			}
		                webConstantPrompt.validate();
		                // update#1
		                //webConstantPrompt.answerPrompt();
		    		}
		    		else{//为null
		    			String arr[] = map.get(webprompt.getTitle());
		    			logger.info("从 "+webprompt.getTitle()+" 拿值");
		    			if(null!=arr){
		    			   if(arr.length>0){
		    			   	 logger.info("参数="+arr[0]);
		    			   	   webConstantPrompt.setAnswer(arr[0]);
		    			   	   webConstantPrompt.validate();
				               webConstantPrompt.answerPrompt();
		    			   }
		    			}
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
		     			//System.out.println(allWebFolder.get(j).getType() + " " +allWebFolder.get(j).getName());    //
		     			String key = allWebFolder.get(j).getName();
				    	if(null!=map.get(key)){			    		    
				    		// set answers to this prompt   
				    		
				    		//System.out.println(" " + allWebFolder.get(0));
				    		//System.out.println(" " + allWebFolder.get(1));
				    		defaultWebFolder.add(allWebFolder.get(j));         
				    	}
		     			
		     		}                        // another method print all answers//                   
		     		webObjectsPrompt.setAnswer(defaultWebFolder);                    // answer this prompt        
		     		webObjectsPrompt.validate();       
		     		webObjectsPrompt.answerPrompt(); 
		             
		             
		             
		             
		    	}
		    	
		    	if(webprompt instanceof WebExpressionPrompt ){
		    		logger.info("----WebExpressionPrompt----");
		    		/*WebExpressionPrompt po = (WebExpressionPrompt) webprompt;
		    		 WebExpression webExpression = po.getAnswer();
		    		 webExpression.clear();
		    		 WebDimension webDimension = (WebDimension) po.getOrigin();    
		             WebAttribute webAttribute0 = webDimension.get(0).getAttribute();
		             WebAttribute webAttribute1 = webDimension.get(1).getAttribute();
		             WebElements webElements0 = webAttribute0.getElementSource().getElements();     
		             WebElements webElements1 = webAttribute1.getElementSource().getElements();     
		             WebOperatorNode webRootNode = (WebOperatorNode) webExpression.getRootNode();            
		             webRootNode.setExpressionType(EnumDSSXMLExpressionType.DssXmlFilterBranchQual);
		             webRootNode.setFunction(EnumDSSXMLFunction.DssXmlFunctionOr);                                                        
                     WebOperatorNode webOperatorNode1 =
		                     webExpression.createOperatorNode(EnumDSSXMLExpressionType.DssXmlFilterListQual,
		                     EnumDSSXMLFunction.DssXmlFunctionIn);
		             WebOperatorNode webOperatorNode2 =
		                     webExpression.createOperatorNode(EnumDSSXMLExpressionType.DssXmlFilterListQual,
		                     EnumDSSXMLFunction.DssXmlFunctionIn);                    
		             WebShortcutNode webShortcutNode0 = webExpression.createShortcutNode(webAttribute0, webOperatorNode1);
		             WebShortcutNode webShortcutNode1 = webExpression.createShortcutNode(webAttribute1, webOperatorNode2);                    
                     WebElementsObjectNode webElementsObjectNode0 =
		                     webExpression.createElementsObjectNode(webAttribute0, webOperatorNode1);    
		             WebElementsObjectNode webElementsObjectNode1 =
		                      webExpression.createElementsObjectNode(webAttribute1, webOperatorNode2);		     
		             webElementsObjectNode0.getElements().add(webElements0.get(1).getElementID());    
		             po.setAnswer(webExpression);		            
		             po.validate();
		             po.answerPrompt();*/
		    	}		    	
		    }
		    // update#2
		    prompts.answerPrompts();
	};
	
	public static void main(String[] args){
		BiProject mstrProject = new BiProject();
		mstrProject.setServer("192.168.1.175");
		mstrProject.setProject("MicroStrategy Tutorial");
		mstrProject.setPort("0");
		mstrProject.setDefaultUid("administrator");
		mstrProject.setDefaultPwd("");
		
//		mstrProject.setServer("10.1.71.153");
//		mstrProject.setProject("TJXINXI_SMA");
//		mstrProject.setPort(0);
//		mstrProject.setDefaultUid("administrator");
//		mstrProject.setDefaultPwd("mstrcs");
		
		Map<String,String[]> params = new HashMap<String, String[]>();
//		params.put("等于日期", new String[]{"2018-01-02"});
//		params.put("开始日期", new String[]{"2018-01-02"});
//		params.put("结束日期", new String[]{"2018-01-02"});
		
//		params.put("商品大类", new String[]{"电影","书籍"});
//		params.put("值 (日期)", new String[]{"20161001"});
		
//		params.put("0490E1884F59CB02BF3BE7B79A48FA29", new String[]{"电影","功夫片"});
//		params.put("值 (日期)", new String[]{"20161001"});
//		params.put("值 (日期)2", new String[]{"20161002"});
		
//		params.put("商品大类", new String[]{"电影^"});
//		params.put("商品子类", new String[]{"电影^"});
		
//		params.put("预定义对象列表", new String[]{"12^商品大类"});
		
		params.put("商品大类", new String[]{"12"});
		params.put("商品子类", new String[]{"12"});
		//8D679D3711D3E4981000E787EC6DE8A4 商品大类
		//8D679D4F11D3E4981000E787EC6DE8A4 商品子类
		
//		String mstrid = "D82423B94049DAAC899A219983E4C08F";
//		String mstrid = "56E805F3412055A153FC2190B64D5E51";
		
//		String mstrid = "15747447475E4FAF7E0448A6F0D1E1CE";
//		String mstrid = "08DC97684E8D573974EC069707B23D40";
//		String mstrid = "791AE0354BA661CB5C0C7AA62D090EC4";
		String mstrid = "A182DC724EBE10C43EBF899B92C976CD";//A182DC724EBE10C43EBF899B92C976CD 对象提示report id
		double name = Math.random();
		String path = "D:/mstr/"+name+".xlsx";
		try {
//			exportDom(mstrProject, mstrid, path, params, "");
			exportReport(mstrProject, mstrid, path, params);
		} catch (WebObjectsException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * 设置参数
	 * 
	 * @param obj
	 * @param map
	 * @throws WebObjectsException
	 */
	public static void setParams(WebPrompts prompts, List<String[]> listParams) throws WebObjectsException {
		logger.info("---设置参数---");
		int size = prompts.size(); // 参数个数
		WebElementsPrompt webElementsPrompt = null;
		for (int i = 0; i < size; i++) {
			//System.out.println("i==" + i);
			WebPrompt webprompt = prompts.get(i);
			logger.info("----当前处理的参数名称:---" + webprompt.getName());
			logger.info("----当前处理的参数ID:---" + webprompt.getID());
			if (webprompt instanceof WebElementsPrompt) {
				logger.info("----元素应答---");
				webElementsPrompt = (WebElementsPrompt) webprompt;
				logger.info("Shortxml==" + webElementsPrompt.getShortAnswerXML());
				WebElements defaultWebElements = webElementsPrompt.getAnswer();

				String key = "";
				if (!webElementsPrompt.getName().endsWith("的元素")) {
					key = webElementsPrompt.getName();
				} else if (!webElementsPrompt.getTitle().endsWith("的元素")) {
					key = webElementsPrompt.getTitle();
				} else if (!webElementsPrompt.getDisplayName().endsWith("的元素")) {
					key = webElementsPrompt.getDisplayName();
				}
				if ("".equals(key)) {
					throw new RuntimeException("参数名有异常!");
				}

				defaultWebElements.clear();
				String[] params = listParams.get(i);
				// 判断数组是否为空

				if (params != null || (params == null && params.length != 0)) {
					if (params.length > 1) {
						for (int j = 0; j < params.length; j++) {
							logger.info("多个选项之一 =====  " + params[j]);
							logger.info("elementID = " + params[j].substring(0, params[j].indexOf("^")));
							defaultWebElements
									.add(params[j].substring(0, params[j].indexOf("^")),
											params[j].substring(
													params[j].indexOf(
															"^") + 1,
											params[j].length())/*
																 * turnValue(
																 * webElementsPrompt
																 * ,arr[j])
																 */);

						}
					}
					if (params.length == 1) {
						logger.info("只有一个选项 =====  " + params[0]);
						//System.out.println("id为:" + params[0].substring(0, params[0].indexOf("^")));
						defaultWebElements.add(params[0].substring(0, params[0].indexOf("^")),
								params[0].substring(params[0].indexOf("^") + 1, params[0].length()));

					}

					webElementsPrompt.setAnswer(defaultWebElements);
					webElementsPrompt.validate();
					webElementsPrompt.answerPrompt();
					if (params.length == 0) {
						//System.out.println("ok");
					}
				
				}

			}

			if (webprompt instanceof WebConstantPrompt) {
				logger.info("----WebConstantPrompt----");
				WebConstantPrompt webConstantPrompt = (WebConstantPrompt) webprompt;
				if (webConstantPrompt.getDefaultAnswer() != null) {// 如果默认值不为null
					logger.info(" 默认参数---" + webConstantPrompt.getDefaultAnswer());
					logger.info(" ShortAnswerXML---" + webConstantPrompt.getShortAnswerXML());
					// String arr[] = map.get(webprompt.getTitle());
					String arr[] = listParams.get(i);
					logger.info("选择参数个数==" + arr.length);
					if (arr.length > 0) {
						logger.info("参数=" + arr[0]);
						webConstantPrompt.setAnswer(arr[0]);
					} else {
						webConstantPrompt.setAnswer(webConstantPrompt.getDefaultAnswer());
					}
					webConstantPrompt.validate();
					// update#1
					// webConstantPrompt.answerPrompt();
				} else {
					// 为null
					// String arr[] = map.get(webprompt.getTitle());
					// String arr[] = map.get(webprompt.getTitle());
					String arr[] = listParams.get(i);
					logger.info("从 " + webprompt.getTitle() + " 拿值");
					if (null != arr) {
						if (arr.length > 0) {
							logger.info("参数=" + arr[0]);
							webConstantPrompt.setAnswer(arr[0]);
							webConstantPrompt.validate();
							webConstantPrompt.answerPrompt();
						}
					}
				}
			}

			if (webprompt instanceof WebObjectsPrompt) {

				logger.info("----WebObjectsPrompt----");
				WebObjectsPrompt po = (WebObjectsPrompt) webprompt;
				logger.info("ShortAnswerXML=" + po.getShortAnswerXML());
				WebFolder answer = po.getAnswer();
				answer.clear();
				// String arr[] = map.get(po.getTitle());
				String arr[] = listParams.get(i);
				//System.out.println(arr);
				for (int j = 0; j < arr.length; j++) {
					//System.out.println("arr[j]=" + arr[j]);
					//System.out.println("arr[j]=" + (arr[j].split("~")[1]));
					logger.info("参数ID===" + (arr[j].split("~")[0]));
					logger.info("参数类型===" + Integer.parseInt((arr[j].split("~")[1])));
					WebObjectInfo object = WebObjectsFactory.getInstance().getObjectSource()
							.getObject((arr[j].split("~")[0]), Integer.parseInt((arr[j].split("~")[1])));
					answer.add(object);
				}
				po.setAnswer(answer);
				po.validate();
				po.answerPrompt();
			}

			if (webprompt instanceof WebExpressionPrompt) {
				logger.info("----WebExpressionPrompt----");

			}
	    logger.info("第"+i+"个参数是否为空======"+webprompt.isAnswerEmpty());	
	    logger.info("第"+i+"个参数是否被取消======"+webprompt.isCanceled());		    
	    logger.info("第"+i+"个参数提示是否被关闭======"+webprompt.isClosed ());	
	    logger.info("第"+i+"个参数当前是否存在非空答案======"+webprompt.hasAnswer());	  
	    logger.info("第"+i+"个参数提示答案的XML======"+webprompt.getAnswerXML());	
	    logger.info("第"+i+"个参数用户提示答案的集合======"+webprompt.getUserAnswers ());	
		}
		// update#2

		/*
		 * try { Thread.sleep(10000); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	
		
		prompts.populateAnswers(prompts.getAnswerXML());
	//	prompts.answerPrompts();

		logger.info("----end+++++----");
	};
	
	
	public static void exportReport2(BiProject mstrProject,String mstrid, String path, Map<String, String[]> params)
		    throws WebObjectsException, IOException
		  {
		WebObjectsFactory m_factory = WebObjectsFactory.getInstance();
		WebIServerSession m_serverSession = m_factory.getIServerSession();
		m_serverSession.setServerName(mstrProject.getIp());
		m_serverSession.setServerPort(Integer.parseInt(mstrProject.getPort().toString()));
		m_serverSession.setProjectName(mstrProject.getProject());
		m_serverSession.setLogin(mstrProject.getDefaultUid());
		m_serverSession.setPassword(mstrProject.getDefaultPwd());
		m_serverSession.setDisplayLocale(Locale.CHINESE);// 设置语言

		    String accessToken = m_serverSession.getAccessToken();
		    //System.out.println("token=" + accessToken);
		    outMapKey(params);

		    WebReportSource reportSource = m_serverSession.getFactory().getReportSource();
		    reportSource
		      .setExecutionFlags(2432);

		    WebReportInstance reportI = reportSource.getNewInstance(mstrid);
		    try
		    {
		      setParams(reportI.getPrompts(), params);
		    } catch (Exception e) {
		      e.printStackTrace();
		      //System.out.println(e.getMessage());
		      throw new RuntimeException("设置参数错误！", e);
		    }

		    byte[] data = null;

		    if (path.endsWith(".html")) {
		      data = "目前不支持html格式显示".getBytes();
		    }
		    if (path.endsWith(".pdf")) {
		      reportI.setExecutionMode(2);
		      WebReportPDFExportSettings exportSettings = (WebReportPDFExportSettings)reportI.getExportSettings();
		      exportSettings.setMode(0);
		      reportI.setAsync(false);
		      reportI.pollStatus();
		      data = reportI.getBinaryData();
		    }

		    if ((path.endsWith(".xls")) || (path.endsWith(".xlsx")))
		    {
		      reportI.setExecutionMode(3);
		      WebReportExcelExportSettings exportSettings = (WebReportExcelExportSettings)reportI.getExportSettings();
		      exportSettings.setMode(0);
		      exportSettings.setVersion(4);
		      reportI.setAsync(false);

		      int lStatus = reportI.pollStatus();

		      data = reportI.getExcelBinaryData();
		    }

		    File file = new File(path);

		    if (!file.getParentFile().exists())
		    {
		     // System.out.println("目标文件所在目录不存在，准备创建它！");
		      if (!file.getParentFile().mkdirs()) {
		        //System.out.println("创建目标文件所在目录失败!");
		        throw new IOException("创建目标文件所在目录失败!" + path);
		      }
		    }

		    if (file.createNewFile()) {
		     // System.out.println("创建单个文件" + path + "成功！");
		    } else {
		     // System.out.println("创建单个文件" + path + "失败！");
		      throw new IOException("创建单个文件失败!" + path);
		    }

		    FileOutputStream os = new FileOutputStream(file);
		    os.write(data);
		    os.flush();
		    os.close();
		    m_serverSession.closeSession();
		  }
	
}
