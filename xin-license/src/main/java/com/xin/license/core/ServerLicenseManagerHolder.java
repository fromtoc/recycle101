package com.xin.license.core;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;

public class ServerLicenseManagerHolder {
	
	private static LicenseManager licenseManager ;
	
	public static LicenseManager getLicenseManager(LicenseParam licenseParam) {
		if (licenseManager == null) {
			licenseManager = new LicenseManager(licenseParam);
		}
		return licenseManager;
	}

}
