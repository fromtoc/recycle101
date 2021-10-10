package com.xin.portal.system.filter;

import org.apache.shiro.authc.AuthenticationToken;

public class OAuth2Token implements AuthenticationToken {

    public OAuth2Token(String authCode) {
        this.authCode = authCode;
    }

    private String authCode;
    private String principal;

    //getter、setter略。

    @Override
    public Object getCredentials() {
        return authCode;
    }

	@Override
	public Object getPrincipal() {
		return principal;
	}
}
