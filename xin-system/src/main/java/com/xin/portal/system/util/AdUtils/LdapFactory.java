package com.xin.portal.system.util.AdUtils;

import com.xin.portal.system.controller.ActivateController;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class LdapFactory
{

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ActivateController.class);
    /**
     * @throws IOException
     * @Title: initLdapConnection
     * @Description: 初始化Ldap连接（389通道不需要ssl连接）
     * @return
     * @throws NamingException
     * @return DirContext
     * @throws
     */
    public static LdapContext initLdapConnection(String providerUrl, String username, String password)
    {
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, providerUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        try{
            return new InitialLdapContext(env, null);
        }catch (AuthenticationException e) {
            logger.info("身份验证失败!");
            e.printStackTrace();
            return null;
        } catch (javax.naming.CommunicationException e) {
            logger.info("AD域连接失败!");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            logger.info("身份验证未知异常!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Title: initLdapConnectionBySSL
     * @Description: 初始化Ldap连接（636通道需要ssl连接，进行双向加密协议）
     * @return
     * @throws NamingException
     * @throws IOException
     * @return LdapContext
     * @throws
     */
    public static LdapContext initLdapSSLConnection(String providerUrl, String username, String password, String sslTrustStore)
    {
        System.setProperty("javax.net.ssl.trustStore", sslTrustStore);
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, providerUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, username);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        try{
            return new InitialLdapContext(env, null);
        }catch (Exception e){
            logger.info("参数有误，获取Ldap连接失败");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @throws NamingException
     * @Title: closeLdapConnection
     * @Description: 关闭Ldap连接
     * @param ctx
     * @return void
     * @throws
     */
    public static void closeLdapConnection(LdapContext ctx)
    {
        if (ctx != null) {
            try {
                ctx.close();
            }
            catch (NamingException e) {
                e.printStackTrace();
            }
            ctx = null;
        }
    }

}
