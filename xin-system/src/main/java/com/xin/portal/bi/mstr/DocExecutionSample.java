/**
 * MicroStrategy SDK
 *
 * Copyright � 2001-2006 MicroStrategy Incorporated. All Rights Reserved.
 *
 * MICROSTRATEGY MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THIS SAMPLE CODE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. MICROSTRATEGY SHALL NOT
 * BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THIS SAMPLE CODE OR ITS DERIVATIVES.
 */

package com.xin.portal.bi.mstr;


import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebReportInstance;

import java.beans.Encoder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.microstrategy.web.objects.EnumWebReportExecutionModes;
import com.microstrategy.web.objects.WebDocumentInstance;
import com.microstrategy.web.objects.WebDocumentSource;
import com.microstrategy.web.objects.WebResultWindow;
import com.microstrategy.webapi.EnumDSSXMLResultFlags;
import com.xin.portal.bi.mstr.api.ExportApi;


/**
 *
 * <p>Title: ReportExecutionSample </p>
 * <p>Description: Executes a report and displays the results in XML format.</p>
 * <p>Company: Microstrategy, Inc.</p>
 */
public class DocExecutionSample {

    /**
     * Use Web Objects to fetch the contents for the "Electronics Revenue vs. Forecast" report
     * on the "MicroStrategy Tutorial" project
     */
    public static void displayReportXML() {
        WebIServerSession serverSession = null;
        String xmlResult = null;
        
        WebDocumentInstance wdInst = null;
        WebDocumentSource wdSrc = null;
        //Create a session using the SessionManagementSample class
        serverSession = getSession();

        //Create Report Source
        wdSrc = serverSession.getFactory().getDocumentSource();

        //Set Window Settings for the report
//        WebResultWindow resultWindow = reptSrc.getResultWindow();
//
//        resultWindow.setGridMaxCols(10);
//        resultWindow.setGridMaxRows(50);

        //Report ID for Electronics Revenue vs. Forecast - EB3CD5D14F4C8C77782AC0882C986B8D
        String documentId = "0CB3259746665A9B831EA2BEBAC20AF2";

        //Instantiate the report
        try {
            wdInst = wdSrc.getNewInstance(documentId);
            
            Map<String,String[]> params = new HashMap<String, String[]>();
    		params.put("等于日期", new String[]{"2018-01-02"});
    		params.put("开始日期", new String[]{"2018-01-02"});
    		params.put("结束日期", new String[]{"2018-01-02"});
    		
    		String strShortAnswerXML1 = wdInst.getPrompts().getShortAnswerXML();
    		
    		
    	     System.out.println(strShortAnswerXML1);
    	     try {
				System.out.println(URLEncoder.encode(strShortAnswerXML1, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	     System.out.println("--------------------------------------------------------");
    	     String strEncodedAnsXML = wdInst.getPrompts().getShortAnswerXML(true);
    	     System.out.println(strEncodedAnsXML);
            
    		ExportApi.setParams(wdInst.getPrompts(), params);
    		
    		String strShortAnswerXML2 = wdInst.getPrompts().getShortAnswerXML();
    		System.out.println(strShortAnswerXML2);
   	     try {
				System.out.println(URLEncoder.encode(strShortAnswerXML2, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   	     System.out.println("--------------------------------------------------------");
   	     String strEncodedAnsXML2 = wdInst.getPrompts().getShortAnswerXML(true);
   	     System.out.println(strEncodedAnsXML2);
            
        } catch (WebObjectsException ex) {
            handleError(serverSession, "Error obtaining report instance: " + ex.getMessage());
        }

        //Set flags on the report instance
        wdInst.setAsync(false);
        wdInst.setViewMode(1);
        //wdInst.setResultFlags(EnumDSSXMLResultFlags.DssXmlResultGrid);
        
        //Fetch XML fot the report
        try {
            xmlResult = wdInst.getResults();
            
            System.out.println("\nReport XML:");
            System.out.println(xmlResult);
        } catch (WebObjectsException ex) {
            handleError(serverSession, "Error fetching report XML: " + ex.getMessage());
        }

        //Close session
        closeSession(serverSession);
    }

    /**
     * Execute sample to display report XML
     * @param args String[]
     */
    public static void main(String args[]) {
        displayReportXML();
    }
    
	private static WebObjectsFactory factory = null;
	private static WebIServerSession serverSession = null;
    
    public static WebIServerSession getSession() {
        if (serverSession == null) {
            //create factory object
            factory = WebObjectsFactory.getInstance();
            serverSession = factory.getIServerSession();
            
          //Should be replaced with
//			 serverSession.setServerName("WIN-TQCR65U0I26"); 
//			 serverSession.setServerPort(0);
//			 serverSession.setProjectName("MicroStrategy Tutorial"); 
//			 serverSession.setLogin("administrator"); 
//			 serverSession.setPassword("123"); 
             
             serverSession.setServerName("192.168.1.88"); 
			 serverSession.setServerPort(0);
			 serverSession.setProjectName("MicroStrategy Tutorial"); 
			 serverSession.setLogin("administrator"); 
			 serverSession.setPassword("wcnzxb123!"); 
			 
//			 serverSession.setServerName("202.189.3.29"); 
//			 serverSession.setServerPort(34956);
//			 serverSession.setProjectName("swhydemo"); 
//			 serverSession.setLogin("administrator"); 
//			 serverSession.setPassword(""); 

            //Initialize the session with the above parameters
            try {
                System.out.println("\nSession created with ID: " + serverSession.getSessionID());
            } catch (WebObjectsException ex) {
               ex.printStackTrace();
            }
        }
        //Return session
        return serverSession;
    }
    
    
    /**
     * Close Intelligence Server Session
     * @param serverSession WebIServerSession
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

    /**
     * Print out error message, close session and abort execution
     * @param serverSession WebIServerSession
     * @param message String
     */
    public static void handleError(WebIServerSession serverSession, String message) {
        System.out.println(message);
        if (serverSession != null) {
            closeSession(serverSession);
        }
        System.exit(0);
    }

}
