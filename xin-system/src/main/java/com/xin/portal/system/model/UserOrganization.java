package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_user_organization")
public class UserOrganization extends Model<UserOrganization>{
	
	@TableId(value="id",type=IdType.ID_WORKER_STR)
	private String id;
	/**
	 * 用户id
	 */
	@TableField(value="user_id")
	private String userId;
	/**
	 * 组织id
	 */
	@TableField(value="org_id")
	private String orgId;
	/**
	 * 是否兼职;0:否/1:是
	 */
	@TableField(value="is_deputy")
	private Integer isDeputy;
	/**
	 * 租户id
	 */
	@TableField(value="tenant_id")
	private String tenantId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public final String getOrgId() {
		return orgId;
	}
	public final void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public final Integer getIsDeputy() {
		return isDeputy;
	}
	public final void setIsDeputy(Integer isDeputy) {
		this.isDeputy = isDeputy;
	}
	public final String getTenantId() {
		return tenantId;
	}
	public final void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	@Override
    protected Serializable pkVal() {
        return this.id;
    }
	@Override
	public String toString() {
		return "UserOrganization [id=" + id + ", userId=" + userId + ", orgId=" + orgId + ", isDeputy=" + isDeputy
				+ ", tenantId=" + tenantId + "]";
	}
	
}
