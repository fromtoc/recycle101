package com.xin.portal.system.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.google.common.collect.Lists;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.bo.BoUtil;
import com.xin.portal.bi.mstr.MstrSessionFactory;
import com.xin.portal.bi.mstr.MstrSessionManagement;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.bean.TreeNode;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.Constant.BiType;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassPath: com.xin.portal.report.controller.MstrProjectController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-11-27 下午6:25:48
 */
@Controller
@RequestMapping("/biProject")
public class BiProjectController extends BaseController {
	
	private final Logger logger = LoggerFactory.getLogger(BiProjectController.class);
	
	private static final String DIR = "biProject/";
	
	@Autowired
	private BiProjectService service;
	@Autowired
	private BiServerService biServerService;
	
	/**
	 * @Title: index 
	 * @Description:  TODO
	 * @return String
	 * @author zhoujun
	 * @Date 2017-11-27 下午6:26:14
	 */
	@RequestMapping("/index")
	public String index(){
		return DIR + "index";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:53:38
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}
	
	/**
	 * @Title: select 
	 * @Description:  TODO
	 * @param id
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2017-11-27 下午6:28:17
	 */
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<BiProject> page(BiProject query, Integer pageNumber, Integer pageSize){
		return service.page(query,pageNumber,pageSize);
	}
	
	@RequestMapping("/findList")
	@ResponseBody
	public List<BiProject> findList(BiProject query,Model model){
		return service.findList(query);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:53:47
	 */
	@RequestMapping("/edit")
	public String edit(BiProject query, Model model){
		model.addAttribute("record", query.selectById());
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-8-2 上午10:54:41
	 */
	@SystemLog(module = LanguageParam.BI_PROJECT_INTEGRATION , operation=LanguageParam.ADD_PROJECT,  sort="name", name="title", type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(BiProject query){
		if(query.getIsIndependPro()==null || query.getIsIndependPro()==0){
			query.setIsIndependPro(0);
		}else if(query.getIsIndependPro()==1){
			BiProject bip = new BiProject();
			bip.setIsIndependPro(0);
			Wrapper<BiProject> w = new EntityWrapper<>();
			w.eq("bi_server_id", query.getBiServerId());
			service.update(bip, w);
		}
		return query.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-11-29 上午10:08:08
	 */
	@SystemLog(module = LanguageParam.BI_PROJECT_INTEGRATION , operation=LanguageParam.UPDATE_PROJECT, type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(BiProject query){
		if(query.getIsIndependPro()==null || query.getIsIndependPro()==0){
			query.setIsIndependPro(0);
		}else if(query.getIsIndependPro()==1){
			BiProject bip = new BiProject();
			bip.setIsIndependPro(0);
			Wrapper<BiProject> w = new EntityWrapper<>();
			w.eq("bi_server_id", query.getBiServerId());
			w.ne("id", query.getId());
			service.update(bip, w);
		}
		return query.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.BI_PROJECT_INTEGRATION , operation=LanguageParam.DELETE_PROJECT, type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(BiProject query){
		return query.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
	@RequestMapping("/select/{type}")
	public String select(Model model,@PathVariable("type")Integer type){
		EntityWrapper<BiServer> ewServer = new EntityWrapper<>();
//		if (BiServer.TYPE_MSTR == type) {
//			ewServer.in("type",BiServer.TYPE_MSTR + "," + BiServer.TYPE_MSTR_LIBRARY);
//		} else {
			ewServer.eq("type",type);
//		}
		List<BiServer> servers = biServerService.selectList(ewServer);
		model.addAttribute("servers", servers);
		if (servers!=null && !servers.isEmpty()) {
			EntityWrapper<BiProject> ew = new EntityWrapper<>();
			if (!StringUtils.isEmpty(type)){
				//ew.eq("type", type);
				//ew.orderBy("sort");
				ew.where("bi_server_id in (select id from t_bi_server where type = {0})", type);
			}
			List<BiProject> projects = service.selectList(ew);
			model.addAttribute("projects", projects);
		}

		model.addAttribute("type", type);
		return DIR + "select_reports";
	}
	
	@RequestMapping("/{id}/reports")
	@ResponseBody
	public List<TreeNode> reports(@PathVariable("id")String id, String parentId){
//		EntityWrapper<BiProject> ew = new EntityWrapper<>(query);
//		List<BiProject> projects = biProjectService.selectList(ew);
//		FolderBrowsing.browseSharedReportsFolder(project.getServer(), project.getPort(),
//				project.getProject(), project.getDefaultUid(), project.getDefaultPwd());
		BiProject biProject = service.findById(id);
		List<TreeNode> list = Lists.newArrayList();
		long startTime = System.currentTimeMillis();
		if (1==biProject.getType()) {
			list = MstrSessionManagement.getMenuFolder(biProject,parentId);
			
		} else if (2==biProject.getType()){
			list = BoUtil.getReportList(biProject, parentId);
			
		}
		
		logger.info("==========getForder cost =========={}",System.currentTimeMillis()-startTime);
		return list;
	}
	
	/**
	 * 
	 * @Title: validate 
	 * @Description:  验证系统是否正常
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018年12月20日 上午11:17:17
	 */
	@SystemLog(module=LanguageParam.BI_PROJECT_INTEGRATION ,operation=LanguageParam.VERIFY_STATUS,type=LogType.query)
	@RequestMapping(value="/validate/{id}",method=RequestMethod.GET)
	@ResponseBody
	public BaseApi validate(@PathVariable String id){
		BaseApi result = BaseApi.success();
		BiProject record = service.findById(id);
		switch(record.getType()){
		case BiServer.TYPE_MSTR:
			try {
				MstrSessionFactory.check(record);
				result = new BaseApi().put("code",0);
				//result = BaseApi.success("正常");//"正常"
				record.setState("normal");//"正常"
			}catch (Exception e) {
				result = BaseApi.error(e.getMessage());
				record.setState(e.getMessage());
			}
			break;
		case BiServer.TYPE_BO:
			try {
				BoUtil.check(record);
				result = new BaseApi().put("code",0);
				//result = BaseApi.success("正常");//"正常"
				record.setState("normal");//"正常"
			}catch (Exception e) {
				result = BaseApi.error(e.getMessage());
				record.setState(e.getMessage());
			}
			break;
		case BiServer.TYPE_MYSQL:
			break;
		case BiServer.TYPE_DIY:
			break;
		}
		record.updateById();
		return result;
	}
	
	@RequestMapping("/getReportUrl")
	@ResponseBody
	public BaseApi getReportUrl(Resource resource){
		//MstrProject project = (MstrProject) CacheManager.get(Constant.CACHE_PROJECT+resource.getProjectId());
		Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
//		for (int i=0;i<5;i++) {
//			BiProject mstrProject = cache.get(Constant.CACHE_MSTR_PROJECT+i, BiProject.class);
//			if (mstrProject==null) {
//				break;
//			} else if (mstrProject.getId().equals(report.getProjectId())) {
//				project = mstrProject;
//			}
//		}
		EntityWrapper<BiProject> ewBiProject = new EntityWrapper<>();
		ewBiProject.eq("type", BiType.MSTR);
		ewBiProject.eq("id", resource.getProjectId());
		BiProject project = service.selectOne(ewBiProject);
		StringBuilder sb = new StringBuilder();
		if (project!=null) {
			sb.append(project.getUrl()).append("?");
			sb.append("Server=").append(project.getServer());
			sb.append("&Project=").append(project.getProject());
			sb.append("&Port=").append(project.getPort());
			if (EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition==resource.getTypeValue()) {
				sb.append(project.getParamDoc());
				sb.append("&documentID=").append(resource.getReportId());
			} else if (EnumDSSXMLObjectTypes.DssXmlTypeFolder==resource.getTypeValue()) {
				sb.append(project.getParamFolder());
				sb.append("&folderID=").append(resource.getReportId());
			} else {
				sb.append(project.getParamReport());
				sb.append("&reportId=").append(resource.getReportId());
			}
			Object obj = SessionUtil.getSession(SessionKeys.USER_MAPPING_MSTR);
			if (obj!=null) {
				sb.append("&uid=").append(((BiUser)obj).getUsername());
				sb.append("&pwd=").append(((BiUser)obj).getPassword());
			} else {
				sb.append("&uid=").append(project.getDefaultUid());
				sb.append("&pwd=").append(project.getDefaultPwd());
			}
			if (!StringUtils.isEmpty(resource.getHiddenSections())) {
				sb.append("&hiddensections=").append(resource.getHiddenSections());
			}
			return BaseApi.data(sb.toString());
		}
		
		return BaseApi.error();
	}
	
	@RequestMapping("/getProjectList")
	@ResponseBody
	public List<BiProject> getProjectList(BiProject biProject){
		Wrapper<BiProject> w = new EntityWrapper<BiProject>(biProject);
		return biProject.selectList(w);
	}

}
