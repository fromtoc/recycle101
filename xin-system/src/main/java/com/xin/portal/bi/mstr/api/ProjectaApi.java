/**   
* @Title: ProjectaAPI.java 
* @Package com.xin.portal.bi.mstr 
* @Description: TODO
* @author zhoujun 
* @date 2018年12月17日 下午1:34:56 
* @version V1.0   
*/
package com.xin.portal.bi.mstr.api;
import com.google.common.collect.Lists;
import com.microstrategy.web.objects.*;
import com.microstrategy.webapi.EnumDSSXMLAuthModes;
import com.microstrategy.webapi.EnumDSSXMLProjectStatus;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/** @ClassPath: com.xin.portal.bi.mstr.ProjectaAPI 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年12月17日 下午1:34:56 
 */
public class ProjectaApi {
	
	private static Logger logger = LoggerFactory.getLogger(ProjectaApi.class);
	
	public static List<BiProject> list(BiServer server){
		logger.info(server.toString());
		WebObjectsFactory objectFactory = WebObjectsFactory.getInstance();
		WebIServerSession serverSession = objectFactory.getIServerSession();
		serverSession.setAuthMode(EnumDSSXMLAuthModes.DssXmlAuthStandard);
		serverSession.setLogin(server.getDefaultUid());
		serverSession.setPassword(server.getDefaultPwd());
		serverSession.setServerName(server.getIp());
		serverSession.setServerPort(Integer.parseInt(server.getPort()));
		
		List<BiProject> list = Lists.newArrayList();
		
		try {
			WebProjectSource oProjectSource = objectFactory.getProjectSource();
			
			WebProjectInstances oPInstanceList = oProjectSource.getAccessibleProjectsInCluster();
			
			WebProjectInstance oPInstance = null;
			
			int _pindex = 0;
			for(int i=0;i<oPInstanceList.size();i++){
	
				oPInstance = oPInstanceList.get(i);
		
				if (oPInstance.getStatus() == EnumDSSXMLProjectStatus.DssXmlProjectStatusActive){
		
					_pindex = i+1;
			
					logger.info("Project "+ _pindex +": " + oPInstance.getProjectName());
			
					logger.info(oPInstance.getProjectDescription());
					BiProject biProject = new BiProject();
					biProject.setBiServerId(server.getId());
					biProject.setType(BiServer.TYPE_MSTR);
					biProject.setTitle(oPInstance.getProjectName());
					biProject.setProject(oPInstance.getProjectName());
					biProject.setId(String.valueOf(oPInstance.getID()));
					list.add(biProject);
		
				}
	
			} 
		}catch (Exception e){

			logger.error("Error: "+ e.getMessage());

		}
        
        return list; 
	}




	public static void main(String[] args) {

		BiServer biServer = new BiServer();
		biServer.setId("1078599653123579905");
		biServer.setPort("0");
		biServer.setDefaultUid("administrator");
		biServer.setDefaultPwd("");
		biServer.setIp("192.168.1.175");
		
		list(biServer);
		logger.info("end...");
	}

}
