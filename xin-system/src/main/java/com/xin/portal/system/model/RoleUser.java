package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_role_user")
public class RoleUser extends Model<RoleUser>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("role_id")
    private String roleId;
    @TableField("user_id")
    private String userId;
    @TableField("role_code")
    private String roleCode;
    
    @TableField("tenant_id")
    private String tenantId;
    
    public RoleUser() {	}

    public RoleUser(String roleCode) {
		this.roleCode = roleCode;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
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

	@Override
	public String toString() {
		return "RoleUser {id=" + id + ", roleId=" + roleId + ", userId=" + userId + ", roleCode=" + roleCode + "}";
	}
	
	
}