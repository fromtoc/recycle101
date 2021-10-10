package com.xin.portal.system.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.aspose.slides.Collections.ArrayList;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Banner;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.Permission;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.BannerService;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.util.FileUtil;
import com.xin.portal.system.util.OfficeUtil;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

/**
 * @ClassPath: com.xin.portal.system.controller.UploadController
 * @Description: 文件上传
 * @author zhoujun
 * @date 2017-12-4 上午10:54:17
 */
@Controller
@RequestMapping("/file")
public class FileController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(FileController.class);

	private static final String DIR = "file/";

	@Autowired
	private FileService service;
	@Autowired
	private DictService dictService;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping("/index")
	public String index(Model model) {
		EntityWrapper<FileModel> ew = new EntityWrapper<>();
		ew.orderBy("create_time", false);
		List<String> fileType = new ArrayList();
		fileType.add("picture");//默认图
		fileType.add("thumbnail");//缩略图
		fileType.add("banner");//轮播图
		ew.in("business_type", fileType);
		List<FileModel> fileModel=service.selectList(ew);
		model.addAttribute("fileModel", fileModel);
		return DIR + "index";
	}
	@RequestMapping("/searchByName")
	@ResponseBody
	public List<FileModel> searchByName(FileModel query) {
		EntityWrapper<FileModel> ew = new EntityWrapper<>();
		ew.orderBy("create_time", false);
		ew.like("name_before", query.getNameBefore());
		List<FileModel> fileModel=service.selectList(ew);
		return fileModel;
	}
	
	@RequestMapping("/page")
	@ResponseBody
	public PageModel<FileModel> list(FileModel query) {
		return service.page(query);
	}
	
	/**
	 * 
	 * @param file 上传的文件
	 * @param path 模块名（upload下的文件夹名）
	 * @param request
	 * @param model
	 * @return
	 */
	@SystemLog(module = LanguageParam.FILE_MANAGEMENT , operation=LanguageParam.ACTION_UPLOAD, type=LogType.upload)
	@RequestMapping(value = "/upload/{path}", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi upload(@RequestParam("file") MultipartFile file, @PathVariable("path") String path,
			HttpServletRequest request, Model model) {
		String businessInfo = request.getParameter("businessInfo");
		// 上传目录为 系统部署路径平级的 upload文件夹
		//String uploadPath = request.getSession().getServletContext().getRealPath("upload") + File.separator + path;
		String uploadPath = getUploadPath(request) + path;
		
		FileModel f = service.upload(uploadPath, "/upload/" + path, file, businessInfo,false, path);

		return f != null ? BaseApi.success("上传成功") : BaseApi.error();
	}
	
	/**
	 * 下载模板文件
	 * @param fileName 项目 static/doc 目录下文件名称
	 * @param request
	 * @param response
	 */
	@SystemLog(module = LanguageParam.FILE_MANAGEMENT , operation=LanguageParam.DOWNLOAD_TEMPLATE, type=LogType.download)
	@RequestMapping(value = "/download/template/{fileName:.+}", method = RequestMethod.GET)
	public void downloadTemplate(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
		byte[] buffer = new byte[1024];
		// 文件输入流
		InputStream fis = getClass().getClassLoader().getResourceAsStream("doc/"+fileName);
		if (fis!=null) {
			try {
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
				
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			response.setHeader("conent-type", "application/octet-stream");
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 输出流
			OutputStream os = null;
			try {
				os = response.getOutputStream();
				int i = fis.read(buffer);
				while (i != -1) {
					os.write(buffer);
					i = fis.read(buffer);
				}
				os.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
				
	}
	
	/**
	 * 
	 * @param id t_file表id
	 * @param request
	 * @param response
	 */
	@SystemLog(module = LanguageParam.FILE_MANAGEMENT , operation=LanguageParam.ACTION_DOWNLOAD, type=LogType.download)
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public void download(@PathVariable("id") String id, HttpServletRequest request, HttpServletResponse response) {
		FileModel query = new FileModel();
		query.setId(id);
		FileModel record = service.find(query);
		if (record!=null) {
			String fileName = "";
			try {
				fileName = new String(record.getNameBefore().getBytes("UTF-8"), "ISO-8859-1");
				
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			File file = new File(record.getSavedPath());
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
	
	@SystemLog(module = LanguageParam.FILE_MANAGEMENT , operation=LanguageParam.ACTION_PREVIEW, type=LogType.openfile)
	@RequestMapping("/view/{id}")
	public String view(@PathVariable("id") String id,Model model,HttpServletRequest request) {
		FileModel record = service.selectById(id);
		
		if (record!=null && "doc^docx^ppt^pptx^xls^xlsx^txt^java^python".indexOf(record.getFileType().toLowerCase())>-1) {
			String newPath = record.getFilePathSave() + File.separator ;
			String fileName =  DigestUtils.md5DigestAsHex(record.getNameAfter().getBytes()) + ".pdf";
			File file = new File(newPath+fileName);
			logger.info("===new file Path=== {} {}",newPath + fileName ,file.getTotalSpace());
			if (!file.exists() || file.length()==0) {
//				try {
//					file.createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				String viewPath = record.getFilePathView().substring(0, record.getFilePathView().indexOf(record.getNameAfter()));
				logger.info("===viewPath=== {}",viewPath);
				switch (record.getFileType()) {
				case "doc":
				case "docx":
					OfficeUtil.doc2pdf(record.getSavedPath(), newPath+fileName);
					record.setFilePathView(viewPath + fileName);
					break;
				case "ppt":
				case "pptx":
					OfficeUtil.ppt2pdf(record.getSavedPath(), newPath+fileName);
					record.setFilePathView(viewPath + fileName);
					break;
				case "xls":
				case "xlsx":
					OfficeUtil.excel2pdf(record.getSavedPath(), newPath+fileName);
					record.setFilePathView(viewPath + fileName);
					break;
				}
					
			} else {
				String viewPath = record.getFilePathView().substring(0, record.getFilePathView().indexOf(record.getNameAfter()));
				record.setFilePathView(viewPath + fileName);
				logger.info("===viewPath=== {}",record.getFilePathView());
			}
			
			
			
		} 
		model.addAttribute("record",record);
		return DIR + "view";
	}
	
	@SystemLog(module = LanguageParam.FILE_MANAGEMENT , operation=LanguageParam.ACTION_PREVIEW, type=LogType.openfile)
	@RequestMapping("/mobileView/{id}")
	public String mobileView(@PathVariable("id") String id,Model model,HttpServletRequest request) {
		FileModel record = service.selectById(id);
		
		if (record!=null && "doc^docx^ppt^pptx^xls^xlsx^txt^java^python".indexOf(record.getFileType().toLowerCase())>-1) {
			String newPath = record.getFilePathSave() + File.separator ;
			String fileName =  DigestUtils.md5DigestAsHex(record.getNameAfter().getBytes()) + ".pdf";
			File file = new File(newPath+fileName);
			logger.info("===new file Path=== {} {}",newPath + fileName ,file.getTotalSpace());
			if (!file.exists() || file.length()==0) {
//				try {
//					file.createNewFile();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				String viewPath = record.getFilePathView().substring(0, record.getFilePathView().indexOf(record.getNameAfter()));
				logger.info("===viewPath=== {}",viewPath);
				switch (record.getFileType()) {
				case "doc":
				case "docx":
					OfficeUtil.doc2pdf(record.getSavedPath(), newPath+fileName);
					record.setFilePathView(viewPath + fileName);
					break;
				case "ppt":
				case "pptx":
					OfficeUtil.ppt2pdf(record.getSavedPath(), newPath+fileName);
					record.setFilePathView(viewPath + fileName);
					break;
				case "xls":
				case "xlsx":
					OfficeUtil.excel2pdf(record.getSavedPath(), newPath+fileName);
					record.setFilePathView(viewPath + fileName);
					break;
				}
					
			} else {
				String viewPath = record.getFilePathView().substring(0, record.getFilePathView().indexOf(record.getNameAfter()));
				record.setFilePathView(viewPath + fileName);
				logger.info("===viewPath=== {}",record.getFilePathView());
			}
		} 
		model.addAttribute("record",record);
		return DIR + "mobile_view";
	}
	
	
	@SystemLog(module = LanguageParam.FILE_MANAGEMENT , operation=LanguageParam.ACTION_PREVIEW, type=LogType.openfile)
	@RequestMapping("/read/{id}")
	public void read(@PathVariable("id") String id,HttpServletResponse response) {
		FileModel query = new FileModel();
		query.setId(id);
		FileModel record = service.find(query);
		BufferedReader bis = null;
		FileInputStream fis = null;
		try {
			File file = new File(record.getSavedPath());
			
			//System.out.println(FileUtil.getFileEncode(record.getSavedPath()));
			
			fis = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fis,"GBK");
			bis = new BufferedReader(inputStreamReader);
			response.setContentType("text/html;charset=utf-8");
			StringBuffer buf = new StringBuffer();
			String temp;
			while ((temp = bis.readLine()) != null) {
				buf.append(temp);
				response.getWriter().write(temp);
				if (buf.length() >= 1000) {
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				if (bis!=null)
				bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	/**
	 * 
	 * 查看PDF文件
	 * 
	 * @param fileId
	 * @return
	 * @throws IOException
	 *//*
		 * @RequestMapping(value =
		 * "/app/credit/loanApplication/contracts/{fileId}.pdf", method =
		 * RequestMethod.GET) public ResponseEntity<byte[]>
		 * loadContract(@PathVariable String fileId) throws IOException { byte[]
		 * bytes=FileUtil.read(filePath).downContract(fileId); final HttpHeaders
		 * headers = new HttpHeaders();
		 * headers.setContentType(MediaType.valueOf("application/pdf"));
		 * headers.setContentLength(bytes.length);
		 * headers.add(HttpHeaders.ACCEPT_RANGES,"bytes"); return new
		 * ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK); }
		 */
	@SystemLog(module = LanguageParam.ACTIONDELETEPIC , operation = LanguageParam.ACTIONDELETEPIC ,type=LogType.delete)  //删除文件
	@RequestMapping("/deleteFileById")
	@ResponseBody
	public BaseApi deleteFileById(Model model,FileModel fileModel){
		FileModel file=service.find(fileModel);
		if(file!=null){
			switch (file.getBusinessType()){
			case "banner":
				EntityWrapper<Banner> ba = new EntityWrapper<>();
				ba.eq("file_id", file.getId());
				List<Banner> bannerList=bannerService.selectList(ba);
				if(bannerList.size()>0){
					StringBuffer mes=new StringBuffer();
					for(int i=0;i<bannerList.size();i++){
						mes.append(bannerList.get(i).getTitle()+",");
					}
					return BaseApi.error(mes+"");	
				}else{
					service.deleteById(fileModel);
				}
				break;
			case "thumbnail":
				EntityWrapper<Resource> re = new EntityWrapper<>();
				re.eq("file_id", file.getId());
				List<Resource> resourceList=resourceService.selectList(re);
				if(resourceList.size()>0){
					StringBuffer mes=new StringBuffer();
					for(int i=0;i<resourceList.size();i++){
						mes.append(resourceList.get(i).getName()+",");
					}
					return BaseApi.error(mes+"");	
				}else{
					service.deleteById(fileModel);
				}
				break;
			case "logo":
				break;	
			case "picture":
				List<FileModel> file2= service.findBannerAndThumbnail(fileModel);
				if(file2.size()>0){
					StringBuffer mes=new StringBuffer();
					for(int i=0;i<file2.size();i++){
						mes.append(file2.get(i).getNameBefore()+",");
					}
					return BaseApi.error(mes+"");	
				}else{
					service.deleteById(fileModel);
				}
				break;		
			default : 
				return BaseApi.error();	
			}
			
			return BaseApi.success();
		}
		return BaseApi.error();
		
	}
	@SystemLog(module = LanguageParam.ACTIONDELETEPIC , operation = LanguageParam.ACTIONDELETEPIC ,type=LogType.delete)  //删除全部
	@RequestMapping("/deletePicAll")
	@ResponseBody
	public BaseApi deletePicAll(Model model,FileModel fileModel){
		List<FileModel> file= service.findBannerAndThumbnail(fileModel);
		if(file.size()>0){
			StringBuffer mes=new StringBuffer();
			for(int i=0;i<file.size();i++){
				mes.append(file.get(i).getNameBefore()+",");
			}
			return BaseApi.error(mes+"");	
		}else{
			service.deleteAllPic();
			return BaseApi.success();
		}
		
	}
	@RequestMapping("/add")
	public String add(Model model) {
		return DIR + "add";
	}
	/**
	 * 上传图片统一接口
	 * by xue
	 * type 是上传图片的类型，默认是picture,资源管理上传的缩略图就是thumbnail，轮播图就是banner
	 */
	@SystemLog(module = LanguageParam.ACTIONUPLOADPIC, operation=LanguageParam.ACTIONUPLOADPIC, type=LogType.upload)
	@RequestMapping(value = "/picture/upload/{type}", method = RequestMethod.POST)
	@ResponseBody
	public BaseApi upload(@RequestParam("file") MultipartFile file,HttpServletRequest request, Model model,@PathVariable("type")  String type) {
		String fileName = file.getOriginalFilename();
		/*判断上传文件的类型,要求必须是PNG、JPG格式的图片才可以上传*/
		String fileType=fileName.substring(fileName.length()-3,fileName.length());
		/*fileName.length限制图片的名称长短*/
		if(fileName.length()>=4 && fileName.length() <=50){
			long size = file.getSize();
			int flag = FileUtil.checkFileSizeOrType(fileName,size,"image");
			if(flag==1){
				return new BaseApi().put("code",1);
			}else if(flag == 2){
				return new BaseApi().put("code",2);
			}else{
				// 上传目录为 系统部署路径\\upload
				String con = request.getSession().getServletContext().getRealPath("");
				String webappPath = con.substring(0, con.lastIndexOf("\\"));
				String uploadPath = webappPath.substring(0, webappPath.lastIndexOf("\\"))+ File.separator + "upload" + File.separator + type;
				FileModel f = service.upload(uploadPath,"/upload/"+type, file,null,true,type);
				switch(type){
					case "banner":
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
						break;
					case "thumbnail":
						break;
					default:

				}	
				return new BaseApi().put("code",3);
			}
		}
		return BaseApi.error(-9,"");
	}
}
