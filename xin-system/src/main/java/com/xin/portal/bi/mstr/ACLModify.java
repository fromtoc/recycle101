package com.xin.portal.bi.mstr;

import java.util.Locale;

import com.microstrategy.web.objects.WebAccessControlList;
import com.microstrategy.web.objects.WebAttribute;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectInfo;
import com.microstrategy.web.objects.WebObjectSecurity;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.webapi.EnumDSSXMLAccessEntryType;
import com.microstrategy.webapi.EnumDSSXMLAccessRightFlags;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLObjectFlags;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;

public class ACLModify {

	private static WebObjectsFactory factory = null;
	private static WebIServerSession serverSession = null;

	public static void main(String[] args) throws WebObjectsException {
		factory = WebObjectsFactory.getInstance();
		serverSession = factory.getIServerSession();
		serverSession.setServerName("192.168.1.175"); //IServer name
		serverSession.setServerPort(0);
		serverSession.setProjectName("MicroStrategy Tutorial"); //Project to create the session
		serverSession.setLogin("administrator"); //UserID
		serverSession.setPassword(""); //Password
		serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp); //change source for userconnection - Desktop
		serverSession.setLocale(Locale.CHINESE);
		//Initialize the session with the above parameters
		WebObjectSource objSource = serverSession.getFactory().getObjectSource();
		int flags = objSource.getFlags();
		objSource.setFlags(flags | EnumDSSXMLObjectFlags.DssXmlObjectTotalObject);
		
//		WebObjectInfo woi = objSource.getObject("B210375B4FE2F6B293B25BAEE65EB58D", EnumDSSXMLObjectTypes.DssXmlTypeFolder, true);
		WebFolder folder = (WebFolder) objSource.getObject("D3C7D461F69C4610AA6BAA5EF51F4125",
				EnumDSSXMLObjectTypes.DssXmlTypeFolder);
//		WebFolder attribute = (WebFolder) woi;
		folder.populate();
		System.out.println(folder.getDisplayName());
		System.out.println(folder.getName());
		WebObjectSecurity wos = folder.getSecurity();
		WebAccessControlList acl = wos.getACL();

		for (int i = 0; i < acl.size(); i++) {
			//gets the users/group under Security for this object

			System.out.println("Trustee " + acl.get(i).getTrustee().getDisplayName());

			//get the rights for each user/group
			System.out.println("Rights " + acl.get(i).getRights());

			//change acl to modify for all group/users

//			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightBrowse);
//			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightControl);
//			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightDelete);
//			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightExecute);
			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightFullControl);
//			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightRead);
//			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightUse);
//			acl.get(i).setRights(EnumDSSXMLAccessRightFlags.DssXmlAccessRightWrite);
			objSource.save(folder);

		}
		serverSession.closeSession();

	}
}
