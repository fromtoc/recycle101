package com.xin.portal.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@TableName("t_resource")
public class Resource extends Model<Resource>{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8500056971733597137L;
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;

    private String name;
    @TableField("link_url")
    private String linkUrl;

    private String code;

    private Integer state;
    @TableField("parent_id")
    private String parentId;
    @TableField("create_time")
    private Date createTime;
    @TableField("update_time")
    private Date updateTime;
    @TableField("icon_name")
    private String iconName;
    @TableField("auto_refreshtime")
    private BigDecimal autoRefreshtime;
    @TableField("show_tools")
    private Integer showTools;

    private Integer sort;

    private String type;
    @TableField("link_type")
    private Integer linkType;
    
    @TableField("tenant_id")
    private String tenantId;
    
    
    @TableField(exist=false)
    private List<Resource> children;
    @TableField(exist=false)
    private String title;
    @TableField(exist=false)
    private String icon;
    @TableField(exist=false)
    private String href;
	@TableField(exist=false)
	private Integer resourceData;
	@TableField(exist=false)
	private Integer resourceActive;
	@TableField(exist=false)
	private String resourceActivity;
	@TableField(exist=false)
	private String username;
	@TableField(exist=false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String createTimeStart;
	@TableField(exist=false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String createTimeEnd;
	@TableField(exist = false)
	private Integer hits;

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getResourceData() {
		return resourceData;
	}

	public void setResourceData(Integer resourceData) {
		this.resourceData = resourceData;
	}

	public Integer getResourceActive() {
		return resourceActive;
	}

	public void setResourceActive(Integer resourceActive) {
		this.resourceActive = resourceActive;
	}

	public String getResourceActivity() {
		return resourceActivity;
	}

	public void setResourceActivity(String resourceActivity) {
		this.resourceActivity = resourceActivity;
	}

	private String introduce;
    
    private Integer lv;
    
    private String creater;
	@TableField("server_id")
	private String serverId;
    
    @TableField("project_id")
    private String projectId;
    @TableField("report_id")
    private String reportId;
    @TableField("type_name")
    private String typeName;
    @TableField("type_value")
    private Integer typeValue;
    @TableField("hidden_sections")
    private String hiddenSections;
    
    
    
    private String thumbnail;//缩略图访问路径
    private String thumbnailId;//缩略图文件id
    @TableField("view_num")
    private String viewNum;
    @TableField("download_num")
    private String downloadNum;
    @TableField(exist=false)
    private String collect;
    @TableField("collect_num")
    private String collectNum;
    @TableField("comment_num")
    private String commentNum;
    private String path;
    @TableField("resource_type1")
    private String resourceType1;
    @TableField("resource_type2")
    private String resourceType2;
    @TableField("file_id")
    private String fileId;
	/**
	 * 所有人可见
	 */
	private String everyone;

    @TableField(exist=false)
    private String roleResourceId;
    @TableField(exist=false)
    private String roleId;

    
    @TableField(exist=false)
    private String listId;
    
    @TableField(exist=false)
    private Integer xNum;
	@TableField(exist=false)
	private Boolean manage;
	
	@TableField("is_mobile")
	private Integer isMobile;

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
        this.title = name;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
        this.href = linkUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLinkType() {
        return linkType;
    }

    public void setLinkType(Integer linkType) {
        this.linkType = linkType;
    }

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public String getTitle() {
		return title;
	}

	public String getIcon() {
		return icon;
	}

	public String getHref() {
		return href;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(Integer typeValue) {
		this.typeValue = typeValue;
	}

	public String getHiddenSections() {
		return hiddenSections;
	}

	public void setHiddenSections(String hiddenSections) {
		this.hiddenSections = hiddenSections;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getThumbnailId() {
		return thumbnailId;
	}

	public void setThumbnailId(String thumbnailId) {
		this.thumbnailId = thumbnailId;
	}

	public String getViewNum() {
		return viewNum;
	}

	public void setViewNum(String viewNum) {
		this.viewNum = viewNum;
	}

	public String getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(String downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}

	public String getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(String commentNum) {
		this.commentNum = commentNum;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getResourceType1() {
		return resourceType1;
	}

	public void setResourceType1(String resourceType1) {
		this.resourceType1 = resourceType1;
	}

	public String getResourceType2() {
		return resourceType2;
	}

	public void setResourceType2(String resourceType2) {
		this.resourceType2 = resourceType2;
	}
	
	public String getRoleResourceId() {
		return roleResourceId;
	}

	public void setRoleResourceId(String roleResourceId) {
		this.roleResourceId = roleResourceId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}


	public Integer getxNum() {
		return xNum;
	}

	public void setxNum(Integer xNum) {
		this.xNum = xNum;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getCollect() {
		return collect;
	}

	public void setCollect(String collect) {
		this.collect = collect;
	}

	public String getEveryone() {
		return everyone;
	}

	public void setEveryone(String everyone) {
		this.everyone = everyone;
	}

	public Boolean getManage() {
		return manage;
	}

	public void setManage(Boolean manage) {
		this.manage = manage;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public final Integer getIsMobile() {
		return isMobile;
	}

	public final void setIsMobile(Integer isMobile) {
		this.isMobile = isMobile;
	}

	public BigDecimal getAutoRefreshtime() {
		return autoRefreshtime;
	}

	public void setAutoRefreshtime(BigDecimal autoRefreshtime) {
		this.autoRefreshtime = autoRefreshtime;
	}

	public Integer getShowTools() {
		return showTools;
	}

	public void setShowTools(Integer showTools) {
		this.showTools = showTools;
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", name=" + name + ", linkUrl=" + linkUrl + ", code=" + code + ", state=" + state
				+ ", parentId=" + parentId + ", createTime=" + createTime + ", updateTime=" + updateTime + ", iconName="
				+ iconName + ", sort=" + sort + ", type=" + type + ", linkType=" + linkType + ", tenantId=" + tenantId
				+ ", children=" + children + ", title=" + title + ", icon=" + icon + ", href=" + href + ", introduce="
				+ introduce + ", lv=" + lv + ", creater=" + creater + ", projectId=" + projectId + ", reportId="
				+ reportId + ", typeName=" + typeName + ", typeValue=" + typeValue + ", hiddenSections="
				+ hiddenSections + ", thumbnail=" + thumbnail + ", thumbnailId=" + thumbnailId + ", viewNum=" + viewNum
				+ ", downloadNum=" + downloadNum + ", collect=" + collect + ", collectNum=" + collectNum
				+ ", commentNum=" + commentNum + ", path=" + path + ", resourceType1=" + resourceType1
				+ ", resourceType2=" + resourceType2 + ", fileId=" + fileId + ", roleResourceId=" + roleResourceId
				+ ", roleId=" + roleId + ", everyone=" + everyone + ", listId=" + listId + ", xNum=" + xNum 
				+ ",isMobile=" + isMobile + ",autoRefreshtime=" + ",autoRefreshtime=" + ",showTools=" + ",showTools=" +"]";
	}
    
}