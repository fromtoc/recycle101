package com.xin.portal.system.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun123
 * @since 2018-04-19
 */
@TableName("t_data_import")
public class DataImport extends Model<DataImport> {

    private static final long serialVersionUID = 1L;

    @TableField("file_id")
    private Integer fileId;
    @TableField("table_name")
    private String tableName;


    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    @Override
    protected Serializable pkVal() {
        return this.fileId;
    }

    @Override
    public String toString() {
        return "DataImport{" +
        "fileId=" + fileId +
        ", tableName=" + tableName +
        "}";
    }
}
