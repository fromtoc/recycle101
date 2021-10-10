package com.xin.license.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 证书验证类.
 */
public class ServerVerifyLicense {
    //common param
    private static String PUBLICALIAS = "";
    private static String STOREPWD = "";
    private static String SUBJECT = "";
    private static String licPath = "";
    private static String pubPath = "";

    public void setParam(String propertiesPath) {
        // 获取参数
        Properties prop = new Properties();
        InputStream in = ServerVerifyLicense.class.getClassLoader().getResourceAsStream(propertiesPath);
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PUBLICALIAS = prop.getProperty("PUBLICALIAS");
        STOREPWD = prop.getProperty("STOREPWD");
        SUBJECT = prop.getProperty("SUBJECT");
        licPath = prop.getProperty("licPath");
        pubPath = prop.getProperty("pubPath");
    }

   /* public boolean verify() {
        *//************** 证书使用者端执行 ******************//*
        MyLicenseManager licenseManager = LicenseManagerHolder
                .getLicenseManager(initLicenseParams());
        // 安装证书
        try {
            licenseManager.uninstall();
            URL url = VerifyLicense.class.getClassLoader().getResource(licPath);
            System.out.println(url.getFile());
            File f = new File(url.getFile());
            licenseManager.install(f);
            System.out.println("客户端安装证书成功!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("客户端证书安装失败!");
            return false;
        }
        // 验证证书
        try {
            licenseManager.verify();
            System.out.println("客户端验证证书成功!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("客户端证书验证失效!");
            return false;
        }
        return true;
    }

    // 返回验证证书需要的参数
    private static LicenseParam initLicenseParams() {
        Preferences preference = Preferences
                .userNodeForPackage(VerifyLicense.class);
        CipherParam cipherParam = new DefaultCipherParam(STOREPWD);
        KeyStoreParam privateStoreParam = new DefaultKeyStoreParam(
                VerifyLicense.class, pubPath, PUBLICALIAS, STOREPWD, null);
        LicenseParam licenseParams = new DefaultLicenseParam(SUBJECT,
                preference, privateStoreParam, cipherParam);
        return licenseParams;
    }*/
    
    
    public static void main(String[] args) {
//        VerifyLicense vLicense = new VerifyLicense();
//        //获取参数
//        vLicense.setParam("verify.properties");
//        //验证证书
//        vLicense.verify();
    }
}
