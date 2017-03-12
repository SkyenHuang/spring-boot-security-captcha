package com.example.captcha;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.Assert;

import com.example.captcha.utils.CaptchaFactory.Captcha;

public class DaoWithCaptchaAuthenticationProvider extends DaoAuthenticationProvider {

	public ConcurrentHashMap<String, Captcha> captchaMap = new ConcurrentHashMap<>();

	protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.isInstanceOf(UsernamePasswordWithCaptchaAuthenticationToken.class, authentication, messages
				.getMessage("CaptchaAuthenticationToken.onlySupports", "Only CaptchaAuthenticationToken is supported"));
		UsernamePasswordWithCaptchaAuthenticationToken captchaAuthenticationToken = (UsernamePasswordWithCaptchaAuthenticationToken) authentication;
		Captcha captcha = captchaMap.get(captchaAuthenticationToken.getSessionId());
		if (captcha != null) {
			if (captcha.expired()) {
				throw new CaptchaException("expired captcha");
			}
			if (!captchaAuthenticationToken.getCaptcha().toUpperCase().equals(captcha.getValue())) {
				throw new CaptchaException("invalid captcha");
			}
		}
		return super.authenticate(captchaAuthenticationToken.toUsernamePasswordAuthenticationToken());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordWithCaptchaAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
