package com.xin.portal.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 
 *
 * @author zhoujun
 * @since 2018-11-15
 */
@TableName("t_permission")
public class Permission<getter> extends Model<Permission> implements Cloneable{

    private static final long serialVersionUID = 1L;

    public static final String MANAGE = "manage";//管理权限 -- 暂时废弃
    public static final String VIEW = "view";//可见权限
    public static final String COMMENT = "comment";//评论权限
    public static final String SHARE = "share";//分享权限
    public static final String PERMISSION = "permission";//权限用户权限
    public static final String LOG = "log";//日志权限
    public static final String EXPORT = "export";//导出权限
    public static final String DOWNLOAD = "download";//可下载权限
    public static final String SUBSCRIBE = "subscribe";//可订阅权限
    

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    
    /**
     * 资源类型id
     */
    @TableField("resource_type_id")
    private String resourceTypeId;

    /**
     * 资源id
     */
    @TableField("resource_id")
    private String resourceId;

    /**
     * 权限名称
     */
    private String name;
    private String code;

    @TableField("sort")
    private Integer sort;

    @TableField("tenant_id")
    private String tenantId;
    @TableField(exist=false)
    private String rolePermissionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getResourceTypeId() {
        return resourceTypeId;
    }

    public void setResourceTypeId(String resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getRolePermissionId() {
		return rolePermissionId;
	}

	public void setRolePermissionId(String rolePermissionId) {
		this.rolePermissionId = rolePermissionId;
	}

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
	public Permission clone() { 
		Permission clone = null; 
        try{ 
            clone = (Permission) super.clone(); 
 
        }catch(CloneNotSupportedException e){ 
            throw new RuntimeException(e); // won't happen 
        }
          
        return clone; 
    }

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id='" + id + '\'' +
                ", resourceTypeId='" + resourceTypeId + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", sort=" + sort +
                ", tenantId='" + tenantId + '\'' +
                ", rolePermissionId='" + rolePermissionId + '\'' +
                '}';
    }
}
