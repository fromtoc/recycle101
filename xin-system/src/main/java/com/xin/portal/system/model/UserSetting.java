package com.xin.portal.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 *
 * @author zhoujun
 * @since 2019-07-11
 */
@TableName("t_user_setting")
public class UserSetting extends Model<UserSetting> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private String userId;

    /**
     * 导航风格
     */
    @TableField("naviga_style")
    private String navigaStyle;

    /**
     * 主题风格
     */
    @TableField("theme_style")
    private String themeStyle;

    /**
     * 是否发送系统消息
     */
    @TableField("system_msg")
    private Integer systemMsg;

    /**
     * 是否发送评论消息
     */
    @TableField("comment_msg")
    private Integer commentMsg;

    /**
     * 是否发送通知消息
     */
    @TableField("notice_msg")
    private Integer noticeMsg;

    /**
     * 是否发送问题回复消息
     */
    @TableField("issue_msg")
    private Integer issueMsg;

    /**
     * 租户id
     */
    @TableField("tenant_id")
    private String tenantId;

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
    public String getNavigaStyle() {
        return navigaStyle;
    }

    public void setNavigaStyle(String navigaStyle) {
        this.navigaStyle = navigaStyle;
    }
    public String getThemeStyle() {
        return themeStyle;
    }

    public void setThemeStyle(String themeStyle) {
        this.themeStyle = themeStyle;
    }
    public Integer getSystemMsg() {
        return systemMsg;
    }

    public void setSystemMsg(Integer systemMsg) {
        this.systemMsg = systemMsg;
    }
    public Integer getCommentMsg() {
        return commentMsg;
    }

    public void setCommentMsg(Integer commentMsg) {
        this.commentMsg = commentMsg;
    }
    public Integer getNoticeMsg() {
        return noticeMsg;
    }

    public void setNoticeMsg(Integer noticeMsg) {
        this.noticeMsg = noticeMsg;
    }
    public Integer getIssueMsg() {
        return issueMsg;
    }

    public void setIssueMsg(Integer issueMsg) {
        this.issueMsg = issueMsg;
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
        return "UserSetting{" +
        "id=" + id +
        ", userId=" + userId +
        ", navigaStyle=" + navigaStyle +
        ", themeStyle=" + themeStyle +
        ", systemMsg=" + systemMsg +
        ", commentMsg=" + commentMsg +
        ", noticeMsg=" + noticeMsg +
        ", issueMsg=" + issueMsg +
        ", tenantId=" + tenantId +
        "}";
    }
}
