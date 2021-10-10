package com.xin.portal.bi.mstr.api;

import java.util.HashMap;
import java.util.Map;

import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.rw.RWInstance;
import com.microstrategy.web.objects.rw.RWSource;
import com.microstrategy.webapi.EnumDSSXMLObjectFlags;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.mstr.MstrSessionFactory;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiUser;

public class ReportApi {
	
	public static String HAVESUCCESS = "0";//成功
	public static String HAVENOREPORTTEMPLATE = "-2";//为获取session或者为获取到报表模版
	public static String GETINSTANCEFAIL = "-3";//获取报表示例发生错误
	public static String GETFOLDERFAIL = "-4";//获取目标文件夹失败
	public static String ILLEGALARGUMENTFAIL = "-5";//类型转换失败
	public static String SAVEREPORTTOFOLDERFAIL = "-6";//保存报表到目标文件夹失败
	public static String REPORTID = "reporID";
	
	public static Map<String, String> CopyReportOrDocumentOrLibraryToOtherFolder(BiProject project,BiUser biUser,String reportId,String folderId,String reportName,String reportDesc){
		Map<String, String> result = new HashMap<String, String>();
		String uid = biUser.getUsername()!=null?biUser.getUsername():project.getDefaultUid();
		String pwd = biUser.getPassword()!=null?biUser.getPassword():project.getDefaultPwd();
		WebIServerSession session = MstrSessionFactory.getSession(project, uid, pwd);
		WebObjectsFactory factory = session.getFactory();
		RWSource rwSource = factory.getRWSource();
		WebObjectSource objsrc = factory.getObjectSource();
		objsrc.setFlags(EnumDSSXMLObjectFlags.DssXmlObjectFindHidden);
		// Get the template object of the document whose ID can be got from
		// desktop by right clicking the Blank document and selecting
		// properties in Desktop at the path Project - Object Templates -
		// Documents.In order to see the object templates folder enable the
		// option to display hidden object in desktop under Tools - Desktop
		// Preferences - Desktop - Browsing
		WebObjectInfo objInfo = null;
		try {
			objInfo = objsrc.getObject(reportId,
					EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition);
			objInfo.populate();
		} catch (WebObjectsException e) {
			result.put("code", ReportApi.HAVENOREPORTTEMPLATE);
			e.printStackTrace();
			return result;
		}
		String docId = objInfo.getID();
		RWInstance rwInst = null;
		try {
			rwInst = rwSource.getNewInstance(docId);
			rwInst.pollStatus();
		} catch (WebObjectsException e) {
			result.put("code", ReportApi.GETINSTANCEFAIL);
			e.printStackTrace();
			return result;
		}
		// Get the folder object by passing the object ID
		WebFolder folder = null;
		try {
			folder = (WebFolder) objsrc.getObject(folderId,
					EnumDSSXMLObjectTypes.DssXmlTypeFolder);
			folder.populate();
		} catch (WebObjectsException e) {
			result.put("code", ReportApi.GETFOLDERFAIL);
			e.printStackTrace();
			return result;
		} catch (IllegalArgumentException e) {
			result.put("code", ReportApi.ILLEGALARGUMENTFAIL);
			e.printStackTrace();
			return result;
		}
		// Save the document under the required folder
		try {
			WebObjectInfo newReport = rwInst.saveAs(folder, reportName, reportDesc);
			String newID = newReport.getID();
			result.put(ReportApi.REPORTID, newID);
		} catch (WebObjectsException e) {
			result.put("code", ReportApi.SAVEREPORTTOFOLDERFAIL);
			e.printStackTrace();
			return result;
		}
		MstrSessionFactory.closeSession(session);//close session
		result.put("code", ReportApi.HAVESUCCESS);
		return result;
	}
	
	
	
	
}
