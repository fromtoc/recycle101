package com.xin.portal.developer.model;

import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

public class FileOutConfigXin extends FileOutConfig {
	
	private String outPutFile;
	
	public FileOutConfigXin () {
		super();
	}
	
	public FileOutConfigXin (String templatePath, String outPutFile) {
		super();
		this.setTemplatePath(templatePath);
		this.outPutFile = outPutFile;
	}

	@Override
	public String outputFile(TableInfo tableInfo) {
		return this.outPutFile;
	}

}
