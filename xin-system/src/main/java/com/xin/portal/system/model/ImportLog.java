package com.xin.portal.system.model;

import java.sql.Timestamp;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun123
 * @since 2018-04-20
 */
@TableName("t_import_log")
public class ImportLog  {


    /**
     * 模板名称
     */
    @TableField("template_name")
    private String templateName;
    /**
     * 数据库表名
     */
    @TableField("table_name")
    private String tableName;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Timestamp createTime;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 数据截止时间
     */
    @TableField("data_time")
    private Date dataTime;
    /**
     * 数据量大小
     */
    @TableField("data_size")
    private Integer dataSize;
    /**
     * 状态
     */
    private Integer state;
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;


    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

    @Override
    public String toString() {
        return "ImportLog{" +
        "templateName=" + templateName +
        ", tableName=" + tableName +
        ", createTime=" + createTime +
        ", creater=" + creater +
        ", dataTime=" + dataTime +
        ", dataSize=" + dataSize +
        ", state=" + state +
        "}";
    }
}
