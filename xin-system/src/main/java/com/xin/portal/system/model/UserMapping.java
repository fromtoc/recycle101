package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 用户映射表
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
@TableName("t_user_mapping")
public class UserMapping extends Model<UserMapping> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("user_id")
    private String userId;
    /**
     * 类型：1MSTR
     */
    private String type;
    @TableField("mapping_user_id")
    private String mappingUserId;
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;
	@TableField(exist=false)
    private String mappingName;
    @TableField(exist=false)
    private String mappingPwd;
	@TableField(exist=false)
    private String username;
    @TableField(exist=false)
    private String orgName;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMappingUserId() {
        return mappingUserId;
    }

    public void setMappingUserId(String mappingUserId) {
        this.mappingUserId = mappingUserId;
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
    protected Serializable pkVal() {
        return this.id;
    }

    public String getMappingName() {
		return mappingName;
	}

	public void setMappingName(String mappingName) {
		this.mappingName = mappingName;
	}

	public String getMappingPwd() {
		return mappingPwd;
	}

	public void setMappingPwd(String mappingPwd) {
		this.mappingPwd = mappingPwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Override
    public String toString() {
        return "UserMapping{" +
        "id=" + id +
        ", userId=" + userId +
        ", type=" + type +
        ", mappingUserId=" + mappingUserId +
        ", mappingName=" + mappingName +
        ", mappingPwd=" + mappingPwd +
        "}";
    }
}
