package com.xin.portal.system.service;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.FileModel;

public interface FileService extends IService<FileModel> {
	
	public List<FileModel> findList(FileModel query);

	public FileModel save(FileModel query);

	public FileModel update(FileModel query);

	public FileModel find(FileModel query);

	/**
	 * 
	 * @param loadPath 上传存储路径
	 * @param viewPath 访问路径
	 * @param file 文件
	 * @param businessId 业务id
	 * @param needHandle 是否需要处理成400 * 200 （资源缩略图逻辑需要）
	 * @param type 业务模块
	 * @param isRandomPath 文件目录是否随机生成
	 * @return
	 */
	public FileModel upload(String loadPath, String viewPath, MultipartFile file, String businessId, boolean needHandle,String type,boolean isRandomPath);
	
	/**
	 * 
	 * @param loadPath 上传存储路径
	 * @param viewPath 访问路径
	 * @param file 文件
	 * @param businessId 业务id
	 * @param needHandle 是否需要处理成400 * 200 （资源缩略图逻辑需要）
	 * @param type 业务模块
	 * @return
	 */
	public FileModel upload(String loadPath, String viewPath, MultipartFile file, String businessId, boolean needHandle,String type);

	/**
	 *
	 * @param loadPath 上传存储路径
	 * @param viewPath 访问路径
	 * @param file 文件
	 * @param businessId 业务id
	 * @param needHandle 是否需要处理成width * height （资源缩略图逻辑需要）
	 * @param type 业务模块
	 * @return
	 */
	public FileModel uploadConfig(String loadPath, String viewPath, MultipartFile file, String businessId, boolean needHandle,String type,Integer width,Integer height);

	public PageModel<FileModel> page(FileModel query);
	
	FileModel findByNameAfter(String nameAfter);

	public boolean delete(String fileId);

	public void transformToPdf(FileModel resourceFile);

	public boolean deleteAllPic();

	public List<FileModel> findBannerAndThumbnail(FileModel fileModel);

	public FileModel uploadImg(String loadPath, String viewPath, MultipartFile file, String businessId, boolean needHandle,String type);
	
	public FileModel uploadImg(String loadPath, String viewPath, MultipartFile file, String businessId, boolean needHandle,String type,boolean isRandomPath);

	public List<FileModel> selectByCreateTime();
}
