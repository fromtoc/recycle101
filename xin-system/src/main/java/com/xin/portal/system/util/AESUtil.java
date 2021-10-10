package com.xin.portal.system.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

/**
 * @version V1.0
 * @desc AES 加密工具类
 */
public class AESUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法
    
    private static Logger logger = LoggerFactory.getLogger(AESUtil.class);

    
    
    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String salt) {
            return Base64.encodeBase64String(encryptToByte(content,salt));//通过Base64转码返回
    }
    
    public static byte[] encryptToByte(String content, String salt) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

            byte[] byteContent = content.getBytes("utf-8");

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(salt));// 初始化为加密模式的密码器

            return cipher.doFinal(byteContent);// 加密
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {

        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64.decodeBase64(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String key) {
    	if (null == key || key.length() == 0) {
            throw new NullPointerException("key not is null");
        }
    	
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom srandom = SecureRandom.getInstance("SHA1PRNG");
            srandom.setSeed(key.getBytes());

            //AES 要求密钥长度为 128
            kg.init(128, srandom);

            //生成一个密钥
            SecretKey secretKey = kg.generateKey();

            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String s = "1&82A01D1C591B868737193AF51E1A516B&/caseInfo/index";

        //System.out.println("s:" + s);

        String s1 = AESUtil.encrypt(s, Constant.DEFAULT_SALT); 
        //System.out.println("加密:" + s1);
        try {
        	//System.out.println("加密base:" + Base64Utils.encodeToUrlSafeString(s1.getBytes()));
        	//System.out.println("加密base:" + new String(Base64Utils.decodeFromUrlSafeString("dDNIdDVCZkVIMjFNb3M4c2pMeVIvZTZxQThrTDVTQ1M4cENrK2lUOVlEMTdsMHg3UHBDWE11STNWY0lORjYwVQ")));
        	String s11 = URLEncoder.encode(s1,"utf-8");
			//System.out.println("加密en:" + s11);
			String s22 = URLDecoder.decode(s11,"utf-8");
			//System.out.println("加密de:" + s22);
			//System.out.println("解密"+AESUtil.decrypt(s22, Constant.DEFAULT_SALT));
			//System.out.println(URLDecoder.decode("X8CY8CVU0jNjWZoCKn56j06XLUmYRjN7uDN4Z711QpKBb+G+5v/QV8r63g1rsWPU","utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        String testStr = "123456";
        //System.out.println(Md5Utils.encode(testStr));
        String tsetStrEn = AESUtil.encrypt(testStr, Constant.DEFAULT_SALT);
        //System.out.println("testStr:"+tsetStrEn);
       // System.out.println("解密testStr："+AESUtil.decrypt(tsetStrEn, Constant.DEFAULT_SALT));
       // System.out.println(AESUtil.decrypt("+pXg7QtuQvQtySr8dmHDZw==", Constant.DEFAULT_SALT));
//X8CY8CVU0jNjWZoCKn56j06XLUmYRjN7uDN4Z711QpKBb+G+5v/QV8r63g1rsWPU
//        t3Ht5BfEH21Mos8sjLyR/e6qA8kL5SCS8pCk+iT9YD17l0x7PpCXMuI3VcINF60U
    }

}