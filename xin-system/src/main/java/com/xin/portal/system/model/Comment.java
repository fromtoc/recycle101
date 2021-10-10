package com.xin.portal.system.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 
 *
 * @author xue
 * @since 2018-12-03
 */
@TableName("t_comment")
public class Comment extends Model<Comment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("user_id")
    private String userId;
    
    @TableField(exist=false)
    private String userName;
    
    @TableField(exist=false)
    private String avatar;
    
    @TableField("resource_id")
    private String resourceId;
    @TableField("score")
    private String score;
    
    private String content;

    @TableField("create_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField("reply_content")
    private String replyContent;
    
    @TableField("reply_userid")
    private String replyId;

    @TableField("reply_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date replyTime;
    
    @TableField("roof")
    private Integer roof;
    
    @TableField(exist=false)
    private String commentName;

    @TableField(exist=false)
    private String replyName;  
  
    @TableField(exist=false)
    private String resourceName; 
         
	@TableField(exist=false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTimeStart;
	@TableField(exist=false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

	@TableField(exist=false)
	private String remindUser;
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

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

	public String getResourceId() {
		return resourceId;
	}


	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}


	public String getScore() {
		return score;
	}


	public void setScore(String score) {
		this.score = score;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTenantId() {
		return tenantId;
	}


	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}


	public String getReplyContent() {
		return replyContent;
	}


	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	
	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	
	
	public Integer getRoof() {
		return roof;
	}

	public void setRoof(Integer roof) {
		this.roof = roof;
	}
	
	public String getCommentName() {
		return commentName;
	}

	public void setCommentName(String commentName) {
		this.commentName = commentName;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}
	
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	public Date getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getRemindUser() {
		return remindUser;
	}

	public void setRemindUser(String remindUser) {
		this.remindUser = remindUser;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", userId=" + userId + ", userName=" + userName + ", avatar=" + avatar
				+ ", resourceId=" + resourceId + ", score=" + score + ", content=" + content + ", createTime="
				+ createTime + ", tenantId=" + tenantId + ", replyContent=" + replyContent + ", replyId=" + replyId
				+ ", replyTime=" + replyTime + ", roof=" + roof + ", commentName=" + commentName + ", replyName="
				+ replyName + ", resourceName=" + resourceName + ", createTimeStart=" + createTimeStart
				+ ", createTimeEnd=" + createTimeEnd + "]";
	}
}
