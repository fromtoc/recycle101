package com.xin.portal.system.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.model.Banner;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.service.BannerService;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.util.FileUtil;
import com.xin.portal.system.util.SessionUtil;

/**
 * @ClassPath: com.xin.portal.system.controller.OrganizationController 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-8-2 下午3:37:27
 */
@Controller
@RequestMapping("/banner")
public class BannerController extends BaseController {

	private static final String DIR = "banner/";
	
	@Autowired
	private BannerService service;
	@Autowired
	private FileService fileService;
	@RequestMapping("/index")
	public String index() {
		return DIR + "index";
	}
	
	@RequestMapping("/add")
	public String add() {
		return DIR + "add";
	}
	
	@RequestMapping("/edit")
	public String edit(Banner query, Model model) {
		Banner record = service.find(query);
		model.addAttribute("record", record);
		return DIR + "edit";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public List<Banner> list(Banner query) {
		return service.findList(query);
	}
	
	@SystemLog(module = LanguageParam.BANNER, type=LogType.query ,operation=LanguageParam.ACTIONLISTSEE)
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<Banner> page(Banner query){
		return service.page(query);
	}
	
	@SystemLog(module = LanguageParam.BANNER, sort="name", name="title", operation=LanguageParam.ACTIONADD, type=LogType.add)
	@RequestMapping("/save")
	@ResponseBody
	public BaseApi save(Banner query) {
		boolean result = query.insert();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.BANNER, sort="name", name="title", operation=LanguageParam.ACTIONUPDATE, type=LogType.update)
	@RequestMapping("/update")
	@ResponseBody
	public BaseApi update(Banner query) {
		query.setStatus(query.getStatus()!=null?query.getStatus():0);
		boolean result = query.updateById();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
	@SystemLog(module = LanguageParam.BANNER, before="tfToName1", sort="tfToName1", 
			tfToName1="query,id,title,com.xin.portal.system.mapper.BannerMapper,selectById", 
			operation=LanguageParam.ACTIONDELETE, type=LogType.delete)
	@RequestMapping("/delete")
	@ResponseBody
	public BaseApi delete(Banner query) {
		return service.delete(query)>0 ? BaseApi.success() : BaseApi.error();
	}
	/*
	 * 废弃，使用filecontroller里面的统一图片上传接口
	 * */
	@SystemLog(module = LanguageParam.BANNER, operation=LanguageParam.ACTIONUPLOADPIC, type=LogType.upload)
	@RequestMapping(value = "/upload/banner", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi upload(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, Model model) {

		String fileName = file.getOriginalFilename();
		if(fileName.length()>=2 && fileName.length() <=18){
			long size = file.getSize();
			int flag = FileUtil.checkFileSizeOrType(fileName,size,"image");
			if(flag==1){
				return new BaseApi().put("code",2);
			}else if(flag == 2){
				return new BaseApi().put("code",3);
			}else{
				// 上传目录为 系统部署路径\\upload
				String uploadPath = getUploadPath(request) + "banner";
				FileModel f = fileService.upload(uploadPath,"/upload/banner", file,null,false,FileModel.BANNER);
				if (f.getId()!=null) {
					Banner banner = new Banner();
					banner.setFileId(f.getId());
					banner.setTitle(f.getNameBefore());
					banner.setSort(99);
					banner.setStatus(1);
					banner.setCreateTime(new Date());
					banner.setCreater(SessionUtil.getUserId());
					banner.insert();
				}
				return new BaseApi().put("code",4);
			}
		}
		return BaseApi.error(-9,"");
	}
	
	@SystemLog(module = LanguageParam.BANNER, operation=LanguageParam.ACTIONREUPLOADPIC, type=LogType.upload)
	@RequestMapping(value = "/upload/editBanner", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi editBanner(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, Model model) {

		BaseApi result = new BaseApi();
		String fileName = file.getOriginalFilename();
		long size = file.getSize();
		int flag = FileUtil.checkFileSizeOrType(fileName,size,"image");
		if(flag==1){
			return new BaseApi().put("code",2);
		}else if(flag == 2){
			return new BaseApi().put("code",3);
		}else{
			// 上传目录为 系统部署路径
			String uploadPath = getUploadPath(request) + "banner";
			FileModel f = fileService.upload(uploadPath,"/upload/banner", file,null,false,FileModel.BANNER);
			if (f.getId()!=null) {
				result.put("data", f);
				//result.put("msg", "successfully upload");//上传成功
				result.put("code", 0);
				return result;
			}else{
				//result.put("msg", "fail to upload");//上传失败
				result.put("code", -1);
				return result;
			}
		}
	}
	@SystemLog(module = LanguageParam.BANNER, sort="name", name="title", operation=LanguageParam.ACTIONADD, type=LogType.add)
	@RequestMapping("/saveFromFile")
	@ResponseBody
	public BaseApi saveFromFile(Banner query) {
		FileModel f=fileService.selectById(query.getId());
		Banner banner = new Banner();
		banner.setFileId(f.getId());
		banner.setTitle(f.getNameBefore());
		banner.setSort(99);
		banner.setStatus(1);
		banner.setCreateTime(new Date());
		banner.setCreater(SessionUtil.getUserId());
		boolean result=banner.insert();
		return result ? BaseApi.data(query) : BaseApi.error();
	}
	
}
