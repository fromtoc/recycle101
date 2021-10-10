package com.xin.portal.system.util.AdUtils.entity;

import java.util.Hashtable;
import java.util.TreeSet;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

/**
 * Created by 11468 on 2019/12/4.
 */
public class ADUtils_copy {





    /**
     * 此方法加载AD域内的组织架构
     * @param username
     * @param password
     * @param ip
     * @param port
     * @return
     */
    public static LdapContext  getOrganization(String username,String password,String ip,String port){

        LdapContext  ctx=null;
        Hashtable<String,String> HashEnv = new Hashtable<String,String>();
        HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong)
        HashEnv.put(Context.SECURITY_PRINCIPAL, username); //AD的用户名
        HashEnv.put(Context.SECURITY_CREDENTIALS, password); //AD的密码
        HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
        HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");//连接超时设置为3秒
        HashEnv.put(Context.PROVIDER_URL, "ldap://" + ip + ":" + port);// 默认端口389
        try {
            ctx = new InitialLdapContext(HashEnv,null);// 初始化上下文
            System.out.println("身份验证成功!");
            try{
                //搜索控制器
                SearchControls searchCtls = new SearchControls();
                //创建搜索控制器
                searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                //LDAP搜索过滤器类，此处只获取AD域用户，所以条件为用户user或者person均可
                //(&(objectCategory=person)(objectClass=user)(name=*))
               /* String searchFilter = "objectClass=organizationalUnit";*/
                String searchFilter = "objectClass=user";

                //AD域节点结构
                String searchBase = "DC=datawindow,DC=vm,DC=com";

                String returnedAtts[] = {"canonicalName", "distinguishedName", "id",
                        "name", "userPrincipalName", "departmentNumber", "telephoneNumber", "homePhone",
                        "mobile", "department", "sAMAccountName", "whenChanged"}; // 定制返回属性

               /* String returnedAtts[] = { "url", "whenChanged", "employeeID", "name",
                        "userPrincipalName", "physicalDeliveryOfficeName", "departmentNumber",
                        "telephoneNumber", "homePhone", "mobile", "department",
                        "sAMAccountName", "whenChanged", "mail" };*/

                searchCtls.setReturningAttributes(returnedAtts);
                NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);
                TreeSet<AdDepartment> treeSet = new TreeSet<AdDepartment>();
                while (answer.hasMoreElements()) {
                    SearchResult sr = (SearchResult) answer.next();
                    AdDepartment adDepartment = new AdDepartment();
                    Attributes ats  = sr.getAttributes();
                    String name = ats.get("name").toString();
                    String canonicalName = ats.get("canonicalName").toString();
                    String distinguishedName = ats.get("distinguishedName").toString();
                    adDepartment.setName(name);
                    adDepartment.setcName(canonicalName);
                    adDepartment.setDistinguishedName(distinguishedName);
                    treeSet.add(adDepartment);
                }
                getTreeAdDepartment(treeSet);
            }catch (Exception e){
                e.printStackTrace();
            }
            return ctx;
        } catch (AuthenticationException e) {
            System.out.println("身份验证失败!");
            e.printStackTrace();
            return ctx;
        } catch (javax.naming.CommunicationException e) {
            System.out.println("AD域连接失败!");
            e.printStackTrace();
            return ctx;
        } catch (Exception e) {
            System.out.println("身份验证未知异常!");
            e.printStackTrace();
            return ctx;
        } finally{
            if(null!=ctx){
                try {
                    ctx.close();
                    ctx=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 此方法为验证AD登录
     * @param username
     * @param password
     * @param ip
     * @param port
     * @return
     */
    public static Boolean  ldapCheck(String username,String password,String ip,String port){

        LdapContext  ctx=null;
        Hashtable<String,String> HashEnv = new Hashtable<String,String>();
        HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong)
        HashEnv.put(Context.SECURITY_PRINCIPAL, username); //AD的用户名
        HashEnv.put(Context.SECURITY_CREDENTIALS, password); //AD的密码
        HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
        HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");//连接超时设置为3秒
        HashEnv.put(Context.PROVIDER_URL, "ldap://" + ip + ":" + port);// 默认端口389
        try {
            ctx = new InitialLdapContext(HashEnv,null);// 初始化上下文
            System.out.println("身份验证成功!");
            return true;
        } catch (AuthenticationException e) {
            System.out.println("身份验证失败!");
            e.printStackTrace();
            return false;
        } catch (javax.naming.CommunicationException e) {
            System.out.println("AD域连接失败!");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.out.println("身份验证未知异常!");
            e.printStackTrace();
            return false;
        } finally{
            if(null!=ctx){
                try {
                    ctx.close();
                    ctx=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static AdDepartment getTreeAdDepartment(TreeSet<AdDepartment> treeSet) {
        AdDepartment root = new AdDepartment();
        root.setName("datawindow.vm.com");
        root.setcName("datawidow.vm.com");
        for (AdDepartment ad : treeSet) {
            AdDepartment parentAdDepartment = null;
            if ((parentAdDepartment = root.getParentAdDepartmentBycName(ad.getcName())) != null) {
                parentAdDepartment.addChildren(ad);
            } else {
                root.addChildren(ad);
            }
        }
        return root;
    }

    /**
     * 此方法获取LdapContext（ldap上下文）
     * @param username
     * @param password
     * @param ip
     * @param port
     * @return
     */
    public static LdapContext  getCTX(String username,String password,String ip,String port){

        LdapContext  ctx=null;
        Hashtable<String,String> HashEnv = new Hashtable<String,String>();
        HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong)
        HashEnv.put(Context.SECURITY_PRINCIPAL, username); //AD的用户名
        HashEnv.put(Context.SECURITY_CREDENTIALS, password); //AD的密码
        HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
        HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");//连接超时设置为3秒
        HashEnv.put(Context.PROVIDER_URL, "ldap://" + ip + ":" + port);// 默认端口389
        try {
            ctx = new InitialLdapContext(HashEnv,null);// 初始化上下文
            return ctx;
        } catch (AuthenticationException e) {
            System.out.println("身份验证失败!");
            e.printStackTrace();
            return null;
        } catch (javax.naming.CommunicationException e) {
            System.out.println("AD域连接失败!");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.out.println("身份验证未知异常!");
            e.printStackTrace();
            return null;
        } finally{
            if(null!=ctx){
                try {
                    ctx.close();
                    ctx=null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }



}

