package com.xin.portal.bi.mstr.api;

import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microstrategy.web.beans.RWBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.beans.WebBeanFactory;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebGridHeaders;
import com.microstrategy.web.objects.WebGridRows;
import com.microstrategy.web.objects.WebGridTitles;
import com.microstrategy.web.objects.WebHeader;
import com.microstrategy.web.objects.WebHeaders;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebReportData;
import com.microstrategy.web.objects.WebReportGrid;
import com.microstrategy.web.objects.WebReportInstance;
import com.microstrategy.web.objects.WebReportManipulation;
import com.microstrategy.web.objects.WebReportSource;
import com.microstrategy.web.objects.WebRow;
import com.microstrategy.web.objects.WebRowValue;
import com.microstrategy.web.objects.WebTemplate;
import com.microstrategy.web.objects.WebTitle;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.microstrategy.webapi.EnumDSSXMLAxisName;
import com.microstrategy.webapi.EnumDSSXMLDocSaveAsFlags;
import com.microstrategy.webapi.EnumDSSXMLExecutionFlags;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLResultFlags;
import com.xin.portal.system.model.BiUser;

public class CreatorApi {
	
	private static Logger logger = LoggerFactory.getLogger(CreatorApi.class);

	public static void main(String[] args) {
		try {
			BiUser project = new BiUser("192.168.1.175","MicroStrategy Tutorial","0","administrator","");
//			String id = createReport(project,"我的新报表1");
//			String id = createDocument(project,"我的新文档");
//			String id = FolderCreationSample.createNewFolder("edu_test");
//			create();
			String id = createDoss(project,"我的新Doss");
			//11212306478E3C366B04B09441E17B64
			//System.out.println("===id==="+id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String createReport(BiUser biUser, String reportName) {
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName(biUser.getProjectName());
		serverSession.setLogin(biUser.getUsername());
		serverSession.setPassword(biUser.getPassword());
		serverSession.setServerName(biUser.getServerName());
		serverSession.setServerPort(Integer.parseInt(biUser.getServerPort()));
		try {
			logger.info(biUser.toString());
			WebObjectSource objectSource = objectFactory.getObjectSource();
			WebReportSource reportSource = objectFactory.getReportSource();
			reportSource.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionResolve);
			reportSource.setResultFlags(
					EnumDSSXMLResultFlags.DssXmlResultWorkingSet | EnumDSSXMLResultFlags.DssXmlResultViewReport
					| EnumDSSXMLResultFlags.DssXmlResultStatusOnlyIfNotReady);
			// retrieve Blank Report for manipulation
			
			// 8154998B41AE3328BBB70692605904E4 
			WebReportInstance reportInstance = reportSource.getNewInstance("05B202B9999F4C1AB960DA6208CADF3D");
			reportInstance.setAsync(false);
			reportInstance.pollStatus();
			
//		// retrieve report template
			WebTemplate template = reportInstance.getTemplate();
//
//		// add Region and Subcategory to the template
			WebObjectInfo regionInfo = objectSource.getObject("8D679D4B11D3E4981000E787EC6DE8A4",
					EnumDSSXMLObjectTypes.DssXmlTypeAttribute);
			template.add(regionInfo, EnumDSSXMLAxisName.DssXmlAxisNameRows, 1);
			//template.clearTemplate();
			
			// apply changes
			WebReportManipulation rptManip = reportInstance.getReportManipulator();
			WebReportInstance newInst = null;
			rptManip.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionResolve);
			rptManip.setResultFlags(1268388256);
			newInst = rptManip.applyChanges();
			newInst.setAsync(false);
			newInst.pollStatus();
			
			// save report to Shared Reports folder
			String folderID = objectSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
			WebFolder myReports = (WebFolder) objectSource.getObject(folderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
			
			WebObjectInfo woi = newInst.saveAs(myReports, reportName);
			if (woi!=null) {
				return woi.getID();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSession!=null) serverSession.closeSession();
			} catch (WebObjectsException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String createDocument(BiUser mstrUser, String docName) throws WebObjectsException, WebBeanException {
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName(mstrUser.getProjectName());
		serverSession.setLogin(mstrUser.getUsername());
		serverSession.setPassword(mstrUser.getPassword());
		serverSession.setServerName(mstrUser.getServerName());
		serverSession.setServerPort(Integer.parseInt(mstrUser.getServerPort()));
		WebObjectSource objectSource = objectFactory.getObjectSource();
		WebReportSource reportSource = objectFactory.getReportSource();
		reportSource.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionResolve);
		reportSource.setResultFlags(
				EnumDSSXMLResultFlags.DssXmlResultWorkingSet | EnumDSSXMLResultFlags.DssXmlResultViewReport
						| EnumDSSXMLResultFlags.DssXmlResultStatusOnlyIfNotReady);
		// retrieve Blank Report for manipulation
		
		// 8154998B41AE3328BBB70692605904E4 
		RWBean rwb = (RWBean) WebBeanFactory.getInstance().newBean("RWBean");
		rwb.setSessionInfo(serverSession);
		rwb.setObjectID("8154998B41AE3328BBB70692605904E4");
		//System.out.println("name: "+rwb.getDisplayName());

		// Retrieve an RWInstance of the document
		RWInstance rwi = rwb.getRWInstance();
		rwi.setAsync(false);

		rwi.pollStatus();

		
		RWInstance newInst = rwi.getRWManipulator().applyChanges();
		rwi.setSaveAsFlags(EnumDSSXMLDocSaveAsFlags.DssXmlDocSaveAsOverwrite);

		String folderID = objectSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
		WebFolder parentFolder = (WebFolder) objectSource.getObject(folderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);

		WebObjectInfo WebObjectInfo = newInst.saveAs(parentFolder, docName);

		if (WebObjectInfo!=null) {
			return WebObjectInfo.getID();
		}
		return null;
	}
	
	public static String createDossier(BiUser mstrUser, String docName) throws WebObjectsException, WebBeanException {
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName(mstrUser.getProjectName());
		serverSession.setLogin(mstrUser.getUsername());
		serverSession.setPassword(mstrUser.getPassword());
		serverSession.setServerName(mstrUser.getServerName());
		serverSession.setServerPort(Integer.parseInt(mstrUser.getServerPort()));
		WebObjectSource objectSource = objectFactory.getObjectSource();
		WebReportSource reportSource = objectFactory.getReportSource();
		reportSource.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionResolve);
		reportSource.setResultFlags(
				EnumDSSXMLResultFlags.DssXmlResultWorkingSet | EnumDSSXMLResultFlags.DssXmlResultViewReport
						| EnumDSSXMLResultFlags.DssXmlResultStatusOnlyIfNotReady);
		// retrieve Blank Report for manipulation
		
		// 8154998B41AE3328BBB70692605904E4 
		RWBean rwb = (RWBean) WebBeanFactory.getInstance().newBean("RWBean");
		rwb.setSessionInfo(serverSession);
		rwb.setObjectID("11212306478E3C366B04B09441E17B64");
		//System.out.println("name: "+rwb.getDisplayName());

		// Retrieve an RWInstance of the document
		RWInstance rwi = rwb.getRWInstance();
		rwi.setAsync(false);

		rwi.pollStatus();

		
		RWInstance newInst = rwi.getRWManipulator().applyChanges();
		rwi.setSaveAsFlags(EnumDSSXMLDocSaveAsFlags.DssXmlDocSaveAsOverwrite);

		String folderID = objectSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
		WebFolder parentFolder = (WebFolder) objectSource.getObject(folderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);

		WebObjectInfo WebObjectInfo = newInst.saveAs(parentFolder, docName);

		if (WebObjectInfo!=null) {
			return WebObjectInfo.getID();
		}
		return null;
	}
	
	
	private static void transformResults(WebReportInstance reportInstance) {
		 
		  try {
		   WebReportData results = reportInstance.getResults();
		  
		   WebReportGrid reportGrid = results.getWebReportGrid();
		   //System.out.println("Report returned with "+reportGrid.getGridRows().size()+" rows");
		       
		         //Code to display Row Title.
		         WebGridTitles rowTitles = reportGrid.getRowTitles();
		   Enumeration rowTitlesEnumeration = rowTitles.elements();
		   while (rowTitlesEnumeration.hasMoreElements()){
		    WebTitle rowTitle = (WebTitle) rowTitlesEnumeration.nextElement();
		    /*try {
		     //System.out.print(rowTitle.getWebTemplateUnit().getName());
		    } catch (WebObjectsException e) {
		     e.printStackTrace();
		    }*/
		   }
		       
		   // Display column headers - names of template units placed in columns
		         WebGridRows gridRows=reportGrid.getGridRows();
		         WebGridHeaders columnHeaders=reportGrid.getColumnHeaders();
		 
		         for (int i=0;i<columnHeaders.size();i++) {
		             for (int j=0;j<reportGrid.getRowTitles().size();j++) {
		                 //System.out.print("\t");
		              }
		             WebHeaders headers=columnHeaders.get(i);
		             for (int j=0;j<headers.size();j++) {
		                 WebHeader header=headers.get(j);
		                // System.out.print(header.getDisplayName()+"\t");
		                }
		            // System.out.print("\n");
		            }
		 
		         for (int i=0;i<gridRows.size();i++) {
		             WebRow row=gridRows.get(i);
		             WebHeaders rowHeaders=row.getHeaderElements();
		             for (int j=0;j<rowHeaders.size();j++) {
		                 WebHeader header=rowHeaders.get(j);
		                 //System.out.print(header.getDisplayName()+"\t");
		                }
		 
		             for (int j=0;j<row.size();j++) {
		                 WebRowValue value=row.get(j);
		                 value.getRawValue();
		                 //System.out.print(value.getValue());
		               }
		             System.out.print("\n");
		         }
		         System.out.print("\n");
		       
		  
		  }catch (Exception e) {
		   e.printStackTrace();
		  }
	}
	
	public static String createDoss(BiUser mstrUser, String reportName) {
		try {
			logger.info(mstrUser.toString());
			WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
			WebIServerSession serverSession = objectFactory.getIServerSession();
			serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
			serverSession.setProjectName(mstrUser.getProjectName());
			serverSession.setLogin(mstrUser.getUsername());
			serverSession.setPassword(mstrUser.getPassword());
			serverSession.setServerName(mstrUser.getServerName());
			serverSession.setServerPort(Integer.parseInt(mstrUser.getServerPort()));
			WebObjectSource objectSource = objectFactory.getObjectSource();
			
			
			
			
			
			WebReportSource reportSource = objectFactory.getReportSource();
			reportSource.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionResolve);
			reportSource.setResultFlags(
					EnumDSSXMLResultFlags.DssXmlResultWorkingSet | EnumDSSXMLResultFlags.DssXmlResultViewReport
					| EnumDSSXMLResultFlags.DssXmlResultStatusOnlyIfNotReady);
			// retrieve Blank Report for manipulation
			
			// 8154998B41AE3328BBB70692605904E4 
			WebReportInstance reportInstance = reportSource.getNewInstance("11212306478E3C366B04B09441E17B64");
			reportInstance.setAsync(false);
			reportInstance.pollStatus();
			
//		// retrieve report template
			WebTemplate template = reportInstance.getTemplate();
//
//		// add Region and Subcategory to the template
			WebObjectInfo regionInfo = objectSource.getObject("8D679D4B11D3E4981000E787EC6DE8A4",
					EnumDSSXMLObjectTypes.DssXmlTypeAttribute);
			template.add(regionInfo, EnumDSSXMLAxisName.DssXmlAxisNameRows, 1);
			//template.clearTemplate();
			
			// apply changes
			WebReportManipulation rptManip = reportInstance.getReportManipulator();
			WebReportInstance newInst = null;
			rptManip.setExecutionFlags(EnumDSSXMLExecutionFlags.DssXmlExecutionResolve);
			rptManip.setResultFlags(1268388256);
			newInst = rptManip.applyChanges();
			newInst.setAsync(false);
			newInst.pollStatus();
			
			// save report to Shared Reports folder
			String folderID = objectSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNameProfileReports);
			WebFolder myReports = (WebFolder) objectSource.getObject(folderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
			
			WebObjectInfo woi = newInst.saveAs(myReports, reportName);
			if (woi!=null) {
				return woi.getID();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return null;
	}

}
