package com.xin.portal.system.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import java.util.Date;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun123
 * @since 2018-06-14
 */
@TableName("t_user_likes")
public class UserLikes extends Model<UserLikes> {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("resource_id")
    private String resourceId;
    @TableField("resource_type")
    private Integer resourceType;
    @TableField("user_id")
    private String userId;
    @TableField("create_time")
    private Date createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserLikes{" +
        "id=" + id +
        ", resourceId=" + resourceId +
        ", resourceType=" + resourceType +
        ", userId=" + userId +
        ", createTime=" + createTime +
        "}";
    }
}
