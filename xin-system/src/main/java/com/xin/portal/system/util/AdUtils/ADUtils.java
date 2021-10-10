package com.xin.portal.system.util.AdUtils;


import com.xin.portal.system.util.AdUtils.entity.AdDepartment;
import com.xin.portal.system.util.AdUtils.entity.AdEntity;
import com.xin.portal.system.util.AdUtils.entity.AdUser;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapContext;
import java.util.*;

/**
 * Created by 11468 on 2019/12/20.
 */
public class ADUtils {

    private static  String userReturnedAtts[]={"uSNCreated","sAMAccountName", "distinguishedName","userPrincipalName","mail","displayName","mobile","userPassword"};

    private  static  String departmentReturnedAtts[] = {"uSNCreated","canonicalName", "distinguishedName", "name"};

    /**
     * 此方法得到用户在AD域内的基础信息
     * @param adEntity
     * @return
     */
    public static AdUser getAdUser(AdEntity adEntity){
        LdapContext userLdapContext = LdapFactory.initLdapConnection(adEntity.getLdapURL(),adEntity.getUserPrincipalName(),adEntity.getPassword());
        LdapContext departmentLdapContext = LdapFactory.initLdapConnection(adEntity.getLdapURL(),adEntity.getUserPrincipalName(),adEntity.getPassword());
        String userSearchFilter = "(&(objectCategory=person)(objectClass=user)(userPrincipalName="+adEntity.getUserPrincipalName()+"))";
        String userSearchBase = adEntity.getSearchBase();
        //SearchControls.OBJECT_SCOPE======返回指定对象
        //SearchControls.OBJECT_SCOPE======返回指定对象及其直接子实体
        //SearchControls.SUBTREE_SCOPE=====返回指定对象为根的子节点查找
        Map<String,String> userMap = LdapUtils.LdapSearchAccountMap(userLdapContext,userSearchFilter,userSearchBase,userReturnedAtts, SearchControls.SUBTREE_SCOPE);
        userMap.put("password",adEntity.getPassword());
        String departmentSearchFilter="objectClass=organizationalUnit";
        Map<String,String> departmentMap = new HashMap<>();
        if(userMap.get("distinguishedName")!= null &&!"".equals(userMap.get("distinguishedName"))){
            String departmentSearchBase =userMap.get("distinguishedName").substring(userMap.get("distinguishedName").indexOf(",")+1);
            departmentMap = LdapUtils.LdapSearchAccountMap(departmentLdapContext,departmentSearchFilter,departmentSearchBase,departmentReturnedAtts,SearchControls.SUBTREE_SCOPE);
        }else{
            departmentMap.put("uSNCreated","Domain");
        }

        LdapFactory.closeLdapConnection(userLdapContext);
        LdapFactory.closeLdapConnection(departmentLdapContext);
        return setEntity(userMap,departmentMap);
    }

    /**
     * 此方法得到对应域内的所有组织架构基础信息
     * @param adEntity
     * @return
     */
    public static AdDepartment getAdDepartment(AdEntity adEntity){
        LdapContext ctx = LdapFactory.initLdapConnection(adEntity.getLdapURL(),adEntity.getUserPrincipalName(),adEntity.getPassword());
        AdDepartment adDepartment = LdapUtils.getTree(ctx,adEntity.getDomain(),adEntity.getDomain());
        LdapFactory.closeLdapConnection(ctx);
        return adDepartment;
    }


    /**
     * 此方法得到对应域内的组织架构的id
     * @param adEntity
     * @return
     */
    public static List<String> getIdsByLdap(AdEntity adEntity){
        LdapContext ctx = LdapFactory.initLdapConnection(adEntity.getLdapURL(),adEntity.getUserPrincipalName(),adEntity.getPassword());
        List<String> ids =  LdapUtils.getIdsByLdap(ctx);
        LdapFactory.closeLdapConnection(ctx);
        return ids;
    }


    /**
     * 此方法将属性值放入实体中
     * @param userMap
     * @param departmentMap
     * @return
     */
    public static AdUser setEntity(Map<String,String>userMap,Map<String,String>departmentMap){
        AdUser adUser = new AdUser();
        if(userMap.get("uSNCreated")!=null){
            adUser.setId(userMap.get("uSNCreated"));
        }
        if(userMap.get("displayName")!=null){
            adUser.setDisplayName(userMap.get("displayName"));
        }
        if(userMap.get("sAMAccountName")!=null){
            adUser.setUsername(userMap.get("sAMAccountName"));
        }
        if(userMap.get("password")!=null){
            adUser.setPassword(userMap.get("password"));
        }
        if(userMap.get("mail")!=null){
            adUser.setMail(userMap.get("mail"));
        }
        if(userMap.get("mobile")!=null){
            adUser.setTelephone(userMap.get("mobile"));
        }
        if(departmentMap.get("uSNCreated")!=null){
            adUser.setDepartment(departmentMap.get("uSNCreated"));
        }
        return adUser;
    }






}
