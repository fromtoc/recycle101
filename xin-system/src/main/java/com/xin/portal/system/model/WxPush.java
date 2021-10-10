package com.xin.portal.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * (微信推送表)---变更为推送任务功能表（内部包含微信推送）。
 *
 * @author zhoujun
 * @since 2019-02-11
 */
@TableName("t_wx_push")
public class WxPush extends Model<WxPush> {

    private static final long serialVersionUID = 1L;

    public static final String TYPE_NOW = "push_now";
    public static final String TYPE_JOB_ONCE = "push_job_once";
    public static final String TYPE_JOB = "push_job";
    public static final String TYPE_CUSTOM = "push_custom";

    public static final String TO_TYPE_USER = "to_user";
    public static final String TO_TYPE_ROLE = "to_role";
    public static final String TO_TYPE_ORG = "to_org";
    
    public static final String TASK_TYPE_PUSH = "task_push";//推送任务
    public static final String TASK_TYPE_RUN =  "task_run";//执行脚本
    
    public static final String TASK_USER_COMMON = "common";
    public static final String TASK_USER_MANAGE = "manage";
    
    public static final String PUSH_TYPE_SYSMSG = "sys_message";
    public static final String PUSH_TYPE_EMAIL = "email";
    public static final String PUSH_TYPE_WECHAT = "wechat";
    public static final String PUSH_TYPE_LINE = "line";
    
    public static final String JOB_STATE_RUN = "runJob";//执行中
    public static final String JOB_STATE_STOP = "stopJob";//暂停
    public static final String JOB_STATE_NEW = "newJob";//创建后未开始
    public static final String JOB_STATE_OVER = "overJob";//任务完成状态，没有开始，恢复，暂停按钮。
    
    public static final String PUSH_MESSAGETYPE_TEXT = "text";//文本
    public static final String PUSH_MESSAGETYPE_TEXTCARD = "textcard";//卡片
    public static final String PUSH_MESSAGETYPE_NEWS = "news";//图文
    public static final String PUSH_MESSAGETYPE_REPORT = "report";//报表

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    @TableField("agent_id")
    private Integer agentId;
    /**
     * 当创建第三方应用任务时关联的应用id（t_third_app_param表的id） 
     */
    @TableField("app_id")
    private String appId;
    /**
	 * 推送类型（即使、定时、定时循环）
     */
    private String type;
    /**
	 * 任务类型  （推送任务、执行脚本）
     */
    @TableField("task_type")
    private String taskType;
    /**
     * 推送方式(站内信，邮件，微信)
     */
    @TableField("push_type")
    private String pushType;
    /**
     * 创建类型（表示任务创建的类型 1、普通创建（普通用户或者管理在“我的”中创建的）  2、管理员创建（管理员用户在管理系统中创建的）
     */
    @TableField("create_type")
    private Integer createType;
    @TableField("user_type")
    private String userType;
    @TableField("task_name")
    private String taskName;
    @TableField("task_desc")
    private String taskDesc;
    /**
	 *	推送范围
     */
    @TableField("to_type")
    private String toType;
    @TableField("to_content")
    private String toContent;
    @TableField("msg_type")
    private String msgType;
    @TableField("msg_content")
    private String msgContent;
    @TableField("msg_title")
    private String msgTitle;
    @TableField("msg_descript")
    private String msgDescript;
    @TableField("msg_url")
    private String msgUrl;
    @TableField("msg_pic_url")
    private String msgPicUrl;

    @TableField("resource_id")
    private String resourceId;

    @TableField("job_name")
    private String jobName;
    
    @TableField("job_group")
    private String jobGroup;
    
    @TableField("trigger_name")
    private String triggerName;
    
    @TableField("trigger_group")
    private String triggerGroup;
    
    @TableField("job_state")
    private String jobState;
    
    @TableField("job_last_run_time")
    private Date jobLastRunTime;
    
    @TableField("job_next_run_time")
    private Date jobNextRunTime;
    
    @TableField("job_curr_run_time")
    private Date jobCurrRunTime;

    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String creater;

    @TableField("job_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date jobTime;

    @TableField("job_cron")
    private String jobCron;
    
    @TableField("update_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    private String updater;
    
    @TableField("tenant_id")
    private String tenantId;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public final String getAppId() {
		return appId;
	}

	public final void setAppId(String appId) {
		this.appId = appId;
	}

	public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public void setMsgContetn(String msgContent) {
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

    public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public Integer getCreateType() {
		return createType;
	}

	public void setCreateType(Integer createType) {
		this.createType = createType;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdater() {
		return updater;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	
	public String getJobState() {
		return jobState;
	}

	public void setJobState(String jobState) {
		this.jobState = jobState;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerGroup() {
		return triggerGroup;
	}

	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
	
	public Date getJobLastRunTime() {
		return jobLastRunTime;
	}

	public void setJobLastRunTime(Date jobLastRunTime) {
		this.jobLastRunTime = jobLastRunTime;
	}

	public Date getJobNextRunTime() {
		return jobNextRunTime;
	}

	public void setJobNextRunTime(Date jobNextRunTime) {
		this.jobNextRunTime = jobNextRunTime;
	}
	
	public Date getJobCurrRunTime() {
		return jobCurrRunTime;
	}

	public void setJobCurrRunTime(Date jobCurrRunTime) {
		this.jobCurrRunTime = jobCurrRunTime;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

	@Override
	public String toString() {
		return "WxPush [id=" + id + ", agentId=" + agentId + ", type=" + type + ", taskType=" + taskType + ", pushType="
				+ pushType + ", createType=" + createType + ", userType=" + userType + ", taskName=" + taskName
				+ ", taskDesc=" + taskDesc + ", toType=" + toType + ", toContent=" + toContent + ", msgType=" + msgType
				+ ", msgContent=" + msgContent + ", msgTitle=" + msgTitle + ", msgDescript=" + msgDescript + ", msgUrl="
				+ msgUrl + ", msgPicUrl=" + msgPicUrl + ", resourceId=" + resourceId + ", jobName=" + jobName
				+ ", createTime=" + createTime + ", creater=" + creater + ", jobTime=" + jobTime + ", jobCron="
				+ jobCron + ", updateTime=" + updateTime + ", updater=" + updater + ", tenantId=" + tenantId + "]";
	}

	
}
