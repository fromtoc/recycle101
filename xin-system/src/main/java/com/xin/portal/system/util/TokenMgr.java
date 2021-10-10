package com.xin.portal.system.util;

import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

import com.alibaba.fastjson.JSON;
import com.xin.portal.system.model.UserInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenMgr {

    public static SecretKey generalKey() {
        byte[] encodedKey = Base64Utils.decodeFromString(Constant.DEFAULT_SALT);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 签发JWT
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     * @throws Exception
     */
    public static String createJWT(String id, String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey secretKey = generalKey();
        JwtBuilder builder = Jwts.builder()
                .setId(id)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, secretKey);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date expDate = new Date(expMillis);
            builder.setExpiration(expDate);
        }
        return builder.compact();
    }

    /**
     * 验证JWT
     * @param jwtStr
     * @return
     */
//    public static CheckResult validateJWT(String jwtStr) {
//        CheckResult checkResult = new CheckResult();
//        Claims claims = null;
//        try {
//            claims = parseJWT(jwtStr);
//            checkResult.setSuccess(true);
//            checkResult.setClaims(claims);
//        } catch (ExpiredJwtException e) {
//            checkResult.setErrCode(Constant.JWT_ERRCODE_EXPIRE);
//            checkResult.setSuccess(false);
//        } catch (SignatureException e) {
//            checkResult.setErrCode(Constant.JWT_ERRCODE_FAIL);
//            checkResult.setSuccess(false);
//        } catch (Exception e) {
//            checkResult.setErrCode(Constant.JWT_ERRCODE_FAIL);
//            checkResult.setSuccess(false);
//        }
//        return checkResult;
//    }

    /**
     * 
     * 解析JWT字符串
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(jwt)
            .getBody();
    }

    /**
     * 生成subject信息
     * @param user
     * @return
     */
//    public static String generalSubject(SubjectModel sub){
//        return GsonUtil.objectToJsonStr(sub);
//    }
    
    public static void main(String[] args) {
    	UserInfo userInfo = new UserInfo();
    	userInfo.setId("1");
    	userInfo.setUsername("admin");
    	userInfo.setRealname("admin");
    	String s = createJWT("1", JSON.toJSONString(userInfo), Long.valueOf(1000));
    	//System.out.println(s);
    	String sss = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxIiwic3ViIjoie1wiaWRcIjoxLFwicmVhbG5hbWVcIjpcImFkbWluXCIsXCJ1c2VybmFtZVwiOlwiYWRtaW5cIn0iLCJpYXQiOjE1Mjk0ODY1OTcsImV4cCI6MTUyOTQ4NjU5OH0.vkORcaqi26yxguVp4mG2mO7bmgEDbShFLhi-y5ionXM";
    	try {
			Claims c = parseJWT(sss);
			//System.out.println(c.getId());
			//System.out.println(c.getSubject());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
