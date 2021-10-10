package com.xin.portal.system.controller;

import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.service.BiMappingService;
import com.xin.portal.system.service.BiServerService;
import com.xin.portal.system.service.BiUserService;
import com.xin.portal.system.service.ConfigService;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.UserService;
import com.xin.portal.system.util.ExcelUtil;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.PropertiesUtil;
import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.User;
import com.xin.portal.system.model.UserMappingCount;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;

/**  
* @Title: com.xin.portal.system.controller.BiMappingController 
* @Description: 集成账户
* @author zhoujun  
* @date 2018-12-07
* @version V1.0  
*/ 
@Controller
@RequestMapping("/biMapping")
public class BiMappingController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(BiMappingController.class);

	private static final String PATH = "biMapping/";
	
	@Autowired
	private BiMappingService service;
	@Autowired
	private FileService fileService;
	@Autowired
	private BiServerService biServerService;
	@Autowired
	private UserService userService;
	@Autowired
	private BiUserService biUserService;
	@Autowired
	private ConfigService configService;
	
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
	
	@RequestMapping(value = "/import", method = RequestMethod.GET)
	public String importExcel() {
		return PATH + "import";
	}
	
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-07 
	 */
	@SystemLog(module=LanguageParam.INTEGRATEDACCOUNT ,operation=LanguageParam.ACTIONLISTSEE ,type=LogType.query)
	@GetMapping("/page")
	@ResponseBody
	public BaseApi page(BiMapping query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query,pageNumber,pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param BiMapping
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-07 
	 */
	@GetMapping("/list")
	@ResponseBody
	public BaseApi list(BiMapping query){
		EntityWrapper<BiMapping> warpper = new EntityWrapper<BiMapping>(query);
		return BaseApi.list(service.selectList(warpper));
	}
	
	/**
	 * @Title: info 
	 * @Description:  按id查询
	 * @param BiMapping
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-07-04 
	 */
	@SystemLog(module=LanguageParam.INTEGRATEDACCOUNT ,operation=LanguageParam.DETAILSQUERY,type=LogType.query)
	@GetMapping("/info/{id}")
	@ResponseBody
	public BaseApi info(@PathVariable String id){
		BiMapping result = service.selectById(id);
		return result != null ? BaseApi.data(result) : BaseApi.error();
	}
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-07 
	 */
	@SystemLog(module=LanguageParam.INTEGRATEDACCOUNT ,operation=LanguageParam.NEW_MAPPING_RELATION ,sort="tfPToROut2,tfPToROut1",
			tfPToROut1="userIds,id,username,com.xin.portal.system.mapper.UserMapper,selectById",
			tfPToROut2="biUserId,id,username,com.xin.portal.system.mapper.BiUserMapper,selectById",type=LogType.add)
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(String biUserId,String biServerId, @RequestParam(value = "userIds[]",required=false)String[] userIds){
		BiServer biServer = biServerService.selectById(biServerId);
		int count = service.selectUserMappingCountByType(biServer.getTypeName());
		String type= biServer.getTypeName();
		int userMappingCount=0;
		if (type=="MSTR") {
			userMappingCount=UserMappingCount.MSTRUserMappingCount;
    	}else if (type=="BO") {
    		userMappingCount=UserMappingCount.BOUserMappingCount;
    	}else if (type=="MySql") {
    		userMappingCount=UserMappingCount.MySqlUserMappingCount;
    	}else if (type=="自定义") {
    		userMappingCount=UserMappingCount.CustomUserMappingCount;
        }else if (type=="Library") {
        	userMappingCount=UserMappingCount.LibraryUserMappingCount;
        }else if (type=="Tableau") {
        	userMappingCount=UserMappingCount.TableauUserMappingCount;
        }else if (type=="FineReport"){
        	userMappingCount=UserMappingCount.FineReportUserMappingCount;
        }else{
        	userMappingCount=9999;
    	}
		
		if((count+userIds.length)<=userMappingCount) {
			List<BiMapping> list = Lists.newArrayList();
			for (String userId : userIds) {
				BiMapping record = new BiMapping();
				record.setUserId(userId);
				record.setBiUserId(biUserId);
				record.setType(biServer.getTypeName());
				list.add(record);
			}
			return service.insertBatch(list) ? BaseApi.success() : BaseApi.error();
		}else {
			return BaseApi.error("超出映射用户限制,无法映射");
		}
		
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-12-07 
	 */
	@SystemLog(module=LanguageParam.INTEGRATEDACCOUNT ,operation=LanguageParam.ACTION_RENEWAL ,type=LogType.update)
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(BiMapping record){
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
	@SystemLog(module=LanguageParam.INTEGRATEDACCOUNT ,operation=LanguageParam.ACTIONDELETE ,before="tfPToROut1",
			sort="tfPToROut1",tfPToROut1="id,id,username,com.xin.portal.system.mapper.UserMapper,selectById",type=LogType.delete)
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	public BaseApi delete(@PathVariable String id){
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	
	@SystemLog(module = LanguageParam.INTEGRATEDACCOUNT ,operation=LanguageParam.IMPORT_USER , type = LogType.importData)
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request, Model model) {
		FileModel fileModel = fileService.upload(getUploadPath(request) + "import", "/upload/import", file, null, false,
				FileModel.BIMAPPING_IMPORT);

		if (fileModel != null) {
			List<Map<Integer, String>> excelList = ExcelUtil.readExcelData(fileModel.getSavedPath());
			// 判断数据条数
			if (excelList == null || excelList.size() < 2) {
				return new BaseApi().put("code",7);
				/*String msg = PropertiesUtil.getLocaleMessage(getLocale(), "validation.excel_check_content");
				return BaseApi.error(msg)*/
			}

			// 校验服务器名称列，只取第一条有效数据判断，默认一个Excel对应一个Server
			EntityWrapper<BiServer> ew = new EntityWrapper<>();
			ew.eq("name", excelList.get(1).get(1));
			logger.info("===import server : {}", excelList.get(1).get(1));
			BiServer biServer = biServerService.selectOne(ew);
			if (biServer == null) {
				return new BaseApi().put("code",1);
			}
			
			
			
			/*
			 * 示例Excel 
			 * 系统账号	|	源系统			|	源系统账号
			 * ------------------------------------------------------- 
			 * demo		|	测试环境175Mstr	| demo_mstr
			 * 
			 */
			List<BiMapping> listRecord = Lists.newArrayList();// 原始数据列表
			for (int i = 1; i < excelList.size(); i++) {
				Map<Integer, String> rowMap = excelList.get(i);
				if (StringUtils.isNotEmpty(rowMap.get(0)) && StringUtils.isNotEmpty(rowMap.get(2))) {
					BiMapping record = new BiMapping(rowMap.get(0),biServer.getName(), rowMap.get(2));
					record.setType(biServer.getTypeName());
					listRecord.add(record);
				}
			}
			if (listRecord.size()!=(excelList.size()-1)) {
				BaseApi res=new BaseApi();
				res.put("code",2);
				res.put("msg",excelList.size()-listRecord.size());
				return res;
				//return BaseApi.error(excelList.size()-listRecord.size() + " The bar data account cannot be empty");//条数据账号不能为空
			}
			
			// 排除Excel中自身账号重复的数据列表
			List<BiMapping> biMappingListUnique = listRecord.stream()
					.collect(Collectors.collectingAndThen(
							Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BiMapping::getUsername))),
							ArrayList<BiMapping>::new))
					.stream().collect(Collectors.collectingAndThen(
									Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BiMapping::getBiUserName))),
									ArrayList<BiMapping>::new));
			if (biMappingListUnique.size() != listRecord.size()) {
				//return BaseApi.error("The import account name cannot be repeated");//导入账户名不能重复
				return new BaseApi().put("code",3);
			}
			
			
			List<String> usernameList = listRecord.stream().map(BiMapping::getUsername).collect(Collectors.toList());
			EntityWrapper<User> userEw = new EntityWrapper<>();
			userEw.eq("is_delete", 0);
			userEw.in("username", usernameList);			
			List<User> userList = userService.selectList(userEw);
			if (userList.size()!=biMappingListUnique.size()) {
				List<String> dbUsernameList = userList.stream().map(User::getUsername).collect(Collectors.toList());
				List<String> notInList = usernameList.stream().filter(item -> !dbUsernameList.contains(item)).collect(Collectors.toList());
				BaseApi res=new BaseApi();
				res.put("code",4);
				res.put("msg",notInList.toString());
				return res;
				//return BaseApi.error("The system account does not exist： "+notInList.toString());//系统账号不存在
			}
			
			
			
			List<String> biUserNameList = listRecord.stream().map(BiMapping::getBiUserName).collect(Collectors.toList());
			EntityWrapper<BiUser> biUserEw = new EntityWrapper<>();
			biUserEw.in("username", biUserNameList);
			List<BiUser> biUserList = biUserService.selectList(biUserEw);
			if (biUserList.size()!=biMappingListUnique.size()) {
				List<String> dbBiUsernameList = biUserList.stream().map(BiUser::getUsername).collect(Collectors.toList());
				List<String> notInList = biUserNameList.stream().filter(item -> !dbBiUsernameList.contains(item)).collect(Collectors.toList());
				BaseApi res=new BaseApi();
				res.put("code",5);
				res.put("msg",notInList.toString());
				return res;
				//return BaseApi.error("The source system account does not exist： "+notInList.toString());//源系统账号不存在
			}
			
			for (BiMapping biMapping : listRecord) {
				for (User user : userList) {
					if (user.getUsername().equals(biMapping.getUsername())) {
						biMapping.setUserId(user.getId());
						break;
					}
				}
				for (BiUser biUser : biUserList) {
					if (biUser.getUsername().equals(biMapping.getBiUserName())) {
						biMapping.setBiUserId(biUser.getId());
						break;
					}
				}
			}
			List<BiMapping> dbList = service.selectList(null);
			
			List<BiMapping> existList = listRecord.stream().filter(item -> dbList.contains(item)).collect(Collectors.toList());
			if (!existList.isEmpty()) {
				List<String> existNameList = existList.stream().map(BiMapping::getUsername).collect(Collectors.toList());
				return BaseApi.error(6,existNameList.toString());
				//return BaseApi.error("The relationship already exists： "+existNameList.toString());//关系已经存在
			}
			
			
			boolean result = service.insertBatch(listRecord);
			return result ? BaseApi.success(listRecord.size()+""):BaseApi.error();//导入成功！共 " + listRecord.size() + " 条数据"
		}
		return BaseApi.error();
	}
	
	
	@SystemLog(module = LanguageParam.INTEGRATEDACCOUNT, operation=LanguageParam.EXPORT_USER , type = LogType.export)
	@RequestMapping(value = "/export", method = RequestMethod.GET)
	public void export(BiMapping query, HttpServletRequest request, HttpServletResponse response) {
		// 获取数据
		List<BiMapping> list = service.findList(query);
		String lang = "zh_CN";
		Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, SessionUtil.getUserInfo().getTenantId());
		if(config!=null){
			lang = config.getValue();
		}
		// excel标题
		String[] title ={LangTransform.getLocaleLang(lang, LanguageParam.SYSTEM_ACCOUNT),LangTransform.getLocaleLang(lang, LanguageParam.SOURCE_SYSTEM),LangTransform.getLocaleLang(lang, LanguageParam.SOURCE_SYSTEM_ACCOUNT)};
		String fileName = LangTransform.getLocaleLang(lang, LanguageParam.BI_MAPPING_INFO_EXPORT) +System.currentTimeMillis() + ".xls";
		String sheetName = LangTransform.getLocaleLang(lang, LanguageParam.BI_ACCOUNT_INFO);
//		if (Locale.SIMPLIFIED_CHINESE.toString().equals(lang)) {
//		    title[0]="系统账号";
//			title[1]="源系统";
//			title[2]= "源系统账号";
//			fileName = "BI账户映射信息_导出\" + System.currentTimeMillis() + \".xls\"";
//			sheetName = "账户信息";
//		} else if (Locale.TRADITIONAL_CHINESE.toString().equals(lang)) {
//			title[0]="系統賬號";
//			title[1]="源系統";
//			title[2]= "源系統賬號";
//			fileName = "BI賬戶映射信息_導出\" + System.currentTimeMillis() + \".xls\"";
//			sheetName = "賬戶信息";
//		} else if (Locale.US.toString().equals(lang)) {
//			title [0]=" system account ";
//			title [1]=" source system ";
//			title [2]= "source system account ";
//			 fileName = "BI account mapping information _ export " + System.currentTimeMillis() + ".xls";
//			 sheetName = "Account information ";
//		}else{
//			title[1]="源系统";
//			title[2]= "源系统账号";
//			fileName = "BI账户映射信息_导出\" + System.currentTimeMillis() + \".xls\"";
//			sheetName = "账户信息";
//		}
		/*//String[] title = { "System account "," source system ", "source system account"};//"系统账号", "源系统", "源系统账号"

		// excel文件名
		//String fileName = "BI account mapping information _ export " + System.currentTimeMillis() + ".xls";//"BI账户映射信息_导出" + System.currentTimeMillis() + ".xls";

		// sheet名
		String sheetName = "Account information ";//"账户信息";*/
		String[][] content = new String[list.size()][title.length];
		for (int i = 0; i < list.size(); i++) {
			content[i] = new String[title.length];
			BiMapping obj = list.get(i);
			content[i][0] = obj.getUsername();
			content[i][1] = obj.getBiServerName();
			content[i][2] = obj.getBiUserName();
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

