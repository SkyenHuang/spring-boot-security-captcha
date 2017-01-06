package com.example.captcha;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class LoginWithCaptchaSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final CaptchaAuthenticationProvider captchaAuthenticationProvider;

	public LoginWithCaptchaSuccessHandler(CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		super();
		this.captchaAuthenticationProvider = captchaAuthenticationProvider;
	}

	public LoginWithCaptchaSuccessHandler(String defaultUrl,
			CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		super(defaultUrl);
		this.captchaAuthenticationProvider = captchaAuthenticationProvider;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		captchaAuthenticationProvider.captchaMap.remove(request.getSession().getId());
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
