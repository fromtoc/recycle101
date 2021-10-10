package com.xin.portal.system.util;


/**
 * MD5密码加密
 */
public class PwdEncoder {
	public static String encodePassword(String rawPass) {
		return encodePassword(rawPass, Constant.DEFAULT_SALT);
	}

	/**
	 * 加密
	 * @param rawPass
	 * @param salt
	 * @return
	 */
	public static String encodePassword(String rawPass, String salt) {
		String saltedPass = mergePasswordAndSalt(rawPass, salt);
		return Md5Utils.encodeUpper(saltedPass);
	}

	/**
	 * 判断秘密是否相等
	 * @param encPass 加密后密码
	 * @param rawPass 原始密码
	 * @return
	 */
	public static boolean isPasswordValid(String encPass, String rawPass) {
		return isPasswordValid(encPass, rawPass, Constant.DEFAULT_SALT);
	}

	/**
	 * 
	 * @param encPass 加密后密码
	 * @param rawPass 原始密码
	 * @param salt 混淆码
	 * @return
	 */
	public static boolean isPasswordValid(String encPass, String rawPass, String salt) {
		if (encPass == null) {
			return false;
		}
		String pass2 = encodePassword(rawPass, salt);
		return encPass.equals(pass2);
	}

	/**
	 * 
	 * @param password
	 * @param salt
	 * @param strict
	 * @return
	 */
	protected static String mergePasswordAndSalt(String password, Object salt) {
		if (password == null) {
			password = "";
		}
		if ((salt == null) || "".equals(salt)) {
			return password + "{" + Constant.DEFAULT_SALT + "}" ;
		} else {
			return password + "{" + salt.toString() + "}";
		}
	}
}

