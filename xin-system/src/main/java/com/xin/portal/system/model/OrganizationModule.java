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
@TableName("t_organization_module")
public class OrganizationModule extends Model<OrganizationModule> {

    private static final long serialVersionUID = 1L;

    @TableField("organization_id")
    private String organizationId;

    @TableField("module_id")
    private String moduleId;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }
    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

    @Override
    public String toString() {
        return "OrganizationModule{" +
        "organizationId=" + organizationId +
        ", moduleId=" + moduleId +
        "}";
    }
}
