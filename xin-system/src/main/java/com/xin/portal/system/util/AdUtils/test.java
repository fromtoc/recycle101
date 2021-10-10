package com.xin.portal.system.util.AdUtils;

import com.xin.portal.system.util.AdUtils.entity.AdDepartment;
import com.xin.portal.system.util.AdUtils.entity.AdEntity;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.util.AdUtils.entity.AdUser;

import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapContext;
import java.util.*;

/**
 * Created by 11468 on 2019/12/18.
 */
public class test {

    public static void main(String[] args) {
        //wangwu    mAServicE89.Jl
        //administrator mAServicE8m.Jl
        AdEntity adEntity = new AdEntity();
        adEntity.setUsername("administrator");
        adEntity.setPassword("mAServicE7m.Jl");
/*        adEntity.setUsername("lisi");
        adEntity.setPassword("mAServicE8mJl");*/
        adEntity.setDomain("datawindow.vm.com");
        adEntity.setLdapIP("192.168.43.137");
        adEntity.setLdapPORT("389");

        AdUser adUser =  ADUtils.getAdUser(adEntity);
        AdDepartment adDepartment = ADUtils.getAdDepartment(adEntity);
        System.out.println(11);
    }

    public void importAdDepartment(AdDepartment adDepartment){
        Organization org=new Organization();
        org.setCode(adDepartment.getId());
        org.setExtId(adDepartment.getId());
        org.setName(adDepartment.getName());
        org.setStatus(2);
        /*insert*/String insertId = "";
        if(adDepartment.getChildren().size()>0){
            saveFor(adDepartment.getChildren(),insertId);
        }
    }

    public void saveFor(List<AdDepartment> list,String parentId){
        Organization org=new Organization();

        org.setParentId(parentId);
        for(AdDepartment department:list){
            org.setCode(department.getId());
            org.setExtId(department.getId());
            org.setName(department.getName());
            org.setStatus(2);
             /*insert*/String insertId = "";
            if(department.getChildren().size()>0){
                saveFor(department.getChildren(),insertId);
            }
        }
    }

    public String  getAccountOrg(LdapContext  ctx){

        boolean b = LdapUtils.IsHave("lisi@datawindow.vm.com", "mAServicE8m.Jl", "ldap://192.168.43.128:389");
        System.out.println(b);

        ctx =LdapFactory.initLdapConnection("ldap://192.168.43.128:389","administrator@datawindow.vm.com", "mAServicE7m.Jl");
        AdDepartment adDepartment = LdapUtils.getTree(ctx, "datawindow.vm.com", "datawindow.vm.com");

        LdapFactory.closeLdapConnection(ctx);




        //搜索控制器参数设置
        //uSNCreated============唯一标识ID
        //sAMAccountname========账户
        //mobile================电话
        //displayName===========显示名称
        //distinguishedName=====节点标识名（搜索条件）
        String searchFilter =  "(objectClass=organizationalUnit)";
        String searchBase = "OU=软件研发部,DC=datawindow,DC=vm,DC=com";
        String returnedAtts[]={"uSNCreated","sAMAccountname", "distinguishedName","userPrincipalName","mail","displayName","mobile"};
        //SearchControls.OBJECT_SCOPE======返回指定对象
        //SearchControls.OBJECT_SCOPE======返回指定对象及其直接子实体
        //SearchControls.SUBTREE_SCOPE=====返回指定对象为根的子节点查找
        Map<String,String> map =  LdapUtils.LdapSearchAccountMap(ctx,searchFilter,searchBase,returnedAtts,SearchControls.OBJECT_SCOPE);
        return "";
    }



}
