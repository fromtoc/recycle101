package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 
 * @author SongYi
 * @since 2018-12-11
 */
@TableName("t_service_state_record")
public class ServiceStateRecord extends Model<ServiceStateRecord> {

	//服务id
	@TableField(value="service_id")
	private String serviceId;
	//记录时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@TableField(value="create_time")
	private Date createTime;
	@TableField(exist = false)
	private String createTime1;
	//服务状态
	@TableField(value="service_state")
	private Integer serviceState;
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;
	
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

	public String getCreateTime1() {
		return createTime1;
	}

	public void setCreateTime1(String createTime1) {
		this.createTime1 = createTime1;
	}

	@Override
	protected Serializable pkVal() {
		return null;
	}

}
