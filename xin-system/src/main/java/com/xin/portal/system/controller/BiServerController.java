package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.microstrategy.webapi.EnumDSSXMLObjectSubTypes;
import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.bi.bo.BoUtil;
import com.xin.portal.bi.mstr.MstrSessionFactory;
import com.xin.portal.bi.mstr.RestApiParam;
import com.xin.portal.bi.mstr.RestApiUtil;
import com.xin.portal.bi.mstr.api.ProjectaApi;
import com.xin.portal.bi.tableau.RestApiUtils;
import com.xin.portal.bi.tableau.api.TsResponse;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.TreeNode;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.service.BiMappingService;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**  
* @Title: com.xin.portal.system.controller.BiServerController 
* @Description: 集成系统
* @author zhoujun  
* @date 2018-12-04
* @version V1.0  
*/ 
@Controller
@RequestMapping("/biServer")
public class BiServerController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(BiServerController.class);

	private static final String PATH = "biServer/";
	
	@Autowired
	private BiServerService service;
	@Autowired
	private BiProjectService biProjectService;
	@Autowired
	private BiMappingService biMappingService;
	@Autowired
	private BiUserService biUserService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return PATH + "index";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return PATH + "add";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(){
		return PATH + "edit";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-04 
	 */
	@SystemLog(module=LanguageParam.BI_SYSTEM_INTEGRATED ,operation=LanguageParam.ACTIONLISTSEE,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(BiServer query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-04 
	 */
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(BiServer query){
		EntityWrapper<BiServer> warpper = new EntityWrapper<BiServer>(query);
		warpper.orderBy("sort", false);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.BI_SYSTEM_INTEGRATED ,operation=LanguageParam.DETAILSQUERY,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		BiServer result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}

	@GetMapping("/{id:.+}/authToken")
	@ResponseBody
	public BaseApi authToken(@PathVariable String id, HttpServletResponse response){
		BiServer biServer = service.selectById(id);
		Map<String,Object> params = new HashMap<String,Object>(){{
			put("username",biServer.getDefaultUid());
			put("password",biServer.getDefaultPwd());
			put("loginMode","1");
		}};
		String authToken = RestApiUtil.getToken(biServer.getUrl(),params);
		response.addHeader(RestApiParam.XMSTRAUTHTOKEN,authToken);
		response.addHeader("Access-Control-Allow-Credentials","true");
		response.addHeader("Access-Control-Allow-Headers","content-type");
		response.addHeader("Access-Control-Allow-Methods","HEAD,GET,POST,PUT,DELETE,PATCH");
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Expose-Headers","x-mstr-authToken, x-mstr-projectId, x-mstr-identitytoken, updatePolicy");
		return BaseApi.data(authToken);
	}

	@GetMapping("/{id:.+}/identityToken")
	@ResponseBody
	public BaseApi identityToken(@PathVariable String id){
		BiServer biServer = service.selectById(id);
		Map<String,Object> params = new HashMap<String,Object>(){{
			put("username",biServer.getDefaultUid());
			put("password",biServer.getDefaultPwd());
			put("loginMode","1");
		}};
		String identityToken = RestApiUtil.identityTokenPost(biServer.getUrl(),params);
		return BaseApi.data(identityToken);
	}
	
	@GetMapping("/{id:.+}/projects")
	@ResponseBody
	public BaseApi projects(@PathVariable String id){
		BiServer biServer = service.selectById(id);
		BaseApi result = null;
		switch (biServer.getType()) {
//			case BiServer.TYPE_MSTR:
//				result = BaseApi.list(ProjectaApi.list(biServer));
//				break;
			case BiServer.TYPE_MSTR_LIBRARY:
				Map<String,Object> params = new HashMap<String,Object>(){{
					put("username",biServer.getDefaultUid());
					put("password",biServer.getDefaultPwd());
					put("loginMode","1");
				}};
				JSONObject resultProject = RestApiUtil.projects(biServer.getUrl(),params);
				if (HttpStatus.OK.value() == resultProject.getIntValue("code")) {
					JSONArray jsonArray = resultProject.getJSONArray("content");
					result = BaseApi.list(jsonArray);
				}
				break;
			default :
				//result = BaseApi.list(ProjectaApi.list(biServer));
				EntityWrapper<BiProject> ew = new EntityWrapper<>();
				ew.eq("bi_server_id",id);
				List<BiProject> list = biProjectService.selectList(ew);
				result = BaseApi.list(list);
				break;

		}
		
		return result;
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-04 
	 */
	@SystemLog(module=LanguageParam.BI_SYSTEM_INTEGRATED ,operation=LanguageParam.ACTIONADD,type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(BiServer record){//由于上传服务用户使用的是服务名称所以这个名称应该校验重复
		if(record.getName()!=null){
			EntityWrapper<BiServer> ew = new EntityWrapper<>();
			ew.eq("name", record.getName().trim());
			List<BiServer> list = service.selectList(ew);
			if(list!=null && list.size()>0){
				return new BaseApi().put("code", -2);
			}else{
				record.setWaterMark(record.getWaterMark()==null?0:1);
				return record.insert() ? BaseApi.success() : BaseApi.error();
			}
		}
		return BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param record
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-04 
	 */
	@SystemLog(module=LanguageParam.BI_SYSTEM_INTEGRATED ,operation=LanguageParam.ACTIONUPDATE,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(BiServer record){//由于上传服务用户使用的是服务名称所以这个名称应该校验重复
		if(record.getName()!=null){
			EntityWrapper<BiServer> ew = new EntityWrapper<>();
			ew.eq("name", record.getName().trim());
			ew.ne("id", record.getId());
			List<BiServer> list = service.selectList(ew);
			if(list!=null && list.size()>0){
				return new BaseApi().put("code", -2);
			}else{
				record.setWaterMark(record.getWaterMark()==null?0:1);
				return record.updateById() ? BaseApi.success() : BaseApi.error();
			}
		}
//		record.setWaterMark(record.getWaterMark()==null?0:1);
//		return record.updateById() ? BaseApi.success() : BaseApi.error();
		return BaseApi.error();
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module=LanguageParam.BI_SYSTEM_INTEGRATED ,operation=LanguageParam.ACTIONDELETE,type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		//查询系统中包含的项目
		EntityWrapper<BiProject> ew1 = new EntityWrapper<>();
		ew1.eq("bi_server_id",id);
		List<BiProject> projectList = biProjectService.selectList(ew1);
		//查询系统中包含的用户
		EntityWrapper<BiUser> ew2 = new EntityWrapper<>();
		ew2.eq("bi_server_id",id);
		List<BiUser> userList = biUserService.selectList(ew2);
		
		if (!projectList.isEmpty()||!userList.isEmpty()) {
			return BaseApi.error("系统中包含项目或用户,无法删除!");
		}
		
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
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
	@SystemLog(module=LanguageParam.BI_SYSTEM_INTEGRATED ,operation=LanguageParam.VERIFY_STATUS,type=LogType.query)
	@RequestMapping(value="/validate/{id}",method=RequestMethod.GET)
	@ResponseBody
	public BaseApi validate(@PathVariable String id){
		BaseApi result = BaseApi.success();
		BiServer record = service.selectById(id);
		switch(record.getType()){
		case BiServer.TYPE_MSTR:
			try {
				MstrSessionFactory.checkServer(record);
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
				BoUtil.checkServer(record);
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
		case BiServer.TYPE_TABLEAU:
			try {
				TsResponse tsResponse = RestApiUtils.getInstance().serverinfo();
				result = new BaseApi().put("code",0);
				//result = BaseApi.success("正常");//"正常"
				record.setState("normal");//"正常"
			}catch (Exception e) {
				result = BaseApi.error(e.getMessage());
				record.setState(e.getMessage());
			}
			break;
		case BiServer.TYPE_DIY:
			break;
		}
		record.updateById();
		return result;
	}


	/**
	 * Library 报表
	 * @return
	 */
	@RequestMapping("/{serverId}/{projectId}/{folderId}/reports")
	@ResponseBody
	public List<TreeNode> reports(@PathVariable("serverId")String serverId,@PathVariable("projectId")String projectId, @PathVariable("folderId")String folderId){
		BiServer biServer = service.selectById(serverId);
		List<TreeNode> list = Lists.newArrayList();

		Map<String,Object> params = new HashMap<String,Object>(){{
			put("username",biServer.getDefaultUid());
			put("password",biServer.getDefaultPwd());
			put("loginMode","1");
			put(RestApiUtil.HEADER_PROJECTID,projectId);
			put("id",folderId);
		}};

		JSONObject resultProject = RestApiUtil.folderGet(biServer.getUrl(),params);
		if (HttpStatus.OK.value() == resultProject.getIntValue("code")) {
			JSONArray jsonArray = resultProject.getJSONArray("content");
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				TreeNode treeNode = new TreeNode();
				treeNode.setId(jsonObject.getString("id"));
				treeNode.setName(jsonObject.getString("name"));
				treeNode.setParentId(folderId);
				treeNode.setPid(folderId);
				int type = jsonObject.getIntValue("type");
				switch (type) {
					case EnumDSSXMLObjectTypes.DssXmlTypeFolder:
						treeNode.setIsParent(true);
						treeNode.setNocheck(true);
						break;
					case EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition:
						treeNode.setIcon("../../images/Report.png");
						break;
					case EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition:
						int subtype = jsonObject.getIntValue("subtype");
						if (EnumDSSXMLObjectSubTypes.DssXmlSubTypeReportWritingDocument==subtype) {
							treeNode.setIcon("../../images/Dossier.png");
						}
						break;
					default:
						break;
				}
				list.add(treeNode);
			}
		}

		return list;
	}


	@RequestMapping("/{serverId}/{projectId}/{documentId}/prompts")
	@ResponseBody
	public BaseApi prompts(@PathVariable("serverId")String serverId,@PathVariable("projectId")String projectId, @PathVariable("documentId")String documentId) {
		BiServer biServer = service.selectById(serverId);
		List<JSONObject> list = Lists.newArrayList();
		
		BiMapping bm = new BiMapping();
		bm.setUserId(SessionUtil.getUserId());
		bm.setBiServerId(biServer.getId());
		List<BiMapping> biList = biMappingService.selectBiUserBySysUserAndServerId(bm);
		String userName = (biList!=null&&biList.size()>0)?biList.get(0).getBiUserName():biServer.getDefaultUid();
		String password = (biList!=null&&biList.size()>0)?biList.get(0).getBiPassword():biServer.getDefaultPwd();
		Map<String, Object> params = new HashMap<String, Object>() {{
			put("username", userName);
			put("password", password);
			put("loginMode", "1");
			put(RestApiUtil.HEADER_PROJECTID, projectId);
			put("id", documentId);
		}};

		JSONObject resultProject = RestApiUtil.instancePrompts(biServer.getUrl(), params);
		if (HttpStatus.OK.value() == resultProject.getIntValue("code")) {
			return BaseApi.list(resultProject.getJSONArray("content"));
		}
		return BaseApi.list(list);
	}

	@RequestMapping("/{serverId}/{projectId}/{documentId}/promptsAnswers")
	@ResponseBody
	public BaseApi promptsAnswers(@PathVariable("serverId")String serverId,@PathVariable("projectId")String projectId, @PathVariable("documentId")String documentId,@RequestBody JSONObject prompts) {
		if(prompts.getJSONArray("prompts")!=null && (prompts.getJSONArray("prompts").size()>0)){
			BiServer biServer = service.selectById(serverId);
			List<JSONObject> list = Lists.newArrayList();

			Map<String, Object> params = new HashMap<String, Object>() {{
				put("username", biServer.getDefaultUid());
				put("password", biServer.getDefaultPwd());
				put("loginMode", "1");
				put(RestApiUtil.HEADER_PROJECTID, projectId);
				put("dossierId", documentId);
			}};

			logger.info(prompts.toJSONString());

			JSONObject resultProject = RestApiUtil.promptsAnswers(biServer.getUrl(), params, prompts.toJSONString());
			if (HttpStatus.NO_CONTENT.value() == resultProject.getIntValue("code")) {
				return BaseApi.success();
			}
			return BaseApi.error();
		}
		return BaseApi.success();
	}

	@RequestMapping("/{serverId}/{projectId}/{reportId}/instances")
	@ResponseBody
	public BaseApi reportInstances(@PathVariable("serverId")String serverId,@PathVariable("projectId")String projectId, @PathVariable("reportId")String reportId,Integer pageSize,Integer pageNum) {
		BiServer biServer = service.selectById(serverId);
		List<JSONObject> list = Lists.newArrayList();

		Map<String, Object> params = new HashMap<String, Object>() {{
			put("username", biServer.getDefaultUid());
			put("password", biServer.getDefaultPwd());
			put("loginMode", "1");
			put(RestApiUtil.HEADER_PROJECTID, projectId);
			put("offset",pageNum);
			put("limit",pageSize);
			put("id", reportId);
		}};


		JSONObject resultProject = RestApiUtil.reportInstances(biServer.getUrl(), params);
		if (HttpStatus.OK.value() == resultProject.getIntValue("code")) {
			return BaseApi.success();
		}
		return BaseApi.error();
	}
	
}

