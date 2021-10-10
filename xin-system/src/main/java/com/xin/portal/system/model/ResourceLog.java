package com.xin.portal.system.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * 资源操作记录表
 *
 * @author zhoujun
 * @since 2018-11-28
 */
@TableName("t_resource_log")
public class ResourceLog extends Model<ResourceLog> {

    private static final long serialVersionUID = 1L;
    
    public static final int TYPE_VIEW = 1;
    public static final int TYPE_DOWNLOAD = 2;
    public static final int TYPE_COLLECT_ADD = 3;
    public static final int TYPE_COLLECT_DEL = 4;
    public static final int TYPE_COMMENT_ADD = 5;
    public static final int TYPE_COMMENT_DEL = 6;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @TableField("resource_id")
    private String resourceId;

    
    private Integer type;
    
    @TableField(exist=false)
    private String typeName;

    private String creater;

    @TableField("creater_name")
    private String createrName;

    @TableField("create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String ip;
    @TableField(exist=false)
    private String name;
    @TableField(exist=false)
	private String resourceType;
	@TableField(exist=false)
	private String resourceTypeName;
	@TableField(exist=false)
	private String linkUrl;
	@TableField(exist=false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String createTimeStart;
	@TableField(exist=false)
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String createTimeEnd;
	@TableField(exist=false)
	private Integer top;
	@TableField(exist=false)
	private Integer myCount;
	@TableField(exist = false)
	private Integer pageNumber;
	@TableField(exist = false)
	private Integer pageSize;
	@TableField(exist = false)
	private Integer num;
	@TableField("browser")
	private String browser;
    @TableField(exist = false)
	private String organization;//参数

	@TableField(exist = false)
	private Resource resource;
	@TableField(exist = false)
	private String resourceTypeIn;
	@TableField(exist = false)
	private String resourName;
	@TableField(exist = false)
	private Integer state;
	@TableField(exist = false)
	private String rdname;
	@TableField(exist = false)
	private Integer hits;
	@TableField(exist = false)
	private String typename;
	@TableField(exist = false)
	private Integer monthCount;
	@TableField(exist = false)
	private String username;
	@TableField(exist = false)
	private String codename;

	public String getCodename() {
		return codename;
	}

	public void setCodename(String codename) {
		this.codename = codename;
	}
	public Integer getNum() {
		return num;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getMonthCount() {
		return monthCount;
	}

	public void setMonthCount(Integer monthCount) {
		this.monthCount = monthCount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRdname() {
		return rdname;
	}

	public void setRdname(String rdname) {
		this.rdname = rdname;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

	public String getResourName() {
		return resourName;
	}

	public void setResourName(String resourName) {
		this.resourName = resourName;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}


	public void setNum(Integer num) {
		this.num = num;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
        if(type!=null){
        	switch(this.type){
            case TYPE_VIEW:
            	setTypeName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(), LanguageParam.ACTIVE_SEE));
            	break;
            case TYPE_DOWNLOAD:
            	setTypeName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(), LanguageParam.ACTION_DOWNLOAD));
            	break;
            case TYPE_COLLECT_ADD:
            	setTypeName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(), LanguageParam.ADD_COLLECTION));
            	break;
            case TYPE_COLLECT_DEL:
            	setTypeName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(), LanguageParam.CANCEL_COLLECTION));
            	break;
            case TYPE_COMMENT_ADD:
            	setTypeName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(), LanguageParam.COMMENTS));
            	break;
            case TYPE_COMMENT_DEL:
            	setTypeName(LangTransform.getLocaleLang(((Locale)SessionUtil.getSession(ConfigKeys.SYS_LOCALE)).toString(), LanguageParam.DELETE_COMMENT));
            	break;
            }
        }
    }
    
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
    public String getCreaterName() {
        return createrName;
    }

    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
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

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getMyCount() {
		return myCount;
	}

	public void setMyCount(Integer myCount) {
		this.myCount = myCount;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getResourceTypeIn() {
		return resourceTypeIn;
	}

	public void setResourceTypeIn(String resourceTypeIn) {
		this.resourceTypeIn = resourceTypeIn;
	}

	@Override
    public String toString() {
        return "ResourceLog{" +
        "id=" + id +
        ", resourceId=" + resourceId +
        ", type=" + type +
        ", creater=" + creater +
        ", createrName=" + createrName +
        ", createTime=" + createTime +
        ", ip=" + ip +
        "}";
    }
}
