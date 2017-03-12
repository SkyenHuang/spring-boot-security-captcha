package com.example.captcha;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class UsernamePasswordWithCaptchaAuthenticationToken extends UsernamePasswordAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String captcha;

	private final String sessionId;

	public UsernamePasswordWithCaptchaAuthenticationToken(String captcha, String sessionId, Object principal,
			Object credentials) {
		super(principal, credentials);
		this.setCaptcha(captcha);
		this.sessionId = sessionId;
		super.setAuthenticated(false);
	}

	public UsernamePasswordWithCaptchaAuthenticationToken(String captcha, String sessionId, Object principal,
			Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		this.setCaptcha(captcha);
		this.sessionId = sessionId;
		super.setAuthenticated(true);
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
	
	public UsernamePasswordAuthenticationToken toUsernamePasswordAuthenticationToken(){
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(this.getPrincipal(), this.getCredentials());
		return usernamePasswordAuthenticationToken;
	}

}
