package com.xin.portal.system.model;

import java.text.SimpleDateFormat;
import java.util.Date;

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
 * @since 2019-06-21
 */
@TableName("t_message_center")
public class MessageCenter extends Model<MessageCenter> {

    private static final long serialVersionUID = 1L;
    
    /*系统提示   只提示
    1.过期提醒  （用户过期提醒）
    2.监控提醒  （系统警告：监控异常）
          交互消息     提示，点击可以跳转到其他页面。
    3.回复消息  
    4.评论消息
          通知消息  
    5.公告消息（系统公告） 仅使用公告方式
    6.通知消息（系统通知；如开通权限通知，关闭权限通知，赋角色通知）--只提示
    */
    /**过期提醒  （用户过期提醒）、监控提醒  （系统警告：监控异常）*/
    public static final Integer SYSTEM_PROMPT_MESSAGE = 1;
    /**回复消息、评论消息*/
    public static final Integer INTERACTIVE_MESSAGE = 2;
    /**公告消息（系统公告）、通知消息（系统通知；如开通权限通知，关闭权限通知，赋角色通知）*/
    public static final Integer NOTICE_MESSAGE = 3;
    /**问题反馈回复消息*/
    public static final Integer PROBLEMFEEDBACK_MESSAGE = 4;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 接收人
     */
    @TableField("receive_user")
    private String receiveUser;
    
    /**
     * 消息产生人； 如果是系统直接赋值：“system”，其他的用户填写用户id
     */
    @TableField("produce_user")
    private String produceUser;
    
    /**
     * 消息创建时间
     */
    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 消息类型
     */
    private Integer type;

    /**
     * 是否已读 0、未读；1、已读
     */
    @TableField("is_read")
    private Integer isRead;

    /**
     * 读取时间
     */
    @TableField("read_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date readTime;

    /**
     * 消息等级
     */
    private Integer levels;

    /**
     * 资源id
     */
    @TableField("resource_id")
    private String resourceId;
    
    @TableField("message_source_id")
    private String messageSourceId;

    /**
     * 问题id
     */
    @TableField("issue_id")
    private String issueId;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private String tenantId;

    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private String realName;
    @TableField(exist = false)
    private String resourceName;
    @TableField(exist = false)
    private String linkType;


    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }
    public String getProduceUser() {
		return produceUser;
	}
	public void setProduceUser(String produceUser) {
		this.produceUser = produceUser;
	}
	public Date getCreateTime() {
        return createTime;
    }
    public String getCreateTimeTran(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(createTime);
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
    public Integer getLevels() {
        return levels;
    }

    public void setLevels(Integer levels) {
        this.levels = levels;
    }
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    
    public String getMessageSourceId() {
		return messageSourceId;
	}

	public void setMessageSourceId(String messageSourceId) {
		this.messageSourceId = messageSourceId;
	}

	public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }


    @Override
    public String toString() {
        return "MessageCenter{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", receiveUser='" + receiveUser + '\'' +
                ", produceUser='" + produceUser + '\'' +
                ", createTime=" + createTime +
                ", type=" + type +
                ", isRead=" + isRead +
                ", readTime=" + readTime +
                ", levels=" + levels +
                ", resourceId='" + resourceId + '\'' +
                ", issueId='" + issueId + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", realName='" + realName + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", linkType='" + linkType + '\'' +
                '}';
    }
}
