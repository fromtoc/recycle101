package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun
 * @since 2018-01-08
 */
@TableName("t_prompt")
public class Prompt extends Model<Prompt> {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    private String name;
    private String code;
    @TableField("dict_code")
    private String dictCode;
    private Integer type;
    private String special;
    @TableField("cascade_code")
    private String cascadeCode;
    private String remark;
    @TableField("prompt_type")
    private Integer promptType;//1.值提示 ；2.元素提示；3.对象提示；4.组织权限
    @TableField("object_type")
    private Integer objectType;//如果提示类型是对象提示，需要指定对象的类型（4-度量；12-实体；如果为空默认为12） 
    @TableField(exist = false)
	private String dictName;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;


	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
	public Integer getPromptType() {
		return promptType;
	}

	public void setPromptType(Integer promptType) {
		this.promptType = promptType;
	}
	
	public final Integer getObjectType() {
		return objectType;
	}

	public final void setObjectType(Integer objectType) {
		this.objectType = objectType;
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
    protected Serializable pkVal() {
        return this.id;
    }
    
    public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getCascadeCode() {
		return cascadeCode;
	}

	public void setCascadeCode(String cascadeCode) {
		this.cascadeCode = cascadeCode;
	}
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "Prompt {id=" + id + ", name=" + name + ", code=" + code + ", dictCode=" + dictCode + ", type=" + type
				+ ", special=" + special + ", remark=" + remark + ", promptType=" + promptType + ", objectType="
				+ objectType + ", dictName=" + dictName + "}";
	}

	
}
