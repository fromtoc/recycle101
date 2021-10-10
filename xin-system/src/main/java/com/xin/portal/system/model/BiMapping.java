package com.xin.portal.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;


/**
 * 用户映射表
 *
 * @author zhoujun
 * @since 2018-12-07
 */
@TableName("t_bi_mapping")
public class BiMapping extends Model<BiMapping> {

    private static final long serialVersionUID = 1L;
    
    
    public BiMapping() {
		super();
	}


	public BiMapping(String username, String biServerName, String biUserName) {
		super();
		this.username = username;
		this.biServerName = biServerName;
		this.biUserName = biUserName;
	}



	@TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * BI用户id
     */
    @TableField("bi_user_id")
    private String biUserId;

    /**
     * portal用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 类型：1MSTR
     */
    private String type;
    

    @TableField("tenant_id")
    private String tenantId;
    /**
     * 源系统id
     */
    @TableField(exist=false)
    private String biServerId;
    /**
     * 源系统
     */
    @TableField(exist=false)
    private String biServerName;
    /**
     * 源系统账号
     */
    @TableField(exist=false)
    private String biUserName;
    
    @TableField(exist=false)
    private String biPassword;
    /**
     * 系统账号
     */
    @TableField(exist=false)
    private String username;
    /**
     * 姓名
     */
    @TableField(exist=false)
    private String realname;
    /**
     * 组织
     */
    @TableField(exist=false)
    private String orgName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getBiUserId() {
        return biUserId;
    }

    public void setBiUserId(String biUserId) {
        this.biUserId = biUserId;
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
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBiUserName() {
		return biUserName;
	}

	public void setBiUserName(String biUserName) {
		this.biUserName = biUserName;
	}

	public String getUsername() {
		return username;
	}
	
	public String getBiPassword() {
		return biPassword;
	}

	public void setBiPassword(String biPassword) {
		this.biPassword = biPassword;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getBiServerName() {
		return biServerName;
	}


	public void setBiServerName(String biServerName) {
		this.biServerName = biServerName;
	}

	public String getBiServerId() {
		return biServerId;
	}


	public void setBiServerId(String biServerId) {
		this.biServerId = biServerId;
	}


	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BiMapping{" +
        "id=" + id +
        ", biUserId=" + biUserId +
        ", userId=" + userId +
        ", type=" + type +
        ", tenantId=" + tenantId +
        "}";
    }
}
