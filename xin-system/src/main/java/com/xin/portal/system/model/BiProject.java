package com.xin.portal.system.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("t_bi_project")
public class BiProject extends Model<BiProject>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;
	
	private String title;

    private String project;
    @TableField("bi_server_id")
    private String biServerId;

    @TableField("param_dossier")
    private String paramDossier;
    @TableField("param_doc")
    private String paramDoc;
    @TableField("param_report")
    private String paramReport;
    @TableField("param_folder")
    private String paramFolder;
    @TableField("bo_authentication")
    private String boAuthentication;
    private String remark;
    private String param;
    private String state;
    
    @TableField(exist=false)
    private String biServerName;
    @TableField(exist=false)
    private String defaultUid;
    @TableField(exist=false)
    private String defaultPwd;
    @TableField(exist=false)
    private String server;
    @TableField(exist=false)
    private String url;
    @TableField(exist=false)
    private String port;
    @TableField(exist=false)
    private String ip;
    @TableField(exist=false)
    private Integer type;
    @TableField(exist=false)
    private String paramServer;
    @TableField("is_indepen_pro")
    private Integer isIndependPro;
    @TableField("tenant_id")
    private String tenantId;
    
    
    public BiProject(){}
    
    public BiProject(String server,String project,String port,String defaultUid,String defaultPwd){
    	this.server = server;
    	this.project = project;
    	this.port = port;
    	this.defaultUid = defaultUid;
    	this.defaultPwd = defaultPwd;
    }
    

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }


	public String getParamDoc() {
		return paramDoc;
	}

	public void setParamDoc(String paramDoc) {
		this.paramDoc = paramDoc;
	}

	public String getParamReport() {
		return paramReport;
	}

	public void setParamReport(String paramReport) {
		this.paramReport = paramReport;
	}
	
	public String getParamDossier() {
		return paramDossier;
	}

	public void setParamDossier(String paramDossier) {
		this.paramDossier = paramDossier;
	}

	public String getParamFolder() {
		return paramFolder;
	}

	public void setParamFolder(String paramFolder) {
		this.paramFolder = paramFolder;
	}

	public String getBiServerId() {
		return biServerId;
	}

	public void setBiServerId(String biServerId) {
		this.biServerId = biServerId;
	}

	public String getBoAuthentication() {
		return boAuthentication;
	}

	public void setBoAuthentication(String boAuthentication) {
		this.boAuthentication = boAuthentication;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public String getBiServerName() {
		return biServerName;
	}

	public void setBiServerName(String biServerName) {
		this.biServerName = biServerName;
	}

	public String getDefaultUid() {
		return defaultUid;
	}

	public void setDefaultUid(String defaultUid) {
		this.defaultUid = defaultUid;
	}

	public String getDefaultPwd() {
		return defaultPwd;
	}

	public void setDefaultPwd(String defaultPwd) {
		this.defaultPwd = defaultPwd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getParamServer() {
		return paramServer;
	}

	public void setParamServer(String paramServer) {
		this.paramServer = paramServer;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Integer getIsIndependPro() {
		return isIndependPro;
	}

	public void setIsIndependPro(Integer isIndependPro) {
		this.isIndependPro = isIndependPro;
	}
	
	public final String getTenantId() {
		return tenantId;
	}

	public final void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "BiProject [id=" + id + ", title=" + title + ", project=" + project + ", biServerId=" + biServerId
				+ ", paramDossier=" + paramDossier + ", paramDoc=" + paramDoc + ", paramReport=" + paramReport
				+ ", paramFolder=" + paramFolder + ", boAuthentication=" + boAuthentication + ", remark=" + remark
				+ ", param=" + param + ", state=" + state + ", biServerName=" + biServerName + ", defaultUid="
				+ defaultUid + ", defaultPwd=" + defaultPwd + ", server=" + server + ", url=" + url + ", port=" + port
				+ ", ip=" + ip + ", type=" + type + ", paramServer=" + paramServer + ", isIndependPro=" + isIndependPro
				+ "]";
	}
    
	
	
    
}