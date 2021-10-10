package com.xin.portal.system.controller;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.cache.CacheManager;
import com.xin.portal.system.license.ClientVerifyLicense;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.License;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.LicenseService;
import com.xin.portal.system.util.AESUtil;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.FileUtil;
import com.xin.portal.system.util.HttpClientUtils;
import com.xin.portal.system.util.WebUtil;

@Controller
@RequestMapping("/activate")
public class ActivateController extends BaseController{
	
	private static Logger logger = LoggerFactory.getLogger(ActivateController.class);
	
	private static String DIR = "activate/";
	
	private static String ACTIVE_FILE = "activate.dod";
	
	private static String APPLY_URL = "http://localhost:8080/xin-support/license/new/online";
	@Value("${system.name:DataWindow}")
	private String name;
	@Value("${system.version:1.0.0}")
	private String version;
	
	@Autowired
	private FileService service;
	@Autowired
	private LicenseService licenseService;
	@Autowired
    @Qualifier("druidDataSource")
    private DruidDataSource dataSource;
	
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String activate(){
		return DIR + "index";
	}
	
	/**
	 * @Title: info 
	 * @Description:  激活信息
	 * @return String
	 * @author zhoujun
	 * @Date 2018年12月17日 下午4:38:18
	 */
	@RequestMapping(value="/info", method=RequestMethod.GET)
	public String info(Model model,HttpServletRequest request){
		EntityWrapper<License> wrapper = new EntityWrapper<>();
		wrapper.orderBy("activate_time", false);
		License license = licenseService.selectOne(wrapper);
		license.setVersion(version);
		model.addAttribute("record",license);
		model.addAttribute("brower", WebUtil.getBrower(request));
		model.addAttribute("name", name);
		return DIR + "info";
	}
	
	/**
	 * 
	 * @param license
	 * @param path online：在线 offline：离线
	 * @param request
	 * @param model
	 * @param res
	 * @return
	 */
	@RequestMapping(value = "/apply/{applyType}")
	@ResponseBody
	public BaseApi apply(License license,@PathVariable("applyType")String path,
			HttpServletRequest request, Model model,HttpServletResponse res) {
		license.setApplyTime(new Date());
		license.setMacAddress(WebUtil.getMacAddress());
		license.setMacEnabled(true);
		license.setTimeEnabled(true);
		license.setAmountEnabled(false);
		license.setJvm(System.getProperty("java.version"));
		license.setOsName(System.getProperty("os.name"));
		license.setOsVersion(System.getProperty("os.version"));
		license.setOsArch(System.getProperty("os.arch"));
		license.setMemory(String.valueOf(Runtime.getRuntime().maxMemory() / 1024L / 1024L));
		license.setComputerName(System.getenv().get("COMPUTERNAME"));
		license.setVersion(version);
		license.setDbInfo(dataSource.getDbType());
		
		if ("online".equals(path)) {
			Map<String,String> param = new HashMap<>();
			param.put("filePath", request.getSession().getServletContext()
					.getRealPath("upload") + File.separator +"lic" + File.separator +"demand_bi.lic");
			param.put("license", JSON.toJSON(license).toString());
			try {
				HttpClientUtils.download(APPLY_URL, param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ClientVerifyLicense verify = new ClientVerifyLicense();
			License licenseRecord = verify.installAndVerify(param.get("filePath").toString());
			if (licenseRecord!=null){
				return licenseRecord.insert() ? BaseApi.success() : BaseApi.error();
			}
			return BaseApi.error();
//			return verify.installAndVerify(param.get("filePath").toString()) ? BaseApi.success() : BaseApi.error();
		} else {
			license.setState(License.STATE_APPLY);
			String licenseJson = JSON.toJSONString(license);
			logger.info("licenseJson: {}",licenseJson);
			String entryJson = AESUtil.encrypt(licenseJson, Constant.DEFAULT_SALT);
			logger.info("entryJson: {}",entryJson);
			boolean result = FileUtil.write(getUploadPath(request) + File.separator + "license" +File.separator + ACTIVE_FILE, entryJson);
			return result ? BaseApi.success() : new BaseApi().put("code",2);
			//BaseApi.error("创建激活资料失败")

		}
		
	}
	
	
	
	
	@RequestMapping(value = "/upload/{path}", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi active(@RequestParam("file") MultipartFile file,@PathVariable("path")String path,
			HttpServletRequest request, Model model) {
		// 上传目录为 系统部署路径\\upload
		String uploadPath = getUploadPath(request) + "lic";
		FileModel fileModel = service.upload(uploadPath,"/upload/"+path, file,FileModel.LICENSE,false,path);
		ClientVerifyLicense verify = new ClientVerifyLicense();
		License license = verify.installAndVerify(fileModel.getSavedPath());
		if (license!=null){
			Integer maxUserCount=license.getMaxUserCount();
			CacheManager.put("maxUserCount", maxUserCount);
			return license.insertOrUpdate() ? BaseApi.success() : BaseApi.error();
		}
		return BaseApi.error();
	}
	
	@RequestMapping(value = "/uninstall")
	@ResponseBody
	public BaseApi active(Model model) {
		ClientVerifyLicense verify = new ClientVerifyLicense();
		return verify.uninstall() ? BaseApi.success() : BaseApi.error();
	}
	
	
	@RequestMapping(value="/file/download",method=RequestMethod.GET)
    public void downLoad(HttpServletRequest request, HttpServletResponse response){
		FileUtil.download(getUploadPath(request) + File.separator + "license/" +File.separator + ACTIVE_FILE, ACTIVE_FILE, request, response);
    }

}
