package com.xin.portal.system.model;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

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
 * @since 2018-12-03
 */
@TableName("t_issue")
public class Issue extends Model<Issue> {

    private static final long serialVersionUID = 1L;
    /**
     * id，主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 标题
     */
    private String title;
    /**
     * 优先级  1、高；2、中；3、低
     */
    private Integer lv;
    /**
     * 是否有新回复的标记  0：无回复；1：有新回复
     */
    @TableField("is_reply")
    private String isReply;
    /**
     * 客户端ip
     */
    private String ip;
    /**
     * 提出人（创建人）
     */
    private String creater;
    
    @TableField(exist=false)
    private String createName;
    /**
     * 提出人的组织
     */
    @TableField("org_id")
    private String orgId;
    
    @TableField(exist=false)
    private String orgCode;
    
    @TableField(exist=false)
    private String orgName;
    /**
     * 提出时间（创建时间）
     */
    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 状态（1、新建；2、处理中；3、关闭）
     */
    private Integer state;
    
    @TableField(exist=false)
    private List<Integer> states;
    /**
     * 最后更新人（修改人/回复人）
     */
    private String updater;
    
    @TableField(exist=false)
    private String updateName;
    /**
     * 最后更新时间
     */
    @TableField("update_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 问题描述（问题内容）
     */
    private String content;
    /**
     * 系统授权（被授权的系统名称）
     */
    @TableField("sys_impower")
    private String sysImpower;
    /**
     * 系统版本（被授权的系统版本）
     */
    @TableField("sys_version")
    private String sysVersion;
    /**
     * 浏览器版本信息
     */
    @TableField("browser_version")
    private String browserVersion;
    /**
     * 客户端分辨率
     */
    private String resolution;
    /**
     * 租户id
     */
    @TableField("tenant_id")
    private String tenantId;
    @TableField(exist=false)
    private Integer pageNumber;
    @TableField(exist=false)
    private Integer pageSize;
    @TableField(exist=false)
    private List<Organization> userOrgs;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Integer getLv() {
        return lv;
    }
    public void setLv(Integer lv) {
        this.lv = lv;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getCreater() {
        return creater;
    }
    public void setCreater(String creater) {
        this.creater = creater;
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public List<Integer> getStates() {
		return states;
	}
	public void setStates(List<Integer> states) {
		this.states = states;
	}
	public String getUpdater() {
        return updater;
    }
    public void setUpdater(String updater) {
        this.updater = updater;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getSysImpower() {
        return sysImpower;
    }
    public void setSysImpower(String sysImpower) {
        this.sysImpower = sysImpower;
    }
    public String getSysVersion() {
        return sysVersion;
    }
    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }
    public String getBrowserVersion() {
        return browserVersion;
    }
    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }
    public String getResolution() {
        return resolution;
    }
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
    public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public String getTenantId() {
        return tenantId;
    }
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    public String getIsReply() {
        return isReply;
    }

    public void setIsReply(String isReply) {
        this.isReply = isReply;
    }
    public final List<Organization> getUserOrgs() {
		return userOrgs;
	}
	public final void setUserOrgs(List<Organization> userOrgs) {
		this.userOrgs = userOrgs;
	}
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", title='" + title + '\'' +
                ", lv=" + lv +
                ", isReply='" + isReply + '\'' +
                ", ip='" + ip + '\'' +
                ", creater='" + creater + '\'' +
                ", createName='" + createName + '\'' +
                ", orgId='" + orgId + '\'' +
                ", orgCode='" + orgCode + '\'' +
                ", orgName='" + orgName + '\'' +
                ", createTime=" + createTime +
                ", state=" + state +
                ", states=" + states +
                ", updater='" + updater + '\'' +
                ", updateName='" + updateName + '\'' +
                ", updateTime=" + updateTime +
                ", content='" + content + '\'' +
                ", sysImpower='" + sysImpower + '\'' +
                ", sysVersion='" + sysVersion + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", resolution='" + resolution + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", pageNumber=" + pageNumber +
                ", pageSize=" + pageSize +
                '}';
    }
}
