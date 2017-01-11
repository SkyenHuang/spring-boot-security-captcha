package com.example.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class UsernamePasswordWithCaptchaAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private String captchaParam = "captcha";

	public UsernamePasswordWithCaptchaAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super();
		this.setRequiresAuthenticationRequestMatcher(requiresAuthenticationRequestMatcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String captcha = obtainCaptcha(request);
		if (captcha == null) {
			captcha = "";
		}
		String username = obtainUsername(request);
		if (username == null) {
			username = "";
		}
		String password = obtainPassword(request);
		if (password == null) {
			password = "";
		}
		captcha = captcha.trim();
		UsernamePasswordWithCaptchaAuthenticationToken authRequest = new UsernamePasswordWithCaptchaAuthenticationToken(captcha, request.getSession().getId(),
				username, password);
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
