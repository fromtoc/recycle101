package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_role_resource")
public class RoleResource extends Model<RoleResource>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableId(value = "id", type = IdType.ID_WORKER_STR)
	private String id;
	@TableField("role_id")
    private String roleId;
	@TableField("resource_id")
    private String resourceId;
    @TableField("tenant_id")
    private String tenantId;
    
    public RoleResource() {
	}
    
    public RoleResource(String id) {
    	this.id = id;
	}

    public RoleResource(String roleId, String resourceId) {
    	super();
		this.roleId = roleId;
		this.resourceId = resourceId;
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

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public boolean equals(Object obj) {   
        if (obj instanceof RoleResource) {   
        	RoleResource rec = (RoleResource) obj;   
            return this.roleId.equals(rec.roleId)   
                    && this.resourceId.equals(rec.resourceId);   
        }   
        return super.equals(obj);  
	}
	
	

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}