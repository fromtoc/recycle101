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
import com.microstrategy.web.objects.EnumWebReportExecutionModes;
import com.microstrategy.web.objects.WebResultWindow;
import com.microstrategy.webapi.EnumDSSXMLResultFlags;


/**
 *
 * <p>Title: ReportExecutionSample </p>
 * <p>Description: Executes a report and displays the results in XML format.</p>
 * <p>Company: Microstrategy, Inc.</p>
 */
public class ReportExecutionSample {

    /**
     * Use Web Objects to fetch the contents for the "Electronics Revenue vs. Forecast" report
     * on the "MicroStrategy Tutorial" project
     */
    public static void displayReportXML() {
        WebIServerSession serverSession = null;
        WebReportSource reptSrc = null;
        WebReportInstance rptInst = null;
        String xmlResult = null;

        //Create a session using the SessionManagementSample class
        serverSession = getSession();

        //Create Report Source
        reptSrc = serverSession.getFactory().getReportSource();

        //Set Window Settings for the report
        WebResultWindow resultWindow = reptSrc.getResultWindow();

        resultWindow.setGridMaxCols(10);
        resultWindow.setGridMaxRows(50);

        //Report ID for Electronics Revenue vs. Forecast - EB3CD5D14F4C8C77782AC0882C986B8D
        String reportID = "EA29903049251F389824A7A12582E185";

        //Instantiate the report
        try {
            rptInst = reptSrc.getNewInstance(reportID);
        } catch (WebObjectsException ex) {
            handleError(serverSession, "Error obtaining report instance: " + ex.getMessage());
        }

        //Set flags on the report instance
        rptInst.setAsync(false);
        rptInst.setExecutionMode(EnumWebReportExecutionModes.REPORT_MODE_DEFAULT);
        rptInst.setResultFlags(EnumDSSXMLResultFlags.DssXmlResultGrid);

        //Fetch XML fot the report
        try {
            xmlResult = rptInst.getResultsAsXML();
           // System.out.println("\nReport XML:");
           // System.out.println(xmlResult);
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
//            serverSession.setServerName("10.5.5.83"); 
//			 serverSession.setServerPort(0);
//			 serverSession.setProjectName("MicroStrategy Tutorial"); 
//			 serverSession.setLogin("administrator"); 
//			 serverSession.setPassword(""); 
			 
			 serverSession.setServerName("192.168.1.88"); 
			 serverSession.setServerPort(0);
			 serverSession.setProjectName("MicroStrategy Tutorial"); 
			 serverSession.setLogin("administrator"); 
			 serverSession.setPassword("wcnzxb123!"); 

            //Initialize the session with the above parameters
            /*try {
                //System.out.println("\nSession created with ID: " + serverSession.getSessionID());
            } catch (WebObjectsException ex) {
               ex.printStackTrace();
            }*/
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
            //System.out.println("Error closing session:" + ex.getMessage());
            return;
        }
       // System.out.println("Session closed.");
    }

    /**
     * Print out error message, close session and abort execution
     * @param serverSession WebIServerSession
     * @param message String
     */
    public static void handleError(WebIServerSession serverSession, String message) {
        //System.out.println(message);
        if (serverSession != null) {
            closeSession(serverSession);
        }
        System.exit(0);
    }

}
