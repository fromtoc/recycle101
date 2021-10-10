package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xin.portal.system.util.SessionUtil;

@TableName("t_sys_log")
public class SysLog extends Model<SysLog>{
	
	public SysLog() {
    }

	/**
     * 
     * @param creater 操作人姓名
     * @param tableId 操作的数据库表id
     * @param tableName	操作的数据库表名
     * @param type 操作类型 1查 2增3删4改
     * @param content 日志内容
     * @param normal 
     * @param method 
     */
    public SysLog(String tableId, String tableName, Integer type, String content, int normal, String method) {
		super();
		this.creater = SessionUtil.getUserId();
		this.tableId = tableId;
		this.tableName = tableName;
		this.type = type;
		this.content = content;
		this.createTime = new Date();
		this.method=method;
		this.normal=normal;
	}
    
	
	public SysLog(String creater, Date createTime, Integer type, String method, int normal, String ip, String operation,
			String organization, String returnResult, String content) {
		super();
		this.creater = creater;
		this.createTime = createTime;
		this.type = type;
		this.method = method;
		this.normal = normal;
		this.ip = ip;
		this.operation = operation;
		this.organization = organization;
		this.returnResult = returnResult;
		this.content = content;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ID_WORKER_STR)
	private String id;

    private String creater;
    @TableField("create_time")
    private Date createTime;
    @TableField("table_id")
    private String tableId;
    @TableField("table_name")
    private String tableName;

    private Integer type;  

    private String method;  //执行方法
    
    private int normal;   //执行结果
    
    private String ip;   //请求ip
    
    private String realname;    //操作人姓名
    
    private String operation;    //操作
    
    private String organization;    //组织
    @TableField(exist = false)
    private String organizationName;
    @TableField("resource_id")
    private String resourceId;     //资源ID
    @TableField(exist = false)
    private String resourceName;
    @TableField("resource_type")
    private String resourceType;    //资源类型id
    @TableField(exist = false)
    private String resourceTypeName;
    @TableField("return_result")
    private String returnResult;
    private String content;      //内容
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;//查询条件
    @TableField(exist = false)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd ;//查询条件
    
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
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreateTimeStart() {
		return createTimeStart;
	}
	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
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
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public int getNormal() {
		return normal;
	}
	public void setNormal(int normal) {
		this.normal = normal;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getOrganization() {
		return organization;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getResourceTypeName() {
		return resourceTypeName;
	}
	public void setResourceTypeName(String resourceTypeName) {
		this.resourceTypeName = resourceTypeName;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getReturnResult() {
		return returnResult;
	}
	public void setReturnResult(String returnResult) {
		this.returnResult = returnResult;
	}
	
	@Override
	protected Serializable pkVal() {
		return this.id;
	}
	@Override
	public String toString() {
		return "SysLog {id=" + id + ", creater=" + creater + ", createTime=" + createTime + ", tableId=" + tableId
				+ ", tableName=" + tableName + ", type=" + type + ", content=" + content + ", createTimeStart="
				+ createTimeStart + ", createTimeEnd=" + createTimeEnd + "}";
	}
	
}
