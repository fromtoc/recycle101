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
@TableName("t_mo_content")
public class MoContent extends Model<MoContent> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    private String name;

    private String url;

    private Integer sort;

    @TableField("x_axis")
    private String xAxis;

    @TableField("y_axis")
    private String yAxis;
    
    @TableField("type")
    private String type;
    
    @TableField(exist=false)
    private String moduleId;
    
    @TableField("content")
    private String content;
    
    @TableField("relate")
    private String relate;
    
    @TableField("x_num")
    private Integer xNum;
    
    @TableField("y_num")
    private Integer yNum;
    
    @TableField("hidden_more")
    private Integer hiddenMore;
    
    @TableField("show_type")
    private Integer showType;
    
    @TableField("resource_type")
    private String resourceType;
    
    @TableField("top_num")
    private Integer topNum;
    
    @TableField("date_type")
    private Integer dateType;
    
    @TableField(exist=false)
    private String html;
    
    @TableField("tenant_id")
    private String tenantId;

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

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }
    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRelate() {
		return relate;
	}

	public void setRelate(String relate) {
		this.relate = relate;
	}



	public Integer getxNum() {
		return xNum;
	}

	public void setxNum(Integer xNum) {
		this.xNum = xNum;
	}

	public Integer getyNum() {
		return yNum;
	}

	public void setyNum(Integer yNum) {
		this.yNum = yNum;
	}

	public Integer getHiddenMore() {
		return hiddenMore;
	}

	public void setHiddenMore(Integer hiddenMore) {
		this.hiddenMore = hiddenMore;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}


	public Integer getTopNum() {
		return topNum;
	}

	public void setTopNum(Integer topNum) {
		this.topNum = topNum;
	}

	public Integer getDateType() {
		return dateType;
	}

	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}

	@Override
    public String toString() {
        return "MoContent{" +
        "id=" + id +
        ", name=" + name +
        ", type=" + type +
        ", url=" + url +
        ", sort=" + sort +
        ", xAxis=" + xAxis +
        ", yAxis=" + yAxis +
        ", tenantId=" + tenantId +
        "}";
    }
}
