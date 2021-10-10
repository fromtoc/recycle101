package com.xin.portal.system.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

	/** 全局数组 */
	private final static String[] DIGITS = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/** 返回形式为数字跟字符串*/
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return DIGITS[iD1] + DIGITS[iD2];
	}

	/** 转换字节数组为16进制字串*/
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String encode(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest() 该函数返回值为存放哈希值结果的byte数组
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	/**
	 * <B>方法名称：</B>encode<BR>
	 * <B>概要说明：</B>将密码进行MD5加密<BR>
	 * 
	 * @param password
	 *            传入密码
	 * @return 经过MD5加密的密码
	 */
	public static String encodeUpper(String password) {
		return encode(password).toUpperCase();
	}
	
	public static boolean isEquals(String str1,String str2) {
		return encode(str1).equals(str2);
	}

	/*public static void main(String[] args) {
		//System.out.println(encode(encode("123456")));
	}*/

}
