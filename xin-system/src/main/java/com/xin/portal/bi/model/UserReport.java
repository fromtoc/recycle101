package com.xin.portal.bi.model;

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
 * @since 2018-06-05
 */
@TableName("t_user_report")
public class UserReport extends Model<UserReport> {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("user_id")
    private String userId;
    @TableField("report_id")
    private String reportId;
    @TableField("report_type")
    private Integer reportType;
    @TableField("project_id")
    private String projectId;
    private String params;
    private String remark;
    private String name;
    @TableField("create_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @TableField("read_num")
    private Integer readNum;
    @TableField("likes_num")
    private Integer likesNum;
    private Integer score;
    
    @TableField(exist=false)
    private Integer pageSize;
    @TableField(exist=false)
    private Integer pageNumber;
    
    @TableField(exist=false)
    private Integer likedId;
    @TableField(exist=false)
    private Integer creater;//分享人id
    @TableField(exist=false)
    private Integer shareType;//分享类型
    @TableField(exist=false)
    private String createrName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    public Integer getLikesNum() {
		return likesNum;
	}

	public void setLikesNum(Integer likesNum) {
		this.likesNum = likesNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	
	public Integer getReadNum() {
		return readNum;
	}

	public void setReadNum(Integer readNum) {
		this.readNum = readNum;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getLikedId() {
		return likedId;
	}

	public void setLikedId(Integer likedId) {
		this.likedId = likedId;
	}
	
	public Integer getCreater() {
		return creater;
	}

	public void setCreater(Integer creater) {
		this.creater = creater;
	}

	public Integer getShareType() {
		return shareType;
	}

	public void setShareType(Integer shareType) {
		this.shareType = shareType;
	}
	
	public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	@Override
    public String toString() {
        return "UserReport{" +
        "id=" + id +
        ", userId=" + userId +
        ", reportId=" + reportId +
        ", reportType=" + reportType +
        ", projectId=" + projectId +
        ", params=" + params +
        "}";
    }
}
