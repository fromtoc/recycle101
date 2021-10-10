
package com.xin.portal.bi.mstr.api;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.microstrategy.web.beans.BeanFactory;
import com.microstrategy.web.beans.UserBean;
import com.microstrategy.web.beans.WebBeanException;
import com.microstrategy.web.objects.WebFolder;
import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectSource;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;
import com.microstrategy.web.objects.WebSearch;
import com.microstrategy.web.objects.admin.users.WebPrivilegeCategories;
import com.microstrategy.web.objects.admin.users.WebPrivilegeCategory;
import com.microstrategy.web.objects.admin.users.WebPrivilegeEntry;
import com.microstrategy.web.objects.admin.users.WebStandardLoginInfo;
import com.microstrategy.web.objects.admin.users.WebUser;
import com.microstrategy.web.objects.admin.users.WebUserEntity;
import com.microstrategy.webapi.EnumDSSXMLApplicationType;
import com.microstrategy.webapi.EnumDSSXMLObjectFlags;
import com.microstrategy.webapi.EnumDSSXMLObjectSubTypes;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.microstrategy.webapi.EnumDSSXMLSearchDomain;
import com.microstrategy.webapi.EnumDSSXMLSearchFlags;
import com.xin.portal.bi.mstr.MstrSessionFactory;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;

public class UserApi {
	
	private static Logger logger = LoggerFactory.getLogger(UserApi.class);
	
	public static void main(String[] args) {
		BiProject project = new BiProject();
		createUserAndGrant(project,"ces3", "123456", "ces3", "edu user", null);
	}

	
	public static boolean createUser(BiProject project,String loginName,String password,String fullName,String description) throws WebBeanException{
		return createUser(project,loginName,password, fullName, description, null);
	}
	
	public static boolean createUser(BiProject project,String loginName,String password,String fullName,String description,int pwdExpireDays) throws WebBeanException{
		return createUser(project,loginName,password, fullName, description,DateUtils.addDays(new Date(), pwdExpireDays));
	}
	
	public static boolean createUser(BiProject project,String loginName,String password,String fullName,String description,Date pwdExpireDay) {
		WebIServerSession serverSession = MstrSessionFactory.getSession(project);
		try {
			UserBean user = null;
			//Instantiate a new user
			user = (UserBean)BeanFactory.getInstance().newBean("UserBean");
			user.setSessionInfo(serverSession);
			user.InitAsNew();
			
			//Fetch properties for the user
			WebUser ua = (WebUser) user.getUserEntityObject();
			WebStandardLoginInfo loginInfo = ua.getStandardLoginInfo();
			
			//Set basic user information
			ua.setLoginName(loginName);
			ua.setFullName(fullName);
			user.getObjectInfo().setDescription(description);
			
			loginInfo.setPassword(password);
			if (pwdExpireDay==null) {
				loginInfo.setPasswordExpiresAutomatically(false); //If set to false, password never expires
			} else {
				loginInfo.setPasswordExpirationDate(pwdExpireDay);  //Password expires on pwdExpireDay
			}
			//loginInfo.setPasswordExpirationFrequency(90); //90 days to expire
			loginInfo.setStandardAuthAllowed(true); //The user can log in using standard auth
			
			user.save();
		} catch (Exception e) {
			
		} finally {
			try {
				if (serverSession!=null) {
					serverSession.closeSession();
				}
			} catch (WebObjectsException ex) {
				//System.out.println("Error closing session:" + ex.getMessage());
			}
		}
            
        return true;
	}
	
	public static String createUserAndGrant(BiProject project,String loginName,String password,String fullName,String description,Date pwdExpireDay) {
		WebIServerSession serverSession = MstrSessionFactory.getSession(project);
		WebObjectSource  source =serverSession.getFactory().getObjectSource();
		String userObjectId = null;
		try {
			UserBean user = null;
			//Instantiate a new user
			user = (UserBean)BeanFactory.getInstance().newBean("UserBean");
			user.setSessionInfo(serverSession);
			user.InitAsNew();
			
			//Fetch properties for the user
			WebUser ua = (WebUser) user.getUserEntityObject();
			WebStandardLoginInfo loginInfo = ua.getStandardLoginInfo();
			
			//Set basic user information
			ua.setLoginName(loginName);
			ua.setFullName(fullName);
			user.getObjectInfo().setDescription(description);
			
			loginInfo.setPassword(password);
			if (pwdExpireDay==null) {
				loginInfo.setPasswordExpiresAutomatically(false); //If set to false, password never expires
			} else {
				loginInfo.setPasswordExpirationDate(pwdExpireDay);  //Password expires on pwdExpireDay
			}
			//loginInfo.setPasswordExpirationFrequency(90); //90 days to expire
			loginInfo.setStandardAuthAllowed(true); //The user can log in using standard auth
			
			user.save();
			
			
			WebPrivilegeCategories cats = serverSession.getFactory().getObjectSource().getUserServicesSource().getPrivilegeCategories(ua);
            //Loop though all categories and privileges and grant all privileges
            for (int i = 0; i < cats.size(); i++) {
                WebPrivilegeCategory cat = cats.get(i);
                String catName = cat.getName(); //Category Name
            	//System.out.println("Privilege Category: " + catName);
            	for (int j = 0; j < cat.size(); j++) {
            		WebPrivilegeEntry privilegeEntry = cat.get(j);
            		//System.out.println("      Privilege name: "+privilegeEntry.getName()+"  ---  "+privilegeEntry.getDescription());
            		if ("Web Reporter".equalsIgnoreCase(catName) && "Web user".equalsIgnoreCase(privilegeEntry.getName())
//            				"Web save dossier".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web save to Shared Reports".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web create new report".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web Edit Dossier".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web create dossier".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web create dashboard".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web dashboard design".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web Edit Dashboard".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web save dashboard".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web manage objects".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web pivot report".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Access Data (Files) from Local, URL, DropBox, Google Drive, Sample Files, Clipboard".equalsIgnoreCase(privilegeEntry.getName())
        				) {
            			privilegeEntry.grant(); //Grant privilege
            		}
            		if ("Web Analyst".equalsIgnoreCase(catName)
//                				"Web save dossier".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web save to Shared Reports".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web create new report".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web Edit Dossier".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web create dossier".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web create dashboard".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web dashboard design".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web Edit Dashboard".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web save dashboard".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web manage objects".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web pivot report".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Access Data (Files) from Local, URL, DropBox, Google Drive, Sample Files, Clipboard".equalsIgnoreCase(privilegeEntry.getName())
            				
//                				 || "Web configure transaction".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web define advanced report options".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web define Intelligent Cube report".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web define MDX cube report".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web create HTML container".equalsIgnoreCase(privilegeEntry.getName())
//                				 || "Web save derived elements".equalsIgnoreCase(privilegeEntry.getName())
            				) {
            			privilegeEntry.grant(); //Grant privilege
            			//System.out.println("#grant#"+privilegeEntry.getName());
            		}
            		if ("Web Professional".equalsIgnoreCase(catName)
//            				&& ("Web save dossier".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web save to Shared Reports".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web create new report".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web Edit Dossier".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web create dossier".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web create dashboard".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web dashboard design".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web Edit Dashboard".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web save dashboard".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web manage objects".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Web pivot report".equalsIgnoreCase(privilegeEntry.getName())
//            				 || "Access Data (Files) from Local, URL, DropBox, Google Drive, Sample Files, Clipboard".equalsIgnoreCase(privilegeEntry.getName())
        				) {
	        			privilegeEntry.grant(); //Grant privilege
	        			//System.out.println("#grant#"+privilegeEntry.getName());
            		}
            		if ("Common Privileges".equals(catName) && 
            				("Create application objects".equals(privilegeEntry.getName())
							|| "Use OLAP services".equalsIgnoreCase(privilegeEntry.getName())
							|| "Create new folder".equalsIgnoreCase(privilegeEntry.getName())
							|| "Create schema objects".equalsIgnoreCase(privilegeEntry.getName())
							|| "Create shortcut to objects".equalsIgnoreCase(privilegeEntry.getName())
            			)) {
            				//System.out.println("#grant#"+privilegeEntry.getName());
							privilegeEntry.grant(); // Grant privilege
						}
					}
            }
            source.save(ua);
            userObjectId = ua.getID();
            logger.info("new mstr user id : {}",userObjectId);
            
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (serverSession!=null) {
					serverSession.closeSession();
				}
			} catch (WebObjectsException ex) {
				//System.out.println("Error closing session:" + ex.getMessage());
			}
		}
            
		return userObjectId;
	}
	
	public static String createUserAndGrant(BiServer biServer,BiUser biUser) {
		WebIServerSession serverSession = MstrSessionFactory.getSession(biServer);
		WebObjectSource  source = serverSession.getFactory().getObjectSource();
		try {
			UserBean user  = (UserBean)BeanFactory.getInstance().newBean("UserBean");
			user.setSessionInfo(serverSession);
			user.InitAsNew();
			
			//Fetch properties for the user
			WebUser ua = (WebUser) user.getUserEntityObject();
			WebStandardLoginInfo loginInfo = ua.getStandardLoginInfo();
			
			//Set basic user information
			ua.setLoginName(biUser.getUsername());
			ua.setFullName(biUser.getUsername());
			user.getObjectInfo().setDescription("import from demand portal at "+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			loginInfo.setPassword(biUser.getPassword());
			loginInfo.setStandardAuthAllowed(true); //The user can log in using standard auth
			user.save();
			
			WebPrivilegeCategories cats = serverSession.getFactory().getObjectSource().getUserServicesSource().getPrivilegeCategories(ua);
			//Loop though all categories and privileges and grant all privileges
			for (int i = 0; i < cats.size(); i++) {
				WebPrivilegeCategory cat = cats.get(i);
				String catName = cat.getName(); //Category Name
				//System.out.println("Privilege Category: " + catName);
				for (int j = 0; j < cat.size(); j++) {
					WebPrivilegeEntry privilegeEntry = cat.get(j);
					//System.out.println("      Privilege name: "+privilegeEntry.getName()+"  ---  "+privilegeEntry.getDescription());
					if ("Web Reporter".equalsIgnoreCase(catName)) {
						privilegeEntry.grant(); //Grant privilege
					}
					if ("Web Analyst".equalsIgnoreCase(catName)
							) {
						privilegeEntry.grant(); //Grant privilege
						//System.out.println("#grant#"+privilegeEntry.getName());
					}
					if ("Web Professional".equalsIgnoreCase(catName)
							) {
						privilegeEntry.grant(); //Grant privilege
						//System.out.println("#grant#"+privilegeEntry.getName());
					}
					//新添加了权限：Create new folder、Create schema objects、Create shortcut to objects、* Drill within Intelligent cube
					//分别为：创建文件夹、创建框架对象、创建快捷方式、在智能立方体内钻取。
					if ("Common Privileges".equals(catName) && 
							("Create application objects".equals(privilegeEntry.getName())
									|| "Use OLAP services".equalsIgnoreCase(privilegeEntry.getName())
									|| "Create new folder".equalsIgnoreCase(privilegeEntry.getName())
									|| "Create schema objects".equalsIgnoreCase(privilegeEntry.getName())
									|| "Create shortcut to objects".equalsIgnoreCase(privilegeEntry.getName())
							)) {
//						System.out.println("#grant#"+privilegeEntry.getName());
						privilegeEntry.grant(); // Grant privilege
					}
				}
			}
			source.save(ua);
			biUser.setBiObjectId(ua.getID());
			logger.info("new mstr user id : {}",biUser.getBiObjectId());
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		MstrSessionFactory.closeSession(serverSession); 
		return biUser.getBiObjectId();
	}
	
	
	public static List<BiUser> createUserAndGrantBatch(BiServer biServer,List<BiUser> biUsers) {
		WebIServerSession serverSession = MstrSessionFactory.getSession(biServer);
		WebObjectSource  source = serverSession.getFactory().getObjectSource();
		Iterator<BiUser> iterator = biUsers.iterator();
		while (iterator.hasNext()) {
			BiUser biUser = iterator.next();
			try {
				UserBean user  = (UserBean)BeanFactory.getInstance().newBean("UserBean");
				user.setSessionInfo(serverSession);
				user.InitAsNew();
				
				//Fetch properties for the user
				WebUser ua = (WebUser) user.getUserEntityObject();
				WebStandardLoginInfo loginInfo = ua.getStandardLoginInfo();
				
				//Set basic user information
				ua.setLoginName(biUser.getUsername());
				ua.setFullName(biUser.getUsername());
				user.getObjectInfo().setDescription("import from demand portal at "+DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				loginInfo.setPassword(biUser.getPassword());
				loginInfo.setStandardAuthAllowed(true); //The user can log in using standard auth
				user.save();
				
				WebPrivilegeCategories cats = serverSession.getFactory().getObjectSource().getUserServicesSource().getPrivilegeCategories(ua);
				//Loop though all categories and privileges and grant all privileges
				for (int i = 0; i < cats.size(); i++) {
					WebPrivilegeCategory cat = cats.get(i);
					String catName = cat.getName(); //Category Name
					//System.out.println("Privilege Category: " + catName);
					for (int j = 0; j < cat.size(); j++) {
						WebPrivilegeEntry privilegeEntry = cat.get(j);
						//System.out.println("      Privilege name: "+privilegeEntry.getName()+"  ---  "+privilegeEntry.getDescription());
						if ("Web Reporter".equalsIgnoreCase(catName)) {
							privilegeEntry.grant(); //Grant privilege
						}
						if ("Web Analyst".equalsIgnoreCase(catName)
								) {
							privilegeEntry.grant(); //Grant privilege
							//System.out.println("#grant#"+privilegeEntry.getName());
						}
						if ("Web Professional".equalsIgnoreCase(catName)
								) {
							privilegeEntry.grant(); //Grant privilege
							//System.out.println("#grant#"+privilegeEntry.getName());
						}
						if ("Common Privileges".equals(catName) && 
								("Create application objects".equals(privilegeEntry.getName())
										|| "Use OLAP services".equalsIgnoreCase(privilegeEntry.getName())
										|| "Create new folder".equalsIgnoreCase(privilegeEntry.getName())
										|| "Create schema objects".equalsIgnoreCase(privilegeEntry.getName())
										|| "Create shortcut to objects".equalsIgnoreCase(privilegeEntry.getName())
								)) {
							//System.out.println("#grant#"+privilegeEntry.getName());
							privilegeEntry.grant(); // Grant privilege
						}
					}
				}
				source.save(ua);
				biUser.setBiObjectId(ua.getID());
				logger.info("new mstr user id : {}",biUser.getBiObjectId());
				
				
			} catch (Exception e) {
				e.printStackTrace();
				if (e.getMessage().indexOf("该登录用户名已经在使用")>-1) {
					biUser.setState(e.getMessage());
				} else {
					iterator.remove();
				}
			}
		}
		MstrSessionFactory.closeSession(serverSession); 
		return biUsers;
	}
	
	public static boolean enable(BiProject project,String userId, boolean enabled){
		try {
			WebIServerSession serverSession = MstrSessionFactory.getSession(project);
			WebObjectSource  source =serverSession.getFactory().getObjectSource();
			WebUser user= (WebUser) source.getObject(userId, EnumDSSXMLObjectTypes.DssXmlTypeUser);
			user.populate(); //Replace the object with user's UID.
			user.setFlags(user.getFlags() | EnumDSSXMLObjectFlags.DssXmlObjectTotalObject );
			user.setEnabled(enabled);
			source.save(user);
			
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static JSONObject sync(BiServer biServer,BiUser biUser) {
		JSONObject result = new JSONObject();
		WebObjectsFactory factory = WebObjectsFactory.getInstance();
		WebIServerSession webIServerSession = factory.getIServerSession();
		webIServerSession.setServerName(biServer.getIp());
		webIServerSession.setServerPort(Integer.parseInt(biServer.getPort()));
		webIServerSession.setApplicationType(EnumDSSXMLApplicationType.DssXmlApplicationCustomApp);
		webIServerSession.setLogin(biUser.getUsername());
		webIServerSession.setPassword(biUser.getPassword());
		webIServerSession.setLocale(Locale.getDefault());
		
		
		try {
			webIServerSession.getSessionID();
			
		} catch (WebObjectsException e) {
			if (e.getMessage().indexOf("登录失败")>-1) {
				WebUser webUser = search(biServer, biUser.getUsername());
				if (webUser==null) {
					String biObjectId = createUserAndGrant(biServer, biUser);
					result.put("biObjectId", biObjectId);
					result.put("state", "Have synchronous");//result.put("state", "已同步");
				} else {
					result.put("state", "Incorrect MSTR account password");//result.put("state", "MSTR密码不正确");
				}
			}
			else {
				result.put("state", e.getMessage());
			}
			logger.error(e.getMessage());
			logger.error("{}	{}	 {}   {}   {}",
					new Object[] { biServer.getName(), biServer.getIp(), biServer.getPort(),biUser.getUsername(),biUser.getPassword() });
			return result;
		}
		MstrSessionFactory.closeSession(webIServerSession); 
		result.put("state", "normal");//result.put("state", "正常");
		return result;
	}
	
	public static WebUser search(BiServer biServer,String username) {
		WebIServerSession serverSession = MstrSessionFactory.getSession(biServer);
		WebObjectSource  source =serverSession.getFactory().getObjectSource();
		
		WebSearch userSearch = source.getNewSearchObject();
		
		//search for login id
		userSearch.setAbbreviationPattern(username);
		userSearch.setSearchFlags(userSearch.getSearchFlags() | EnumDSSXMLSearchFlags.DssXmlSearchAbbreviationWildCard);
        //asynchronized search
		userSearch.setAsync(false);
        //search for user
		userSearch.types().add(new Integer(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser));
        //search on repository
		userSearch.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainRepository);

		try {
			userSearch.submit();
			WebFolder folder = userSearch.getResults();
			if(folder.size()>0){
				if(folder.size()==1){
					return (WebUser)folder.get(0);
				}else{
					logger.info("Search returns more than 1 object, returning first object");
					return (WebUser)folder.get(0);
				}
			}
			//serverSession.closeSession();
		} catch (WebObjectsException ex) {
			logger.error("Error performing search: "+ex.getMessage());
			
		}
		MstrSessionFactory.closeSession(serverSession); 
		return null;
		
	}
	
	public static boolean updatePassword(BiServer biServer,BiUser biUser) {
		WebIServerSession serverSession = MstrSessionFactory.getSession(biServer);
		WebObjectSource  source =serverSession.getFactory().getObjectSource();
		
		WebSearch userSearch = source.getNewSearchObject();
		
		//search for login id
		userSearch.setAbbreviationPattern(biUser.getUsername());
		userSearch.setSearchFlags(userSearch.getSearchFlags() | EnumDSSXMLSearchFlags.DssXmlSearchAbbreviationWildCard);
        //asynchronized search
		userSearch.setAsync(false);
        //search for user
		userSearch.types().add(new Integer(EnumDSSXMLObjectSubTypes.DssXmlSubTypeUser));
        //search on repository
		userSearch.setDomain(EnumDSSXMLSearchDomain.DssXmlSearchDomainRepository);
		WebUser webUser = null;
		try {
			userSearch.submit();
			WebFolder folder = userSearch.getResults();
			if(folder.size()>0){
				if(folder.size()==1){
					webUser = (WebUser)folder.get(0);
				}else{
					logger.info("Search returns more than 1 object, returning first object");
					webUser = (WebUser)folder.get(0);
				}
			}
			if (webUser!=null) {
				webUser.getStandardLoginInfo().setPassword(biUser.getPassword());
				source.save(webUser);
			}
			//serverSession.closeSession();
		} catch (WebObjectsException ex) {
			logger.error("Error performing search: "+ex.getMessage());
			return false;
			
		}
		MstrSessionFactory.closeSession(serverSession); 
		return true;
		
	}
	
	public static boolean deleteUser(BiServer biServer,BiUser biUser) {
		WebIServerSession serverSession = MstrSessionFactory.getSession(biServer);
		WebObjectSource  source =serverSession.getFactory().getObjectSource();
		try {
			WebUserEntity userEntity = (WebUserEntity)source.getObject(biUser.getBiObjectId(), EnumDSSXMLObjectTypes.DssXmlTypeUser);
			source.deleteObject(userEntity);
		} catch (WebObjectsException ex) {
			ex.printStackTrace();
			logger.error("===deleteUser: "+ex.getMessage());
			return false;
			
		}
		MstrSessionFactory.closeSession(serverSession); 
		return true;
		
	}
	
	
}
