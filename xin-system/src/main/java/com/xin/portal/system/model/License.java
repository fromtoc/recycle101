package com.xin.portal.system.model;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotations.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhoujun123
 * @since 2018-05-08
 */
@TableName("t_license")
public class License extends Model<License> {

    private static final long serialVersionUID = 1L;
    
    public static Integer STATE_APPLY=1;
    public static Integer STATE_YES=2;
    public static Integer STATE_NO=3;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
    /**
     * 申请时间
     */
    @TableField("apply_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    /**
     * 申请来源：1线上2文件
     */
    @TableField("apply_type")
    private String applyType;
    /**
     * 授权起始日期
     */
    @TableField("start_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 激活日期
     */
    @TableField("activate_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date activateTime;
    
    
    /**
     * 授权终止日期
     */
    @TableField("end_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 金额
     */
    private Integer amount;
    @TableField("ext_info")
    private String extInfo;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 审批人
     */
    private String approver;
    
    private String path;
    /**
     * 审核时间
     */
    @TableField("approve_time")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date approveTime;
    @TableField
    private String company;
    @TableField("customer_type")
    private String customerType;
    @TableField
    private String phone;
    @TableField
    private String email;
    @TableField
    private String address;
    @TableField("mac_address")
    private String macAddress;
    @TableField
    private String reason;
    @TableField("time_enabled")
    private Boolean timeEnabled;
    @TableField("amount_enabled")
    private Boolean amountEnabled;
    @TableField("mac_enabled")
    private Boolean macEnabled;
    
    /**
     * dataWdindow 版本
     */
    private String version;
    /**
     * 
     */
    private String jvm;
    /**
     * 服务器操作系统名称
     */
    @TableField("os_name")
    private String osName;
    /**
     * 操作系统版本
     */
    @TableField("os_version")
    private String osVersion;
    /**
     * 服务器操作系统位数
     */
    @TableField("os_arch")
    private String osArch;
    /**
     * 内存信息
     */
    @TableField("memory")
    private String memory;
    /**
     * 数据库信息
     */
    @TableField("db_info")
    private String dbInfo;
    /*
     * 服务器名称
     */
    @TableField("computer_name")
    private String computerName;
    
    /**
     * 最大用户数
     */
    @TableField("max_user_count")
    private Integer maxUserCount;
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

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    
    public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getPhone() {
		return phone;
	}

	public Integer getMaxUserCount() {
		return maxUserCount;
	}

	public void setMaxUserCount(Integer maxUserCount) {
		this.maxUserCount = maxUserCount;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
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
    
    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Boolean getTimeEnabled() {
		return timeEnabled;
	}

	public void setTimeEnabled(Boolean timeEnabled) {
		this.timeEnabled = timeEnabled;
	}

	public Boolean getAmountEnabled() {
		return amountEnabled;
	}

	public void setAmountEnabled(Boolean amountEnabled) {
		this.amountEnabled = amountEnabled;
	}

	public Boolean getMacEnabled() {
		return macEnabled;
	}

	public void setMacEnabled(Boolean macEnabled) {
		this.macEnabled = macEnabled;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getActivateTime() {
		return activateTime;
	}

	public void setActivateTime(Date activateTime) {
		this.activateTime = activateTime;
	}
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getJvm() {
		return jvm;
	}

	public void setJvm(String jvm) {
		this.jvm = jvm;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public String getMemory() {
		return memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	public String getDbInfo() {
		return dbInfo;
	}

	public void setDbInfo(String dbInfo) {
		this.dbInfo = dbInfo;
	}

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	@Override
    public String toString() {
        return "License{" +
        "id=" + id +
        ", applyTime=" + applyTime +
        ", applyType=" + applyType +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", type=" + type +
        ", amount=" + amount +
        ", extInfo=" + extInfo +
        ", state=" + state +
        ", maxUserCount=" + maxUserCount +
        ", creater=" + creater +
        ", approver=" + approver +
        ", approveTime=" + approveTime +
        "}";
    }
}
