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
 * 模块表
 *
 * @author zhoujun
 * @since 2018-10-31
 */
@TableName("t_menu")
public class Menu extends Model<Menu> {

    private static final long serialVersionUID = 1L;
    
    public static final String SHOWTYPE_LIST = "list";
    public static final String SHOWTYPE_CARD = "card";
    
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 模块名
     */
    private String name;

    @TableField("link_url")
    private String linkUrl;

    /**
     * 模块编号
     */
    private String code;

    /**
     * 0启用  1禁用
     */
    private Integer status;

    /**
     * 父级id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 创建时间
     */
    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField("update_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 图标名
     */
    @TableField("icon_name")
    private String iconName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 1:菜单;2:报表
     */
    private String type;

    /**
     * 1:页面内跳转;2:系统内;3外部
     */
    @TableField("link_type")
    private Integer linkType;

    private Integer state;

    private Integer lv;

    private String introduce;

    private String creater;

    @TableField("resource_id")
    private String resourceId;

    @TableField(exist=false)
    private String thumbnail;
    
    @TableField("tenant_id")
    private String tenantId;
    @TableField(exist=false)
    private String resourceName;
    
    @TableField(exist=false)
    private List<Menu> children;
    
    @TableField(exist=false)
    private Integer xNum;
    
    @TableField(exist=false)
    private Integer yNum;

    @TableField(exist=false)
    private String collect;
    
    @TableField(exist=false)
    private String collectNum;

    @TableField(exist=false)
    private String shortTitle;

    @TableField(exist=false)
    private Integer colorNum;
    
    @TableField(exist=false)
    private Integer viewNum;
    
    @TableField("is_mobile")
    private Integer isMobile;
    
    @TableField("show_style")
    private String showStyle;
    
    @TableField(exist=false)
    private String fileId;

    public final Integer getViewNum() {
		return viewNum;
	}

	public final void setViewNum(Integer viewNum) {
		this.viewNum = viewNum;
	}

	public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public Integer getColorNum() {
        return colorNum;
    }

    public void setColorNum(Integer colorNum) {
        this.colorNum = colorNum;
    }

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
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    public final String getFileId() {
		return fileId;
	}

	public final void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public String getResourceName() {
		return resourceName;
	}

	public Integer getxNum() {
		return xNum;
	}

	public void setxNum(Integer xNum) {
		this.xNum = xNum;
	}

	public Integer getyNum() {
		return yNum;
	}

	public void setyNum(Integer yNum) {
		this.yNum = yNum;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}


	public String getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(String collectNum) {
		this.collectNum = collectNum;
	}

	public Integer getLv() {
		return lv;
	}

	public void setLv(Integer lv) {
		this.lv = lv;
	}

	public String getCollect() {
		return collect;
	}

	public void setCollect(String collect) {
		this.collect = collect;
	}

	public final Integer getIsMobile() {
		return isMobile;
	}

	public final void setIsMobile(Integer isMobile) {
		this.isMobile = isMobile;
	}
	
	public final String getShowStyle() {
		return showStyle;
	}

	public final void setShowStyle(String showStyle) {
		this.showStyle = showStyle;
	}

	@Override
    public String toString() {
        return "Menu{" +
        "id=" + id +
        ", name=" + name +
        ", linkUrl=" + linkUrl +
        ", code=" + code +
        ", colorNum=" + colorNum +
        ", shortTitle=" + shortTitle +
        ", status=" + status +
        ", parentId=" + parentId +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", iconName=" + iconName +
        ", sort=" + sort +
        ", type=" + type +
        ", linkType=" + linkType +
        ", state=" + state +
        ", lv=" + lv +
        ", introduce=" + introduce +
        ", creater=" + creater +
        ", isMobile=" + isMobile +
        ", showStyle=" + showStyle +
        ", tenantId=" + tenantId +
        "}";
    }
}
