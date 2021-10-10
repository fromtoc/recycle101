package com.xin.portal.bi.controller;



import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.model.UserReport;
import com.xin.portal.bi.mstr.api.CreatorApi;
import com.xin.portal.bi.service.UserReportService;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.BiIndependent;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.model.UserLikes;
import com.xin.portal.system.model.UserMapping;
import com.xin.portal.system.service.BiIndependentService;
import com.xin.portal.system.service.BiMappingService;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.UserLikesService;
import com.xin.portal.system.util.AESUtil;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.Constant.BiType;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.SessionUtil;

import springfox.documentation.spring.web.json.Json;

/**  
* @Title: com.xin.portal.bi.controller.UserReportController 
* @Description: 
* @author zhoujun  
* @date 2018-06-05
* @version V1.0  
*/ 
@Controller
@RequestMapping("/userReport")
public class UserReportController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(UserReportController.class);

	private static final String DIR = "userReport/";
	
	@Autowired
	private UserReportService service;
	@Autowired
	private UserLikesService userLikesService;
	@Autowired
	private BiMappingService biMappingService;
	@Autowired
	private BiUserService biUserService;
	@Autowired
	private BiProjectService biProjectService;
	@Autowired
	private BiServerService biServerService;
	@Autowired
	private BiIndependentService biIndependentService;
	

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-06-05 
	 */
	/*@RequestMapping("/index")
	public String index(Model model){//原高校逻辑
		EntityWrapper<UserReport> ew = new EntityWrapper<>();
		UserInfo userInfo = SessionUtil.getUserInfo();
		ew.eq("user_id", userInfo.getId());
		ew.eq("name", userInfo.getUsername());
		ew.eq("report_type", EnumDSSXMLObjectTypes.DssXmlTypeFolder);
		
		String commonUrl = getMstrProjectUrl();
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.hasRole("role_teacher")) {
			model.addAttribute("userFolder", commonUrl+"&hiddensections=header,path&evt=2001&src=mstrWeb.2001&systemFolder=7");
		} else {
//			UserReport userReport = service.selectOne(ew);
			//获取用户的映射用户信息
			BiUser biUser = (BiUser) SessionUtil.getSession(SessionKeys.USER_MAPPING_MSTR);
			if (biUser!=null && biUser.getOwnFolderId()!=null && biUser.getOwnFolderId()!="" && biUser.getOwnFolderId().length()>0 ) {
				model.addAttribute("userFolder", commonUrl+"&hiddensections=header,path&evt=2001&src=mstrWeb.shared.fbb.fb.2001&folderID="+biUser.getOwnFolderId());
//				model.addAttribute("userFolder", commonUrl+"&hiddensections=header,path&evt=2001&src=mstrWeb.shared.fbb.fb.2001&folderID="+"A81DBB0F46027F0F03264295D8617648");
			}
		}
		return DIR + "index2";
	}*/
	
	@RequestMapping("/index")
	public String index(Model model){
		//获取用户的MSTR映色用户
		Wrapper<BiMapping> wu = new EntityWrapper<>();
		wu.eq("user_id", SessionUtil.getUserId());
		wu.eq("type", "MSTR");//mstr
		List<BiMapping> mList = biMappingService.selectList(wu);//获取所有的映射用户
		if(mList!=null && mList.size()>0){
			//到 t_bi_independent表中找到 自主分析地址用户
			List<BiIndependent> ilist = new ArrayList<>();
			for (BiMapping bml : mList) {
				//查找mstr用户中哪些设置自主分析
				BiIndependent query = new BiIndependent();
				query.setBiUserId(bml.getBiUserId());
				List<BiIndependent> bid = biIndependentService.findByQuery(query);
				if(bid!=null && bid.size()>0){
					ilist.add(bid.get(0));
				}
			}
			if(ilist!=null && ilist.size()>0){
				BiIndependent bin = ilist.get(0);
				String id = bin.getBiUserId();
				BiUser bu = biUserService.selectById(id);//获取biuser信息
				if(bu!=null){
					//获取BIUser的biserver
					BiServer bs = biServerService.selectById(bu.getBiServerId());
					//获取BIProject
					BiProject bp = biProjectService.selectById(bin.getBiProjectId());
					if(bs!=null && bp!=null){
						//获取server下的project的信息
						String commonUrl = getMstrIndependReportCommonUrl(bu,bp,bs);
						model.addAttribute("userFolder", commonUrl+"&hiddensections=header,path&evt=2001&src=mstrWeb.shared.fbb.fb.2001&folderID="+bin.getBiOwnFolderId());
					}
				}
			}
		}
		return DIR + "index2";
	}
	
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-06-05 
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}
	
	
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<UserReport> page(UserReport query, Model model){
		return service.page(query);
	}
	
	@RequestMapping("/pageShare")
	@ResponseBody
	public PageModel<UserReport> pageShare(UserReport query, Model model){
		return service.pageShare(query);
	}
	
	@RequestMapping("/show/{type}")
	@ResponseBody
	public BaseApi show(@PathVariable("type")String type,UserReport query, Model model){
		List<BiProject> projects = Lists.newArrayList();
		Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
		for (int i=0;i<5;i++) {
			BiProject mstrProject = cache.get(Constant.CACHE_MSTR_PROJECT+i, BiProject.class);
			if (mstrProject==null) {
				break;
			} else {
				projects.add(mstrProject) ;
			}
		}
		BiUser biUser = null;
		Object userMappingObj = SessionUtil.getSession(SessionKeys.USER_MAPPING_MSTR);
		if (userMappingObj!=null){
			biUser = (BiUser)userMappingObj;
		}
		StringBuilder sb = new StringBuilder();
		for (BiProject project : projects) {
			if (project.getId().equals(query.getProjectId())) {
				sb.append(project.getUrl()).append("?");
				sb.append("Server=").append(project.getServer());
				sb.append("&Project=").append(project.getProject());
				sb.append("&Port=").append(project.getPort());
				if (biUser!=null) {
					sb.append("&uid=").append(biUser.getUsername());
					if (StringUtils.isNotEmpty(biUser.getPassword())) {
						sb.append("&pwd=").append(biUser.getPassword());
					} else {
						sb.append("&pwd=");
					}
				} else {
					sb.append("&uid=").append(project.getDefaultUid());
					if (StringUtils.isNotEmpty(project.getDefaultPwd())) {
						sb.append("&pwd=").append(AESUtil.decrypt(project.getDefaultPwd(),Constant.DEFAULT_SALT));
					} else {
						sb.append("&pwd=");
					}
				}
				if (query.getReportType().intValue()==EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition) {
					sb.append("&reportId=").append(query.getReportId());
					sb.append("&evt=4001&src=mstrWeb.4001&visMode=0");
					if ("edit".equals(type)) {
						sb.append("&reportViewMode=4");
					}
				} else if (query.getReportType().intValue()==EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition) {
					sb.append("&documentID=").append(query.getReportId());
					sb.append("&evt=2048001&src=mstrWeb.2048001&visMode=0");
					if ("edit".equals(type)) {
						sb.append("&currentViewMedia=4");
					}
				}
				sb.append("&hiddensections=header,footer,path");
				if ("share".equals(type)) {
					sb.append(",dockTop,dockLeft");
					sb.append("&share=1");
				}
				break;
			}
		}
		
		return StringUtils.isNotEmpty(sb)?BaseApi.success(sb.toString()):BaseApi.error();
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param UserReport
	 * @param Model
	 * @return List<UserReport>
	 * @author zhoujun
	 * @Date 2018-06-05 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<UserReport> list(UserReport query, Model model){
		EntityWrapper<UserReport> warpper = new EntityWrapper<UserReport>(query);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-06-05 
	 */
	@RequestMapping("/edit")
	public String edit(UserReport query, Model model){
		EntityWrapper<UserReport> warpper = new EntityWrapper<UserReport>(query);
		UserReport record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-06-05 
	 */
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(UserReport record){
		EntityWrapper<UserReport> ew = new EntityWrapper<>();
		ew.eq("name", record.getName());
		UserReport report = service.selectOne(ew);
		if (report!=null) {
			return BaseApi.error("报表名称已存在！");
		}
		BiUser biUser = null;
		Object userMappingObj = SessionUtil.getSession(SessionKeys.USER_MAPPING_MSTR);
		if (userMappingObj!=null){
			biUser = (BiUser)userMappingObj;
		}
		
		
		if (biUser==null) {
			biUser = new BiUser();
			BiProject mstrProject = getCache(Constant.CACHE_MSTR_PROJECT+"0", BiProject.class);
			biUser.setUsername(mstrProject.getDefaultUid());
			biUser.setPassword(AESUtil.decrypt(mstrProject.getDefaultPwd(), Constant.DEFAULT_SALT));
			biUser.setServerName(mstrProject.getServer());
			biUser.setProjectName(mstrProject.getProject());
			biUser.setServerPort(mstrProject.getPort());
			//biUser.setProjectId(mstrProject.getId());
		}
		
		String reportId="";
		try {
			//record.setProjectId(biUser.getProjectId());
			if (record.getReportType().intValue()==EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition) {
				reportId = CreatorApi.createReport(biUser,record.getName());
			} else if(record.getReportType().intValue()==EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition) {
				reportId = CreatorApi.createDocument(biUser,record.getName());
			}
			if (StringUtils.isNotEmpty(reportId)) {
				record.setReportId(reportId);
				record.setUserId(SessionUtil.getUserId());
				record.setCreateTime(new Date());
				return record.insert() ? BaseApi.success() : BaseApi.error();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-06-05 
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(UserReport record){
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(UserReport record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	@RequestMapping("/updateNum/{action}")
	@ResponseBody
	public BaseApi updateNum(@PathVariable("action")String type,String id, String methodType) {
		boolean result = false;
		if ("read".equals(type)){
			result = service.updateNum(id,"read_num","+");
		} else if ("likes".equals(type)){
			if ("+".equals(methodType)) {
				UserLikes userLikes = new UserLikes();
				userLikes.setUserId(SessionUtil.getUserId());
				userLikes.setResourceId(id);
				userLikes.setResourceType(2);
				userLikes.setCreateTime(new Date());
				userLikes.insert();
				//userLikesService.save(userLikes);
			} else {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("resource_id", id);
				params.put("resource_type", 2);
				params.put("user_id", SessionUtil.getUserId());
				userLikesService.deleteByMap(params);
				//userLikesService.delete(2,SessionUtil.getUserId(), id);
			}
			result = service.updateNum(id,"likes_num",methodType);
		}
		return result ? BaseApi.success() : BaseApi.error();
	}
	
	private String getMstrProjectUrl() {
		BiUser biUser = null;
		Object userMappingObj = SessionUtil.getSession(SessionKeys.USER_MAPPING_MSTR);
		if (userMappingObj!=null){
			biUser = (BiUser)userMappingObj;
		}
		
		BiProject project = getCache(Constant.CACHE_MSTR_PROJECT+ "0", BiProject.class);
		if (biUser==null) {
			biUser = new BiUser();
			biUser.setUsername(project.getDefaultUid());
			biUser.setPassword(AESUtil.decrypt(project.getDefaultPwd(), Constant.DEFAULT_SALT));
			biUser.setServerName(project.getIp());
			biUser.setProjectName(project.getProject());
			biUser.setServerPort(project.getPort());
			//biUser.setProjectId(project.getId());
		}
		StringBuilder sb = new StringBuilder();
		sb.append(project.getUrl()).append("?");
		sb.append("Server=").append(project.getServer());
		sb.append("&Project=").append(project.getProject());
		sb.append("&Port=").append(project.getPort());
		if (biUser!=null) {
			sb.append("&uid=").append(biUser.getUsername());
			if (StringUtils.isNotEmpty(biUser.getPassword())) {
				sb.append("&pwd=").append(biUser.getPassword());
			} else {
				sb.append("&pwd=");
			}
		}
		return sb.toString();
	}
	
	private String getMstrIndependReportCommonUrl(BiUser bu,BiProject bp,BiServer bs){
		StringBuilder sb = new StringBuilder();
		sb.append(bs.getUrl()).append("?");
		sb.append("Server=").append(bs.getServer());
		sb.append("&Project=").append(bp.getProject());
		sb.append("&Port=").append(bs.getPort());
		sb.append("&uid=").append(bu.getUsername());
		sb.append("&pwd=").append(bu.getPassword());
		return sb.toString();
	}
	
}
