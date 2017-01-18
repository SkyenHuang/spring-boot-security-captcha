package com.example.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	@RequestMapping({ "" })
	public String home(Model model, HttpSession httpSession) {
		return "login/home";
	}

	@RequestMapping("navigator")
	public String navigator(Model model, HttpServletRequest request) {
		String XRequestHeader = request.getHeader("X-Requested-With");
		if (!StringUtils.isEmpty(XRequestHeader) && XRequestHeader.equals("XMLHttpRequest")) {
			model.addAttribute("ajax", true);
		}
		return "navigator";
	}
}
