package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_data_permission")
public class DataPermission extends Model<DataPermission>{
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
	
	@TableField("dpname")
    private String dpname;
	
    @TableField("type_id")
    private String typeId;

    @TableField("extcode")
    private String extcode;
    
    @TableField("remark")
    private String remark;
    
    @TableField("sort")
    private Integer sort;
    
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField(exist = false)
    private String dptype;
    
    @TableField(exist = false)
	private String orgId;
    
    @TableField(exist = false)
    private String userId;
    
    @TableField(exist = false)
	private Integer pageNumber;
    
	@TableField(exist = false)
	private Integer pageSize;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDpname() {
		return dpname;
	}
	public void setDpname(String dpname) {
		this.dpname = dpname;
	}
	public final String getTypeId() {
		return typeId;
	}
	public final void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getExtcode() {
		return extcode;
	}
	public void setExtcode(String extcode) {
		this.extcode = extcode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getDptype() {
		return dptype;
	}
	public void setDptype(String dptype) {
		this.dptype = dptype;
	}
	public String getOrgId() {
		return orgId;
	}
	public final String getUserId() {
		return userId;
	}
	public final void setUserId(String userId) {
		this.userId = userId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
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
	@Override
	public String toString() {
		return "DataPermission {id=" + id + ", dpname=" + dpname + ", dptype=" + dptype + ", typeId=" + typeId
				+ ", remark=" + remark + ", sort=" + sort + ", tenantId=" + tenantId + ", extcode=" + extcode
				+ ", orgId=" + orgId + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + "}";
	}
    
}