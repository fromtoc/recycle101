package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_organization")
public class Organization extends Model<Organization>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
	@TableField("parent_id")
    private String parentId;
    private String code;
    @TableField("ext_code")
    private String extCode;

    private String name;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    private Integer state;
    private String remark;
    @TableField("sort")
    private Integer sort;
    @TableField("status")
    private Integer status;
    @TableField("ext_id")
    private String extId;
    @TableField("tenant_id")
    private String tenantId;
    
  //查询时code like 
    @TableField(exist = false)
    private String codeLike;
  //查询时code like 但不包含其本身，例：001 返回001001，不包含001
    @TableField(exist = false)
    private String codeLikeChild;
    @TableField(exist = false)
    private Integer level;
    @TableField(exist = false)
    private String userId;
    @TableField(exist = false)
    private Integer isDeputy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExtCode() {
		return extCode;
	}

	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getCodeLike() {
		return codeLike;
	}

	public void setCodeLike(String codeLike) {
		this.codeLike = codeLike;
	}

	public String getCodeLikeChild() {
		return codeLikeChild;
	}

	public void setCodeLikeChild(String codeLikeChild) {
		this.codeLikeChild = codeLikeChild;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public final Integer getIsDeputy() {
		return isDeputy;
	}

	public final void setIsDeputy(Integer isDeputy) {
		this.isDeputy = isDeputy;
	}

	@Override
	public String toString() {
		return "Organization [id=" + id + ", parentId=" + parentId + ", code=" + code + ", extCode=" + extCode
				+ ", name=" + name + ", createTime=" + createTime + ", updateTime=" + updateTime + ", state=" + state
				+ ", remark=" + remark + ", sort=" + sort + ", status=" + status + ", extId=" + extId + ", tenantId="
				+ tenantId + ", codeLike=" + codeLike + ", codeLikeChild=" + codeLikeChild + ", level=" + level
				+ ", userId=" + userId + ", isDeputy=" + isDeputy + "]";
	}
	
    
}