package com.example.captcha;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CaptchaAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	protected final Log logger = LogFactory.getLog(getClass());

	private final CaptchaAuthenticationProvider captchaAuthenticationProvider;

	public CaptchaAuthenticationSuccessHandler(CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		super();
		this.captchaAuthenticationProvider = captchaAuthenticationProvider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		authentication.setAuthenticated(true);
	}

	public CaptchaAuthenticationProvider getCaptchaAuthenticationProvider() {
		return captchaAuthenticationProvider;
	}

}
