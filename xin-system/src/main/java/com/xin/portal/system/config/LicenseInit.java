package com.xin.portal.system.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.cache.CacheManager;
import com.xin.portal.system.license.ClientVerifyLicense;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.model.License;
import com.xin.portal.system.service.FileService;

/**
 * 初始化读取lic文件
 * @author Administrator
 *
 */
@Component
public class LicenseInit implements CommandLineRunner{

	@Autowired FileService fileService;

	@Override
	public void run(String... args) throws Exception {
		ClientVerifyLicense verify = new ClientVerifyLicense();
		Boolean flag =verify.uninstall();
		System.out.println("删除license是否成功："+flag);
		List<FileModel> list=fileService.selectByCreateTime();
		if (list.size()!=0) {
			FileModel file=list.get(0);
			if(file!=null) {
				License license = verify.installAndVerify(file.getSavedPath());
				if (license!=null){
					Integer maxUserCount=license.getMaxUserCount();
					CacheManager.put("maxUserCount", maxUserCount==null?2:maxUserCount);
				}
			}
		}
	}

}
