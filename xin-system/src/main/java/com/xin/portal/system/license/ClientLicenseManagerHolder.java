package com.xin.portal.system.license;

import de.schlichtherle.license.LicenseParam;

public class ClientLicenseManagerHolder {
	
	private static MyLicenseManager licenseManager ;
	
	public static MyLicenseManager getLicenseManager(LicenseParam licenseParam) {
		if (licenseManager == null) {
			licenseManager = new MyLicenseManager(licenseParam);
		}
		return licenseManager;
	}

}
