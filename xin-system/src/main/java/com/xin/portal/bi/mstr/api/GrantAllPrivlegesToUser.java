package com.xin.portal.bi.mstr.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebSearch;
import com.microstrategy.web.objects.admin.users.WebPrivilegeCategories;
import com.microstrategy.web.objects.admin.users.WebPrivilegeCategory;
import com.microstrategy.web.objects.admin.users.WebPrivilegeEntry;
import com.microstrategy.web.objects.admin.users.WebUser;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.microstrategy.webapi.EnumDSSXMLObjectSubTypes;
import com.microstrategy.webapi.EnumDSSXMLSearchDomain;
import com.xin.portal.system.model.BiUser;

public class GrantAllPrivlegesToUser {
	
	private static Logger logger = LoggerFactory.getLogger(CreatorApi.class);

	public static void main(String[] args) {
		try {
			BiUser project = new BiUser("192.168.1.175","MicroStrategy Tutorial","0","administrator","");
//			String id = createReport(project,"我的新报表1");
//			String id = createDocument(project,"我的新文档");
//			String id = FolderCreationSample.createNewFolder("edu_test");
//			create();
			grant(project,"我的新Doss");
			//11212306478E3C366B04B09441E17B64
			//System.out.println("===ok===");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void grant(BiUser mstrUser, String reportName) {
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setProjectName(mstrUser.getProjectName());
		serverSession.setLogin(mstrUser.getUsername());
		serverSession.setPassword(mstrUser.getPassword());
		serverSession.setServerName(mstrUser.getServerName());
		serverSession.setServerPort(Integer.parseInt(mstrUser.getServerPort()));
		WebObjectSource  source =serverSession.getFactory().getObjectSource();
		WebSearch search = source.getNewSearchObject();

		search.setNamePattern("ces1");

		search.setAsync(false);
		search.types().add(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser);
		search.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainConfiguration);

		try {
			search.submit();
			WebFolder folder = search.getResults();
			if(folder.size()>0){

				WebUser user = (WebUser) folder.get(0);
				WebPrivilegeCategories cats = serverSession.getFactory().getObjectSource().getUserServicesSource().getPrivilegeCategories(user);
	              //Loop though all categories and privileges and grant all privileges
	              for (int i = 0; i < cats.size(); i++) {
	                  WebPrivilegeCategory cat = cats.get(i);
	                  String catName = cat.getName(); //Category Name
	                 // System.out.println("Privilege Category: " + catName);
	                  for (int j = 0; j < cat.size(); j++) {
	                      WebPrivilegeEntry privilegeEntry = cat.get(j);
	                      //System.out.println("      Privilege name: "+privilegeEntry.getName()+"  ---  "+privilegeEntry.getDescription());
	                      privilegeEntry.grant(); //Grant privilege
	                  }
	              }
	              source.save(user);
			}

		} catch (WebObjectsException e) {
			e.printStackTrace();
		}
	}

}
