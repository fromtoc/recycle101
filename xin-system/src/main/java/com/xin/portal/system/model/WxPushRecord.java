package com.xin.portal.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信推送记录
 *
 * @author zhoujun
 * @since 2019-02-27
 */
@TableName("t_wx_push_record")
public class WxPushRecord extends Model<WxPushRecord> {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 推送id
     */
    @TableField("wx_push_id")
    private String wxPushId;

    /**
     * 创建时间
     */
    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 1成功0失败
     */
    @TableField("result_code")
    private Integer resultCode;
    @TableField("result_msg")
    private String resultMsg;
    @TableField("to_user")
    private String toUser;
    @TableField("to_user_error")
    private String toUserError;
    @TableField("tenant_id")
    private String tenantId;

    @TableField(exist = false)
    private Integer agentId;
    @TableField(exist = false)
    private String type;
    @TableField(exist = false)
    private String toType;
    @TableField(exist = false)
    private String toContent;
    @TableField(exist = false)
    private String msgType;
    @TableField(exist = false)
    private String msgContent;
    @TableField(exist = false)
    private String msgTitle;
    @TableField(exist = false)
    private String msgDescript;
    @TableField(exist = false)
    private String msgUrl;
    @TableField(exist = false)
    private String msgPicUrl;
    @TableField(exist = false)
    private String queryReport;
    @TableField(exist = false)
    private String resourceId;


    @TableField(exist = false)
    private String jobId;

    @TableField(exist = false)
    private String creater;

    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date jobTime;

    @TableField(exist = false)
    private String jobCron;

    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTimeFrom;

    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTimeTo;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getWxPushId() {
        return wxPushId;
    }

    public void setWxPushId(String wxPushId) {
        this.wxPushId = wxPushId;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToType() {
        return toType;
    }

    public void setToType(String toType) {
        this.toType = toType;
    }

    public String getToContent() {
        return toContent;
    }

    public void setToContent(String toContent) {
        this.toContent = toContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgDescript() {
        return msgDescript;
    }

    public void setMsgDescript(String msgDescript) {
        this.msgDescript = msgDescript;
    }

    public String getMsgUrl() {
        return msgUrl;
    }

    public void setMsgUrl(String msgUrl) {
        this.msgUrl = msgUrl;
    }

    public String getMsgPicUrl() {
        return msgPicUrl;
    }

    public void setMsgPicUrl(String msgPicUrl) {
        this.msgPicUrl = msgPicUrl;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getJobTime() {
        return jobTime;
    }

    public void setJobTime(Date jobTime) {
        this.jobTime = jobTime;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Date getCreateTimeFrom() {
        return createTimeFrom;
    }

    public void setCreateTimeFrom(Date createTimeFrom) {
        this.createTimeFrom = createTimeFrom;
    }

    public Date getCreateTimeTo() {
        return createTimeTo;
    }

    public void setCreateTimeTo(Date createTimeTo) {
        this.createTimeTo = createTimeTo;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getQueryReport() {
        return queryReport;
    }

    public void setQueryReport(String queryReport) {
        this.queryReport = queryReport;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getToUserError() {
		return toUserError;
	}

	public void setToUserError(String toUserError) {
		this.toUserError = toUserError;
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
        return "WxPushRecord{" +
        "id=" + id +
        ", wxPushId=" + wxPushId +
        ", createTime=" + createTime +
        "}";
    }
}
