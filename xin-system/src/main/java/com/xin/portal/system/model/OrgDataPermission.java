package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_org_data_permission")
public class OrgDataPermission extends Model<OrgDataPermission>{
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
	
	@TableField("org_id")
    private String orgId;
	
    @TableField("dp_id")
    private String dpId;
    
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField(exist = false)
    private String dptype;
    
    @TableField(exist = false)
    private String typeId;
    
    @TableField(exist = false)
    private String dpname;
    
    @TableField(exist = false)
    private String extcode;
    
    @TableField(exist = false)
    private Integer sort;
    
    @TableField(exist = false)
    private String orgName;
    
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

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
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

	public final String getTypeId() {
		return typeId;
	}

	public final void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getDpname() {
		return dpname;
	}

	public void setDpname(String dpname) {
		this.dpname = dpname;
	}
	
	public String getExtcode() {
		return extcode;
	}

	public void setExtcode(String extcode) {
		this.extcode = extcode;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
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
	public String toString() {
		return "OrgDataPermission [id=" + id + ", orgId=" + orgId + ", dpId=" + dpId + ", tenantId=" + tenantId
				+ ", dptype=" + dptype + ", typeId=" + typeId + ", dpname=" + dpname + ", extcode=" + extcode
				+ ", sort=" + sort + ", orgName=" + orgName + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize
				+ "]";
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
    
}