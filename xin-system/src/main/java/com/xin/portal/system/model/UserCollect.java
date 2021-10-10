package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_user_collect")
public class UserCollect extends Model<UserCollect> implements Comparable<UserCollect>{
	
	private static final long serialVersionUID = 1L;
	
	@TableId(value="id",type=IdType.ID_WORKER_STR)
	private String id;
	@TableField(value="parent_id")
	private String parentId;
	@TableField(value="user_id")
	private String userId;
	@TableField(value="collect_name")
	private String collectName;
	@TableField(value="collect_type")
	private String collectType;
	@TableField(value="resource_id")
	private String resourceId;
	@TableField(value="col_create_time")
	private Date colCreateTime;
	@TableField(value="col_sort")
	private Integer colSort;
	@TableField(value="tenant_id")
	private String tenantId;
	
    //资源字段
	@TableField(exist = false)
    private String linkUrl ;
	@TableField(exist = false)
    private String thumbnailId;  //缩略图文件id
	@TableField(exist = false)  
	private String thumbnail;  //缩略图文件路径
	@TableField(exist = false)
	private String name; //资源名称
	@TableField(exist = false)
	private String introduce; //介绍
	@TableField(exist = false)
	private Integer collectNum;   //收藏数
	@TableField(exist = false)
	private Integer linkType;  //1:页面内跳转;2:系统内;3外部  
	
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
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCollectName() {
		return collectName;
	}
	public void setCollectName(String collectName) {
		this.collectName = collectName;
	}
	public String getCollectType() {
		return collectType;
	}
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public Date getColCreateTime() {
		return colCreateTime;
	}
	public void setColCreateTime(Date colCreateTime) {
		this.colCreateTime = colCreateTime;
	}
	public Integer getColSort() {
		return colSort;
	}
	public void setColSort(Integer colSort) {
		this.colSort = colSort;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public String getThumbnailId() {
		return thumbnailId;
	}
	public void setThumbnailId(String thumbnailId) {
		this.thumbnailId = thumbnailId;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public Integer getCollectNum() {
		return collectNum;
	}
	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}
	public Integer getLinkType() {
		return linkType;
	}
	public void setLinkType(Integer linkType) {
		this.linkType = linkType;
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
	@Override
	public int compareTo(UserCollect o) {
		int result = 0;
		result = this.colSort - o.colSort;		
		return result;
	}

}
