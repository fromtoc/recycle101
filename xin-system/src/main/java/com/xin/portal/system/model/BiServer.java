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
 * @author zhoujun
 * @since 2018-12-04
 */
@TableName("t_bi_server")
public class BiServer extends Model<BiServer> {

    private static final long serialVersionUID = 1L;
    
    public static final int TYPE_MSTR = 1; 
    public static final int TYPE_BO = 2;
    public static final int TYPE_MYSQL = 3;
    public static final int TYPE_DIY = 4;
    public static final int TYPE_MSTR_LIBRARY = 5;
    public static final int TYPE_TABLEAU = 6;
    public static final int TYPE_FIREREPORT = 7;
    public static final int TYPE_SMARTBI = 8;
    public static final int TYPE_QLIK = 11;
    public static final int TYPE_COGNOS = 12;
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 服务器名称
     */
    private String name;

    /**
     * 类型：1Mstr 2BO 3MySql 4自定义
     */
    private Integer type;

    /**
     * 服务器ip
     */
    private String ip;

    /**
     * 端口
     */
    private String port;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 描述
     */
    private String remark;

    /**
     * 默认用户名
     */
    @TableField("default_uid")
    private String defaultUid;

    /**
     * 默认密码
     */
    @TableField("default_pwd")
    private String defaultPwd;

    /**
     * 访问url
     */
    private String url;

    /**
     * server参数
     */
    private String server;
    private String param;
    private String state;
    /**
    *水印标识：0 关闭     1 开启水印
    */
    @TableField("water_mark")
    private  Integer  waterMark;
    /**
     *导出方式：0 SDK方式     1 URL方式
     */
    @TableField("export_mode")
    private  Integer  exportMode;
    /**
     *水印文本
     */
    @TableField("water_mark_text")
    private  String  watermarkText;

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
    public Integer getType() {
        return type;
    }
    
    /**
     * 类型：1MSTR 2BO 3MySql 4自定义
     */
    public String getTypeName() {
    	return codeToName(this.type);
    }
    
    public static String getTypeName(Integer type) {
    	return codeToName(type);
    }
    
    private static String codeToName(Integer type){
    	if (type==1) {
    		return "MSTR";
    	}else if (type==2) {
    		return "BO";
    	}else if (type==3) {
    		return "MySql";
    	}else if (type==4) {
            return "自定义";
        }else if (type==5) {
            return "Library";
        }else if (type==6) {
            return "Tableau";
        }else if (type==7){
        	return "FineReport";
        }else{
    		return "其他";
    	}
    }

    public void setType(Integer type) {
        this.type = type;
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
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getDefaultUid() {
        return defaultUid;
    }

    public String getDefaultPwd() {
        return defaultPwd;
    }

    public void setDefaultPwd(String defaultPwd) {
        this.defaultPwd = defaultPwd;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    

    public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setDefaultUid(String defaultUid) {
		this.defaultUid = defaultUid;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    public Integer getWaterMark() {
        return waterMark;
    }

    public void setWaterMark(Integer waterMark) {
        this.waterMark = waterMark;
    }

    public Integer getExportMode() {
        return exportMode;
    }

    public void setExportMode(Integer exportMode) {
        this.exportMode = exportMode;
    }

    public String getWatermarkText() {
        return watermarkText;
    }

    public void setWatermarkText(String watermarkText) {
        this.watermarkText = watermarkText;
    }

    @Override
    public String toString() {
        return "BiServer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", sort=" + sort +
                ", remark='" + remark + '\'' +
                ", defaultUid='" + defaultUid + '\'' +
                ", defaultPwd='" + defaultPwd + '\'' +
                ", url='" + url + '\'' +
                ", server='" + server + '\'' +
                ", param='" + param + '\'' +
                ", state='" + state + '\'' +
                ", waterMark=" + waterMark +
                ", exportMode=" + exportMode +
                ", watermarkText='" + watermarkText + '\'' +
                ", tenantId='" + tenantId + '\'' +
                '}';
    }
}
