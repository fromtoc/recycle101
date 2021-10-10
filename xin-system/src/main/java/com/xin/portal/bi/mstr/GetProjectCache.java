package com.xin.portal.bi.mstr;

import java.util.Locale;

import com.microstrategy.utils.xml.XMLBuilder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.admin.WebObjectsAdminException;
import com.microstrategy.web.objects.admin.monitors.CacheResults;
import com.microstrategy.web.objects.admin.monitors.CacheSource;
import com.microstrategy.web.objects.admin.monitors.Caches;
import com.microstrategy.web.objects.admin.monitors.EnumWebMonitorType;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLLevelFlags;

public class GetProjectCache {
	
	public static void main(String[] args) {
		processRequest();
	}

	public static void processRequest() {
		System.out.println("******************getCachesTask*******************");
		XMLBuilder xmlbuilder = new XMLBuilder();
		xmlbuilder.addChild("root");
		try {
			// Create session
			WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
			WebIServerSession serverSession = objectFactory.getIServerSession();
			serverSession.setServerName("192.168.1.150");
			serverSession.setProjectName("MicroStrategy Tutorial");
			serverSession.setServerPort(0);
			serverSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
			serverSession.setLogin("administrator");
			serverSession.setPassword("");
			serverSession.setLocale(Locale.CHINESE);
			serverSession.getSessionID();

			// Open XML tag for serverSummary
			xmlbuilder.addChild("cacheManager");

			// Get caches
			CacheSource cacheSource = (CacheSource) objectFactory
					.getMonitorSource(EnumWebMonitorType.WebMonitorTypeCache);
			cacheSource.setLevel(EnumDSSXMLLevelFlags.DssXmlDetailLevel);
			CacheResults results;
			results = cacheSource.getCaches();
			int cacheCount = results.getCount();
			System.out.println("Total number of caches: " + cacheCount);

			for (int j = 0; j < results.size(); j++) {
				// Caches are group on project level, so get the cache
				// collection for each project
				Caches result = results.get(j);
				System.out.println(result.getCount() + " caches for project: " + result.getProjectDSSID() + ":" + result.getProjectName());

				xmlbuilder.addAttribute(result.getProjectName(), result.getCount());
			}

		} catch (WebObjectsAdminException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		xmlbuilder.closeAll();
		System.out.println(xmlbuilder);

		System.out.println("******************getCachesTask*******************");
	}

}
