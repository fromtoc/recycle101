package com.xin.portal.system.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_file")
public class FileModel extends Model<FileModel>{
	
	/**
	 * bi用户导入
	 */
	public static final String BIUSER_IMPORT = "biUser-import";
	/**
	 * bi映射导入
	 */
	public static final String BIMAPPING_IMPORT = "biMapping-import";
	/**
	 * 字典值导入
	 */
	public static final String DICTVALUE_IMPORT = "dictValue-import";
	/**
	 * 用户导入
	 */
	public static final String IMPORT = "import";
	/**
	 * 资源中文档的文件上传
	 */
	public static final String RESOURCE = "resource";
	/**
	 * 缩略图
	 */
	public static final String THUMBNAIL = "thumbnail";
	/**
	 * 登录页或者主页图标
	 */
	public static final String LOGO = "logo";
	/**
	 * 轮播图
	 */
	public static final String BANNER = "banner";
	/**
	 * 生成激活文件的类型
	 */
	public static final String LICENSE = "license";
	/**
	 * 激活密钥文件上传类型
	 */
	public static final String LIC = "lic";
	/**
	 * 微信推送转换文件
	 */
	public static final String WXPUSH = "wxPush";
	
	private static final long serialVersionUID = 1L;

	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;

    @TableField("name_before")
    private String nameBefore;
    @TableField("name_after")
    private String nameAfter;
    @TableField("create_time")
    private Date createTime;
    @TableField("file_size")
    private Double fileSize;
    @TableField("file_path_save")
    private String filePathSave;
    @TableField("file_path_view")
    private String filePathView;
    @TableField("file_type")
    private String fileType;
    @TableField("business_info")
    private String businessInfo;
    @TableField("business_type")
    private String businessType;
    @TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;
    
	@TableField("tenant_id")
	private String tenantId;
	@TableField("group_code")
	private String groupCode;
	
    public FileModel(){
    	
    }
    
    /**
     * 
     * @param businessInfo
     */
    public FileModel(String businessInfo){
    	this.businessInfo = businessInfo;
    }
    

    public FileModel(String nameBefore, String nameAfter,
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

	public String getId() {
        return id;
    }

    public void setId(String id) {
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

	
	

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSavedPath() {
        return this.getFilePathSave()+File.separator+this.getNameAfter()+"."+this.getFileType();
    }

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public final String getGroupCode() {
		return groupCode;
	}
	public final void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
}