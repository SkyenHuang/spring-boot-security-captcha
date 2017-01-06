package com.example.captcha;

import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.example.captcha.utils.CaptchaFactory;

public class CaptchaAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final CaptchaAuthenticationProvider captchaAuthenticationProvider;

	public CaptchaAuthenticationFailureHandler(CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		super();
		this.captchaAuthenticationProvider = captchaAuthenticationProvider;
	}

	public CaptchaAuthenticationFailureHandler(String defaultUrl,
			CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		super(defaultUrl);
		this.captchaAuthenticationProvider = captchaAuthenticationProvider;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		try {
			captchaAuthenticationProvider.captchaMap.put(request.getSession().getId(),
					new CaptchaFactory().createCaptcha());
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		super.onAuthenticationFailure(request, response, exception);
	}

}
