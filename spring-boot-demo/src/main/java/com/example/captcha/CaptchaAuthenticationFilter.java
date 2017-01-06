package com.example.captcha;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private String captchaParam = "captcha";

	public CaptchaAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher);
	}

	public CaptchaAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String captcha = obtainCaptcha(request);
		if (captcha == null) {
			captcha = "";
		}
		captcha = captcha.trim();
		CaptchaAuthenticationToken authRequest = new CaptchaAuthenticationToken(captcha, request.getSession().getId());
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	protected String obtainCaptcha(HttpServletRequest request) {
		return request.getParameter(captchaParam);
	}

	public String getCaptchaParam() {
		return captchaParam;
	}

	public void setCaptchaParam(String captchaParam) {
		this.captchaParam = captchaParam;
	}

}
