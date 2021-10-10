package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_user_data_permission")
public class UserDataPermission extends Model<UserDataPermission>{
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
	
	@TableField("user_id")
    private String userId;
	
    @TableField("dp_id")
    private String dpId;
    
    private Integer state;
    
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField(exist = false)
    private Integer pageSize;
    @TableField(exist = false)
    private Integer pageNumber;
    @TableField(exist = false)
    private String dptype;
    @TableField(exist = false)
    private String dpname;
    @TableField(exist = false)
    private String extcode;
    @TableField(exist = false)
    private Integer sort;
    @TableField(exist = false)
    private String orgId;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDpId() {
		return dpId;
	}

	public void setDpId(String dpId) {
		this.dpId = dpId;
	}

	public final Integer getState() {
		return state;
	}

	public final void setState(Integer state) {
		this.state = state;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public final Integer getPageSize() {
		return pageSize;
	}

	public final void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public final Integer getPageNumber() {
		return pageNumber;
	}

	public final void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public final String getDptype() {
		return dptype;
	}

	public final void setDptype(String dptype) {
		this.dptype = dptype;
	}

	public final String getDpname() {
		return dpname;
	}

	public final void setDpname(String dpname) {
		this.dpname = dpname;
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

	public final String getOrgId() {
		return orgId;
	}

	public final void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "UserDataPermission {id=" + id + ", userId=" + userId + ", dpId=" + dpId + ", state=" + state
				+ ", tenantId=" + tenantId + ", pageSize=" + pageSize + ", pageNumber=" + pageNumber + ", dptype="
				+ dptype + ", dpname=" + dpname + ", extcode=" + extcode + ", sort=" + sort + ", orgId=" + orgId + "}";
	}

}