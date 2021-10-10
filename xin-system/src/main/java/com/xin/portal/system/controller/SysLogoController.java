package com.xin.portal.system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.SysLogo;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.SysLogoService;
import com.xin.portal.system.util.FileUtil;
import com.xin.portal.system.util.SessionUtil;

/**  
* @Title: com.xin.portal.controller.SysLogoController 
* @Description: Logo设置
* @author zhoujun  
* @date 2019-01-04
* @version V1.0  
*/ 
@Controller
@RequestMapping("/sysLogo")
public class SysLogoController extends BaseController {

	private static final String PATH = "sysLogo/";
	
	@Autowired
	private SysLogoService service;
	@Autowired
	private FileService fileService;
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index(){
		return PATH + "index";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return PATH + "add";
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.GET)
	public String edit(SysLogo sysLogo,Model model){
		SysLogo logo = service.selectById(sysLogo.getId());
		model.addAttribute("record", logo);
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
	 * @Date 2019-01-04 
	 */
	@RequestMapping("/page")
	@ResponseBody
	public BaseApi page(SysLogo query){
		return BaseApi.page(service.page(query));
	}
	
	/**
	 * @Title: save 
	 * @Description:  保存
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-01-04 
	 */
	@PostMapping("/save")
	@ResponseBody
	public BaseApi save(SysLogo record){
		return service.saveSysLog(record);
	}
	
	/**
	 * @Title: update 
	 * @Description:  更新
	 * @param query
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2019-01-04 
	 */
	@RequestMapping(value="/update",method=RequestMethod.POST)
	@ResponseBody
	public BaseApi update(SysLogo record){
		return service.updateSysLogo(record);
	}
	
	
	/**
	 * @Title: delete 
	 * @Description:  删除
	 * @param id
	 * @return BaseApi
	 * @author zhoujun
	 * @Date 2017-7-25 下午3:47:04
	 */
	@SystemLog(module = "LOGO管理", operation="删除",sort="tfPToROut1",tfPToROut1="id,id,name,com.xin.portal.system.mapper.SysLogoMapper,selectById",before="tfPToROut1", type=LogType.delete)
	@RequestMapping(value="/delete")
	@ResponseBody
	public BaseApi delete(String id){
		//查询是否是启用的
		SysLogo logo = service.selectById(id);
		if(logo.getIsEnable()!=null && logo.getIsEnable()==1){
			return BaseApi.error("-9");
		}
		return service.deleteById(id) ? BaseApi.success() : BaseApi.error();
	}
	
	@SystemLog(module = "LOGO管理", operation="上传LOGO", type=LogType.upload)
	@RequestMapping(value="/upload")
	@ResponseBody
	public BaseApi upload(@RequestParam("file") MultipartFile file,
			HttpServletRequest request, Model model){
		BaseApi result = new BaseApi();
		String fileName = file.getOriginalFilename();
		long size = file.getSize();
		int flag = FileUtil.checkFileSizeOrType(fileName,size,"image");
		if(flag==1){
			return BaseApi.error("上传的文件大小超出上限");
		}else if(flag == 2){
			return BaseApi.error("不符合上传要求的文件格式");
		}else{
			String con = request.getSession().getServletContext().getRealPath("");
			String webappPath = con.substring(0, con.lastIndexOf("\\"));
			String uploadPath = webappPath.substring(0, webappPath.lastIndexOf("\\"))+ File.separator + "upload" + File.separator + "logo";
			FileModel f = fileService.upload(uploadPath,"/upload/logo", file,null,false,"logo");
			if (f.getId()!=null && f.getFilePathView()!=null) {
				result.put("data", f);
				result.put("msg", "上传成功");
				result.put("code", 0);
				return result;
			}else{
				result.put("msg", "上传失败");
				result.put("code", -1);
				return result;
			}
		}
	}
	
	@RequestMapping(value="/getIsEnableLogo")
	@ResponseBody
	public BaseApi getIsEnableLogo(SysLogo record){
		Wrapper<SysLogo> ws = new EntityWrapper<>();
		ws.eq("is_enable", 1);
		ws.eq("type", record.getType());
		if(record.getId()!=null && record.getId()!=""){
			ws.ne("id", record.getId());
		}
		List<SysLogo> list = service.selectList(ws); 
		if(list!=null && list.size()>0){
			return BaseApi.data(list.get(0));
		}
		return BaseApi.data(null);
	}
	
}

