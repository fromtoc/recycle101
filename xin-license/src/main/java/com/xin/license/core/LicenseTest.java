package com.xin.license.core;

import java.util.Calendar;
import java.util.Date;

import com.xin.portal.system.model.License;

import de.schlichtherle.license.LicenseContent;

public class LicenseTest {
	
	public static void main(String[] args) {
        LicenseFactory cLicense = new LicenseFactory();
        //获取参数
        cLicense.setParam("license.properties");
        License license = new License();
        license.setApproveTime(new Date());
        license.setStartTime(new Date());
        Calendar c = Calendar.getInstance();
        c.add(c.DATE, +30);
        license.setEndTime(c.getTime());
        license.setCompany("user");
        license.setAmount(1);
        license.setExtInfo("其他附加信息");
        //生成证书
        cLicense.create(license);
    }

}
