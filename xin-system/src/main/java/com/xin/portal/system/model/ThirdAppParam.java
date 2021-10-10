package com.xin.portal.system.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 集成第三方app应用表（特指：wechat、dingding、line等）
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-23
 */
@TableName("t_third_app_param")
public class ThirdAppParam extends Model<ThirdAppParam> {
	
	public final static String APP_TYPE_LINE = "line";
	public final static String APP_TYPE_WECHAT_CP = "wechat_cp";
	public final static String APP_TYPE_DINGDING = "dingding";
	
	public final static String RELATION_PARAM_MOBILE = "Mobile";//关联属性-手机号
	public final static String RELATION_PARAM_USERID = "UserID";//关联属性-用户ID
	
	public final static String APP_TYPE = "line";
	

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type=IdType.ID_WORKER_STR)
    private String id;
    @TableField("app_name")
    private String appName;//集成的第三方应用名称                 
    @TableField("app_type")
    private String appType;// 第三方类型（例如：wechat_cp,line，dingding等）   
    @TableField("app_id")
    private String appId;//应用唯一标识id（微信，line，钉钉 等这些应用都有唯一标识用用id）  
    @TableField("app_secret")
    private String appSecret;//应用密钥（line、微信、钉钉 集成中需要用到参数）     
    @TableField("app_token")
    private String appToken;//应用token（line、微信cp 集成中用到）
    @TableField("app_aeskey")
    private String appAeskey;//应用AES编码key （微信cp 集成中使用）
    @TableField("app_corpid")
    private String appCorpid;//应用企业ID （微信cp 、 钉钉 中集成使用）
    @TableField("callback_url")
    private String callbackUrl;
    @TableField("logo_url")
    private String logoUrl;//应用图标
    @TableField("relation_param")
    private String relationParam;//关联属性
    @TableField("is_enable")
    private String isEnable;//是否启用（1,：启用。0：禁用）    
    private String creater;//创建人
    @TableField("create_time")
    private Date createTime;//创建时间
    private String updater;//修改人id
    @TableField("update_time")
    private Date updateTime;//修改时间
    @TableField("tenant_id")
    private String tenantId;//租户id  
    @TableField(exist=false)
    private String createName;
    @TableField(exist=false)
    private String updateName;
    @TableField(exist=false)
    private Integer pageNumber;
    @TableField(exist=false)
    private Integer pageSize;
    
	@Override
    protected Serializable pkVal() {
        return this.id;
    }

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getAppName() {
		return appName;
	}

	public final void setAppName(String appName) {
		this.appName = appName;
	}

	public final String getAppType() {
		return appType;
	}

	public final void setAppType(String appType) {
		this.appType = appType;
	}

	public final String getAppId() {
		return appId;
	}

	public final void setAppId(String appId) {
		this.appId = appId;
	}

	public final String getAppSecret() {
		return appSecret;
	}

	public final void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public final String getAppToken() {
		return appToken;
	}

	public final void setAppToken(String appToken) {
		this.appToken = appToken;
	}

	public final String getAppAeskey() {
		return appAeskey;
	}

	public final void setAppAeskey(String appAeskey) {
		this.appAeskey = appAeskey;
	}

	public final String getAppCorpid() {
		return appCorpid;
	}

	public final void setAppCorpid(String appCorpid) {
		this.appCorpid = appCorpid;
	}

	public final String getIsEnable() {
		return isEnable;
	}

	public final void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	public final String getCallbackUrl() {
		return callbackUrl;
	}

	public final void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public final String getLogoUrl() {
		return logoUrl;
	}

	public final void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public final String getRelationParam() {
		return relationParam;
	}

	public final void setRelationParam(String relationParam) {
		this.relationParam = relationParam;
	}

	public final String getCreater() {
		return creater;
	}

	public final void setCreater(String creater) {
		this.creater = creater;
	}

	public final Date getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public final String getUpdater() {
		return updater;
	}

	public final void setUpdater(String updater) {
		this.updater = updater;
	}

	public final Date getUpdateTime() {
		return updateTime;
	}

	public final void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public final String getTenantId() {
		return tenantId;
	}

	public final void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	
	public final String getCreateName() {
		return createName;
	}

	public final void setCreateName(String createName) {
		this.createName = createName;
	}

	public final String getUpdateName() {
		return updateName;
	}

	public final void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public final Integer getPageNumber() {
		return pageNumber;
	}

	public final void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public final Integer getPageSize() {
		return pageSize;
	}

	public final void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	

	public ThirdAppParam(String id, String appName, String appType, String appId, String appSecret, String appToken,
			String appAeskey, String appCorpid, String logoUrl, String relationParam, String isEnable, String creater, Date createTime, String updater,
			Date updateTime, String tenantId, String createName, String updateName, Integer pageNumber,
			Integer pageSize) {
		super();
		this.id = id;
		this.appName = appName;
		this.appType = appType;
		this.appId = appId;
		this.appSecret = appSecret;
		this.appToken = appToken;
		this.appAeskey = appAeskey;
		this.appCorpid = appCorpid;
		this.logoUrl = logoUrl;
		this.relationParam = relationParam;
		this.isEnable = isEnable;
		this.creater = creater;
		this.createTime = createTime;
		this.updater = updater;
		this.updateTime = updateTime;
		this.tenantId = tenantId;
		this.createName = createName;
		this.updateName = updateName;
	}

	public ThirdAppParam() {
		super();
	}

	@Override
	public String toString() {
		return "ThirdAppParam [id=" + id + ", appName=" + appName + ", appType=" + appType + ", appId=" + appId
				+ ", appSecret=" + appSecret + ", appToken=" + appToken + ", appAeskey=" + appAeskey + ", appCorpid="
				+ appCorpid + ", logoUrl=" + logoUrl + ", relationParam=" + relationParam + ", isEnable=" + isEnable
				+ ", creater=" + creater + ", createTime=" + createTime + ", updater=" + updater + ", updateTime="
				+ updateTime + ", tenantId=" + tenantId + ", createName=" + createName + ", updateName=" + updateName
				+ ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + "]";
	}
}
