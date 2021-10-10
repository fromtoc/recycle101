package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * 
 * @author SongYi
 * @since 2018-12-11
 */
@TableName("t_service_state")
public class ServiceState extends Model<ServiceState> {

	//id 主键
	@TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
	//服务名称
	private String name;
	//ip地址
	private String ip;
	//端口
	private String port;
	//邮箱
	private String email;
	//状态
	private Integer state;
	//备注
	private String remarks;
	//刷新时间(单位:分)
	@TableField(value="refresh_time")
	private Integer refreshTime;
	//报告记录方式
	@TableField(value="record_type")
	private Integer recordType;
	//记录保留时间(单位:天)
	@TableField(value="retention_time")
	private Integer retentionTime;
	//记录时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@TableField(value="create_time", exist = false)
	private Date createTime;
	//发送邮件频率
	@TableField(value="mail_frequency")
	private Integer mailFrequency;
	//发送邮件次数
	@TableField(value="mail_count")
	private Integer mailCount;
	//异常时是否发送过邮件：0.未发送  1.已发送
	@TableField(value="is_sent_mail")
	private Integer isSentMail;
	@TableField(value="tenant_id")
	private Integer tenantId;
	//服务状态
	@TableField(value="service_state", exist = false)
	private Integer serviceState;
	//服务id
	@TableField(value="service_id", exist = false)
	private String serviceId;
	
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
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	public Integer getRefreshTime() {
		return refreshTime;
	}
	public void setRefreshTime(Integer refreshTime) {
		this.refreshTime = refreshTime;
	}
	public Integer getRecordType() {
		return recordType;
	}
	public void setRecordType(Integer recordType) {
		this.recordType = recordType;
	}
	public Integer getRetentionTime() {
		return retentionTime;
	}
	public void setRetentionTime(Integer retentionTime) {
		this.retentionTime = retentionTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getServiceState() {
		return serviceState;
	}
	public void setServiceState(Integer serviceState) {
		this.serviceState = serviceState;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public Integer getMailFrequency() {
		return mailFrequency;
	}

	public void setMailFrequency(Integer mailFrequency) {
		this.mailFrequency = mailFrequency;
	}

	public Integer getMailCount() {
		return mailCount;
	}

	public void setMailCount(Integer mailCount) {
		this.mailCount = mailCount;
	}

	public Integer getIsSentMail() {
		return isSentMail;
	}

	public void setIsSentMail(Integer isSentMail) {
		this.isSentMail = isSentMail;
	}

	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	protected Serializable pkVal() {
		return null;
	}

}
