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
 * 
 *
 * @author xue
 * @since 2018-12-10
 */
@TableName("t_list_manage")
public class ListManage extends Model<ListManage> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 列表名称
     */
    private String name;

    /**
     * 父id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 创建时间
     */
    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    @TableField("sort")
    private Integer sort;
    
    @TableField("introduce")
    private String introduce;
    
    @TableField("tenant_id")
    private String tenantId;
    
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
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
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
	

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Override
    public String toString() {
        return "ListManage{" +
        "id=" + id +
        ", name=" + name +
        ", parentId=" + parentId +
        ", createTime=" + createTime +
        ", creater=" + creater +
        "}";
    }
}
