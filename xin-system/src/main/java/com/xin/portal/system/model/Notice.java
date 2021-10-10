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
 * @since 2018-03-23
 */
@TableName("t_notice")
public class Notice extends Model<Notice> {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    /**
     * 标题
     */
    private String title;
    @TableId("title_color")
    private String titleColor;
    /**
     * 内容
     */
    private String content;
    /**
     * 发布人
     */
    private String publisher;
    /**
     * 发布人真实姓名
     */
    @TableField(value="publisher_name",exist = false)
    private String publisherName;
    /**
     * 发布时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("publish_time")
    private Date publishTime;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 起草人
     */
    private String creater;
    /**
     * 起草人真实姓名
     */
    @TableField(value="creater_name",exist = false)
    private String createrName;
    /**
     * 优先级：1普通,2重要,3紧急,4,5
     */
    @TableField("notice_level")
    private String noticeLevel;
    @TableField(exist = false)
    private String noticeLevelName;
    /**
     * 状态：0失效1有效
     */
    private Integer state;
    /**
     * 接收单位
     */
    @TableField("org_code")
    private String orgCode;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("valid_start_time")
    private Date validStartTime;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField("valid_end_time")
    private Date validEndTime;
    private Integer sort;
    @TableField("read_num")
    private Integer readNum;
    private String updater;
    @TableField(value="updater_name",exist = false)
    private String updaterName;
    @TableField("update_time")
    private Date updateTime;
    private Integer type;
    @TableField(exist = false)
    private String linkUrl;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @TableField(exist = false)
    private Date nowDate;
    @TableField("is_for_ever")
    private Integer isForEver;
    
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;

	@TableField(exist = false)
	private Integer topNum;


	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/*
        * 用户阅读公告标识：
        * 0.该用户未读公告
        * 1.该用户已读公告
        * */
	@TableField(exist = false)
	private String readState;

	@TableField(exist = false)
	private String avatar;


	public String getReadState() {
		return readState;
	}

	public void setReadState(String readState) {
		this.readState = readState;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitleColor() {
		return titleColor;
	}

	public void setTitleColor(String titleColor) {
		this.titleColor = titleColor;
	}

	public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    
    public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public String getNoticeLevel() {
		return noticeLevel;
	}

	public void setNoticeLevel(String noticeLevel) {
		this.noticeLevel = noticeLevel;
	}
	
	public String getNoticeLevelName() {
		return noticeLevelName;
	}

	public void setNoticeLevelName(String noticeLevelName) {
		this.noticeLevelName = noticeLevelName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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
	
    public Date getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Date validStartTime) {
		this.validStartTime = validStartTime;
	}

	public Date getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getReadNum() {
		return readNum;
	}

	public void setReadNum(Integer readNum) {
		this.readNum = readNum;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public Date getNowDate() {
		return nowDate;
	}
	public void setNowDate(Date nowDate) {
		this.nowDate = nowDate;
	}
	public Integer getIsForEver() {
		return isForEver;
	}
	public void setIsForEver(Integer isForEver) {
		this.isForEver = isForEver;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getTopNum() {
		return topNum;
	}

	public void setTopNum(Integer topNum) {
		this.topNum = topNum;
	}

	@Override
    public String toString() {
        return "Notice{" +
        "id=" + id +
        ", title=" + title +
        ", content=" + content +
        ", publisher=" + publisher +
        ", publishTime=" + publishTime +
        ", createTime=" + createTime +
        ", creater=" + creater +
        ", noticeLevel=" + noticeLevel +
        ", state=" + state +
        ", orgCode=" + orgCode +
        "}";
    }
}
