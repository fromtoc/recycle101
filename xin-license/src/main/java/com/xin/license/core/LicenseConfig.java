package com.xin.license.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LicenseConfig {
	
    @Bean
    public LicenseFactory LicenseFactory(){
    	return new LicenseFactory();
    }

}
