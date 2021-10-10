package com.xin.portal.system.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.util.ImageUtils;
import com.xin.portal.system.util.OfficeUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.FileModelMapper;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.service.FileService;

@Service
public class FileServiceImpl extends ServiceImpl<FileModelMapper, FileModel> implements FileService {
	
	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	@Autowired 
	private FileModelMapper mapper;

	@Override
	public List<FileModel> findList(FileModel query) {
		return mapper.findList(query);
	}

	@Override
	public FileModel save(FileModel query) {
		int result = mapper.save(query);
		return result > 0 ? query : null;
	}

	@Override
	public FileModel update(FileModel query) {
		int result = mapper.update(query);
		return result > 0 ? query : null;
	}

	@Override
	public FileModel find(FileModel query) {
		return mapper.find(query);
	}
	
	@Override
	public FileModel upload(String loadPath, String viewPath, MultipartFile file, String businessId, 
			boolean needHandle,String businessType) {
		return upload(loadPath, viewPath, file, businessId, needHandle, businessType,true);
	}
	
	@Override
	public FileModel uploadImg(String loadPath, String viewPath, MultipartFile file, String businessId, 
			boolean needHandle,String businessType) {
		return uploadImg(loadPath, viewPath, file, businessId, needHandle, businessType,true);
	}


	@Override
	public FileModel upload(String loadPath, String viewPath, MultipartFile file, String businessId, 
			boolean needHandle,String businessType,boolean isRandomPath) {
		// 上传目录为 系统部署路径\\upload
		if (isRandomPath) {
			Random random = new Random();
			String randomStr = File.separator + String.valueOf(random.nextInt(100))+ File.separator +  String.valueOf(random.nextInt(100));
			loadPath += randomStr;
			viewPath += randomStr;
		}
		File fileDir = new File(loadPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		String fileName = file.getOriginalFilename();
		String fileNameAfter = UUID.randomUUID().toString().replace("-", "");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("{}||{}||{}", new Object[]{fileName,fileNameAfter,loadPath + File.separator + fileNameAfter});
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		File targetFile = new File(loadPath, fileNameAfter+fileName.substring(fileName.lastIndexOf(".")));
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		/*needHandle表示文件是否需要处理*/
		if(needHandle){
			try {
	            InputStream is = new FileInputStream(targetFile);
	            fileNameAfter=UUID.randomUUID().toString().replace("-", "");
	            targetFile = new File(loadPath, fileNameAfter+fileName.substring(fileName.lastIndexOf(".")));
	            OutputStream os = new FileOutputStream(targetFile);
	            ImageUtils.resizeImage(is, os, 400, 200, 2, "png");
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		FileModel fileModel = new FileModel(fileName, fileNameAfter, (double)file.getSize(), loadPath,viewPath.replace("\\", "/")+"/"+targetFile.getName(),
				fileName.substring(fileName.lastIndexOf(".") + 1),businessId,businessType);
		//mapper.save(fileModel);
		fileModel.insert();
		
		return fileModel;
	}
	
	@Override
	public FileModel uploadImg(String loadPath, String viewPath, MultipartFile file, String businessId, 
			boolean needHandle,String businessType,boolean isRandomPath) {
		// 上传目录为 系统部署路径\\upload
		if (isRandomPath) {
			Random random = new Random();
			String randomStr = File.separator + String.valueOf(random.nextInt(100))+ File.separator +  String.valueOf(random.nextInt(100));
			loadPath += randomStr;
			viewPath += randomStr;
		}
		File fileDir = new File(loadPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		String fileName = file.getOriginalFilename();
		String fileNameAfter = UUID.randomUUID().toString().replace("-", "");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("{}||{}||{}", new Object[]{fileName,fileNameAfter,loadPath + File.separator + fileNameAfter});
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		File targetFile = new File(loadPath, fileNameAfter+fileName.substring(fileName.lastIndexOf(".")));
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		/*needHandle表示文件是否需要处理*/
		if(needHandle){
			try {
	            InputStream is = new FileInputStream(targetFile);
	            fileNameAfter=UUID.randomUUID().toString().replace("-", "");
	            targetFile = new File(loadPath, fileNameAfter+fileName.substring(fileName.lastIndexOf(".")));
	            OutputStream os = new FileOutputStream(targetFile);
	            ImageUtils.resizeImage(is, os, 1764, 429, 2, "png");
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
		}
		FileModel fileModel = new FileModel(fileName, fileNameAfter, (double)file.getSize(), loadPath,viewPath.replace("\\", "/")+"/"+targetFile.getName(),
				fileName.substring(fileName.lastIndexOf(".") + 1),businessId,businessType);
		//mapper.save(fileModel);
		fileModel.insert();
		
		return fileModel;
	}


	@Override
	public FileModel uploadConfig(String loadPath, String viewPath, MultipartFile file, String businessId, boolean needHandle, String businessType, Integer width, Integer height) {
		// 上传目录为 系统部署路径\\upload
		Random random = new Random();
		String randomStr = File.separator + String.valueOf(random.nextInt(100))+ File.separator +  String.valueOf(random.nextInt(100));
		loadPath += randomStr;
		viewPath += randomStr;

		File fileDir = new File(loadPath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		String fileName = file.getOriginalFilename();
		String fileNameAfter = UUID.randomUUID().toString().replace("-", "");
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("{}||{}||{}", new Object[]{fileName,fileNameAfter,loadPath + File.separator + fileNameAfter});
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

		File targetFile = new File(loadPath, fileNameAfter+fileName.substring(fileName.lastIndexOf(".")));
		try {
			file.transferTo(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		/*needHandle表示文件是否需要处理*/
		if(needHandle){
			try {
				InputStream is = new FileInputStream(targetFile);
				fileNameAfter=UUID.randomUUID().toString().replace("-", "");
				targetFile = new File(loadPath, fileNameAfter+fileName.substring(fileName.lastIndexOf(".")));
				OutputStream os = new FileOutputStream(targetFile);
				ImageUtils.resizeImage(is, os, width,height,2, "png");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileModel fileModel = new FileModel(fileName, fileNameAfter, (double)file.getSize(), loadPath,viewPath.replace("\\", "/")+"/"+targetFile.getName(),
				fileName.substring(fileName.lastIndexOf(".") + 1),businessId,businessType);
		//mapper.save(fileModel);
		fileModel.insert();

		return fileModel;
	}

	@Override
	public PageModel<FileModel> page(FileModel query) {
		Page<FileModel> page = new Page<FileModel>(query.getPageNumber(),query.getPageSize());
		EntityWrapper<FileModel> ew= new EntityWrapper<FileModel>(query);
		ew.orderBy("create_time", false);
		page = selectPage(page, ew);
		return new PageModel<FileModel>(page);
	}
	
	@Override
	public FileModel findByNameAfter(String nameAfter){
		return mapper.findByNameAfter(nameAfter);
	}

	@Override
	public boolean delete(String fileId) {
		FileModel fileModel = selectById(fileId);
		if (fileModel!=null) {
			File file = new File(fileModel.getSavedPath());
			if (file.exists()) {
				file.delete();
			}
			return fileModel.deleteById();
		}
		return true;
	}

	@Override
	public void transformToPdf(FileModel record) {
		try {
			if (record!=null && "doc^docx^ppt^pptx^xls^xlsx^txt^java^python".indexOf(record.getFileType().toLowerCase())>-1) {
				String newPath = record.getFilePathSave() + File.separator ;
				String fileName =  DigestUtils.md5DigestAsHex(record.getNameAfter().getBytes()) + ".pdf";
				File file = new File(newPath+fileName);
				logger.info("===new file Path=== {} {}",newPath + fileName ,file.getTotalSpace());
				if (!file.exists() || file.length()==0) {
					String viewPath = record.getFilePathView().substring(0, record.getFilePathView().indexOf(record.getNameAfter()));
					logger.info("===viewPath=== {}",viewPath);
					switch (record.getFileType()) {
					case "doc":
					case "docx":
						OfficeUtil.doc2pdf(record.getSavedPath(), newPath+fileName);
						break;
					case "ppt":
					case "pptx":
						OfficeUtil.ppt2pdf(record.getSavedPath(), newPath+fileName);
						break;
					case "xls":
					case "xlsx":
						OfficeUtil.excel2pdf(record.getSavedPath(), newPath+fileName);
						break;
					}
				} else {
					String viewPath = record.getFilePathView().substring(0, record.getFilePathView().indexOf(record.getNameAfter()));
					logger.info("===has viewPath=== {}",viewPath+fileName);
				}
			} 
		} catch (Exception e) {
			logger.info("upload file transform PDF is error");
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteAllPic() {
		Model<FileModel> fileModel;
		// TODO Auto-generated method stub
		return mapper.deleteAllPic();
	}

	@Override
	public List<FileModel> findBannerAndThumbnail(FileModel fileModel) {
		// TODO Auto-generated method stub
		return mapper.findBannerAndThumbnail(fileModel);
	}

	@Override
	public List<FileModel> selectByCreateTime() {
		return mapper.selectByCreateTime();
	}
	
	

}
