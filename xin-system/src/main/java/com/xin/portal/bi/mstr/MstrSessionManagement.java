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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microstrategy.web.app.ProjectInfo;
import com.microstrategy.web.app.beans.ProjectBrowser;
import com.microstrategy.web.objects.WebDisplayUnit;
import com.microstrategy.web.objects.WebDisplayUnits;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import com.microstrategy.webapi.EnumDSSXMLObjectSubTypes;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.mstr.api.ExportApi;
import com.xin.portal.system.bean.TreeNode;
import com.xin.portal.system.model.BiProject;

/**
 * 
 * <p>
 * Title: SessionManagementSample
 * </p>
 * <p>
 * Description: Helper class to manage a MicroStrategy Session.
 * </p>
 * <p>
 * Company: Microstrategy, Inc.
 * </p>
 */
public class MstrSessionManagement {
	
	private static Logger logger = LoggerFactory.getLogger(MstrSessionManagement.class);
	
	/**
	 * Creates a new session that can be reused by other classes
	 * @param parentId 
	 * 
	 * @return WebIServerSession
	 */
	

	public static List<TreeNode> getMenuFolder(BiProject project, String parentId) {
		WebIServerSession webIServerSession = MstrSessionFactory.getSession(project);
		WebObjectSource objSource = webIServerSession.getFactory().getObjectSource();

			try {
				if (parentId.equals("0")) {
					parentId = objSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
					
				}
			} catch (WebObjectsException e) {
				e.printStackTrace();
			}
			return getFolderList(objSource, parentId, webIServerSession);
	}
	
//	static int currenId = 0;

	private static List<TreeNode> getFolderList(WebObjectSource objSource,
			 String folderId, WebIServerSession webIServerSession) {
		
		List<TreeNode> list = new ArrayList<TreeNode>();
		//Create internal variables
		try {
            //Obtain ID for Shared Reports Folder and populate WebFolder
            //Setting
			
			WebFolder folder = (WebFolder) objSource.getObject(folderId, EnumDSSXMLObjectTypes.DssXmlTypeFolder);

            //Set number of elements to fetch
			
//            folder.setBlockBegin(1);
//            folder.setBlockCount(100);

            //Fetch Contents from Intelligence Server
            folder.populate();

            //If the folder has any contents, display them
            if (folder.size() > 0) {
                //Extract folder contents
                WebDisplayUnits units = folder.getChildUnits();
                if (units != null) {
                    for (int i = 0; i < units.size(); i++) {
                    	logger.info("children:: {} | {}",units.get(i).getDisplayUnitType(),units.get(i).getDisplayName());
                    	if (EnumDSSXMLObjectTypes.DssXmlTypeFolder == units.get(i).getDisplayUnitType()) {
                    		list.add(initNode(units.get(i),folderId,"Folder",units.get(i).getDisplayUnitType()));
                    	} else if (EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition == units.get(i).getDisplayUnitType()) {
                    		list.add(initNode(units.get(i),folderId,"Report",units.get(i).getDisplayUnitType()));
                    	} else if (EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition == units.get(i).getDisplayUnitType()) {
                    		WebObjectInfo o = objSource.getObject(units.get(i).getID(),EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition,true);
                    		if (o.getViewMediaSettings().getDefaultMode()==8192) {//dossier
                    			list.add(initNode(units.get(i),folderId,"Dossier",units.get(i).getDisplayUnitType()));
                    			
                    		} else if (o.getViewMediaSettings().getDefaultMode()==1) {//document
                    			list.add(initNode(units.get(i),folderId,"Document",units.get(i).getDisplayUnitType()));

                    		}
                    		logger.info("document info {} {} {}",units.get(i).getID(),o.getSubType(),o.getViewMediaSettings().getDefaultMode());
                    	}
                    	
                    	
//                    	if (CacheManager.get(units.get(i).getDisplayUnitType())!=null) {
//                    		list.add(initNode(units.get(i),folderId,(String)CacheManager.get(String.valueOf(units.get(i).getDisplayUnitType())),units.get(i).getDisplayUnitType()));
//                    	}
                    	
                    }
                }

                
            }
        } catch (WebObjectsException ex) {
            ex.printStackTrace();
        }
		MstrSessionFactory.closeSession(webIServerSession);
		return list;
	}
	
	
	private static TreeNode initNode (WebDisplayUnit unit, String folderId, String typeName, int typeValue) {
		TreeNode node = new TreeNode(unit.getID(), unit.getDisplayName(),folderId);
		node.putAttribute("typeName", typeName);
		node.putAttribute("typeValue", String.valueOf(typeValue));
		
		switch (typeName) {
	      case "Folder":
	    	  node.setIsParent(true);
	    	  node.setNocheck(true);
	          break;
	
	      case "Report":
	          //objectType = "Report";
	    	  node.setIcon("../../images/Report.png");
	          break;
	
	      case "Document":
	          //objectType = "Document";
	          //node.setIcon();
	          break;
	      case "Dossier":
	    	  node.setIcon("../../images/Dossier.png");
	          //objectType = "Document";
	          //node.setIcon();
	          break;
//	
//	      case EnumDSSXMLObjectTypes.DssXmlTypeFilter:
//	          objectType = "Filter";
//	          list.add(initNode(units.get(i),folderId,"Report",EnumDSSXMLObjectTypes.DssXmlTypeFolder));
//	          break;
//	
//	      case EnumDSSXMLObjectTypes.DssXmlTypeTemplate:
//	          objectType = "Template";
//	          list.add(initNode(units.get(i),folderId,"Report",EnumDSSXMLObjectTypes.DssXmlTypeFolder));
//	          break;
	      }
		return node;
	}


	public static void main(String args[]) {
		// getSession();
	}

}
