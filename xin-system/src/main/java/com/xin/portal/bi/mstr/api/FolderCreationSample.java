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

package com.xin.portal.bi.mstr.api;

import java.util.Date;

import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.beans.WebException;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.web.objects.WebReportManipulation;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.WebReportValidationException;
import com.microstrategy.web.objects.WebShortcut;
import com.microstrategy.web.objects.WebTemplate;
import com.microstrategy.web.objects.WebTemplateMetrics;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.microstrategy.webapi.EnumDSSXMLExecutionFlags;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLResultFlags;

/**
 * <p>
 * Title: FolderCreationSample
 * </p>
 * <p>
 * Description: Create a folder under "Shared Reports" folder for the
 * "MicroStrategy Tutorial" project.
 * </p>
 * <p>
 * Company: Microstrategy, Inc.
 * </p>
 */
public class FolderCreationSample {

	/**
	 * Create a new folder under "Shared Reports" with the specified name
	 * 
	 * @param folderName
	 *            {@link String} containing the name the new folder will have
	 * @return {@link String} containing the new folder's DSSID
	 */
	public static String createNewFolder(String folderName) {
		WebFolder newFolder = null;

		// Create a session
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setServerPort(0);
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName("MicroStrategy Tutorial");
		serverSession.setLogin("administrator");
		serverSession.setPassword("");
		serverSession.setServerName("192.168.1.175");
		WebObjectSource objSource = serverSession.getFactory().getObjectSource();

		try {
			// Obtain ID for Shared Reports Folder and populate WebFolder
			// Setting
			String sharedReportsFolderID = objSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
			WebFolder folder = (WebFolder) objSource.getObject(sharedReportsFolderID,
					EnumDSSXMLObjectTypes.DssXmlTypeFolder);

			// Obtain new folder from WebObjectSource
			newFolder = (WebFolder) objSource.getNewObject(EnumDSSXMLObjectTypes.DssXmlTypeFolder, folderName);

			// Save new folder under "Shared Reports" folder
			objSource.saveAs(newFolder, newFolder.getName(), folder, true);
		} catch (WebObjectsException ex) {
			System.out.println("Error while creating new folder " + folderName + ": " + ex.getMessage());
		}

		// Close the session to clean up resources on the Intelligence Server
		// SessionManagementSample.closeSession(serverSession);

		return newFolder.getID();
	}

	public static void createReport() {
		String SessionID = "";

		// Create factory object.
		WebObjectsFactory factory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = factory.getIServerSession();

		serverSession.setServerPort(0);
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName("MicroStrategy Tutorial");
		serverSession.setLogin("administrator");
		serverSession.setPassword("");
		serverSession.setServerName("192.168.1.175");
		// serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);

		try {
			SessionID = serverSession.getSessionID();
			System.out.println("Welcome " + serverSession.getLogin());
			System.out.println("SessionID: " + SessionID);

			// Object IDs for both the report and
			String strReportID = "E638377511D5C49EC0000C881FDA1A4F";
			String strParentFolderID = "8D67909211D3E4981000E787EC6DE8A4";

			// Initialize the WebObjectSource Object and get target report (this
			// could also be document) and parent folder where shortcut will
			// reside
			WebObjectSource wos = factory.getObjectSource();
			WebObjectInfo oReportInfo = wos.getObject(strReportID, EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition);
			WebFolder oParentFolder = (WebFolder) wos.getObject(strParentFolderID,
					EnumDSSXMLObjectTypes.DssXmlTypeFolder);

			// Create a new shortcut object and set its target to the report
			// object
			WebShortcut oShortcut = (WebShortcut) wos.getNewObject(EnumDSSXMLObjectTypes.DssXmlTypeShortcut);
			oShortcut.setTarget(oReportInfo);

			// save the object back to the MD, specifying the parent folder as
			// the 'My Reports' folder
			wos.save(oShortcut, oParentFolder);

			// clean up and close the session
			System.out.println("Closing session");
			serverSession.closeSession();
		} catch (WebObjectsException ex) {
			ex.printStackTrace();
		}
	}

	public static void createIcubeReport() throws WebBeanException, WebObjectsException, WebException {
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setServerPort(0);
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName("MicroStrategy Tutorial");
		serverSession.setLogin("administrator");
		serverSession.setPassword("");
		serverSession.setServerName("192.168.1.175");
		 
		serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		  

		 //Get both the source objects

		 WebObjectSource objSrc = objectFactory.getObjectSource();
		WebReportSource rptSrc = objectFactory.getReportSource();

		 

		 rptSrc.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionResolve);

		 rptSrc.setResultFlags(EnumDSSXMLResultFlags.DssXmlResultViewReport | EnumDSSXMLResultFlags.DssXmlResultWorkingSet);

		 

		 //Obtain an empty WebReportInstance from the WebReportSource object. This object represents a new report to be built

		 WebReportInstance rptInst = rptSrc.getNewInstance("9D89247049DDCC7C1203F88284D550C4");
		  

		 //Obtain the WebTemplate for this report

		 WebTemplate template = rptInst.getTemplate();
//		  
//
//		 //Add the Metrics template unit to the report on axis 2, position 1
//
		 template.addMetrics(0, 0);
//		  
//
//		 //Get a WebObjectInfo object representing the attribute that needs to be added to the template
//
//		 WebObjectInfo attrInfo = objSrc.getObject(attrDID, EnumDSSXMLObjectTypes.DssXmlTypeAttribute);
//		  
//
//		 //Add that to the template at axis 1, position 1
//
//		 template.add(attrInfo, 1, 1);
//
//
//		 //Obtain a similar WebObjectInfo object for the metric to add to the report 
//
		 WebObjectInfo metricInfo = objSrc.getObject("metricDID", EnumDSSXMLObjectTypes.DssXmlTypeMetric);
//
//
//		 //Obtain the metric collection on the report, which was put on the report at line 11 
//
		 WebTemplateMetrics wtm = template.getTemplateMetrics();
//
//
//		 //Actually add the metric to the Metrics collection 
//
		 wtm.add(metricInfo);
		  

		 //Obtain the WebReportManipulation interface of the report

		 WebReportManipulation rptManip = rptInst.getReportManipulator();
		  

		 //Submit the result to Intelligence Server for execution
		 String folderID = objSrc.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
			
		 WebFolder myReports = (WebFolder) objSrc.getObject(folderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
			// set report name to report name with current timestamp
			String datedReportName = "EDU report - ".concat((new Date()).toString());
		 try {
			WebReportInstance newInst = rptManip.applyChanges();
			newInst.setAsync(false);
			newInst.pollStatus();
			newInst.saveAs(myReports, datedReportName);
		} catch (WebReportValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("-----end-----");
		 

	}

	/**
	 * Execute Folder Creation Sample
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String args[]) {
		// createNewFolder("newFolder");
//		createReport();
		try {
			createIcubeReport();
		} catch (WebBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WebException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
