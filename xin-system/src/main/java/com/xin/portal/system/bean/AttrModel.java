package com.xin.portal.system.bean;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

public class AttrModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private Integer id;

    private String nameBefore;
    private String nameAfter;
    private Date createTime;
    private Double fileSize;
    private String filePathSave;
    private String filePathView;
    private String fileType;
    private String businessInfo;
    private String businessType;
    
    public AttrModel(){
    	
    }
    
    /**
     * 
     * @param businessInfo
     */
    public AttrModel(String businessInfo){
    	this.businessInfo = businessInfo;
    }
    

    public AttrModel(String nameBefore, String nameAfter,
			Double fileSize, String filePathSave, String filePathView,String fileType,
			String businessInfo, String businessType) {
		super();
		this.nameBefore = nameBefore;
		this.nameAfter = nameAfter;
		this.createTime = new Date();
		this.fileSize = fileSize;
		this.filePathSave = filePathSave;
		this.filePathView = filePathView;
		this.fileType = fileType;
		this.businessInfo = businessInfo;
		this.businessType = businessType;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNameBefore() {
        return nameBefore;
    }

    public void setNameBefore(String nameBefore) {
        this.nameBefore = nameBefore;
    }

    public String getNameAfter() {
        return nameAfter;
    }

    public void setNameAfter(String nameAfter) {
        this.nameAfter = nameAfter;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePathSave() {
		return filePathSave;
	}


	public void setFilePathSave(String filePathSave) {
		this.filePathSave = filePathSave;
	}


	public String getFilePathView() {
		return filePathView;
	}


	public void setFilePathView(String filePathView) {
		this.filePathView = filePathView;
	}


	public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public String getBusinessInfo() {
		return businessInfo;
	}


	public void setBusinessInfo(String businessInfo) {
		this.businessInfo = businessInfo;
	}


	public String getBusinessType() {
		return businessType;
	}


	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}


	public String getSavedPath() {
        return this.getFilePathSave()+"\\"+this.getNameAfter()+"."+this.getFileType();
    }

}