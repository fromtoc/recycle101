package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
@TableName("t_bi_independent")
public class BiIndependent extends Model<BiIndependent> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
   
    private String biUserId;
    @TableField(exist=false)
    private String username;
   
    private String biProjectId;
    @TableField(exist=false)
    private String title;
    
    private String biServerId;
    @TableField(exist=false)
    private String name;
    
    private String biOwnFolderId;
    
    private String biOwnFolderName;
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @TableField("creater")
    private String creater;
    @TableField(value="createName",exist=false)
    private String createName;
    
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @TableField("updater")
    private String updater;
    @TableField(value="updateName",exist=false)
    private String updateName;
    
    private String tenantId;
    
    @TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;
    
	public BiIndependent() {
		super();
	}

	public BiIndependent(String id, String biUserId, String username, String biProjectId, String title,
			String biServerId, String name, String biOwnFolderId, String biOwnFolderName, Date createTime, String creater,
			String createName, Date updateTime, String updater, String updateName, String tenantId) {
		super();
		this.id = id;
		this.biUserId = biUserId;
		this.username = username;
		this.biProjectId = biProjectId;
		this.title = title;
		this.biServerId = biServerId;
		this.name = name;
		this.biOwnFolderId = biOwnFolderId;
		this.biOwnFolderName = biOwnFolderName;
		this.createTime = createTime;
		this.creater = creater;
		this.createName = createName;
		this.updateTime = updateTime;
		this.updater = updater;
		this.updateName = updateName;
		this.tenantId = tenantId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBiUserId() {
		return biUserId;
	}

	public void setBiUserId(String biUserId) {
		this.biUserId = biUserId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getBiProjectId() {
		return biProjectId;
	}

	public void setBiProjectId(String biProjectId) {
		this.biProjectId = biProjectId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBiServerId() {
		return biServerId;
	}
	
	public void setBiServerId(String biServerId) {
		this.biServerId = biServerId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBiOwnFolderId() {
		return biOwnFolderId;
	}

	public void setBiOwnFolderId(String biOwnFolderId) {
		this.biOwnFolderId = biOwnFolderId;
	}

	public String getBiOwnFolderName() {
		return biOwnFolderName;
	}

	public void setBiOwnFolderName(String biOwnFolderName) {
		this.biOwnFolderName = biOwnFolderName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
