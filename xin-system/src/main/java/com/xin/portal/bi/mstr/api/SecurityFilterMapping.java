package com.xin.portal.bi.mstr.api;

import java.sql.ResultSet;

import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebProjectInstance;
import com.microstrategy.web.objects.admin.users.WebMDSecurityFilter;
import com.microstrategy.web.objects.admin.users.WebUserEntity;
import com.microstrategy.web.objects.admin.users.WebUserSearch;
import com.microstrategy.web.objects.admin.users.WebUserServicesSource;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLObjectFlags;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;

public class SecurityFilterMapping {

	public static void main(String arg[]) {
	
		WebObjectsFactory wof = WebObjectsFactory.getInstance();
		WebIServerSession wiss = wof.getIServerSession();
		WebObjectSource wos = wof.getObjectSource();
		
		wos.setFlags(EnumDSSXMLObjectFlags.DssXmlObjectTotalObject);

		try {
			// Creating the connection to the Intelligence server
			wiss.setServerName("192.168.1.175");
			wiss.setServerPort(0);
			wiss.setProjectName("MicroStrategy Tutorial");
			wiss.setLogin("administrator");
			wiss.setPassword("");
			wiss.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);

			// Instantiating the WebUserSearch object
			WebUserServicesSource wuss = wos.getUserServicesSource();
			WebUserSearch wuSearch = wuss.getNewUserSearchObject();
			
			// Instantiating the WebProjectInstance that represents the project for the search
			WebProjectInstance project = wof.getProjectSource().getProjects().itemByName("MicroStrategy Tutorial");
			
			// Instantiating the WebMDSecurityFilter that represents the security filter
			WebMDSecurityFilter securityFilter = (WebMDSecurityFilter) wos.getObject("C82C6B1011D2894CC0009D9F29718E4F", EnumDSSXMLObjectTypes.DssXmlTypeMDSecurityFilter);

			// Setting the parameters for the search
			wuSearch.setProject(project);
			wuSearch.setSecurityFilter(securityFilter);

			WebUserEntity[] webUsers = wuSearch.getResults();

			// Printing the list of users/groups that are using that security filter in that project
			for (int i = 0; i < webUsers.length; i++) {
				//System.out.println(webUsers[i].getName());
				
				//To check if returned is a User(8704) or User Group(8705)
				//System.out.println(webUsers.getSubType());
			}
			// Closing the existing session
			wiss.closeSession();

		} catch (Exception e) {
			e.printStackTrace();
		}
		//System.out.println("end");
	}
}
