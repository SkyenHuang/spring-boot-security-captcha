package com.example.captcha;

import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.example.captcha.utils.CaptchaFactory;

public class LoginWithCaptchaFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private final DaoWithCaptchaAuthenticationProvider captchaAuthenticationProvider;

	public LoginWithCaptchaFailureHandler(DaoWithCaptchaAuthenticationProvider captchaAuthenticationProvider) {
		super();
		this.captchaAuthenticationProvider = captchaAuthenticationProvider;
	}

	public LoginWithCaptchaFailureHandler(String defaultFailureUrl,
			DaoWithCaptchaAuthenticationProvider captchaAuthenticationProvider) {
		super(defaultFailureUrl);
		this.captchaAuthenticationProvider = captchaAuthenticationProvider;
	}

	protected Log logger = LogFactory.getLog(getClass());

	public static final String LOGIN_FAILURE_TIMES = "login_failure_times";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (exception instanceof CaptchaException) {
			try {
				captchaAuthenticationProvider.captchaMap.put(request.getSession().getId(),
						new CaptchaFactory().createCaptcha());
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} else {
			recordLoginFailureTimes(request.getSession());
		}
		super.onAuthenticationFailure(request, response, exception);
	}

	private void recordLoginFailureTimes(HttpSession httpSession) {
		Object loginFailureTimesObject = httpSession.getAttribute(LOGIN_FAILURE_TIMES);
		Integer loginFailureTimes = 0;
		if (loginFailureTimesObject != null) {
			loginFailureTimes = (Integer) loginFailureTimesObject;
		}
		httpSession.setAttribute(LOGIN_FAILURE_TIMES, ++loginFailureTimes);
		if (loginFailureTimes > 2) {
			logger.warn("create captcha for being more login failure times");
			try {
				captchaAuthenticationProvider.captchaMap.put(httpSession.getId(), new CaptchaFactory().createCaptcha());
			} catch (FontFormatException | IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
}
