package com.xin.portal.system.controller;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.xin.portal.bi.mstr.api.UserApi;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.mapper.BiUserMapper;
import com.xin.portal.system.model.BiIndependent;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.service.BiIndependentService;
import com.xin.portal.system.service.BiMappingService;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.util.ExcelUtil;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.PropertiesUtil;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;

/**
 * @Title: com.xin.portal.system.controller.BiUserController
 * @Description:
 * @author zhoujun
 * @date 2018-01-22
 * @version V1.0
 */
@Controller
@RequestMapping("/biUser")
public class BiUserController extends BaseController {

	private static final String DIR = "biUser/";

	private static final Logger logger = LoggerFactory.getLogger(BiUserController.class);

	@Autowired
	private BiUserService service;
	@Autowired
	private FileService fileService;
	@Autowired
	private BiServerService biServerService;
	@Value("${system.default_password:123456}")
	private String defaultPassword;
	@Autowired
	private BiProjectService biProjectService;
	@Autowired
	private BiIndependentService biIndependentService;
	@Autowired
	private ConfigService configService;
	@Autowired
	private BiMappingService biMappingService;
	

	/**
	 * @Title: index
	 * @Description: 访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@RequestMapping("/index")
	public String index() {
		return DIR + "index";
	}

	/**
	 * @Title: add
	 * @Description: 添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@RequestMapping("/add")
	public String add() {
		return DIR + "add";
	}

	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public String importExcel() {
		return DIR + "import";
	}

	/**
	 * @Title: page
	 * @Description: TODO
	 * @param query
	 * @return PageModel<MstrUser>
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION,operation=LanguageParam.ACTIONLISTSEE, type = LogType.query)
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi page(BiUser query, Integer pageNumber, Integer pageSize) {
		return BaseApi.page(service.page(query, pageNumber, pageSize));
	}

	/**
	 * @Title: select
	 * @Description: TODO
	 * @param id
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION ,operation=LanguageParam.USER_INFORMATION_VIEW, type = LogType.query)
	@RequestMapping("/findList")
	@ResponseBody
	public List<BiUser> findList(BiUser query, Model model) {
		EntityWrapper<BiIndependent> biw = new EntityWrapper<>();
		biw.eq("bi_project_id", query.getBiProjectId());
		biw.eq("bi_server_id", query.getBiServerId());
		List<BiIndependent> biiList = biIndependentService.selectList(biw);
		List<String> biUserIdList = new ArrayList<>();
		if(biiList!=null  && biiList.size()>0){
			for (BiIndependent biIndependent : biiList) {
				biUserIdList.add(biIndependent.getBiUserId());
			}
		}
		EntityWrapper<BiUser> warpper = new EntityWrapper<BiUser>(query);
		if(biUserIdList!=null && biUserIdList.size()>0){
			warpper.notIn("id", biUserIdList);
		}
		return service.selectList(warpper);
	}

	/**
	 * @Title: edit
	 * @Description: 编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@RequestMapping("/edit")
	public String edit(BiUser query, Model model) {
		EntityWrapper<BiUser> warpper = new EntityWrapper<BiUser>(query);
		BiUser record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}

	/**
	 * @Title: save
	 * @Description: 保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION , operation=LanguageParam.ACTIONADD, sort="name", name="username", type = LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(BiUser record) {
		BiServer biServer = biServerService.selectById(record.getBiServerId());
		record.setType(""+biServer.getType());
////展示注掉同步创建mstr的方法--后面重新设计如何实现
//		if(biServer.getType()!=null && biServer.getType()==1){//MSTR
//			//record.setType(""+biServer.getType());
//			String objectId = UserApi.createUserAndGrant(biServer, record);
//			if(objectId!=null && objectId!=""){
//				/*//record中存入projectName serverName port 等
//				//根据serverID获取server信息
//				Wrapper<BiProject> w = new EntityWrapper<>();
//				w.eq("bi_server_id", record.getBiServerId());
//				w.eq("is_indepen_pro", 1);
//				BiProject bp = (BiProject) biProjectService.selectOne(w);
//				if(bp!=null){
//					record.setServerName(biServer.getServer());
//					record.setServerPort(biServer.getPort());
//					record.setProjectName(bp.getProject());
//					//获取server下自主分析项目
//					String folderId = FolderApi.createFolder(record,record.getUsername());//创建文件夹
//					if(folderId!=null && folderId!=""){
//						record.setOwnFolderId(folderId);
//					}
//				}*/
//				record.setBiObjectId(objectId);
//				return record.insert() ? BaseApi.success() : BaseApi.error();
//			}
//			return BaseApi.error();
//		}
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}

	/**
	 * @Title: update
	 * @Description: 更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-01-22
	 */
	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION , operation=LanguageParam.ACTIONUPDATE, sort="tfToName1", 
			tfToName1="record,id,username,com.xin.portal.system.mapper.BiUserMapper,selectById", type = LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(BiUser record) {
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION , operation=LanguageParam.RESET_PASSWORD, sort="tfToName1", 
			tfToName1="id,id,username,com.xin.portal.system.mapper.BiUserMapper,selectById", type = LogType.update)
	@RequestMapping("/resetPwd/{id}")
	@ResponseBody
	public BaseApi resetPwd(@PathVariable("id")String id) {
		BiUser biUser = service.selectById(id);
		if (biUser==null) {
			return BaseApi.error();
		}
////    展示去掉mstr重置密码的方法，所有用户都重置为默认密码：123456
		biUser.setPassword(defaultPassword);
//		BiServer biServer = biServerService.selectById(biUser.getBiServerId());
//		if(biServer.getType()!= null && biServer.getType()==BiServer.TYPE_MSTR){//目前只有mstr可以重置服务系统的密码
//			biUser.setPassword(defaultPassword);
//			boolean result = UserApi.updatePassword(biServer,biUser);
//			if (result) {
//				return biUser.updateById() ? BaseApi.success() : BaseApi.error();
//			}
//		}else{//其他的服务系统暂时没有重置密码的方法，提示暂时未实现
//			return new BaseApi().put("code", -2);
//		}
		return BaseApi.error();
	}
	
	

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION , operation=LanguageParam.ACTIONDELETE, before="tfPToROut1", sort="tfPToROut1", 
			tfPToROut1="id,id,username,com.xin.portal.system.mapper.BiUserMapper,selectById", type = LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id) {
		
		BiUser biUser = service.selectById(id);
		if (biUser==null) {
			return BaseApi.error();
		}
		//查询BI账户中是否包含映射关系
		EntityWrapper<BiMapping> ewBiMapping = new EntityWrapper<>();
		ewBiMapping.eq("bi_user_id", id);
		List<BiMapping> biMappingList = biMappingService.selectList(ewBiMapping);
		if (!biMappingList.isEmpty()) {
			return BaseApi.error("BI账户中存在映射关系,无法删除!");
		}
		
		
		return service.del(biUser)? BaseApi.success() : BaseApi.error();
	}

	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION , operation=LanguageParam.ACTION_IMPORT, type = LogType.importData)
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) {
		FileModel fileModel = fileService.upload(getUploadPath(request) + "import", "/upload/import", file, null, false,
				FileModel.BIUSER_IMPORT);

		if (fileModel != null) {
			List<Map<Integer, String>> list = ExcelUtil.readExcelData(fileModel.getSavedPath());
			// 判断数据条数
			if (list == null || list.size() < 2) {
				return new BaseApi().put("code",4);
				/*String msg = PropertiesUtil.getLocaleMessage(getLocale(), "validation.excel_check_content");
				return BaseApi.error(msg);*/
			}

			// 校验服务器名称列，只取第一条有效数据判断，默认一个Excel对应一个Server
			EntityWrapper<BiServer> ew = new EntityWrapper<>();
			ew.eq("name", list.get(1).get(0));
			logger.info("===import server : {}", list.get(1).get(0));
			BiServer biServer = biServerService.selectOne(ew);
			if (biServer == null) {
				return new BaseApi().put("code",1);
				//return BaseApi.error("服务器名称不匹配");//服务器名称不匹配
			}

			/*
			 * 示例Excel 服务器名称 | 账户名 | 账户组 | 密码 | 备注
			 * ------------------------------------------------------- 测试环境175
			 * Mstr | demo | | 123456 | 示例数据
			 * 
			 */
			List<BiUser> biUserList = Lists.newArrayList();// 原始数据列表
			for (int i = 1; i < list.size(); i++) {
				Map<Integer, String> rowMap = list.get(i);
				BiUser biUser = new BiUser(rowMap.get(1), rowMap.get(3));
				biUser.setServerName(rowMap.get(0));
				biUser.setBiServerId(biServer.getId());
				biUser.setRemark(rowMap.get(4));

				biUserList.add(biUser);
			}
			logger.info("===before check size: {}", biUserList.size());
			// List<BiUser> biUserListUnique =
			// biUserList.stream().collect(collectingAndThen(toCollection(() ->
			// new TreeSet<>(comparingLong(BiUser::getUsername))),
			// ArrayList::new));
			// 排除空账号后的数据列表
			List<BiUser> filterList = biUserList.stream().filter(
					item -> StringUtils.isNotEmpty(item.getUsername()) && StringUtils.isNotEmpty(item.getServerName()))
					.collect(Collectors.toList());
			logger.info("===after Empty size: {}", filterList.size());
			String reason = "";
			if (biUserList.size() != filterList.size()) {
				//reason = "Server name, account name cannot be empty";//服务器名称、账户名不能为空。
				reason="1";
			}
			// 排除Excel中自身账号重复的数据列表
			List<BiUser> biUserListUnique = filterList.stream()
					.collect(Collectors.collectingAndThen(
							Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BiUser::getUsername))),
							ArrayList<BiUser>::new));
			if (biUserList.size() != biUserListUnique.size()) {
				//reason = "The import account name cannot be repeated";//导入账户名不能重复
				reason="2";
			}
			logger.info("===after Unique size: {}", biUserListUnique.size());
			// 排除数据库中重名的数据列表
			EntityWrapper<BiUser> ewBiUser = new EntityWrapper<>();
			ewBiUser.eq("bi_server_id", biServer.getId());
			List<BiUser> biUserListDb = service.selectList(ewBiUser);
			List<BiUser> biUserListUniqueDb = biUserListUnique.stream().filter(item -> !biUserListDb.contains(item))
					.collect(Collectors.toList());
			// 与数据库重复的用户名列表，用于前台提示
			List<String> usernamesRepeat = biUserListUnique.stream().filter(item -> biUserListDb.contains(item))
					.collect(Collectors.toList()).stream().map(BiUser::getUsername).collect(Collectors.toList());
			if (biUserListUniqueDb.size() != biUserListUnique.size()) {
				//reason = usernamesRepeat.size() + " The account name is already in the database :" + usernamesRepeat.toString();//账户名已存在数据库中
				reason="3";

			}
			if (biUserListUniqueDb.size() == 0) {
				BaseApi res=new BaseApi();
				res.put("usernamesRepeatsize",usernamesRepeat.size());
				res.put("usernamesRepeattoString",usernamesRepeat.toString());
				res.put("biUserListUniqueDbsize",biUserListUniqueDb.size());
				res.put("biUserListsize",biUserList.size());
				res.put("reason",reason);
				res.put("code",2);
				return res;
				//BaseApi.success("Expected import" + biUserList.size()  + "article, actual import" + biUserListUniqueDb.size() + "article," + reason);
						/*.success("期望导入 " + biUserList.size() + " 条，实际导入 " + biUserListUniqueDb.size() + " 条," + reason);*/
			}
			List<BiUser> listSync = Lists.newArrayList();
			// 1-mstr 2-bo
			if (BiServer.TYPE_MSTR == biServer.getType()) {
				//listSync = UserApi.createUserAndGrantBatch(biServer, biUserListUniqueDb);//同步创建Mstr代码暂时去掉
				List<BiUser> mstrExist = listSync.stream().filter(item -> !biUserListUniqueDb.contains(item))
						.collect(Collectors.toList());
				if (listSync.size() < biUserListUniqueDb.size()) {
					// 同步创建MSTR 账号
					/*reason = "Mstr The account already exists "
						+ mstrExist.stream().map(BiUser::getUsername).collect(Collectors.toList()).toString();//Mstr中账户已存在*/
					reason="4";
					if (listSync.size() == 0) {
						BaseApi res=new BaseApi();
						res.put("reason",reason);
						res.put("code",2);
						res.put("biUserList.size",biUserList.size());
						res.put("biUserListUniqueDb.size",biUserListUniqueDb.size());
						res.put("mstrExist",mstrExist.stream().map(BiUser::getUsername).collect(Collectors.toList()).toString());
						return res;
						/*return BaseApi.success(
							"Expect to import " + biUserList.size() + " individual ，The actual import " + biUserListUniqueDb.size() + " individual," + reason);*/
						/*"期望导入 " + biUserList.size() + " 条，实际导入 " + biUserListUniqueDb.size() + " 条," + reason);*/
					}
				}
			}else{
				listSync = biUserListUniqueDb;
			}

			boolean result = service.saveBatch(biServer, listSync);
			if (biUserList.size() == listSync.size() && result) {
				BaseApi res=new BaseApi();
				res.put("code",3);
				res.put("biUserListUnique",biUserListUnique.size ());
				return res;
				/*return BaseApi.success (" import successful!Total "+ biUserListUnique.size () +" data ");*/
			/*	return BaseApi.success("导入成功！共 " + biUserListUnique.size() + " 条数据");*/
			} else {
				BaseApi res=new BaseApi();
				res.put("usernamesRepeatsize",usernamesRepeat.size());
				res.put("usernamesRepeattoString",usernamesRepeat.toString());
				res.put("biUserListUniqueDbsize",biUserListUniqueDb.size());
				res.put("biUserListsize",biUserList.size());
				res.put("reason",reason);
				res.put("code",2);

				return res;
				/*return BaseApi
						.success (" expect import "+ biUserList.size () +" clause, actual import "+ biUserListUniqueDb.size () +" clause," + reason);
						//.success("期望导入 " + biUserList.size() + " 条，实际导入 " + biUserListUniqueDb.size() + " 条," + reason);*/
			}
		}
		return BaseApi.error();
	}

	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION , operation=LanguageParam.SYNCHRONOUS_USER, type = LogType.sync)
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi check(BiUser query) {
		service.check(query);
		return BaseApi.success();
	}

	@SystemLog(module = LanguageParam.BI_USER_INTEGRATION , operation=LanguageParam.EXPORT_USER, type = LogType.export)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(BiUser query, HttpServletRequest request, HttpServletResponse response) {
		// 获取数据
		List<BiUser> list = service.findList(query);
		String lang = "zh_CN";
		Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, this.getTenantId());
		if(config!=null){
			lang = config.getValue();
		}
		// excel标题
		String[] title = {LangTransform.getLocaleLang(lang, LanguageParam.SERVER_NAME),
				LangTransform.getLocaleLang(lang, LanguageParam.ACCOUNT_NAME), 
				LangTransform.getLocaleLang(lang, LanguageParam.ACCOUNT_GROUP), 
				LangTransform.getLocaleLang(lang, LanguageParam.PASSWORD),
				LangTransform.getLocaleLang(lang, LanguageParam.DESCRIBE), 
				LangTransform.getLocaleLang(lang, LanguageParam.UPDATED_DATE), 
				LangTransform.getLocaleLang(lang, LanguageParam.CHECK_STATUS),};
//		String[] title = {"Server name "," account name ", "account group "," password ", "description "," update date ", "check status"};
		//String[] title = { "服务器名称", "账户名", "账户组", "密码", "描述", "更新日期", "检查状态" };
		// excel文件名
//		String fileName =  "BI account integration information _ export "+ System.currentTimeMillis() + ".xls";
		//String fileName = "BI账户集成信息_导出" + System.currentTimeMillis() + ".xls";
		String fileName = LangTransform.getLocaleLang(lang, LanguageParam.BI_ACCOUNT_INTEGRATION_INFO_EXPORT) + System.currentTimeMillis() + ".xls";
		// sheet名
		String sheetName = LangTransform.getLocaleLang(lang, LanguageParam.BI_ACCOUNT_INFO);//账户信息
		String[][] content = new String[list.size()][title.length];
		for (int i = 0; i < list.size(); i++) {
			content[i] = new String[title.length];
			BiUser obj = list.get(i);
			content[i][0] = obj.getServerName();
			content[i][1] = obj.getUsername();
			content[i][2] = obj.getBiGroup();
			content[i][3] = obj.getPassword();
			content[i][4] = obj.getRemark();
			content[i][5] = obj.getUpdateTime()!=null?new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(obj.getUpdateTime()):"";
			content[i][6] = obj.getState();
		}

		// 创建HSSFWorkbook
		HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

		// 响应到客户端
		try {
			this.setResponseHeader(response, fileName);
			OutputStream os = response.getOutputStream();
			wb.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 发送响应流方法
	public void setResponseHeader(HttpServletResponse response, String fileName) {
		try {
			try {
				fileName = new String(fileName.getBytes(), "ISO8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setContentType("application/octet-stream;charset=ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Pargam", "no-cache");
			response.addHeader("Cache-Control", "no-cache");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
