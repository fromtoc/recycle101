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
 * @since 2020-04-21
 */
@TableName("t_datasource")
public class Datasource extends Model<Datasource> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 数据源名称
     */
    @TableField("source_name")
    private String sourceName;

    /**
     * 自定义 1 /预定义 0 自定义-自己设置URL，预定义-程序预置URL，用户设置参数
     */
    @TableField("is_custom")
    private Integer isCustom;
    /**
     * 数据库名称
     */
    @TableField("database_name")
    private String databaseName;

    /**
     * 数据库类型
     */
    @TableField("database_type")
    private Integer databaseType;//1 mysql,2 oracle,

    /**
     * 数据库地址
     */
    @TableField("database_path")
    private String databasePath;

    /**
     * 端口号
     */
    @TableField("database_port")
    private String databasePort;

    /**
     * 用户名
     */
    @TableField("database_username")
    private String databaseUsername;

    /**
     * 密码
     */
    @TableField("database_password")
    private String databasePassword;

    /**
     * 是否激活（0 否 ，1 是）
     */
    @TableField("is_active")
    private Integer isActive;

    /**
     * 是否删除（0 否， 1 是）
     */
    @TableField("is_delete")
    private Integer isDelete;
    
    @TableField("remark")
    private String remark;
    
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField(exist=false)
    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
    public String getDatabaseName() {
        return databaseName;
    }
    
    public Integer getIsCustom() {
		return isCustom;
	}

	public void setIsCustom(Integer isCustom) {
		this.isCustom = isCustom;
	}

	public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    public Integer getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(Integer databaseType) {
        this.databaseType = databaseType;
    }
    public String getDatabasePath() {
        return databasePath;
    }

    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
    }
    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }
    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }
    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public final String getSql() {
		return sql;
	}

	public final void setSql(String sql) {
		this.sql = sql;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Datasource{" +
        "id=" + id +
        ", sourceName=" + sourceName +
        ", isCustom=" + isCustom +
        ", databaseName=" + databaseName +
        ", databaseType=" + databaseType +
        ", databasePath=" + databasePath +
        ", databasePort=" + databasePort +
        ", databaseUsername=" + databaseUsername +
        ", databasePassword=" + databasePassword +
        ", isActive=" + isActive +
        ", isDelete=" + isDelete +
        ", remark=" + remark +
        ", tenantId=" + tenantId +
        "}";
    }
}
