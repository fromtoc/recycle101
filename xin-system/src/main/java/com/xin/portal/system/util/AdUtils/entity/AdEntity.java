package com.xin.portal.system.util.AdUtils.entity;


/**
 * Created by 11468 on 2019/12/20.
 */
public class AdEntity {

    private String domain;
    private String ldapIP;
    private String ldapPORT;
    private String ldapURL;
    private String username;
    private String password;
    private String userPrincipalName;
    private String searchBase;

    public String getUserPrincipalName() {
        return username+"@"+domain;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLdapIP() {
        return ldapIP;
    }

    public void setLdapIP(String ldapIP) {
        this.ldapIP = ldapIP;
    }

    public String getLdapPORT() {
        return ldapPORT;
    }

    public void setLdapPORT(String ldapPORT) {
        this.ldapPORT = ldapPORT;
    }

    public String getLdapURL() {
        return "ldap://"+ldapIP+":389";
    }

    public void setLdapURL(String ldapURL) {
        this.ldapURL = ldapURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getSearchBase() {
        String dcs = "";
        String arr[] = domain.split("\\.");
        for(int i=0;i<arr.length;i++){
            if(i==arr.length-1){
                dcs += "DC="+arr[i];
            }else{
                dcs += "DC="+arr[i]+",";
            }
        }
        return dcs;
    }

    public void setSearchBase(String searchBase) {
        this.searchBase = searchBase;
    }

    @Override
    public String toString() {
        return "AdEntity{" +
                "domain='" + domain + '\'' +
                ", ldapIP='" + ldapIP + '\'' +
                ", ldapPORT='" + ldapPORT + '\'' +
                ", ldapURL='" + "ldap://"+ldapIP+":389" + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userPrincipalName='" + username+"@"+domain + '\'' +
                '}';
    }
}


