package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xin.portal.system.bean.BasePage;

/**
 * 
 * @ClassPath: com.xin.portal.system.model.Dict 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月8日 上午10:58:14
 */
@TableName("t_dict")
public class Dict extends Model<Dict>{
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("dict_name")
    private String dictName;
    @TableField("dict_code")
    private String dictCode;
    @TableField("item_name")
    private String itemName;
    @TableField("item_value")
    private String itemValue;
    @TableField("item_order")
    private Integer itemOrder;
    @TableField(exist=false)
    private String items;
    @TableField("tenant_id")
    private String tenantId;
    @TableField("is_system")
    private Integer issystem;
    @TableField("is_source")
    private Integer isSource;
    @TableField("query_sql")
    private String querySql;
    @TableField("source_id")
    private String sourceId;
    @TableField("parent_dict_code")
    private String parentDictCode;
    

    public Integer getIssystem() {
        return issystem;
    }

    public void setIssystem(Integer issystem) {
        this.issystem = issystem;
    }

    public Dict() {}
    
    public Dict(String dictCode) {
    	this.dictCode = dictCode;
    }

    public Dict(String dictName,String dictCode,String itemName,String itemValue,Integer itemOrder,String tenantId,Integer issystem, Integer isSource) {
        this.dictName = dictName;
        this.dictCode = dictCode;
        this.itemName = itemName;
        this.itemValue = itemValue;
        this.itemOrder = itemOrder;
        this.tenantId = tenantId;
        this.issystem = issystem;
        this.isSource = isSource;
    }

    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public Integer getItemOrder() {
        return itemOrder;
    }

    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}
	
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public final Integer getIsSource() {
		return isSource;
	}

	public final void setIsSource(Integer isSource) {
		this.isSource = isSource;
	}

	public final String getQuerySql() {
		return querySql;
	}

	public final void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	public final String getSourceId() {
		return sourceId;
	}

	public final void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getParentDictCode() {
		return parentDictCode;
	}

	public void setParentDictCode(String parentDictCode) {
		this.parentDictCode = parentDictCode;
	}
    
}