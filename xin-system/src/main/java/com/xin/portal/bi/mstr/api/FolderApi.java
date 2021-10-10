package com.xin.portal.bi.mstr.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microstrategy.web.objects.WebAccessControlList;
import com.microstrategy.web.objects.WebDisplayUnits;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSecurity;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.webapi.EnumDSSXMLAccessRightFlags;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.microstrategy.webapi.EnumDSSXMLFolderNames;
import com.microstrategy.webapi.EnumDSSXMLObjectSubTypes;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.mstr.MstrSessionFactory;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;

public class FolderApi {
	private static Logger logger = LoggerFactory.getLogger(FolderApi.class);

	public static void main(String[] args) {
		try {
			BiUser project = new BiUser	("192.168.1.175","MicroStrategy Tutorial","0","ces1","123456");
//			String id = createReport(project,"我的新报表1");
//			String id = createDocument(project,"我的新文档");
//			String id = FolderCreationSample.createNewFolder("edu_test");
//			create();
//			String id = createFolder(project,"ces01");
			//11212306478E3C366B04B09441E17B64
//			System.out.println("===id==="+id);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String createFolder(BiUser mstrUser, String folderName){
		logger.info(mstrUser.toString());
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName(mstrUser.getProjectName());
		serverSession.setLogin(mstrUser.getUsername());
		serverSession.setPassword(mstrUser.getPassword());
		serverSession.setServerName(mstrUser.getServerName());
		serverSession.setServerPort(Integer.parseInt(mstrUser.getServerPort()));
        
        return createFolder(serverSession,folderName); 
	}
	
	public static List<BiUser> createFolderBatch(BiServer biServer, List<BiUser> biUsers){
		for (BiUser biUser : biUsers) {
			WebIServerSession serverSession = MstrSessionFactory.getSession(biServer,biUser.getUsername(),biUser.getPassword());
			String foldId = createFolder(serverSession,biUser.getUsername());
			biUser.setOwnFolderId(foldId);
		}
		//WebIServerSession serverSession = MstrSessionFactory.getSession(biServer);
		return biUsers;
	}
	
	//目前是在共享报表中添加文件夹的，初始要在developer中给“报表”的“安全属性”添加everyone的完全控制权限。
	public static String createFolder(WebIServerSession serverSession, String folderName){
		WebFolder newFolder = null;
		
		WebObjectSource objSource = serverSession.getFactory().getObjectSource();

        try {
            // Obtain ID for Shared Reports Folder and populate WebFolder
            // Setting
        	String sharedReportsFolderID = objSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
        	WebFolder folder = (WebFolder) objSource.getObject(sharedReportsFolderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);

            // Obtain new folder from WebObjectSource 
            newFolder = (WebFolder)objSource.getNewObject(EnumDSSXMLObjectTypes.DssXmlTypeFolder, folderName);
            
            
            // Save new folder under "Shared Reports" folder
            objSource.saveAs(newFolder, newFolder.getName(), folder, true);
            
            WebFolder folderACL = (WebFolder) objSource.getObject(newFolder.getID(), EnumDSSXMLObjectTypes.DssXmlTypeFolder);
            
            folderACL.populate();
    		WebObjectSecurity wos = folderACL.getSecurity();
    		
    		WebAccessControlList acl = wos.getACL();
    		//acl.add(EnumDSSXMLAccessRightFlags.DssXmlAccessRightFullControl);
    		for (int i = 0; i < acl.size(); i++) {
    			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightFullControl);
    			objSource.save(folderACL);
    		}
        } catch (WebObjectsException ex) {
            System.out.println("Error while creating new folder " + folderName + ": " + ex.getMessage());
        }finally {
        	MstrSessionFactory.closeSession(serverSession);
		}
        logger.info("new folder id: {}",newFolder.getID());
        return newFolder.getID(); 
	}
	
	public static String createFolder(BiProject biProject, String folderName){
		WebIServerSession serverSession = MstrSessionFactory.getSession(biProject);        
        return createFolder(serverSession,folderName); 
	}
	
	public static String searchFolder(BiProject biProject, String folderName){
		WebIServerSession serverSession = MstrSessionFactory.getSession(biProject);
		//Create internal variables
        WebFolder folder = null;
        String folderId = null;
        //Create a session using
        WebObjectSource objSource = serverSession.getFactory().getObjectSource();

        try {
            //Obtain ID for Shared Reports Folder and populate WebFolder
            //Setting
        	String sharedReportsFolderID = objSource.getFolderID(EnumDSSXMLFolderNames.DssXmlFolderNamePublicReports);
            folder = (WebFolder) objSource.getObject(sharedReportsFolderID, EnumDSSXMLObjectTypes.DssXmlTypeFolder);
            //Fetch Contents from Intelligence Server
            folder.populate();
            //If the folder has any contents, display them
            if (folder.size() > 0) {
                //Extract folder contents
                WebDisplayUnits units = folder.getChildUnits();
                if (units != null) {
                    for (int i = 0; i < units.size(); i++) {
                    	if(EnumDSSXMLObjectTypes.DssXmlTypeFolder==units.get(i).getDisplayUnitType() && folderName.equals(units.get(i).getDisplayName())){
                    		folderId = units.get(i).getID();
                    		System.out.println(folderId);
                    	}
                    }
                }
            }
        } catch (WebObjectsException ex) {
        	logger.error("创建文件夹失败");
            System.out.println("\nError while fetching folder contents: " + ex.getMessage());
        }finally {
        	MstrSessionFactory.closeSession(serverSession); 
		}
        return folderId;
	}

}
