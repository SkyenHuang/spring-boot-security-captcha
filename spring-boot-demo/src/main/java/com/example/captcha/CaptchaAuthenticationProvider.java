package com.example.captcha;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;

import com.example.captcha.utils.CaptchaFactory.Captcha;

public class CaptchaAuthenticationProvider implements AuthenticationProvider {

	public ConcurrentHashMap<String, Captcha> captchaMap = new ConcurrentHashMap<>();

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(CaptchaAuthenticationToken.class, authentication, messages
				.getMessage("CaptchaAuthenticationToken.onlySupports", "Only CaptchaAuthenticationToken is supported"));
		CaptchaAuthenticationToken captchaAuthenticationToken = (CaptchaAuthenticationToken) authentication;
		Captcha captcha = captchaMap.get(captchaAuthenticationToken.getSessionId());
		if (captcha != null) {
			if (captcha.expired()) {
				throw new CaptchaException("expired captcha");
			}
			if (!captchaAuthenticationToken.getCaptcha().toUpperCase().equals(captcha.getValue())) {
				throw new CaptchaException("invalid captcha");
			}
		}
		return authentication;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CaptchaAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
