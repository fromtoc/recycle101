package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_data_permission_type")
public class DataPermissionType extends Model<DataPermissionType>{
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
	
	@TableField("name")
    private String name;
	
	@TableField("parent_id")
    private String parentId;
	
    @TableField("extcode")
    private String extcode;

    @TableField("sort")
    private Integer sort;
    
    @TableField("create_time")
    private Date createTime;
    
    @TableField("creater")
    private String creater;
    
    @TableField("update_time")
    private Date updateTime;
    
    @TableField("updater")
    private String updater;
    
    @TableField("remark")
    private String remark;
    
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField(exist = false)
	private Integer pageNumber;
    
	@TableField(exist = false)
	private Integer pageSize;
	
	public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	public final String getParentId() {
		return parentId;
	}
	public final void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public final String getExtcode() {
		return extcode;
	}
	public final void setExtcode(String extcode) {
		this.extcode = extcode;
	}
	public final Integer getSort() {
		return sort;
	}
	public final void setSort(Integer sort) {
		this.sort = sort;
	}
	public final Date getCreateTime() {
		return createTime;
	}
	public final void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public final String getCreater() {
		return creater;
	}
	public final void setCreater(String creater) {
		this.creater = creater;
	}
	public final Date getUpdateTime() {
		return updateTime;
	}
	public final void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public final String getUpdater() {
		return updater;
	}
	public final void setUpdater(String updater) {
		this.updater = updater;
	}
	public final String getRemark() {
		return remark;
	}
	public final void setRemark(String remark) {
		this.remark = remark;
	}
	public final String getTenantId() {
		return tenantId;
	}
	public final void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public final Integer getPageNumber() {
		return pageNumber;
	}
	public final void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public final Integer getPageSize() {
		return pageSize;
	}
	public final void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	@Override
	public String toString() {
		return "DataPermissionType [id=" + id + ", name=" + name + ", parentId=" + parentId + ", extcode=" + extcode
				+ ", sort=" + sort + ", createTime=" + createTime + ", creater=" + creater + ", updateTime="
				+ updateTime + ", updater=" + updater + ", remark=" + remark + ", tenantId=" + tenantId
				+ ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + "]";
	}
    
}