package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-09
 */
@TableName("t_prompt_rel")
public class PromptRel extends Model<PromptRel> {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("resource_id")
    private String resourceId;
    @TableField("prompt_id")
    private String promptId;
    @TableField("default_value1")
    private String defaultValue1;
    @TableField("default_value2")
    private String defaultValue2;
    @TableField("default_value3")
    private String defaultValue3;
    @TableField("default_value4")
    private String defaultValue4;
    @TableField("date_format")
    private String dateFormat;
    private Integer sort;
    private Integer hidden;
    private Integer required;
    @TableField("tenant_id")
    private String tenantId;
    
    @TableField(exist=false)
    private String name;
    @TableField(exist=false)
    private String code;
    @TableField(exist=false)
    private Integer type;
    @TableField(exist=false)
    private String promptType;
    @TableField(exist=false)
    private String objectType;
    @TableField(exist=false)
    private String dictCode;
    @TableField(exist=false)
    private String special;
    @TableField(exist=false)
    List items;

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

    public String getPromptId() {
        return promptId;
    }

    public void setPromptId(String promptId) {
        this.promptId = promptId;
    }

    public String getDefaultValue1() {
        return defaultValue1;
    }

    public void setDefaultValue1(String defaultValue1) {
        this.defaultValue1 = defaultValue1;
    }

    public String getDefaultValue2() {
        return defaultValue2;
    }

    public void setDefaultValue2(String defaultValue2) {
        this.defaultValue2 = defaultValue2;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
    public Integer getHidden() {
		return hidden;
	}

	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}

	public Integer getRequired() {
		return required;
	}

	public void setRequired(Integer required) {
		this.required = required;
	}
	
	public final String getTenantId() {
		return tenantId;
	}

	public final void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

    public String getPromptType() {
        return promptType;
    }

    public void setPromptType(String promptType) {
        this.promptType = promptType;
    }
    
    public final String getObjectType() {
		return objectType;
	}

	public final void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}
	
	public final String getDefaultValue3() {
		return defaultValue3;
	}

	public final void setDefaultValue3(String defaultValue3) {
		this.defaultValue3 = defaultValue3;
	}

	public final String getDefaultValue4() {
		return defaultValue4;
	}

	public final void setDefaultValue4(String defaultValue4) {
		this.defaultValue4 = defaultValue4;
	}

	public final String getDateFormat() {
		return dateFormat;
	}

	public final void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}
	
	@Override
	public String toString() {
		return "PromptRel{id=" + id + ", resourceId=" + resourceId + ", promptId=" + promptId + ", defaultValue1="
				+ defaultValue1 + ", defaultValue2=" + defaultValue2 + ", defaultValue3=" + defaultValue3
				+ ", defaultValue4=" + defaultValue4 + ", dateFormat=" + dateFormat + ", sort=" + sort + ", hidden="
				+ hidden + ", required=" + required + ", name=" + name + ", code=" + code + ", type=" + type
				+ ", promptType=" + promptType + ", dictCode=" + dictCode + ", special=" + special
				+ "}";
	}

	
}
