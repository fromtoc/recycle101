package com.xin.portal.system.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 
 *
 * @author xue
 * @since 2018-12-11
 */
@TableName("t_list_manage_resource")
public class ListManageResource extends Model<ListManageResource> {

    private static final long serialVersionUID = 1L;

    /**
     * 列表id
     */
    @TableId(value = "list_id", type = IdType.ID_WORKER_STR)
    private String listId;

    /**
     * 资源id
     */
    @TableField("resource_id")
    private String resourceId;
    
    @TableField("sort")
    private Integer sort;

    @TableField(exist=false)
    private String name;
    
    @TableField("tenant_id")
    private String tenantId;

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    protected Serializable pkVal() {
        return this.listId;
    }

    public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
    public String toString() {
        return "ListManageResource{" +
        "listId=" + listId +
        ", resourceId=" + resourceId +
        ", tenantId=" + tenantId +
        "}";
    }
}
