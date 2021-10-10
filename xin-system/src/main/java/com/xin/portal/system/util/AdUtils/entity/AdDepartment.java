package com.xin.portal.system.util.AdUtils.entity;

/**
 * Created by 11468 on 2019/12/13.
 */
import java.util.ArrayList;
import java.util.List;
/**
 * @ClassName AdDepartment
 * @Description: TODO
 * @Author jack
 * @Date 2019/8/31
 * @Version V1.0
 */
public class AdDepartment implements Comparable<AdDepartment>{
    private String id;
    private String name;
    private String cName;
    private String distinguishedName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public void setDistinguishedName(String distinguishedName) {
        this.distinguishedName = distinguishedName;
    }

    public List<AdDepartment> getChildren() {
        return children;
    }

    public void setChildren(List<AdDepartment> children) {
        this.children = children;
    }

    private List<AdDepartment> children = new ArrayList<AdDepartment>();


    public AdDepartment getAdDepartmentBycName(String cName) {
        if (this.cName.equals(cName) ) {
            return this;
        }else{
            for (AdDepartment adDepartment : children) {
                AdDepartment adDepartment1 =null;
                if ((adDepartment1 = adDepartment.getAdDepartmentBycName(cName)) != null) {
                    return adDepartment1;
                }
            }
        }
        return null;
    }
    public AdDepartment getParentAdDepartmentBycName(String cName) {
        int index;
        AdDepartment adDepartment = null;
        while ((index = cName.lastIndexOf("/")) != -1) {
            cName = cName.substring(0, index);
            adDepartment = getAdDepartmentBycName(cName);
            if (adDepartment != null) {
                return adDepartment;
            }
        }
        return adDepartment;
    }


    public int compareTo(AdDepartment o) {
        if(cName.equals(o.getcName())){
            return 0;
        }else{
            return 1;
        }
    }

    public void addChildren(AdDepartment adDepartment) {
        this.children.add(adDepartment);
    }

}
