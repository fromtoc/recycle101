package com.xin.portal.system.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 模板基础信息表
 *
 * @author xue
 * @since 2018-11-08
 */
@TableName("t_module")
public class Module extends Model<Module> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    /**
     * 状态：0正常1禁用
     */
    private String state;

    private String creater;

    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @TableField("update_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 0未删除，1 删除了
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 0不可编辑，1可以编辑
     */
    @TableField("is_edit")
    private Integer isEdit;

    /**
     *0普通用户自己存储 ，其他为非普通用户存储
     */
    @TableField("type")
    private Integer type;

    /**
     *备注
     */
    @TableField("comments")
    private String comments;
    
    @TableField(exist=false)
    private String organizationId;
    
    @TableField(exist=false)
    private Integer pageNumber;
    @TableField(exist=false)
    private Integer pageSize;
    @TableField("tenant_id")
    private String tenantId;

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }
    public Integer getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
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
        return "Module{" +
        "id=" + id +
        ", name=" + name +
        ", state=" + state +
        ", creater=" + creater +
        ", createTime=" + createTime +
        ", isDelete=" + isDelete +
        ", isEdit=" + isEdit +
        ", tenantId=" + tenantId +
        "}";
    }
}
