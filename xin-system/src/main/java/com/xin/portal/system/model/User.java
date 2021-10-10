package com.xin.portal.system.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;


@TableName("t_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "ID", type = IdType.ID_WORKER_STR)
    private String id;

    private String username;

    private String password;
    @TableField("register_time")
    private Date registerTime;
    @TableField("register_ip")
    private String registerIp;
    @TableField("last_login_time")
    private Date lastLoginTime;
    @TableField("last_login_ip")
    private String lastLoginIp;
    @TableField("login_count")
    private Integer loginCount;
    @TableField("reset_key")
    private String resetKey;
    @TableField("reset_pwd")
    private String resetPwd;
    @TableField("reset_key_lasttime")
    private Date resetKeyLasttime;
    @TableField("error_time")
    private Date errorTime;
    @TableField("error_count")
    private Integer errorCount;
    @TableField("error_ip")
    private String errorIp;
    @TableField("status")
    private String status;
    @TableField("ext_id")
    private String extId;


    private Boolean activation;
    @TableField("activation_code")
    private String activationCode;
    @TableField("wx_openid")
    private String wxOpenid;
    private Integer state;
    /**
     * 密码过期时间
     */
    @TableField("pwd_expire_time")
    private Date pwdExpireTime;
    /**
     * 账号有效时间起
     */
    @TableField("activate_start_time")
    private Date activateStartTime;
    /**
     * 账号有效时间止
     */
    @TableField("activate_end_time")
    private Date activateEndTime;
    /**
     * 账号是否删除
     */
    @TableLogic("is_delete")
    private Integer isDelete;
    @TableField("tenant_id")
    private String tenantId;
    
    
    
    
	@TableField(exist=false)
    private String orgName;
    @TableField(exist=false)
    private Integer roleId;
    @TableField(exist=false)
    private Integer roleIdNot;
    @TableField(exist=false)
    private Integer pageNumber;
    @TableField(exist=false)
    private Integer pageSize;
    /**
     * mstr用户映射时查询作为条件载体
     */
    @TableField(exist=false)
    private Integer mstrUserIdNot;

    public User(String id) {
		this.id = id;
	}
    
    public User() {
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

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public String getResetPwd() {
        return resetPwd;
    }

    public void setResetPwd(String resetPwd) {
        this.resetPwd = resetPwd;
    }
    
    public Date getResetKeyLasttime() {
		return resetKeyLasttime;
	}

	public void setResetKeyLasttime(Date resetKeyLasttime) {
		this.resetKeyLasttime = resetKeyLasttime;
	}

	public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }

    public Integer getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Integer errorCount) {
        this.errorCount = errorCount;
    }

    public String getErrorIp() {
        return errorIp;
    }

    public void setErrorIp(String errorIp) {
        this.errorIp = errorIp;
    }

    public Boolean getActivation() {
        return activation;
    }

    public void setActivation(Boolean activation) {
        this.activation = activation;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

	public String getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(String wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getRoleIdNot() {
		return roleIdNot;
	}

	public void setRoleIdNot(Integer roleIdNot) {
		this.roleIdNot = roleIdNot;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
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

	public Integer getMstrUserIdNot() {
		return mstrUserIdNot;
	}

	public void setMstrUserIdNot(Integer mstrUserIdNot) {
		this.mstrUserIdNot = mstrUserIdNot;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getPwdExpireTime() {
		return pwdExpireTime;
	}

	public void setPwdExpireTime(Date pwdExpireTime) {
		this.pwdExpireTime = pwdExpireTime;
	}

	public Date getActivateStartTime() {
		return activateStartTime;
	}

	public void setActivateStartTime(Date activateStartTime) {
		this.activateStartTime = activateStartTime;
	}

	public Date getActivateEndTime() {
		return activateEndTime;
	}

	public void setActivateEndTime(Date activateEndTime) {
		this.activateEndTime = activateEndTime;
	}
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

    public String getActivateEndTimeTran(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        return  activateEndTime==null?null:sdf.format(activateEndTime);
    }

    public String getActivateStartTimeTran(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        return  activateStartTime==null?null:sdf.format(activateStartTime);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", registerTime=" + registerTime
				+ ", registerIp=" + registerIp + ", lastLoginTime=" + lastLoginTime + ", lastLoginIp=" + lastLoginIp
				+ ", loginCount=" + loginCount + ", resetKey=" + resetKey + ", resetPwd=" + resetPwd
				+ ", resetKeyLasttime=" + resetKeyLasttime + ", errorTime=" + errorTime + ", errorCount=" + errorCount
				+ ", errorIp=" + errorIp + ", status=" + status + ", extId=" + extId + ", activation=" + activation
				+ ", activationCode=" + activationCode + ", wxOpenid=" + wxOpenid + ", state=" + state
				+ ", pwdExpireTime=" + pwdExpireTime + ", activateStartTime=" + activateStartTime + ", activateEndTime="
				+ activateEndTime + ", isDelete=" + isDelete + ", tenantId=" + tenantId + ", orgName=" + orgName
				+ ", roleId=" + roleId + ", roleIdNot=" + roleIdNot + ", pageNumber=" + pageNumber + ", pageSize="
				+ pageSize + ", mstrUserIdNot=" + mstrUserIdNot + "]";
	}
    
    
}