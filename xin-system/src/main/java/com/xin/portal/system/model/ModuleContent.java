package com.xin.portal.system.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * 
 *
 * @author xue
 * @since 2018-11-08
 */
@TableName("t_module_content")
public class ModuleContent extends Model<ModuleContent> {

    private static final long serialVersionUID = 1L;

    @TableField("module_id")
    private String moduleId;

    @TableField("content_id")
    private String contentId;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }
    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "ModuleContent{" +
        "moduleId=" + moduleId +
        ", contentId=" + contentId +
        "}";
    }
}
