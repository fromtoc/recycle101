package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 集成line后，line用户信息表和portal用户的对应关系  未自动拼接tenant_id，需要自己指定
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-23
 */
@TableName("t_user_line_work")
public class UserLineWork extends Model<UserLineWork> {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("user_id")
    private String userId;//系统userid（指数窗中的t_user中的id）                    
    @TableField("channel_id")
    private String channelId;// 应用id(应用的唯一标识)  
    @TableField("line_userid")
    private String lineUserid;//line成员id
    
    @TableField("line_name")
    private String lineName;//line成员名称
    @TableField("line_phone")
    private String linePhone;//line中获取的用户手机号（可能获取不到）
    @TableField("line_mail")
    private String lineMail;//line中的用户邮箱（可能获取不到
    private String gender;//租户id  性别（0：表示未定义或者为获取到，1：表示男性，2：表示女性）
    private String avatar;//头像
    @TableField("create_time")
    private Date createTime;
    @TableField("tenant_id")
    private String tenantId;
    
	@Override
    protected Serializable pkVal() {
        return this.id;
    }

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public final String getChannelId() {
		return channelId;
	}

	public final void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public final String getLineUserid() {
		return lineUserid;
	}

	public final void setLineUserid(String lineUserid) {
		this.lineUserid = lineUserid;
	}

	public final String getLineName() {
		return lineName;
	}

	public final void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public final String getLinePhone() {
		return linePhone;
	}

	public final void setLinePhone(String linePhone) {
		this.linePhone = linePhone;
	}

	public final String getLineMail() {
		return lineMail;
	}

	public final void setLineMail(String lineMail) {
		this.lineMail = lineMail;
	}

	public final String getGender() {
		return gender;
	}

	public final void setGender(String gender) {
		this.gender = gender;
	}

	public final String getAvatar() {
		return avatar;
	}

	public final void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public final Date getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public final String getTenantId() {
		return tenantId;
	}

	public final void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public UserLineWork(String id, String userId, String channelId, String lineUserid, String lineName,
			String linePhone, String lineMail, String gender, String avatar, Date createTime, String tenantId) {
		super();
		this.id = id;
		this.userId = userId;
		this.channelId = channelId;
		this.lineUserid = lineUserid;
		this.lineName = lineName;
		this.linePhone = linePhone;
		this.lineMail = lineMail;
		this.gender = gender;
		this.avatar = avatar;
		this.createTime = createTime;
		this.tenantId = tenantId;
	}

	public UserLineWork() {
		super();
	}

	@Override
	public String toString() {
		return "UserLineWork [id=" + id + ", userId=" + userId + ", channelId=" + channelId + ", lineUserid="
				+ lineUserid + ", lineName=" + lineName + ", linePhone=" + linePhone + ", lineMail=" + lineMail
				+ ", gender=" + gender + ", avatar=" + avatar + ", createTime=" + createTime + ", tenantId=" + tenantId
				+ "]";
	}
}
