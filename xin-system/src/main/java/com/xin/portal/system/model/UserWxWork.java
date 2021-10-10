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
 * 在通讯录同步助手中此接口可以读取企业通讯录的所有成员信息，而自建应用可以读取该应用设置的可见范围内的成员信息。

请求方式：GET（HTTPS）
请求地址：https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID
 *
 * @author zhoujun
 * @since 2019-01-07
 */
@TableName("t_user_wx_work")
public class UserWxWork extends Model<UserWxWork> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("open_id")
    private String openId;
    
    @TableField("user_id")
    private String userId;//数窗用户id
    
    @TableField("app_id")
    private String appId;//第三方集成应用id
    
    @TableField("agent_id")
    private Integer agentId;
    
    /**
     * 成员UserID
     */
    @TableField("wx_userid")
    private String wxUserid;

    /**
     * 成员名称
     */
    private String name;

    /**
     * 通讯录手机号码
     */
    private String mobile;

    /**
     * 成员所属部门id列表，仅返回该应用有查看权限的部门id
     */
    private String department;

    /**
     * 部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)
     */
    private String sort;

    /**
     * 职务信息；第三方仅通讯录应用可获取
     */
    private String position;

    /**
     * 性别。0表示未定义，1表示男性，2表示女性
     */
    private String gender;

    /**
     * 邮箱，第三方仅通讯录应用可获取
     */
    private String email;

    /**
     * 表示在所在的部门内是否为上级。；第三方仅通讯录应用可获取
     */
    @TableField("is_leader_in_dept")
    private String isLeaderInDept;

    /**
     * 头像url。注：如果要获取小图将url最后的”/0”改成”/100”即可。第三方仅通讯录应用可获取
     */
    private String avatar;

    /**
     * 座机。第三方仅通讯录应用可获取
     */
    private String telephone;

    /**
     * 成员启用状态。1表示启用的成员，0表示被禁用。注意，服务商调用接口不会返回此字段
     */
    @TableField("is_enable")
    private String isEnable;

    /**
     * 别名；第三方仅通讯录应用可获取
     */
    private String alias;

    private String extattr;

    /**
     * 激活状态: 1=已激活，2=已禁用，4=未激活。
     */
    private String status;

    /**
     * 员工个人二维码，扫描可添加为外部联系人；第三方仅通讯录应用可获取
     */
    @TableField("qr_code")
    private String qrCode;

    /**
     * 成员对外属性，字段详情见对外属性；第三方仅通讯录应用可获取
     */
    @TableField("external_profile")
    private String externalProfile;

    @TableField("external_position")
    private String externalPosition;

    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @TableField("tenant_id")
    private String tenantId;
    
	@TableField(exist=false)
    private String appType;
	@TableField(exist=false)
    private String appName;
	@TableField(exist=false)
    private String logoUrl;
	@TableField(exist=false)
	private String username;//用户名
	@TableField(exist=false)
	private String realname;//真实名称
	@TableField(exist=false)
	private Integer pageSize;
	@TableField(exist=false)
	private Integer pageNumber;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public final String getAppId() {
		return appId;
	}

	public final void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
    public String getWxUserid() {
        return wxUserid;
    }

    public void setWxUserid(String wxUserid) {
        this.wxUserid = wxUserid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getIsLeaderInDept() {
        return isLeaderInDept;
    }

    public void setIsLeaderInDept(String isLeaderInDept) {
        this.isLeaderInDept = isLeaderInDept;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getExtattr() {
        return extattr;
    }

    public void setExtattr(String extattr) {
        this.extattr = extattr;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
    public String getExternalProfile() {
        return externalProfile;
    }

    public void setExternalProfile(String externalProfile) {
        this.externalProfile = externalProfile;
    }
    public String getExternalPosition() {
        return externalPosition;
    }

    public void setExternalPosition(String externalPosition) {
        this.externalPosition = externalPosition;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public final String getTenantId() {
		return tenantId;
	}

	public final void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public final String getAppType() {
		return appType;
	}

	public final void setAppType(String appType) {
		this.appType = appType;
	}

	public final String getAppName() {
		return appName;
	}

	public final void setAppName(String appName) {
		this.appName = appName;
	}

	public final String getLogoUrl() {
		return logoUrl;
	}

	public final void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public final String getUsername() {
		return username;
	}

	public final void setUsername(String username) {
		this.username = username;
	}

	public final String getRealname() {
		return realname;
	}

	public final void setRealname(String realname) {
		this.realname = realname;
	}

	public final Integer getPageSize() {
		return pageSize;
	}

	public final void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public final Integer getPageNumber() {
		return pageNumber;
	}

	public final void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

	@Override
	public String toString() {
		return "UserWxWork {id=" + id + ", openId=" + openId + ", userId=" + userId + ", appId=" + appId + ", agentId="
				+ agentId + ", wxUserid=" + wxUserid + ", name=" + name + ", mobile=" + mobile + ", department="
				+ department + ", sort=" + sort + ", position=" + position + ", gender=" + gender + ", email=" + email
				+ ", isLeaderInDept=" + isLeaderInDept + ", avatar=" + avatar + ", telephone=" + telephone
				+ ", isEnable=" + isEnable + ", alias=" + alias + ", extattr=" + extattr + ", status=" + status
				+ ", qrCode=" + qrCode + ", externalProfile=" + externalProfile + ", externalPosition="
				+ externalPosition + ", createTime=" + createTime + ", tenantId=" + tenantId + ", appType=" + appType
				+ ", appName=" + appName + ", logoUrl=" + logoUrl + ", username=" + username + ", realname=" + realname
				+ "}";
	}

    
}
