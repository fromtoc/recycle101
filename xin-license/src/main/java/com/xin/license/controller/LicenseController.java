package com.xin.license.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.license.core.LicenseFactory;
import com.xin.portal.system.bean.AttrModel;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.mail.MailModel;
import com.xin.portal.system.mail.MailService;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.License;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.LicenseService;
import com.xin.portal.system.util.AESUtil;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.FileUtil;

/**  
* @Title: com.xin.portal.license.controller.LicenseController 
* @Description: 
* @author zhoujun  
* @date 2018-05-08
* @version V1.0  
*/ 
@Controller
@RequestMapping("/license")
public class LicenseController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(LicenseController.class);

	private static final String DIR = "license/";
	
	@Autowired
	private LicenseService service;
	@Autowired
	private MailService mailService;
	@Autowired
	private FileService fileService;

	/**
	 * @Title: index 
	 * @Description:  访问页
	 * @return String
	 * @author zhoujun
	 * @Date 2018-05-08 
	 */
	@RequestMapping("/index")
	public String index(){
		return DIR + "index";
	}
	
	@RequestMapping("/apply")
	public String active(){
		return DIR + "apply";
	}
	
	/**
	 * @Title: add 
	 * @Description:  添加页面
	 * @return String
	 * @author zhoujun
	 * @Date 2018-05-08 
	 */
	@RequestMapping("/add")
	public String add(){
		return DIR + "add";
	}

	/**
	 * @Title: page 
	 * @Description:  TODO
	 * @param query
	 * @return PageForBootstrap<License>
	 * @author zhoujun
	 * @Date 2018-05-08 
	 */
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi page(License query, Integer pageNumber, Integer pageSize){
		return BaseApi.page(service.page(query, pageNumber, pageSize));
	}
	
	/**
	 * @Title: list 
	 * @Description:  列表查询
	 * @param License
	 * @param Model
	 * @return List<License>
	 * @author zhoujun
	 * @Date 2018-05-08 
	 */
	@RequestMapping("/list")
	@ResponseBody
	public List<License> list(License query, Model model){
		EntityWrapper<License> warpper = new EntityWrapper<License>(query);
		return service.selectList(warpper);
	}
	
	/**
	 * @Title: edit 
	 * @Description:  编辑页面
	 * @param query
	 * @param model
	 * @return String
	 * @author zhoujun
	 * @Date 2018-05-08 
	 */
	@RequestMapping("/edit")
	public String edit(License query, Model model){
		EntityWrapper<License> warpper = new EntityWrapper<License>(query);
		License record = service.selectOne(warpper);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-05-08 
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST)
	@ResponseBody
	public BaseApi save(License record,HttpServletRequest request){
		record.setPath(getUploadPath(request)+"license"+File.separator);
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * 
	 * @Title: send 
	 * @Description:  重新发送邮件
	 * @param record
	 * @param request
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019年1月15日 上午11:55:48
	 */
	@RequestMapping(value="/send", method=RequestMethod.GET)
	@ResponseBody
	public BaseApi send(License record,HttpServletRequest request){
		record = record.selectById();
		return sendSuccessEmail(record) ? BaseApi.success() : BaseApi.error();
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2018-05-08 
	 */
	@RequestMapping(value="/update", method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(License record){
		boolean result = false;
		if (License.STATE_YES.equals(record.getState())) {
			LicenseFactory cLicense = new LicenseFactory();
			//获取参数
			//生成证书
			result = record.updateById();
			record = record.selectById();
			boolean licenseFile = cLicense.create(record);
			logger.info("license 证书生成结果： {}",licenseFile);
			if (licenseFile) {
				sendSuccessEmail(record);
			}
		} else if(License.STATE_NO.equals(record.getState())) {
			result = record.updateById();
			record = record.selectById();
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("toUser", record.getCreater());
			
			MailModel mailModel = new MailModel();
			String subject = "德昂数窗平台激活";
			mailModel.setSubject(subject);
			mailModel.setToAddress(record.getEmail());
			mailModel.setContent("欢迎您成为德昂产品用户。由于 "+ record.getReason() +" 无法为您提供激活文件，请联系德昂销售团队。");
			try {
				mailService.sendTemplateMail(mailModel, "tpl/tpl_refuse.html", data);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("===邮件发送失败！！！");
			}
		}
		
		return result ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = "授权文件下载", type=LogType.download)
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		License record = service.selectById(id);
		if (record!=null) {
			String fileName = "";
			try {
				fileName = new String((record.getId()+".lic").getBytes("UTF-8"), "ISO-8859-1");
				
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			File file = new File(record.getPath()+File.separator+fileName);
			// 判断文件父目录是否存在
			if (file.exists()) {
				response.setHeader("conent-type", "application/octet-stream");
				response.setContentType("application/force-download");
				response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
				
				byte[] buffer = new byte[1024];
				// 文件输入流
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				// 输出流
				OutputStream os = null;
				try {
					os = response.getOutputStream();
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer);
						i = bis.read(buffer);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						bis.close();
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
	}
	
	/**
	 * 
	 * @Title: sendSuccessEmail 
	 * @Description:  发送授权文件
	 * @param record
	 * @return boolean
	 * @author zhoujun
	 * @Date 2019年1月15日 下午12:04:45
	 */
	
	private boolean sendSuccessEmail(License record){
		boolean result = true;
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("toUser", record.getCreater());
		data.put("startTime", record.getStartTime());
		data.put("endTime", record.getEndTime());
		
		MailModel mailModel = new MailModel();
		String subject = "德昂数窗平台激活";
		mailModel.setSubject(subject);
		mailModel.setToAddress(record.getEmail());
		mailModel.setContent("欢迎您成为德昂产品用户。请使用附件激活产品，您将成为德昂产品正式用户，享受我们为您提供的优质服务。");
		List<AttrModel> attrs = new ArrayList<AttrModel>();
		AttrModel attr = new AttrModel();
		attr.setNameBefore("portal.lic");
		attr.setFilePathSave(record.getPath());
		attr.setNameAfter(record.getId());
		attr.setFileType("lic");
		attrs.add(attr);
		mailModel.setAttrs(attrs);
		try {
			result = mailService.sendTemplateMail(mailModel, "tpl/tpl_active.html", data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("===邮件发送失败！！！");
			result = false;
		}
		return result;
	}
	
	
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi active(@RequestParam("file") MultipartFile file,HttpServletRequest request, Model model) {
		// 上传目录为 系统部署路径\\upload
		String uploadPath = getUploadPath(request);
		FileModel fileModel = fileService.upload(uploadPath+File.separator+"license","/upload/license", file,"license",false,"license",false);
		String licenseJson = FileUtil.read(fileModel.getSavedPath());
		String detryJson = AESUtil.decrypt(licenseJson, Constant.DEFAULT_SALT);
		logger.info(detryJson);
		License license = JSONObject.parseObject(detryJson, License.class);
		license.setPath(getUploadPath(request)+"license"+File.separator);
		license.setAmount(license.getAmount()!=null?license.getAmount():1);//oracle 默认值报错需要指定默认值
		license.setAmountEnabled(license.getAmountEnabled()!=null?license.getAmountEnabled():true);//oracle 默认值报错需要指定默认值
		boolean result = license.insert();
		return result ? BaseApi.data(license) : BaseApi.error();
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
	public BaseApi delete(License record){
		return record.deleteById() ? BaseApi.success() : BaseApi.error();
	}
	
}
