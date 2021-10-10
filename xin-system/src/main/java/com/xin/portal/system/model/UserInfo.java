package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_user_info")
public class UserInfo  extends Model<UserInfo>{
	
	private static final long serialVersionUID = 1L;
	
	@TableId(value = "ID", type = IdType.INPUT)
    private String id;

	private String realname;

    private String nickname;

    private Integer gender;

    private Date birthday;

    private String intro;

    private String department;

    private String qq;

    private String email;

    private String phone;

    private String avatar;

    private String signature;
    
    private String address;
    
    private String code;
    
    private String secretKey;
    
    private Date outDate;
    
    private String locale;
    
    @TableField("org_code")
    private String orgCode;
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField(exist = false)
    private String orgName;
    @TableField(exist = false)
    private String extCode;
    @TableField(exist = false)
    private String username;
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;
	@TableField(exist = false)
	private Integer wxOpenid;
	@TableField("org_id")
	private String orgId;
	@TableField(exist = false)
	private String roleIdIn;
	@TableField(exist = false)
	private String roleIdNot;
	@TableField(exist = false)
	private String projectId;
	@TableField(exist = false)
	private String projectIdNot;
	@TableField(exist = false)
	private String password;
	@TableField(exist = false)
	private String state;
	@TableField("mstr_user")
	private Integer mstrUser;
	@TableField(exist = false)
	private String orgIdIn;
	@TableField(exist = false)
	private String roleId;
	@TableField(exist = false)
	private String idNot;
	@TableField(exist = false)
	private String biUserIdNot;
	@TableField(exist = false)
	private String roleName;
	@TableField(exist = false)
	private String wxbind;

	/**
     * 密码过期时间
     */
	@TableField(exist = false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date pwdExpireTime;
    /**
     * 账号有效时间起
     */
	@TableField(exist = false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date activateStartTime;
    /**
     * 账号有效时间止
     */
	@TableField(exist = false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date activateEndTime;
	
	/**
     * 账号是否删除
     */
    @TableField(exist = false)
    private Integer isDelete;
    @TableField(exist = false)
	@DateTimeFormat(pattern="yyyy-MM-dd")
    private Date registerTime;
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    

    @TableField(exist = false)
	private String userEmail;
    @TableField("preference_type")
    private String preferenceType;
    @TableField("preference_value")
    private String preferenceValue;
    
    /**当同步创建mstr用户，是否记住mstr项目的选择，如果记住则后续创建mstr用户默认使用这个项目*/
    @TableField(exist=false)
    private String remberMstrChoice;
    /**当同步创建mstr用户,选择的mstr服务*/
    @TableField(exist=false)
    private String mstrServerId;
    /**当同步创建mstr用户，选择的mstr项目*/
    @TableField(exist=false)
    private String mstrProjectId;

	@TableField(exist = false)
	private String ip;

	@TableField(exist = false)
	private String sessionId;
	
	@TableField(exist = false)
	private List<Organization> userOrgs;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public UserInfo(String id) {
		this.id = id;
	}

	public UserInfo() {
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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
	
	public String getExtCode() {
		return extCode;
	}

	public void setExtCode(String extCode) {
		this.extCode = extCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
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
	
	public Integer getWxOpenid() {
		return wxOpenid;
	}

	public void setWxOpenid(Integer wxOpenid) {
		this.wxOpenid = wxOpenid;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	

	public String getRoleIdIn() {
		return roleIdIn;
	}

	public void setRoleIdIn(String roleIdIn) {
		this.roleIdIn = roleIdIn;
	}

	public String getRoleIdNot() {
		return roleIdNot;
	}

	public void setRoleIdNot(String roleIdNot) {
		this.roleIdNot = roleIdNot;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	
	public Integer getMstrUser() {
		return mstrUser;
	}

	public void setMstrUser(Integer mstrUser) {
		this.mstrUser = mstrUser;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

	public String getOrgIdIn() {
		return orgIdIn;
	}

	public void setOrgIdIn(String orgIdIn) {
		this.orgIdIn = orgIdIn;
	}

	public String getIdNot() {
		return idNot;
	}

	public void setIdNot(String idNot) {
		this.idNot = idNot;
	}
	
	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectIdNot() {
		return projectIdNot;
	}

	public void setProjectIdNot(String projectIdNot) {
		this.projectIdNot = projectIdNot;
	}
	
	
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}


	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	public String getPreferenceType() {
		return preferenceType;
	}
	public void setPreferenceType(String preferenceType) {
		this.preferenceType = preferenceType;
	}
	public String getPreferenceValue() {
		return preferenceValue;
	}
	public void setPreferenceValue(String preferenceValue) {
		this.preferenceValue = preferenceValue;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getBiUserIdNot() {
		return biUserIdNot;
	}

	public void setBiUserIdNot(String biUserIdNot) {
		this.biUserIdNot = biUserIdNot;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getRemberMstrChoice() {
		return remberMstrChoice;
	}

	public void setRemberMstrChoice(String remberMstrChoice) {
		this.remberMstrChoice = remberMstrChoice;
	}

	public String getMstrServerId() {
		return mstrServerId;
	}

	public void setMstrServerId(String mstrServerId) {
		this.mstrServerId = mstrServerId;
	}

	public String getMstrProjectId() {
		return mstrProjectId;
	}

	public void setMstrProjectId(String mstrProjectId) {
		this.mstrProjectId = mstrProjectId;
	}


	public String getWxbind() {
		return wxbind;
	}

	public void setWxbind(String wxbind) {
		this.wxbind = wxbind;
	}

	public final List<Organization> getUserOrgs() {
		return userOrgs;
	}

	public final void setUserOrgs(List<Organization> userOrgs) {
		this.userOrgs = userOrgs;
	}

	@Override
	public String toString() {
		return "UserInfo {id=" + id + ", realname=" + realname + ", nickname=" + nickname + ", gender=" + gender
				+ ", birthday=" + birthday + ", intro=" + intro + ", department=" + department + ", qq=" + qq
				+ ", email=" + email + ", phone=" + phone + ", avatar=" + avatar + ", signature=" + signature
				+ ", address=" + address + ", orgCode=" + orgCode + ", orgName=" + orgName + ", username=" + username
				+ ", wxOpenid=" + wxOpenid + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + ",mstrUser="+mstrUser
				+ ",code="+code+"}";
	}
	
	
    
    
}