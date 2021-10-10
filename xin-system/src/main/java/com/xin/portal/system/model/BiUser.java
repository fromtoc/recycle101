package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
@TableName("t_bi_user")
public class BiUser extends Model<BiUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    private String username;
    private String password;
    @TableField("bi_group")
    private String biGroup;
    private String remark;
    @TableField("own_folder_id")
    private String ownFolderId;
    @TableField(exist=false)
    private String ownFolderName;
    @TableField("bi_server_id")
    private String biServerId;
    @TableField("bi_object_id")
    private String biObjectId;
    private String state;
    @TableField("update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @TableField("tenant_id")
    private String tenantId;
    private String type;
    
    @TableField(exist = false)
	private String serverPort;
    @TableField(exist = false)
	private String serverName;
    @TableField(exist = false)
	private String projectName;
    @TableField(exist = false)
    private String userId;
    @TableField(exist = false)
    private String biProjectId;
	
	public BiUser(){};
	
	public BiUser(String username, String password){
		this.username = username;
		this.password = password;
	};


    public BiUser(String serverName,String projectName, String port,String username, String password) {
    	this.serverName = serverName;
    	this.projectName = projectName;
    	this.serverPort = port;
    	this.username = username;
		this.password = password;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBiGroup() {
		return biGroup;
	}

	public void setBiGroup(String biGroup) {
		this.biGroup = biGroup;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOwnFolderId() {
		return ownFolderId;
	}

	public void setOwnFolderId(String ownFolderId) {
		this.ownFolderId = ownFolderId;
	}
	
	public String getOwnFolderName() {
		return ownFolderName;
	}

	public void setOwnFolderName(String ownFolderName) {
		this.ownFolderName = ownFolderName;
	}

	public String getBiServerId() {
		return biServerId;
	}

	public void setBiServerId(String biServerId) {
		this.biServerId = biServerId;
	}

	public String getBiObjectId() {
		return biObjectId;
	}

	public void setBiObjectId(String biObjectId) {
		this.biObjectId = biObjectId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getBiProjectId() {
		return biProjectId;
	}

	public void setBiProjectId(String biProjectId) {
		this.biProjectId = biProjectId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BiUser other = (BiUser) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BiUser [id=" + id + ", username=" + username + ", password=" + password + ", biGroup=" + biGroup
				+ ", remark=" + remark + ", ownFolderId=" + ownFolderId + ", biServerId=" + biServerId + ", biObjectId="
				+ biObjectId + ", state=" + state + ", updateTime=" + updateTime + ", tenantId=" + tenantId + ", type="
				+ type + ", serverPort=" + serverPort + ", serverName=" + serverName + ", projectName=" + projectName
				+ ", userId=" + userId + "]";
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	

   
}
