/**  
* @Title: com.xin.portal.system.controller.MSTRController  
* @Description: TODO 
* @author dod123456  
* @date 2018年1月8日  
* @version V1.0  
*/  
package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.mstr.MstrSessionManagement;
import com.xin.portal.bi.mstr.api.ExportApi;
import com.xin.portal.bi.mstr.api.PromptApi;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.TreeNode;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.*;
import com.xin.portal.system.util.Constant.BiType;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.DateUtil;
import com.xin.portal.system.util.HttpClientUtils;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.datasource.DataSourceUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 
 * @ClassPath: com.xin.portal.system.controller.MSTRController 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月8日 下午2:10:06 
 */
@Controller
@RequestMapping("/bi/mstr")
public class MstrController extends BaseController{
	
	private static final Logger logger = LoggerFactory.getLogger(MstrController.class);

	@Autowired
	private PromptRelService promptRelService;
	@Autowired
	private DictService dictService;
	@Autowired
	private OrganizationService organizationService;
	@Autowired
	private BiUserService mstrUserService;
	@Autowired
	private BiProjectService biProjectService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private BiServerService biServerService;
	@Value("${mstr.SSOESM}")
	private int mstrSSOESM;
	@Autowired
	private BiMappingService biMappingService;
	@Autowired
	private ResourceTypeService resourceTypeService;
	
	@RequestMapping("/index")
	public String index(Model model){
		List<Dict> inputType = dictService.listItem(new Dict("input_type"));
		model.addAttribute("inputType", JSONArray.toJSONString(inputType));
		JSONObject result;
		try {
			result = HttpClientUtils.doGet("http://192.168.1.175:8080/MicroStrategy/servlet/mstrWeb?Server=192.168.1.175&Project=MicroStrategy+Tutorial&Port=0&evt=3140&src=mstrWeb.3140&documentID=E394CAB4419686937D08B48D5502610B&uid=administrator&pwd=");
			model.addAttribute("result", result.get("content"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "mstr/index";
	}
	
	@RequestMapping("/{mstrId}/prompts")
	@ResponseBody
	public List<Prompt> prompts(@PathVariable String mstrId,Model model){
		BiProject mstrProject = new BiProject();
		mstrProject.setServer("192.168.1.175");
		mstrProject.setProject("MicroStrategy Tutorial");
		mstrProject.setPort("0");
		mstrProject.setDefaultUid("administrator");
		mstrProject.setDefaultPwd("");
		List<Prompt> list = Lists.newArrayList();
		try {
			list = PromptApi.prompts(mstrProject,mstrId,5);
		} catch (WebObjectsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping("/dossier/{id:.+}")
	public String dossier(@PathVariable("id")String id, Model model) {
		String userId = SessionUtil.getUserId();
		Resource resource = resourceService.selectById(id);
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		BiServer biServer = biServerService.selectById(resource.getServerId());
		PromptRel query = new PromptRel();
		query.setResourceId(resource.getId());
		List<PromptRel> promptRelList = promptRelService.findList(query);
		promptRelList = DateUtil.timeFunction(promptRelList);//设置时间提示的默认值
		//promptRelList = setOrgValue(promptRelList);//设置下拉，复选，等默认值
		//下拉单选，下拉多选等，m.bi_user_id,
		promptRelList = DataSourceUtils.DatasourceValueFunction(promptRelList);
		//通过iserver获取当前访问用户的映射用户，如果没有使用默认用户。
		BiMapping bm = new BiMapping();
		bm.setUserId(userId);
		bm.setBiServerId(biServer.getId());
		List<BiMapping> biList = biMappingService.selectBiUserBySysUserAndServerId(bm);
		if(biList != null && biList.size() > 0){
			model.addAttribute("biUserId", biList.get(0).getBiUserId());//bi userID
			model.addAttribute("hasMapping", "true");//是否已经映射用户
		}else{
			model.addAttribute("biUserId", null);//bi userID
			model.addAttribute("hasMapping", "false");
		}
		model.addAttribute("url",biServer.getUrl());
		model.addAttribute("resource",resource);
		model.addAttribute("biServer",biServer);
		model.addAttribute("promptRelList",promptRelList);
		model.addAttribute("resourceTypeCode", resourceType.getCode());
		model.addAttribute("userId", userId);//userID
		//提示是否显示
		List<PromptRel> shwoPrompt = promptRelList.stream().filter(item-> 0 == item.getHidden()).collect(Collectors.toList());
		model.addAttribute("showPrompt", shwoPrompt.size() > 0 ? true : false);
		return "mstr/dossier";
	}
	
	@RequestMapping("/mobile/dossier/{id:.+}/{userId:.+}")
	public String mobileDossier(@PathVariable("id")String id, @PathVariable("userId")String userId, Model model) {
		Resource resource = resourceService.selectById(id);
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		BiServer biServer = biServerService.selectById(resource.getServerId());
		PromptRel query = new PromptRel();
		query.setResourceId(resource.getId());
		List<PromptRel> promptRelList = promptRelService.findList(query);
		promptRelList = DateUtil.timeFunction(promptRelList);//设置时间提示的默认值
		//promptRelList = setOrgValue(promptRelList);//设置下拉，复选，等默认值
		//下拉单选，下拉多选等，m.bi_user_id,
		promptRelList = DataSourceUtils.DatasourceValueFunction(promptRelList);
		//通过iserver获取当前访问用户的映射用户，如果没有使用默认用户。
		BiMapping bm = new BiMapping();
		bm.setUserId(userId);
		bm.setBiServerId(biServer.getId());
		List<BiMapping> biList = biMappingService.selectBiUserBySysUserAndServerId(bm);
		if(biList != null && biList.size() > 0){
			model.addAttribute("biUserId", biList.get(0).getBiUserId());//bi userID
			model.addAttribute("hasMapping", "true");//是否已经映射用户
		}else{
			model.addAttribute("biUserId", null);//bi userID
			model.addAttribute("hasMapping", "false");
		}
		model.addAttribute("url",biServer.getUrl());
		model.addAttribute("resource",resource);
		model.addAttribute("biServer",biServer);
		model.addAttribute("promptRelList",promptRelList);
		model.addAttribute("resourceTypeCode", resourceType.getCode());
		model.addAttribute("userId", userId);//userID
		//提示是否显示
		List<PromptRel> shwoPrompt = promptRelList.stream().filter(item-> 0 == item.getHidden()).collect(Collectors.toList());
		model.addAttribute("showPrompt", shwoPrompt.size() > 0 ? true : false);
		return "mstr/mobileDossier";
	}

	@RequestMapping("/dossier/{id:.+}/promptAnswers")
	public String promptAnswers(@PathVariable("id")String id, Model model) {
		Resource resource = resourceService.selectById(id);
		BiServer biServer = biServerService.selectById(resource.getServerId());


		model.addAttribute("serverId",biServer.getId());
		model.addAttribute("url",biServer.getUrl());
		model.addAttribute("biServer",biServer);
		model.addAttribute("resource",resource);
		return "mstr/dossier";
	}
	
	@RequestMapping("/mobile/container/{id:.+}")
	public String reportMobileContainer(@PathVariable("id")String id, Model model) {
		Resource resource = resourceService.selectById(id);
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		UserInfo userInfo = SessionUtil.getUserInfo();
		PromptRel query = new PromptRel();
		query.setResourceId(resource.getId());
		List<PromptRel> promptRelList = promptRelService.findList(query);

		BiProject project = biProjectService.findById(resource.getProjectId());
		//关于水印的部分
		BiServer  server  = biServerService.selectById(project.getBiServerId());
		if(server!=null&&server.getWaterMark()==1){
			model.addAttribute("waterMarkText", server.getWatermarkText());
			model.addAttribute("waterMark", server.getWaterMark());
		}
		BiMapping bm = new BiMapping();
		bm.setUserId(userInfo.getId());
		bm.setBiServerId(server.getId());
		List<BiMapping> biList = biMappingService.selectBiUserBySysUserAndServerId(bm);

		StringBuilder sb = new StringBuilder();
		//String[] reportUrl = new String[3];
		if (project!=null) {
			sb.append(project.getUrl()).append("?");
			sb.append("Server=").append(project.getServer());
			sb.append("&Project=").append(project.getProject());
			sb.append("&Port=").append(project.getPort());
			BiMapping mstrUser = (biList != null && biList.size() > 0)?biList.get(0):null;
			if (mstrUser != null && StringUtils.isNotEmpty(mstrUser.getBiUserName())) {
				sb.append("&uid=").append(mstrUser.getBiUserName());
				if (StringUtils.isNotEmpty(mstrUser.getBiPassword())) {
					sb.append("&pwd=").append(mstrUser.getBiPassword());
				} else {
					sb.append("&pwd=");
				}
			} else {
				sb.append("&uid=").append(project.getDefaultUid());
				if (StringUtils.isNotEmpty(project.getDefaultPwd())) {
					sb.append("&pwd=").append(project.getDefaultPwd());
				} else {
					sb.append("&pwd=");
				}
				//model.addAttribute("userMapping", 0);
			}
			if (StringUtils.isNotEmpty(resource.getHiddenSections())) {
				sb.append("&hiddensections=").append(resource.getHiddenSections());
			}
			if (EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition==resource.getTypeValue()) {
				if ("3".equals(resource.getResourceType1())) {
					sb.append(project.getParamDossier());
				} else {
					sb.append(project.getParamDoc());
				}
				sb.append("&documentID=").append(resource.getReportId());
			} else if (EnumDSSXMLObjectTypes.DssXmlTypeFolder==resource.getTypeValue()) {
				sb.append(project.getParamFolder());
				sb.append("&folderID=").append(resource.getReportId());
			} else {
				sb.append(project.getParamReport());
				sb.append("&reportId=").append(resource.getReportId());
			}
		}
		model.addAttribute("userMapping","true");
		model.addAttribute("reportUrl",sb);
		model.addAttribute("resourceTypeCode", resourceType.getCode());
		//model.addAttribute("resource", resource);
		//model.addAttribute("resourceId", resource.getId());
		//model.addAttribute("userId", resource.getId());
		promptRelList = DateUtil.timeFunction(promptRelList);
		//promptRelList = setOrgValue(promptRelList);
		promptRelList = DataSourceUtils.DatasourceValueFunction(promptRelList);
		model.addAttribute("promptRelList",promptRelList);
		model.addAttribute("hasPrompt",promptRelList.size()>0?true:false);
		
//		logger.info("======mstr_url : {}",sb);
//		boolean showPrompt = false;
//		StringBuilder promptContent = new StringBuilder();
//		if (promptRelList!=null && promptRelList.size()>0) {
//			sb.append("&valuePromptAnswers=");
//			for (PromptRel rec : promptRelList) {
//				if (StringUtils.isNotEmpty(rec.getDefaultValue1())) {
//					//日期
//					if (rec.getType().equals(3)) {
//						sb.append(rec.getDefaultValue1().replace("-", "/"));
//					}else {
//						sb.append(rec.getDefaultValue1());
//					}
//					promptContent.append(rec.getName()).append(":").append(rec.getDefaultValue1());
//				}
//				sb.append("%5e");
//				if (rec.getHidden().equals(0)) {
//					showPrompt = true;
//				}
//				
//				if (rec.getType().equals(4)||rec.getType().equals(5)||rec.getType().equals(6)) {//下拉列表/单选框/复选框
//					if (StringUtils.isNotEmpty(rec.getDictCode())) {
//						List<Dict> items = dictService.listItem(new Dict(rec.getDictCode()));
//						rec.setItems(items);
//					} else if ("#branch".equals(rec.getSpecial())) {//分公司
//						Organization orgQuery = new Organization();
//						orgQuery.setCodeLike(userInfo.getOrgCode());
//						orgQuery.setLevel(userInfo.getOrgCode().length()+3);
//						
//						EntityWrapper<Organization> ew = new EntityWrapper<Organization>(orgQuery);
//						List<Organization> list = organizationService.selectList(ew);
//						rec.setItems(list);
//						model.addAttribute("branchList", JSONArray.toJSONString(list));
//						model.addAttribute("branchCode", rec.getCode());
//					}  else if ("#business".equals(rec.getSpecial())) {//营业部
//						Organization orgQuery = new Organization();
//						orgQuery.setCodeLike(userInfo.getOrgCode());
//						orgQuery.setLevel(userInfo.getOrgCode().length()+6);
//						EntityWrapper<Organization> ew = new EntityWrapper<Organization>(orgQuery);
//						List<Organization> list = organizationService.selectList(ew);
//						for(Organization org : list) {
//							org.setExtCode(org.getExtCode().substring(1));
//						}
//						rec.setItems(list);
//						model.addAttribute("businessList", JSONArray.toJSONString(list));
//						model.addAttribute("businessCode", rec.getCode());
//					}
//				}
//				
//			}
//		}
		//提示是否显示
		List<PromptRel> shwoPrompt = promptRelList.stream().filter(item-> 0 == item.getHidden()).collect(Collectors.toList());
		model.addAttribute("showPrompt", shwoPrompt.size() > 0 ? true : false);
//		model.addAttribute("promptContent",promptContent);
		//有默认值的第一次访问地址
//		model.addAttribute("reportUrlFirst",sb);
//		logger.info("======mstr_url_default : {}",sb);
		return "mstr/mobileContainer";
	}
	
	@RequestMapping("/container/{id:.+}")
	public String reportContainer(@PathVariable("id")String id, Model model) {
		Resource resource = resourceService.selectById(id);
		ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
		UserInfo userInfo = SessionUtil.getUserInfo();
		PromptRel query = new PromptRel();
		query.setResourceId(resource.getId());
		List<PromptRel> promptRelList = promptRelService.findList(query);

		BiProject project = biProjectService.findById(resource.getProjectId());
		//关于水印的部分
		BiServer  server  = biServerService.selectById(project.getBiServerId());
		if(server!=null&&server.getWaterMark()==1){
			model.addAttribute("waterMarkText", server.getWatermarkText());
			model.addAttribute("waterMark", server.getWaterMark());
		}
		BiMapping bm = new BiMapping();
		bm.setUserId(userInfo.getId());
		bm.setBiServerId(server.getId());
		List<BiMapping> biList = biMappingService.selectBiUserBySysUserAndServerId(bm);

		StringBuilder sb = new StringBuilder();
		//String[] reportUrl = new String[3];
		if (project!=null) {
			sb.append(project.getUrl()).append("?");
			sb.append("Server=").append(project.getServer());
			sb.append("&Project=").append(project.getProject());
			sb.append("&Port=").append(project.getPort());
			BiMapping mstrUser = (biList != null && biList.size() > 0)?biList.get(0):null;
			if (mstrUser != null && StringUtils.isNotEmpty(mstrUser.getBiUserName())) {
				sb.append("&uid=").append(mstrUser.getBiUserName());
				if (StringUtils.isNotEmpty(mstrUser.getBiPassword())) {
					sb.append("&pwd=").append(mstrUser.getBiPassword());
				} else {
					sb.append("&pwd=");
				}
			} else {
				sb.append("&uid=").append(project.getDefaultUid());
				if (StringUtils.isNotEmpty(project.getDefaultPwd())) {
					sb.append("&pwd=").append(project.getDefaultPwd());
				} else {
					sb.append("&pwd=");
				}
				//model.addAttribute("userMapping", 0);
			}
			if (StringUtils.isNotEmpty(resource.getHiddenSections())) {
				sb.append("&hiddensections=").append(resource.getHiddenSections());
			}
			if (EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition==resource.getTypeValue()) {
				if ("3".equals(resource.getResourceType1())) {
					sb.append(project.getParamDossier());
				} else {
					sb.append(project.getParamDoc());
				}
				sb.append("&documentID=").append(resource.getReportId());
			} else if (EnumDSSXMLObjectTypes.DssXmlTypeFolder==resource.getTypeValue()) {
				sb.append(project.getParamFolder());
				sb.append("&folderID=").append(resource.getReportId());
			} else {
				sb.append(project.getParamReport());
				sb.append("&reportId=").append(resource.getReportId());
			}
		}
		model.addAttribute("userMapping","true");
		model.addAttribute("reportUrl",sb);
		model.addAttribute("resourceTypeCode", resourceType.getCode());
		model.addAttribute("resource", resource);
		//model.addAttribute("resourceId", resource.getId());
		//model.addAttribute("userId", resource.getId());
		promptRelList = DateUtil.timeFunction(promptRelList);
		//promptRelList = setOrgValue(promptRelList);
		promptRelList = DataSourceUtils.DatasourceValueFunction(promptRelList);
		model.addAttribute("promptRelList",promptRelList);
		model.addAttribute("hasPrompt",promptRelList.size()>0?true:false);//是否有提示（隐藏和显示的）
		
//		logger.info("======mstr_url : {}",sb);
//		boolean showPrompt = false;
//		StringBuilder promptContent = new StringBuilder();
//		if (promptRelList!=null && promptRelList.size()>0) {
//			sb.append("&valuePromptAnswers=");
//			for (PromptRel rec : promptRelList) {
//				if (StringUtils.isNotEmpty(rec.getDefaultValue1())) {
//					//日期
//					if (rec.getType().equals(3)) {
//						sb.append(rec.getDefaultValue1().replace("-", "/"));
//					}else {
//						sb.append(rec.getDefaultValue1());
//					}
//					promptContent.append(rec.getName()).append(":").append(rec.getDefaultValue1());
//				}
//				sb.append("%5e");
//				if (rec.getHidden().equals(0)) {
//					showPrompt = true;
//				}
//				
//				if (rec.getType().equals(4)||rec.getType().equals(5)||rec.getType().equals(6)) {//下拉列表/单选框/复选框
//					if (StringUtils.isNotEmpty(rec.getDictCode())) {
//						List<Dict> items = dictService.listItem(new Dict(rec.getDictCode()));
//						rec.setItems(items);
//					} else if ("#branch".equals(rec.getSpecial())) {//分公司
//						Organization orgQuery = new Organization();
//						orgQuery.setCodeLike(userInfo.getOrgCode());
//						orgQuery.setLevel(userInfo.getOrgCode().length()+3);
//						
//						EntityWrapper<Organization> ew = new EntityWrapper<Organization>(orgQuery);
//						List<Organization> list = organizationService.selectList(ew);
//						rec.setItems(list);
//						model.addAttribute("branchList", JSONArray.toJSONString(list));
//						model.addAttribute("branchCode", rec.getCode());
//					}  else if ("#business".equals(rec.getSpecial())) {//营业部
//						Organization orgQuery = new Organization();
//						orgQuery.setCodeLike(userInfo.getOrgCode());
//						orgQuery.setLevel(userInfo.getOrgCode().length()+6);
//						EntityWrapper<Organization> ew = new EntityWrapper<Organization>(orgQuery);
//						List<Organization> list = organizationService.selectList(ew);
//						for(Organization org : list) {
//							org.setExtCode(org.getExtCode().substring(1));
//						}
//						rec.setItems(list);
//						model.addAttribute("businessList", JSONArray.toJSONString(list));
//						model.addAttribute("businessCode", rec.getCode());
//					}
//				}
//				
//			}
//		}
		//提示是否显示
		List<PromptRel> shwoPrompt = promptRelList.stream().filter(item-> 0 == item.getHidden()).collect(Collectors.toList());
		model.addAttribute("showPrompt", shwoPrompt.size() > 0 ? true : false);
//		model.addAttribute("promptContent",promptContent);
		//有默认值的第一次访问地址
//		model.addAttribute("reportUrlFirst",sb);
//		logger.info("======mstr_url_default : {}",sb);
		return "mstr/container";
	}
	
	@RequestMapping("/export")
	@ResponseBody
	public BaseApi export(String projectId, String mstrId, String params, String type){
		BiProject biProject = biProjectService.selectById(projectId);
		long startTime = System.currentTimeMillis();
		EntityWrapper<Config> ew = new EntityWrapper<>();
		ew.eq("code", "SYS_UPLOAD_PATH");
		Config config = configService.selectOne(ew) ;
		
		
		Map<String, String[]> paramsMap = new HashMap<>();
		if ("1".equals(params)) {//1 元素提示
			paramsMap.put("2692A0C4428F9984960EE094CE50F0F5", new String[]{"电影"});
		} else if ("2".equals(params)) {// 1元素提示 1值提示
			paramsMap.put("2692A0C4428F9984960EE094CE50F0F5", new String[]{"电影"});
			paramsMap.put("值 (日期)", new String[]{"20161001"});
		} else if ("3".equals(params)) { // 2 元素提示 2 值提示
			paramsMap.put("0490E1884F59CB02BF3BE7B79A48FA29", new String[]{"电影","功夫片"});
			paramsMap.put("值 (日期)", new String[]{"20161001"});
			paramsMap.put("值 (日期)2", new String[]{"20161002"});
		} else if ("4".equals(params)) { // 2 元素提示 2 值提示
			paramsMap.put("2692A0C4428F9984960EE094CE50F0F5", new String[]{"电影","功夫片"});
			paramsMap.put("0490E1884F59CB02BF3BE7B79A48FA29", new String[]{"电影","功夫片"});
		}
		
		try {
			String path = "";
			if (config!=null) {
				path = config.getValue() + File.separator + mstrId+"_"+System.currentTimeMillis()+".xlsx";
			} else {
				path = "D:" + File.separator + mstrId+"_"+System.currentTimeMillis()+".xlsx";
			}
			if ("report".equals(type)) {
				ExportApi.exportReport(biProject, mstrId, path, paramsMap);
				
			} else if ("document".equals(type)) {
				ExportApi.exportDom2(biProject, mstrId, path, paramsMap,"");
			}
		} catch (WebObjectsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("==========export cost =========={}",System.currentTimeMillis()-startTime);
		return BaseApi.success();
	}
	
	@RequestMapping("/reports")
	@ResponseBody
	public List<TreeNode> findReports(BiProject query, String parentId){
		EntityWrapper<BiProject> ew = new EntityWrapper<>(query);
//		List<BiProject> projects = biProjectService.selectList(ew);
//		FolderBrowsing.browseSharedReportsFolder(project.getServer(), project.getPort(),
//				project.getProject(), project.getDefaultUid(), project.getDefaultPwd());
		long startTime = System.currentTimeMillis();
		BiProject biProject = query.selectById();
		List<TreeNode> list = MstrSessionManagement.getMenuFolder(biProject,parentId);
		logger.info("==========getForder cost =========={}",System.currentTimeMillis()-startTime);
		return list;
	}
	
	@SystemLog(module = LanguageParam.MSTR_AUTHENTICATION , operation=LanguageParam.VERIFY_PERMISSIONS, type=LogType.login)
	@RequestMapping(value="/auth", produces={"application/xml;charset-utf-8"})
	@ResponseBody
	public String sso(String token) {
		try {
			boolean result = false;
			if (!StringUtils.isEmpty(token)) {
				if (token.indexOf("administrator")!=-1) {
					String[] info = token.split("_");
					if (info!=null) {
						return "<return_code><pass userid=\""+info[0]+"\" pwd=\""+(info.length==1?"":info[1])+"\" /></return_code>";
					}
				}
				result = true;
			}
			if (result) {
				EntityWrapper<BiUser> wrapper = new EntityWrapper<>();
//				query.setUserId(token);
//				query.setType(BiType.MSTR);
				wrapper.setSqlSelect(" id = (select mapping_user_id from t_user_mapping where user_id = '"+token+"' and type='"+BiType.MSTR+"')");
				BiUser mstrUser = mstrUserService.selectOne(wrapper);
				if (mstrUser==null) {
					return "<return_code><fail msg=\"Can not validate the token: "+token+", the token may be invalid or expired.\" /></return_code>";
				} else {
					return "<return_code><pass userid=\""+mstrUser.getUsername()+"\" pwd=\""+mstrUser.getPassword()+"\" /></return_code>";
				}
				
			} else {
				return "<return_code><fail msg=\"Can not validate the token, the token may be invalid or expired.\" /></return_code>";
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "<return_code><fail msg=\"Can not validate the token:"+token+", the token may be invalid or expired.\" /></return_code>";
		}
		
	}
	
	private List<PromptRel> setOrgValue(List<PromptRel> list){
		for (PromptRel promptRel : list) {
			if(promptRel.getPromptType()!=null && "4".equals(promptRel.getPromptType())){//组织筛选
				//判断是否为隐藏，如果隐藏则通过查询code设置默认值，
				if(promptRel.getHidden()!=null && promptRel.getHidden()==1){//隐藏
					//查询出用户code
					Organization org = organizationService.selectById(SessionUtil.getUserInfo().getOrgId());
					promptRel.setDefaultValue1(org.getExtCode());
				}else{//如果不是隐藏则使用用户设置的默认//如果没有默认值，判断是否必须，如过不必须则通过查询获得code
					if(promptRel.getDefaultValue1() ==null || promptRel.getDefaultValue1().length()<=0){
						if(promptRel.getRequired()==null || promptRel.getRequired()==0){
							String orgCode = SessionUtil.getUserInfo().getExtCode();
							promptRel.setDefaultValue1(orgCode);
						}
					}
				}
			}
		}
		return list;
	}

}
