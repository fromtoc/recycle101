package com.xin.portal.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@TableName("t_resource_dict")
public class ResourceDict extends Model<ResourceDict>{
	//主键
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
	//资源id
	@TableField("resource_id")
	private String resourceId;
	//字典名
	@TableField("name")
    private String name;
	//字典值
	@TableField("dict_value")
	private String dictValue;
	//解释说明
    @TableField("explain")
    private String explain;
    //来源
	@TableField("source")
	private String source;
	//算法口径
	@TableField("algorithm")
	private String algorithm;
	//修改时间
	@TableField("modify_time")
	private Date modifyTime;
	//修改人
	@TableField("modifier")
	private String modifier;
	//租户Id
	@TableField("tenant_id")
	private String tenantId;



	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

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

	public String getDictValue() {
		return dictValue;
	}

	public void setDictValue(String dictValue) {
		this.dictValue = dictValue;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public Date getModifyTime() {
		return modifyTime;
	}
	public String getModifyTimeTran() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(modifyTime);
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return "ResourceDict{" +
				"id='" + id + '\'' +
				", resourceId='" + resourceId + '\'' +
				", name='" + name + '\'' +
				", dictValue='" + dictValue + '\'' +
				", explain='" + explain + '\'' +
				", source='" + source + '\'' +
				", algorithm='" + algorithm + '\'' +
				", modifyTime=" + modifyTime +
				", modifier='" + modifier + '\'' +
				", tenantId='" + tenantId + '\'' +
				'}';
	}
}