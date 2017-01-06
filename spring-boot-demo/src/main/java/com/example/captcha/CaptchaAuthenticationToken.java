package com.example.captcha;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CaptchaAuthenticationToken extends AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sessionId;

	private String captcha;

	public CaptchaAuthenticationToken(String captcha, String sessionId) {
		super(null);
		this.setCaptcha(captcha);
		this.setSessionId(sessionId);
		super.setAuthenticated(false);
	}

	public CaptchaAuthenticationToken(String captcha, String sessionId,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.setCaptcha(captcha);
		this.setSessionId(sessionId);
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAuthenticated() {
		// TODO Auto-generated method stub
		return super.isAuthenticated();
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
}
