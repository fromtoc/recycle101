package com.xin.portal.bi.bo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.occa.security.ILogonTokenMgr;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.TreeNode;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ResourceType;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.PromptRelService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.service.ResourceTypeService;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.DateUtil;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.datasource.DataSourceUtils;


/**
 * 
 * @author dod123456
 *
 */
@Controller
@RequestMapping("/bi/bo")
public class BoController  extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(BoController.class);

	private static final String DIR = "bo/";
	
	@Autowired
	private BiProjectService biProjectService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private BiServerService biServerService;
	@Autowired
	private BiUserService biUserService;
	@Autowired
	private PromptRelService promptRelService;
	@Autowired
	private ResourceTypeService resourceTypeService;
	
	/**
	 * 获取session方式为SDK（需要开启防火墙51789,6400端口）
	 * baseUrl:http://xx.xx.xx.xx:8080/BOE/OpenDocument/opendoc/openDocument.jsp
	 * http://xx.xx.xx.xx:8080/BOE/OpenDocument/opendoc/openDocument.jsp?sIDType=CUID&iDocID=bo报表的编号&token=bo登陆后获得的token
	 * @param projectId
	 * @param iDocID
	 * @param model
	 * @return
	 */
	@RequestMapping("/container/{projectId}/{iDocID:.+}")
	private String container(@PathVariable("projectId") String projectId, @PathVariable("iDocID") String iDocID,Model model) {
		String boToken = (String) SessionUtil.getSession(SessionKeys.BO_TOKEN);
		BiProject biProject = biProjectService.findById(projectId);
		if (biProject==null) {
			
		}
		if (boToken==null) {
			try {
//			BiProject biProject = (BiProject) SessionUtil.getSession(SessionKeys.BO_PROJECT);
				if (SessionUtil.getSession(SessionKeys.USER_MAPPING_BO)!=null) {
					BiUser boUser = (BiUser) SessionUtil.getSession(SessionKeys.USER_MAPPING_BO);
					logger.info("get bo token start...");
					boToken = BoSessionUtil.getToken(boUser.getUsername(), boUser.getPassword(), biProject.getServer()+":"+biProject.getPort(), biProject.getBoAuthentication());
					logger.info("get bo token success: {}",boToken);
					SessionUtil.setSession(SessionKeys.BO_TOKEN, boToken);
				} else {
					logger.info("get bo token start...");
					boToken = BoSessionUtil.getToken(biProject.getDefaultUid(), biProject.getDefaultPwd(), biProject.getServer()+":"+biProject.getPort(), biProject.getBoAuthentication());
					logger.info("get bo token success: {}",boToken);
					SessionUtil.setSession(SessionKeys.BO_TOKEN, boToken);
				}
			} catch (Exception e) {
				logger.error("get bo report url error : {}",e.getMessage());
				e.printStackTrace();
			}
			
		}
		StringBuilder url = new StringBuilder();
		url.append(biProject.getUrl()).append("?");
		if (StringUtils.isNotEmpty(biProject.getParamServer())) {
//			url.append(biProject.getParamReport());
			url.append(biProject.getParamServer());
			//);
		}
		url.append("&iDocID=").append(iDocID)
		.append("&token=").append(SessionUtil.getSession(SessionKeys.BO_TOKEN));
		url.append(biProject.getParam());
		logger.info("bo report url : {}",url);
		model.addAttribute("url", url);
		return DIR + "container";
	}
	
	/**
	 * 通过rest api的方式获取token 需要开启restapi的端口
	 * baseUrl:http://xx.xx.xx.xx:8080/BOE/OpenDocument/opendoc/openDocument.jsp?sType=win&sIDType=CUID
	 * http://xx.xx.xx.xx:8080/BOE/OpenDocument/opendoc/openDocument.jsp?
	 * sIDType=CUID&iDocID=bo报表的编号&token=bo登陆后获得的token
	 * 回答筛选 IsSkey 单值  IsMkey 多值 [],[], IsRkey 区间 [A..B]
	 * @param iDocID
	 * @param model
	 * @return
	 */
	@RequestMapping("/boCntainer/{viewType}/{id:.+}")
	private String containerNoProject(@PathVariable(value="viewType",required=false) String viewType, @PathVariable("id") String id,Model model, HttpServletRequest request) {
		try {
			//根据资源id获取资源信息
			Resource resource = resourceService.selectById(id);
			ResourceType resourceType = resourceTypeService.selectById(resource.getResourceType1());
			//获取资源关联的集成BO系统
			BiServer biServer = biServerService.selectById(resource.getServerId());
			//获取用户映射的BO用户
			BiUser biUser = biUserService.findBiUser(biServer.getId(), SessionUtil.getUserId());
			//获取筛选
			PromptRel query = new PromptRel();
			query.setResourceId(resource.getId());
			List<PromptRel> promptRelList = promptRelService.findList(query);
			Long hasPromptShow = promptRelList.stream().filter(item -> item.getHidden()==0).count();
			//String boToken = (String) SessionUtil.getSession(SessionKeys.BO_TOKEN);//考虑是否要放到session中
			//获取token
			String boToken = "0";
			if (viewType != null && "review".equals(viewType)) {
				boToken = RestBoUtil.getLoginToken(biServer);
			}else{
				Object token = request.getSession().getAttribute("User_BO_Token");
				if(token != null 
						&& !"0".equals(String.valueOf(token)) && !"-1".equals(String.valueOf(token))){
					boToken = (String) token;
				}else {
					boToken = RestBoUtil.getLoginToken(biServer, biUser);
					request.getSession().setAttribute("User_BO_Token", boToken);
				}
			}
			if ("-1".equals(boToken) || "0".equals(boToken)) {
				model.addAttribute("url", boToken);
			} else{
				StringBuilder url = new StringBuilder();
				url.append(biServer.getUrl());
				url.append("&iDocID=").append(resource.getLinkUrl());
				url.append("&token=").append(URLEncoder.encode(boToken,"UTF-8"));
				logger.info("bo report url : {}",url);
				model.addAttribute("url", url);
			}
			model.addAttribute("resourceTypeCode", resourceType.getCode());
			model.addAttribute("resourceId", resource.getId());
			model.addAttribute("resourceName", resource.getName());
			//时间默认值初始化
			promptRelList = DateUtil.timeFunction(promptRelList);
			//下拉单选，下拉多选等，
			promptRelList = DataSourceUtils.DatasourceValueFunction(promptRelList);
			//promptRelList = setOrgValue(promptRelList);
			model.addAttribute("promptRelList",promptRelList);
			model.addAttribute("userId", SessionUtil.getUserId());
			model.addAttribute("hasPrompt",promptRelList.size()>0?true:false);
			model.addAttribute("showPrompt", hasPromptShow > 0?true:false);
			//有默认值的第一次访问地址
			model.addAttribute("reportUrlFirst","");
			model.addAttribute("resource",resource);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			model.addAttribute("url", "-2");
		}
		return DIR + "container";
	}
	
	/**
	 * 根据选择的server、parentId 获取server中的报表
	 * @param query
	 * @param parentId
	 * @return
	 */
	@RequestMapping("/reports")
	@ResponseBody
	public List<TreeNode> findReports(BiProject query, String parentId){
//		EntityWrapper<BiProject> ew = new EntityWrapper<>(query);
//		List<BiProject> projects = biProjectService.selectList(ew);
//		FolderBrowsing.browseSharedReportsFolder(project.getServer(), project.getPort(),
//				project.getProject(), project.getDefaultUid(), project.getDefaultPwd());
		long startTime = System.currentTimeMillis();
		BiProject biProject = query.selectById();
		List<TreeNode> list = BoUtil.getReportList(biProject, parentId);
		logger.info("==========getForder cost =========={}",System.currentTimeMillis()-startTime);
		return list;
	}
	
	
	
	
	
	@RequestMapping("/token")
	@ResponseBody
	private BaseApi token(HttpServletRequest request) {
//		HttpSession session = request.getSession();
		 
        String cms = "127.0.0.1:6400";
        //http://52.83.90.175:8090/BOE/CMC

        //认证的安全类型，类型是以上几种，如果是SAP，则类型字符为secSAPR3
        String authentication = "secEnterprise";

        //用户名，如果是SAP系统方式登录，格式为bwp~888/eiacext1
        String poUsername = "Administrator";
        String poPassword = "Dod12345!";//密码
        Object sessionUser = SessionUtil.getSession(SessionKeys.USER_MAPPING_BO);
        if (sessionUser!=null) {
        	BiUser biUser = (BiUser) sessionUser;
        	String tokenStr = BoSessionUtil.getToken(biUser.getUsername(), biUser.getPassword(), cms, authentication);
        	
        	logger.info("bo token: {}",tokenStr);
        	return StringUtils.isEmpty(tokenStr) ? BaseApi.error() : BaseApi.data(tokenStr);
        }
        return null;
//        session.getServletContext().setAttribute("token", tokenStr);
	}
	
	
	@RequestMapping("/getToken")
	private String getToken(HttpServletRequest request) {
//		HttpSession session = request.getSession();
		 
        String cms = "127.0.0.1:6400";
        //http://52.83.90.175:8090/BOE/CMC

        //认证的安全类型，类型是以上几种，如果是SAP，则类型字符为secSAPR3
        String authentication = "secEnterprise";

        //用户名，如果是SAP系统方式登录，格式为bwp~888/eiacext1
        String poUsername = "Administrator";
        String poPassword = "Dod12345!";//密码

        //登录并获得TOKEN，并命名用OpenDocument方式打开一个文件
        ISessionMgr sessionMgr;
        String tokenStr = "tokenStr";
        //System.out.println("go go go... "+poUsername+" | "+poPassword+" | "+cms+" | "+authentication);
		try {
			sessionMgr = CrystalEnterprise.getSessionMgr();
			IEnterpriseSession enterpriseSession = sessionMgr.logon(poUsername, poPassword, cms,authentication);
			ILogonTokenMgr mgr = enterpriseSession.getLogonTokenMgr();
			/**
			 *
			 * String  token=logonTokenMgr.createLogonToken("",10,5);
            其中createLogonToken(java.lang.String clientComputerName, int validMinutes, int validNumOfLogons)
            clientComputerName为使用这个token的客户端计算机名，空字符串表示该token可被任何客户端使用；
            validMinutes为token的有效时间（分钟）；
            validNumOfLogons 表示该token允许被使用的最大次数。
			 *
			 */
			tokenStr = mgr.createLogonToken("",24*60,Integer.MAX_VALUE);
		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("tokenStr: "+tokenStr);
//        session.getServletContext().setAttribute("token", tokenStr);
		return BoSessionUtil.getToken(poUsername, poPassword, cms, authentication);
	}
	
}
