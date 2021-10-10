/*
 * Copyright (C) 2005-2015 Schlichtherle IT Services.
 * All rights reserved. Use is subject to license terms.
 */
package com.xin.portal.system.license;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.xin.portal.system.util.WebUtil;

import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseNotary;
import de.schlichtherle.license.LicenseParam;


/**
 * This is the top level class which manages all licensing aspects like for
 * instance the creation, installation and verification of license keys.
 * The license manager knows how to install, verify and uninstall full and
 * trial licenses for a given subject and ensures the privacy of the license
 * content in its persistent form (i.e. the <i>license key</i>).
 * For signing, verifying and validating licenses, this class cooperates with
 * a {@link LicenseNotary}.
 * <p>
 * This class is thread-safe.
 *
 * @author Christian Schlichtherle
 */
public class MyLicenseManager extends LicenseManager {
	
	private static final Logger logger = LoggerFactory.getLogger(MyLicenseManager.class);
	
	private static final String EXC_CONTENT_IS_NULL="content parse error!!!";
	private static final String EXC_LICENSE_IS_NOT_YET_VALID="Unused time !!!";
	private static final String EXC_LICENSE_HAS_EXPIRED="License has expired!!!";
	private static final String EXC_CONSUMER_AMOUNT = "Invalid consumer amount!!!";
	private static final String EXC_MAC_ADDRESS = "Invalid mac address!!!";

	public MyLicenseManager(LicenseParam param) {
		super.setLicenseParam(param);
	}
    protected synchronized void validate(final LicenseContent content)
    
    throws LicenseContentException {
    	super.validate(content);
        
        JSONObject extData = JSONObject.parseObject(content.getExtra().toString());
        
        if (extData==null ||extData.isEmpty()) {
        	throw new LicenseContentException(EXC_CONTENT_IS_NULL);
        }
        if (extData.getBoolean("timeEnabled")!=null && extData.getBoolean("timeEnabled")){
        	final Date now = new Date();
        	final Date notBefore = content.getNotBefore();
        	if (null != notBefore && now.before(notBefore))
        		throw new LicenseContentException(EXC_LICENSE_IS_NOT_YET_VALID);
        	final Date notAfter = content.getNotAfter();
        	if (null != notAfter && now.after(notAfter))
        		throw new LicenseContentException(EXC_LICENSE_HAS_EXPIRED);
        };
        
        if (extData.getBoolean("amountEnabled")!=null && extData.getBoolean("amountEnabled")) {
        	if (0 >= content.getConsumerAmount())
                throw new LicenseContentException(EXC_CONSUMER_AMOUNT);
//        	if (content.getConsumerAmount()<extData.getInteger("amount"))
//                throw new LicenseContentException(EXC_CONSUMER_AMOUNT);
        }
        
        if (extData.getBoolean("macEnabled")!=null && extData.getBoolean("macEnabled")) {
        	String macAddress = WebUtil.getMacAddress();
        	logger.info("verify mac address : [{}] [{}]",macAddress,extData.get("macAddress"));
        	if (!macAddress.equals(extData.get("macAddress"))) {
        		throw new LicenseContentException(EXC_MAC_ADDRESS);
        	}
        }
    }
        

}

