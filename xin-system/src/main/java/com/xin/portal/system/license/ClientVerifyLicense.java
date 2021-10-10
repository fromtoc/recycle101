package com.xin.portal.system.license;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.Preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.microstrategy.utils.StringUtils;
import com.xin.portal.system.model.License;
import com.xin.portal.system.util.AESUtil;
import com.xin.portal.system.util.Constant;

import de.schlichtherle.license.CipherParam;
import de.schlichtherle.license.DefaultCipherParam;
import de.schlichtherle.license.DefaultKeyStoreParam;
import de.schlichtherle.license.DefaultLicenseParam;
import de.schlichtherle.license.KeyStoreParam;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseParam;

/**
 * 证书验证类.
 */
public class ClientVerifyLicense {
	
	private static Logger logger = LoggerFactory.getLogger(ClientVerifyLicense.class);
    //common param
    private static String PUBLICALIAS = "";
    private static String STOREPWD = "";
    private static String SUBJECT = "";
//    private static String licPath = "";
    private static String pubPath = "";

    public static void setParam(String propertiesPath) {
        // 获取参数
        Properties prop = new Properties();
        propertiesPath = StringUtils.isEmpty(propertiesPath) ? "verify.properties" : propertiesPath;
        InputStream in = ClientVerifyLicense.class.getClassLoader().getResourceAsStream(propertiesPath);
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        PUBLICALIAS = prop.getProperty("PUBLICALIAS");
        STOREPWD = AESUtil.decrypt(prop.getProperty("STOREPWD"), Constant.DEFAULT_SALT);
        SUBJECT = prop.getProperty("SUBJECT");
        pubPath = prop.getProperty("pubPath");
        
        //logger.info(" params: {} | {} | {} | {}",PUBLICALIAS,STOREPWD,SUBJECT,pubPath);
    }
    

    public static  boolean verify() {
        /************** 证书使用者端执行 ******************/
    	boolean result = true;
        // 验证证书
        try {
        	MyLicenseManager licenseManager = ClientLicenseManagerHolder
        			.getLicenseManager(initLicenseParams());
            licenseManager.verify();
        }catch (Exception e) {
            //e.printStackTrace();
        	logger.error("客户端证书验证失效! {}",e.getMessage());
        	result = false;
        }
        return result;
    }
    
    public License installAndVerify(String filePath) {
        /************** 证书使用者端执行 ******************/
        MyLicenseManager licenseManager = ClientLicenseManagerHolder
                .getLicenseManager(initLicenseParams());
        // 安装证书
        try {
            licenseManager.uninstall();
//            URL url = VerifyLicense.class.getClassLoader().getResource(licPath);
//            System.out.println(url.getFile());
            File f = new File(filePath);
           // System.out.println("file path : ===== "+f.getAbsolutePath());
            licenseManager.install(f);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // 验证证书
        try {
        	LicenseContent licenseContent = licenseManager.verify();
        	License license = JSONObject.parseObject(licenseContent.getExtra().toString(),License.class);
        	license.setActivateTime(new Date());
        	return license;
        	
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    

    // 返回验证证书需要的参数
    private static  LicenseParam initLicenseParams() {
        Preferences preference = Preferences.userNodeForPackage(ClientVerifyLicense.class);
        if (StringUtils.isEmpty(STOREPWD)) {
        	ClientVerifyLicense.setParam("verify.properties");
        }
    	CipherParam cipherParam = new DefaultCipherParam(STOREPWD);
    	KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
    			ClientVerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
    	LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
    			preference, privateStoreParam, cipherParam);
    	return licenseParams;
    }
    
    
    public static void main(String[] args) {
//    	VerifyLicense v = new VerifyLicense();
//    	v.setParam("verify.properties");
//    	v.uninstall();
    	install();
    }
    
    public static void install(){
    	ClientVerifyLicense v = new ClientVerifyLicense();
        //验证证书
        v.installAndVerify("verify.properties");
    }
    
    public boolean uninstall(){
    	/************** 证书使用者端执行 ******************/
        MyLicenseManager licenseManager = ClientLicenseManagerHolder
                .getLicenseManager(initLicenseParams());
        // 安装证书
        try {
            licenseManager.uninstall();
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("客户端证书取消失败!");
            return false;
        }
        return true;
   }
}
