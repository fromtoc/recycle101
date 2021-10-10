package com.xin.portal.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

@TableName("t_notice_already_read")
public class NoticeAlreadyRead extends Model<NoticeAlreadyRead>{

    private static final long serialVersionUID = 1L;

	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("notice_id")
    private String noticeId;
    @TableField("user_id")
    private String userId;
    @TableField("tenant_id")
    private String tenantId;


    public NoticeAlreadyRead() {
    }

    public NoticeAlreadyRead(String id, String noticeId, String userId, String tenantId) {
        this.id = id;
        this.noticeId = noticeId;
        this.userId = userId;
        this.tenantId = tenantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
    
}