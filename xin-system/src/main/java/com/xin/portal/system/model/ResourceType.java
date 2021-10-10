package com.xin.portal.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.List;

/**
 * 资源类型表
 *
 * @author zhoujun
 * @since 2018-10-31
 */
@TableName("t_resource_type")
public class ResourceType extends Model<ResourceType> {
	
	public static final  String FUNCTION = "1";
	public static final  String LINK = "2";
	public static final  String MSTR_DOSSIER = "3";
	public static final  String MSTR_REPORT = "4";
	public static final  String MSTR_DOCUMENT = "5";
	public static final  String TABLEAU = "6";
	public static final  String FINEREPORT = "7";
	public static final  String BO = "8";
	public static final  String DOCUMENT = "9";
    public static final  String SMARTBI = "10";
    public static final  String QLIK = "11";
    public static final  String COGNOS = "12";

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private Integer code;
    /**
     * 资源类型
     */
    private String name;

    /**
     * 上级id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 说明
     */
    private String remark;

    private String tenantId;

    @TableField(exist = false)
    private List<Resource> resourceList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

	@Override
	public String toString() {
		return "ResourceType{id=" + id + ", code=" + code + ", name=" + name + ", parentId=" + parentId + ", sort="
				+ sort + ", remark=" + remark + ", tenantId=" + tenantId + ", resourceList=" + resourceList + "}";
	}
    
}
