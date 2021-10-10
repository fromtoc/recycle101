package com.xin.portal.bi.bo;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.TreeNode;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
/**
 * 使用SDK获取session以及报表列表。（需要开启防火墙51789,6400端口）
 * @ClassPath: com.xin.portal.bi.bo.BoUtil 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年12月20日 上午11:36:14
 */
public class BoUtil {
	
	private static Logger logger = LoggerFactory.getLogger(BoUtil.class);
	
	public static IEnterpriseSession getSession(BiServer biServer){
		IEnterpriseSession enterpriseSession = null;
		try {
			ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
			enterpriseSession = sessionMgr.logon(biServer.getDefaultUid(), biServer.getDefaultPwd(), biServer.getIp(), "secEnterprise");
			enterpriseSession.setLocale(Locale.CHINA);
		} catch(Exception e){
			logger.error("error creating bo session : {}",biServer.toString());
			logger.error(e.getMessage());
		}
		return enterpriseSession;
	}
	
	public static void check(BiProject biProject) throws Exception{
		IEnterpriseSession enterpriseSession = null;
		ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
		enterpriseSession = sessionMgr.logon(biProject.getDefaultUid(), biProject.getDefaultPwd(), biProject.getIp(), biProject.getBoAuthentication());
		enterpriseSession.setLocale(Locale.CHINA);
	}
	
	public static void checkServer(BiServer biServer) throws Exception{
		IEnterpriseSession enterpriseSession = null;
		ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
		enterpriseSession = sessionMgr.logon(biServer.getDefaultUid(), biServer.getDefaultPwd(), biServer.getIp(), "secEnterprise");
		enterpriseSession.setLocale(Locale.CHINA);
	}
	
	public static IEnterpriseSession getSession(BiProject biProject){
		IEnterpriseSession enterpriseSession = null;
		try {
			ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
			enterpriseSession = sessionMgr.logon(biProject.getDefaultUid(), biProject.getDefaultPwd(), biProject.getIp(), biProject.getBoAuthentication());
			enterpriseSession.setLocale(Locale.CHINA);
		} catch(Exception e){
			logger.error("error creating bo session : {}",biProject.toString());
			logger.error(e.getMessage());
		}
		return enterpriseSession;
	}
	
	public static List<TreeNode> getReportList(BiProject biProject, String parentId) {
		IEnterpriseSession enterpriseSession = null;
		List<TreeNode> list = Lists.newArrayList();
		try {
			//System.out.println("Connecting...");
			ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
			enterpriseSession = sessionMgr.logon(biProject.getDefaultUid(), biProject.getDefaultPwd(), biProject.getIp(), biProject.getBoAuthentication());
			enterpriseSession.setLocale(Locale.CHINA);
			IInfoStore infoStore = (IInfoStore) enterpriseSession.getService("InfoStore");
			String query = "select * from ci_infoobjects where si_parentid='"+parentId+"' order by si_name";
			IInfoObjects infoObjects = (IInfoObjects) infoStore.query(query);
			//System.out.println("children size : "+infoObjects.size());
			for (Object object : infoObjects) {
				IInfoObject infoObject = (IInfoObject) object;
				TreeNode treeNode = new TreeNode(String.valueOf(infoObject.getID()), infoObject.getTitle(Locale.CHINA), parentId);
				Map<String, String> att = new HashMap<>();
				att.put("cuid", infoObject.getCUID());
				treeNode.setAttribute(att);
				if ("Folder".equals(infoObject.getKind())) {
					treeNode.setIsParent(true);
					treeNode.setNocheck(true);
				} else if ("Hyperlink".equals(infoObject.getKind())) {
					
				}
				//System.out.println(infoObject.getID() + "  " + infoObject.getCommitLevel()+ "  " + infoObject.getKind()+ "  " + infoObject.getCUID() + "  " + infoObject.getTitle()+"  "+infoObject.getTitle(Locale.CHINA));
				list.add(treeNode);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	
	public static void main(String[] args) {
		IEnterpriseSession enterpriseSession = null;
		// ReportParts reportEngines = null;
		try {
		//	System.out.println("Connecting...");
			ISessionMgr sessionMgr = CrystalEnterprise.getSessionMgr();
			enterpriseSession = sessionMgr.logon("Administrator", "Dod12345!", "52.83.90.175", "secEnterprise");
			enterpriseSession.setLocale(Locale.CHINA);
			IInfoStore infoStore = (IInfoStore) enterpriseSession.getService("InfoStore");
			String query = "select * from ci_infoobjects where si_kind = 'folder' and si_parentid='23' order by si_name";
			IInfoObjects infoObjects = (IInfoObjects) infoStore.query(query);
			for (Object object : infoObjects) {
				IInfoObject infoObject = (IInfoObject) object;
				//System.out.println(infoObject.getID() + "  " + infoObject.getCommitLevel()+ "  " + infoObject.getKind()+ "  " + infoObject.getCUID() + "  " + infoObject.getTitle()+"  "+infoObject.getTitle(Locale.CHINA));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
		}
		//System.out.println("Finished!");
	}

}
