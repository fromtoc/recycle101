package com.xin.portal.system.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.CollectTree;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.model.UserCollect;
import com.xin.portal.system.model.UserSetting;
import com.xin.portal.system.service.CollectService;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.ResourceLogService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.service.UserSettingService;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.WebUtil;
/**
 * @Description:  用户收藏
 * @author xue
 * @Date 2018-5-15 上午10:52:43
 */
@RequestMapping("/collect")
@Controller
public class UserCollectController extends BaseController{
	private static final String DIR = "userCollect/";
	@Autowired
	private CollectService collectService;
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private ResourceLogService resourceLogService;
	@Autowired
	private UserSettingService userSettingService;
	
	@RequestMapping("/index")
	public String index(Model model){
		Config config = new Config();
		config.setCode("COLLECT_LOAD_TYPE");//asyncLoad 异步加载  synchLoad 同步加载
		EntityWrapper<Config> ew = new EntityWrapper<>(config);
		model.addAttribute("loadType", configService.selectOne(ew).getValue());
		Config configTheme = configService.findByCode(ConfigKeys.SYS_THEME, getTenantId());
		String ThemeStyle = configTheme.getValue();
		//先根据用户获取Theme 的样式
		UserSetting selectByUserId = userSettingService.selectByUserId(SessionUtil.getUserId());
		//如果用户没有设置则使用系统默认的样式
		if(selectByUserId != null && selectByUserId.getNavigaStyle() != null
				&& selectByUserId.getNavigaStyle() != ""){
			ThemeStyle = selectByUserId.getNavigaStyle();
		}
		model.addAttribute("sys_theme", ThemeStyle);
		return DIR + "list_document";
	}
	
	@RequestMapping("/showTree")
	public String showTree(Model model){
		return DIR + "showTree";
	}
	
	@RequestMapping("/addCollect")
	public String addCollect(Model model){
		return DIR + "addCollect";
	}
	
	@RequestMapping("/toIndex")
	public String toIndex(Model model){
		return DIR + "index";
	}
	
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.LOAD_ALL ,type=LogType.query)//查看用户的收藏（全部）
	@RequestMapping("/findAllList")
	@ResponseBody
	public List<UserCollect> findAllList(Model model, UserCollect record){
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		List<UserCollect> list = collectService.findAllCollectOfUser(record);
		return list;
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT ,operation = LanguageParam.HIERARCHICAL_LOADING ,type=LogType.query)//查看用户的收藏（分级）
	@RequestMapping("/findList")
	@ResponseBody
	public List<UserCollect> findList(Model model,UserCollect record){//参数 父级id ，[收藏id]
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		return collectService.findChildCollect(record);
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT ,operation = LanguageParam.VIEW_SINGLE ,type=LogType.query)//查看单个收藏
	@RequestMapping("/findCollectOne")
	@ResponseBody
	public UserCollect findCollectOne(Model model,UserCollect record){//参数 收藏id ，用户id， [收藏类型]
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		UserCollect collect = collectService.findCollectOne(record);
		return collect;
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.TREE_QUERY_ALL ,type=LogType.query)//查看全部树形
	@RequestMapping("/findAllForTree")
	@ResponseBody
	public List<CollectTree> findAllForTree(Model model, @RequestBody String queryType){//参数 用户id,查询类型
		JSONObject json = JSON.parseObject(queryType);
        String query=json.getString("queryType");
		UserCollect record = new UserCollect();
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		List<CollectTree> list = collectService.findAllCollectForTree(record,query);
		return list;
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.CREATE_FOLDER ,type=LogType.add)//创建文件夹
	@RequestMapping("/createCollectFloder")
	@ResponseBody
	public BaseApi createCollectFloder(Model model,UserCollect record){//参数 parentId  name
		//添加用户id，文件类型
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		record.setCollectType("folder");
		record.setColCreateTime(new Date());
		//获取该parentid下子节点中的最后一个的sort+1的值（新建文件夹的排序）
		int sort = collectService.findSortForNew(record);
		record.setColSort(sort);
		return collectService.insert(record)?BaseApi.success():BaseApi.error();		
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.COLLECTION_RESOURCE ,type=LogType.add)//创建文件夹
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(HttpServletRequest request, Model model,UserCollect record){//参数 parentId  name  resourceId coltype
		//添加用户id，文件类型
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		//查询是否已经收藏过了
		UserCollect query = new UserCollect();
		query.setResourceId(record.getResourceId());
		query.setUserId(userId);
		EntityWrapper<UserCollect> ew = new EntityWrapper<>(query);
		int count = collectService.selectCount(ew);
		if (count<=0) {
			record.setColCreateTime(new Date());
			//获取该parentid下子节点中的最后一个的sort+1的值（新建文件夹的排序）
			int sort = collectService.findSortForNew(record);
			record.setColSort(sort);
			
			//资源访问记录
			ResourceLog resourceLog = new ResourceLog();
			resourceLog.setCreater(userId);
			resourceLog.setCreateTime(new Date());
			resourceLog.setType(ResourceLog.TYPE_COLLECT_ADD);
			resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
			resourceLog.setResourceId(record.getResourceId());
			resourceLog.setIp(WebUtil.getIpAddr(request));
			resourceLog.setBrowser(WebUtil.getBrower(request));
			resourceLogService.save(resourceLog);
			
			return collectService.insert(record)?BaseApi.success():BaseApi.error();		
		}else{
			return BaseApi.error();
		}
	}

	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.MODIFY_FOLDER_NAME ,type=LogType.update)//修改文件夹名称
	@RequestMapping("/modifyCollectFolder")
	@ResponseBody
	public BaseApi modifyCollectFolder(Model model,UserCollect record){//参数 收藏id ，名称
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		return collectService.modifyCollectFolder(record);
		
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.DELETE_COLLECTION ,type=LogType.delete)  //删除收藏、文件夹
	@RequestMapping("/deleteCollect")
	@ResponseBody
	public BaseApi deleteCollect(Model model,UserCollect record,HttpServletRequest request){//参数 收藏id 
		//TODO  删除后要重新排序吗？   --现在没有重新排序
		String userId=SessionUtil.getUserId();
		record.setUserId(userId);
		UserCollect query = new UserCollect();
		query.setParentId(record.getId());
		query.setUserId(userId);
		List<UserCollect> collect = collectService.findCollectByParentId(query);
		if(collect!=null && collect.size()>0){
			return BaseApi.error(-9, "");
		}else{
			EntityWrapper<UserCollect> ew = new EntityWrapper<>(record);
			
			//资源访问记录
			ResourceLog resourceLog = new ResourceLog();
			resourceLog.setCreater(userId);
			resourceLog.setCreateTime(new Date());
			resourceLog.setType(ResourceLog.TYPE_COLLECT_DEL);
			resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
			resourceLog.setResourceId(record.getResourceId());
			resourceLog.setIp(WebUtil.getIpAddr(request));
			resourceLog.setBrowser(WebUtil.getBrower(request));
			resourceLogService.save(resourceLog);
			
			return collectService.delete(ew)==true ? BaseApi.success() : BaseApi.error();
		}
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.CANCEL_COLLECTION ,type=LogType.delete)  //删除收藏、文件夹
	@RequestMapping("/removeCollect")
	@ResponseBody
	public BaseApi removeCollect(Model model,UserCollect record,HttpServletRequest request){//参数 收藏id 
		//TODO  删除后要重新排序吗？   --现在没有重新排序
		String userId=SessionUtil.getUserId();
		if (record.getResourceId()!=null && record.getResourceId()!="") {
			record.setUserId(userId);
			EntityWrapper<UserCollect> ew = new EntityWrapper<>(record);
			
			//资源访问记录
			ResourceLog resourceLog = new ResourceLog();
			resourceLog.setCreater(userId);
			resourceLog.setCreateTime(new Date());
			resourceLog.setType(ResourceLog.TYPE_COLLECT_DEL);
			resourceLog.setCreaterName(SessionUtil.getUserInfo().getRealname());
			resourceLog.setResourceId(record.getResourceId());
			resourceLog.setIp(WebUtil.getIpAddr(request));
			resourceLog.setBrowser(WebUtil.getBrower(request));
			resourceLogService.save(resourceLog);
			
			return collectService.delete(ew)==true ? BaseApi.success() : BaseApi.error();
		}
		return BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.MOVE_COLLECTION ,type=LogType.update)   //移动收藏文件
	@RequestMapping("/movedCollect")
	@ResponseBody
	public BaseApi movedCollect(Model model,
			String ownerId, String ownerPid, String targetId, 
			String targetPid ,String moveType){//参数 收藏id ，用户id， [收藏类型]
		boolean result = false;
		if(!StringUtils.isEmpty(ownerId) && 
				!StringUtils.isEmpty(ownerPid) && 
				!StringUtils.isEmpty(targetId) && 
				!StringUtils.isEmpty(targetPid) &&
				!StringUtils.isEmpty(moveType)){
			if("prev".equals(moveType) || "next".equals(moveType)){
				if(ownerPid.equals(targetPid)){//同级别下移动
					//仅更换自己目录下的排序
					try {
						result = collectService.sameParentToSort(ownerId,targetId,targetPid,moveType);
						return result ? BaseApi.success():BaseApi.error();
					} catch (Exception e) {
						e.printStackTrace();
						return BaseApi.error();
					}
				} else {//非同级别移动
					//需要更换所有的自己原所在目录下的排序
					//更换所有目标所在目录的排序
					try {
						result = collectService.diffParentToSort(ownerId,ownerPid,targetId,targetPid,moveType);
						return result ? BaseApi.success():BaseApi.error();
					} catch (Exception e) {
						e.printStackTrace();
						return BaseApi.error();
					}
					
				}
			}else if("inner".equals(moveType)){
				//更改自己原所在目录下的排序
				//更改自己的排序号
				try {
					result = collectService.innerToSort(ownerId,ownerPid,targetId,targetPid);
					return result ? BaseApi.success():BaseApi.error();
				} catch (Exception e) {
					e.printStackTrace();
					return BaseApi.error();
				}
			}
//			String userId=SessionUtil.getUserId();
//			record.setUserId(userId);
//			UserCollect collect = collectService.findCollectOne(record);
		}else{
			return BaseApi.error();
		}
		return BaseApi.error();
	}
	
	
	
//	@RequestMapping("/showCollectOfType")//废弃
//	@ResponseBody
//	public PageModel<UserCollect> findCollectByType(UserCollect query){
//		String userId=SessionUtil.getUserId();
//		query.setUserId(userId);
//		Page<UserCollect> page = new Page<UserCollect>(query.getPageNumber(),query.getPageSize());
//		if (1==query.getColType()) {//报表
//			page.setRecords(collectService.findCollectList(page,query));
//		}else if(2==query.getColType()){//课件
//			page.setRecords(collectService.findCollectList(page,query));
//		}else if(3==query.getColType()){//案例
//			page.setRecords(collectService.findCollectList(page,query));
//		}
//		PageModel<UserCollect> list = new PageModel<>(page);
//		return list;
//		
//	}
	
//	@ResponseBody
//	@RequestMapping("/authRepeat")//验证是否已经添加过了
//	public BaseApi authRepeat(UserCollect record){//包含参数- 菜单id
//		String userId=SessionUtil.getUserId();
//		record.setUserId(userId);
//		EntityWrapper<UserCollect> ew = new EntityWrapper<>(record);
//		int result = collectService.selectCount(ew);
//		return result <= 0 ? BaseApi.success() : BaseApi.error();
//		
//	}
	
	
//	@SystemLog(module = "收藏 - 添加",name="resourceId",type=LogType.add)
//	@ResponseBody
//	@RequestMapping("/add")
//	@Transactional(rollbackFor=java.lang.Exception.class)
//	public BaseApi add(UserCollect record){//包含参数- 菜单id、收藏的父id
//		//TODO 增加  新增收藏排序。。。目前先不写
//		String userId=SessionUtil.getUserId();
//		record.setUserId(userId);
//		record.setColCreateTime(new Date());
//		//添加, 排序
////		if (record.getColType()==null || record.getColType().intValue()==0 || record.getColType().intValue()==1) {
////			record.setColType(1);
////		}else if (record.getColType().intValue()==2){
////			record.setColType(2);
////		}else if (record.getColType().intValue()==3){
////			record.setColType(3);
////		}
//		boolean result = collectService.save(record);
//		return result ? BaseApi.success() : BaseApi.error();
//		
//	}
//	@SystemLog(module = "收藏 - 移除",name="resourceId",type=LogType.delete)
//	@ResponseBody
//	@RequestMapping("/remove")
//	@Transactional(rollbackFor=java.lang.Exception.class)
//	public BaseApi remove(UserCollect record){//参数包含“菜单id”、‘收藏id’
//		String userId=SessionUtil.getUserId();
//		record.setUserId(userId);
//		EntityWrapper<UserCollect> ew = new EntityWrapper<>();
//		boolean result = collectService.delete(ew);
//		return result ? BaseApi.success() : BaseApi.error();
//	}
	
//	@ResponseBody
//	@RequestMapping("/delete")
//	@Transactional(rollbackFor=java.lang.Exception.class)//删除文件夹
//	public BaseApi delete(UserCollect record){//参数包含“文件夹id”、‘收藏id’,类型
//		boolean result = false;
//		String userId=SessionUtil.getUserId();
//		UserCollect collect = new UserCollect();
//		collect.setUserId(userId);
//		//查看是否有子类，如果含有子类提示不能删除，如果没有子类可以删除
//		//根据父id查找子类
//		List<UserCollect> list  =  collectService.findChildCollect(collect);
//		if (list!=null && list.size()>0) {
//			record.setUserId(userId);
//			EntityWrapper<UserCollect> ew = new EntityWrapper<>();
//			result = collectService.delete(ew);
//		}
//		return result ? BaseApi.success() : BaseApi.error();
//	}
	
	/**
	 * @Title: update 
	 * @Description:  报表收藏、查看
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:46:55
	 */
	@SystemLog(module = LanguageParam.COLLECTION_MANAGEMENT , operation = LanguageParam.ADD_UNCOLLECT ,type=LogType.collect)
	@RequestMapping("/updateNum/{action}")
	@ResponseBody
	public BaseApi updateNum(HttpServletRequest request,@PathVariable("action")String type,String id, String methodType,Integer colType) {
		boolean result = false;
//		if ("download".equals(type)) {
//			result = service.updateNum(id,"download_num",StringUtils.isEmpty(methodType)?"+":"-");
//		} else if ("read".equals(type)){
//			result = service.updateNum(id,"read_num",StringUtils.isEmpty(methodType)?"+":"-");
//		} else 
		if ("collect".equals(type)){
			if ("+".equals(methodType) || "-".equals(methodType)) {
				if ("+".equals(methodType)) {
					UserCollect userCollect = new UserCollect();
					userCollect.setUserId(SessionUtil.getUserId());
					userCollect.setResourceId(id);
					userCollect.setColCreateTime(new Date());
					collectService.save(userCollect);
				} else {
					EntityWrapper<UserCollect> ew = new EntityWrapper<>();
					ew.eq("type", colType);
					ew.eq("user_id", SessionUtil.getUserId());
					ew.eq("resourceId", id);
					collectService.delete(ew);
				}
				if (colType==1) {
					result = resourceService.updateNum(id,"collection_num",methodType);
				}
			}
		}
		return result ? BaseApi.success() : BaseApi.error();
	}
	
	
}
