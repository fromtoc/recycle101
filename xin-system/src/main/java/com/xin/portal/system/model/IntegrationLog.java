package com.xin.portal.system.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 *
 * @author zhoujun
 * @since 2018-11-27
 */
@TableName("t_integration_log")
public class IntegrationLog extends Model<IntegrationLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 类型（数据字典：integration_type）
     */
    private Integer type;

    /**
     * 创建人
     */
    private String creater;
    
    private String createrName;

    /**
     * 创建时间
     */
    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 内容
     */
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    
    public String getCreaterName() {
		return createrName;
	}

	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}

	public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "IntegrationLog{" +
        "id=" + id +
        ", type=" + type +
        ", creater=" + creater +
        ", createTime=" + createTime +
        ", content=" + content +
        "}";
    }
}
