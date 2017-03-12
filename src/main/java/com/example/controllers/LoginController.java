package com.example.controllers;

import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.captcha.DaoWithCaptchaAuthenticationProvider;
import com.example.captcha.utils.CaptchaFactory.Captcha;

@RequestMapping("login")
@Controller
public class LoginController {

	@Autowired
	private DaoWithCaptchaAuthenticationProvider captchaAuthenticationProvider;

	@RequestMapping({ "" })
	public String login(Model model, HttpSession httpSession)
			throws FontFormatException, IOException, URISyntaxException {
		Captcha captcha = captchaAuthenticationProvider.captchaMap.get(httpSession.getId());
		if (captcha != null) {
			model.addAttribute(captcha);
		}
		return "login/login";
	}
	
}
