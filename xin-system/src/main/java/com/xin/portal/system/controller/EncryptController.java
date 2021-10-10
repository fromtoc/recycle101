package com.xin.portal.system.controller;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xin.portal.system.util.RSAUtils;
import com.xin.portal.system.util.SessionUtil;

@Controller
@RequestMapping("/encrypt")
public class EncryptController {
	@RequestMapping("/publicKey")
	@ResponseBody
	public Map<String,String> getPublicKey()throws Exception{  
	     Map<String,String> result = new HashMap<String,String>();   
	     KeyPair kp = RSAUtils.generateKeyPair();  
	     RSAPublicKey pubk = (RSAPublicKey) kp.getPublic();// 生成公钥  
	     RSAPrivateKey prik = (RSAPrivateKey) kp.getPrivate();// 生成私钥  
	       
	     String publicKeyExponent = pubk.getPublicExponent().toString(16);// 16进制  
	     String publicKeyModulus = pubk.getModulus().toString(16);// 16进制 
	     SessionUtil.setSession("prik", prik);
	     result.put("pubexponent", publicKeyExponent);  
	     result.put("pubmodules", publicKeyModulus);  
	     return result;    
	}  
}
