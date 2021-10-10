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
 * @since 2018-03-12
 */
@TableName("t_user_record")
public class UserRecord extends Model<UserRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    /**
     * 类型：1收藏2历史纪录
     */
    private Integer type;
    /**
     * 所属人
     */
    private String owner;
    /**
     * 菜单id
     */
    @TableField("resource_id")
    private String resourceId;
    /**
     * 名称
     */
    private String name;
    /**
     * 地址
     */
    private String url;
    /**
     * 参数
     */
    private String params;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;
	
	/**
	 * 菜单表信息
	 */
	@TableField(exist = false)
    private String linkUrl;
	@TableField(exist = false)
    private String code;
	@TableField(exist = false)
    private Integer state;
	@TableField(exist = false)
    private Integer parentId;
	@TableField(exist = false)
    private String iconName;
	@TableField(exist = false)
    private Integer sort;
	@TableField(exist = false)
    private Integer resourceType;
	@TableField(exist = false)
    private Integer linkType;
	@TableField(exist = false)
    private String title;
	@TableField(exist = false)
    private String icon;
	@TableField(exist = false)
    private String href;
	@TableField(exist = false)
    private String reportId;
	@TableField(exist = false)
    private String hiddenSections;
	@TableField(exist = false)
    private String projectId;
	@TableField(exist = false)
    private String typeValue;


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
	
	
    public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
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

	public Integer getResourceType() {
		return resourceType;
	}

	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}

	public Integer getLinkType() {
		return linkType;
	}

	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getHiddenSections() {
		return hiddenSections;
	}

	public void setHiddenSections(String hiddenSections) {
		this.hiddenSections = hiddenSections;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
    

    @Override
    public String toString() {
        return "UserRecord{" +
        "id=" + id +
        ", type=" + type +
        ", owner=" + owner +
        ", resourceId=" + resourceId +
        ", name=" + name +
        ", url=" + url +
        ", params=" + params +
        ", createTime=" + createTime +
        "}";
    }
}
