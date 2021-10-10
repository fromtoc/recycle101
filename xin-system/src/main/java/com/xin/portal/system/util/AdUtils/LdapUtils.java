package com.xin.portal.system.util.AdUtils;


import com.xin.portal.system.util.AdUtils.entity.AdDepartment;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.*;

public class LdapUtils {



    /**
     * 此方法为验证Ldap服务器中是否有该账户(用户名密码是否正确)
     * @param adminName :username@domain
     * @param adminPassword :password
     * @param ldapURL :ip:port
     * @return 返回“true”则有该用户  返回“false”则无该用户
     */
    public static Boolean IsHave(String adminName,String adminPassword,String ldapURL){
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");//"none","simple","strong"
        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);
        env.put(Context.PROVIDER_URL, ldapURL);
        try {
            LdapContext ctx = new InitialLdapContext(env, null);
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
        }
        return true;
    }

    /**
     * 此方法为查询特定条件下的属性信息
     * @param ctx :传入连接对象
     * @param searchFilter :过滤条件
     * @param searchBase :指定层级目录，从指定目录下查询
     * @param returnedAtts :返回的属性
     * @return 返回Map即为结果
     */
    public static Map<String,String> LdapSearchAccountMap(LdapContext ctx, String searchFilter, String searchBase, String [] returnedAtts, int level){
        Map<String,String> map = new HashMap<String,String>();
        try{
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(level);//返回指定对象
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes a = sr.getAttributes();
                NamingEnumeration<? extends Attribute> attrs = sr.getAttributes().getAll();
                while (attrs.hasMore()) {
                    Attribute attr = attrs.next();
                    map.put(attr.getID(), (String) attr.get());
                }
            }
            ctx.close();
        }catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Problem searching directory: " + e);
            System.out.println("查询有误，请检查参数");
            return null;
        }
        return map;
    }


    /**
     * 此方法为查询特定条件下的属性信息
     * @param ctx :传入连接对象
     * @param searchFilter :过滤条件
     * @param searchBase :指定层级目录，从指定目录下查询
     * @param returnedAtts :返回的属性
     * @return 返回list即为结果
     */
    public static List<Map<String,String>> LdapSearchAccountListMap(LdapContext ctx, String searchFilter, String searchBase, String [] returnedAtts, int level){
        List<Map<String,String>> listMap = new ArrayList<>();
        try{
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(level);//返回指定对象
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter,searchCtls);
            while (answer.hasMoreElements()) {
                Map<String,String> map = new HashMap<String,String> ();
                SearchResult sr = (SearchResult) answer.next();
                Attributes a = sr.getAttributes();
                NamingEnumeration<? extends Attribute> attrs = sr.getAttributes().getAll();
                while (attrs.hasMore()) {
                    Attribute attr = attrs.next();
                    map.put(attr.getID(), (String) attr.get());
                }
                listMap.add(map);
            }
            ctx.close();
        }catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Problem searching directory: " + e);
            System.out.println("查询有误，请检查参数");
            return null;
        }
        return listMap;
    }


    /**
     * 此方法获取组织树结构对象
     * @param ctx
     * @return AdDepartment
     */
    public static AdDepartment getTree(LdapContext ctx, String name, String cName) {
        TreeSet<AdDepartment> treeSet = new TreeSet<AdDepartment>();
        try {
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "objectClass=organizationalUnit";
            String searchBase = "DC=datawindow,DC=vm,DC=com";
            String returnedAtts[] = {"canonicalName", "distinguishedName","uSNCreated",
                    "name", "userPrincipalName", "departmentNumber", "telephoneNumber", "homePhone",
                    "mobile", "department", "sAMAccountName"}; // 定制返回属性
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                AdDepartment adDepartment = new AdDepartment();
                Attributes ats = sr.getAttributes();
                String SRid = ats.get("uSNCreated").toString();
                SRid = SRid.substring(SRid.indexOf(":")+1).trim();
                String SRname = ats.get("name").toString();
                SRname = SRname.substring(SRname.indexOf(":")+1).trim();
                String SRcanonicalName = ats.get("canonicalName").toString();
                String STdistinguishedName = ats.get("distinguishedName").toString();
                adDepartment.setId(SRid);
                adDepartment.setName(SRname);
                adDepartment.setcName(SRcanonicalName);
                adDepartment.setDistinguishedName(STdistinguishedName);
                treeSet.add(adDepartment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AdDepartment root = new AdDepartment();
        root.setName(name);
        root.setcName(cName);
        root.setId("Domain");
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
     * 此方法获取所有组织的id
     * @param ctx
     * @return List<String>
     */
    public static List<String> getIdsByLdap(LdapContext ctx) {
        List<String> ids = new ArrayList<>();
        try {
            SearchControls searchCtls = new SearchControls();
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "objectClass=organizationalUnit";
            String searchBase = "DC=datawindow,DC=vm,DC=com";
            String returnedAtts[] = {"canonicalName", "distinguishedName","uSNCreated",
                    "name", "userPrincipalName", "departmentNumber", "telephoneNumber", "homePhone",
                    "mobile", "department", "sAMAccountName"}; // 定制返回属性
            searchCtls.setReturningAttributes(returnedAtts);
            NamingEnumeration<SearchResult> answer = ctx.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes ats = sr.getAttributes();
                String SRid = ats.get("uSNCreated").toString();
                SRid = SRid.substring(SRid.indexOf(":")+1).trim();
                ids.add(SRid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

}
