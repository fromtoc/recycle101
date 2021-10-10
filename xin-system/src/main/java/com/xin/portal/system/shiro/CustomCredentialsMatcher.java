package com.xin.portal.system.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import com.xin.portal.system.util.PasswordUtils;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
//System.out.println(String.valueOf(token.getPassword()));
//		Object tokenCredentials = encrypt(String.valueOf(token.getPassword()));
		Object accountCredentials = getCredentials(info);
		// 将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
		return equals(String.valueOf(token.getPassword()), accountCredentials);
	}

	// 将传进来密码加密方法
	private String encrypt(String data) {
		//String sha384Hex = new Sha384Hash(data).toBase64();// 这里可以选择自己的密码验证方式 比如
															// md5或者sha256等
		return PasswordUtils.hash(data);
	}
}
