package com.xin.license.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.prefs.Preferences;

import javax.security.auth.x500.X500Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.xin.portal.system.model.License;
import com.xin.portal.system.util.AESUtil;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.FileUtil;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

/**
 * 证书生成类
 */
public class LicenseFactory {
    /**
     * common param
     */
    private static String PRIVATE_ALIAS = "";
    private static String KEY_PWD = "";
    private static String STORE_PWD = "";
    private static String SUBJECT = "";
    private static String priPath = "";
    // 为了方便直接用的API里的例子
    // X500Princal是一个证书文件的固有格式，详见API
    private final static X500Principal DEFAULT_HOLDER_AND_ISSUER = new X500Principal("CN=dataondemand、OU=daaondemand、O=daaondemand、L=china、ST=beijing、C=cn");

    
    private static Logger logger = LoggerFactory.getLogger(LicenseFactory.class);
    
    /**
     * 读取配置文件数据
     */
    public static void setParam(String propertiesPath) {
        // 获取参数
        Properties prop = new Properties();
        InputStream in = LicenseFactory.class.getClassLoader().getResourceAsStream(propertiesPath);
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PRIVATE_ALIAS = prop.getProperty("server.PRIVATEALIAS");
        KEY_PWD = AESUtil.decrypt(prop.getProperty("server.KEYPWD"), Constant.DEFAULT_SALT);
        STORE_PWD = AESUtil.decrypt(prop.getProperty("server.STOREPWD"), Constant.DEFAULT_SALT);
        SUBJECT = prop.getProperty("server.SUBJECT");
        priPath = prop.getProperty("server.priPath");
    }

    /**
     * 证书发布者端执行 
     * @param record 
     */
    public boolean create(License license) {
    	File file = null;
        try {
            LicenseManager licenseManager = ServerLicenseManagerHolder
                    .getLicenseManager(initLicenseParams0());
            LicenseContent content = createLicenseContent(license);
            //File file = new File(license.getPath()+license.getId()+".lic");
            file = FileUtil.create(license.getPath()+license.getId()+".lic");
            logger.info("生成证书路径： {}",file.getAbsolutePath());
            licenseManager.store(content, file);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("客户端证书生成失败!");
            if (file!=null){
            	file.delete();
            }
            return false;
        }
        logger.info("服务器端生成证书成功!");
        return true;
    }

    // 返回生成证书时需要的参数
    private static LicenseParam initLicenseParams0() {
        Preferences preference = Preferences
                .userNodeForPackage(LicenseFactory.class);
        if (StringUtils.isEmpty(STORE_PWD)) {
        	setParam("license.properties");
        }
        // 设置对证书内容加密的对称密码
        CipherParam cipherParam = new DefaultCipherParam(STORE_PWD);
        // 参数1,2从Class.getResource()获得密钥库;
        // 参数3密钥库的别名;
        // 参数4密钥库存储密码;
        // 参数5密钥库密码
        KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
        		LicenseFactory.class, priPath, PRIVATE_ALIAS, STORE_PWD, KEY_PWD);
        LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
                preference, privateStoreParam, cipherParam);
        return licenseParams;
    }

    // 从外部表单拿到证书的内容
    public final static LicenseContent createLicenseContent(License license) {
        LicenseContent content = null;
        content = new LicenseContent();
        content.setSubject(SUBJECT);
        content.setHolder(DEFAULT_HOLDER_AND_ISSUER);
        content.setIssuer(DEFAULT_HOLDER_AND_ISSUER);
        content.setIssued(license.getApproveTime());
        content.setNotBefore(license.getStartTime());
        content.setNotAfter(license.getEndTime());
        content.setConsumerType(license.getCustomerType());
        content.setConsumerAmount(license.getAmount());
        content.setInfo(license.getExtInfo());
        // 扩展，后期可添加自定义数据，比如校验ip和mac地址等。
        
        content.setExtra(JSONObject.toJSONString(license));
        return content;
    }

}
