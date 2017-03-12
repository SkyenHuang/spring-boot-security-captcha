package com.example.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;
import javax.persistence.PostRemove;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.MyUserDetails;
import com.example.entities.ChatMsg;

@Controller
@RequestMapping("/")
@Scope("session")
public class HomeController {

	@Autowired
	@Qualifier("chatMap")
	private HashMap<String, Queue<ChatMsg>> chatMap;

	private final Queue<ChatMsg> msgQueue;

	public HomeController() {
		msgQueue = new ConcurrentLinkedQueue<ChatMsg>();
	}

	@PostConstruct
	public void initChat() {
		chatMap.put(
				((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(),
				msgQueue);
	}

	@PostRemove
	public void removeChat() {
		chatMap.remove(
				((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails()).getUsername(),
				msgQueue);
	}

	@RequestMapping({ "" })
	public String home(Model model, HttpSession httpSession, HttpServletRequest request) {
		String XRequestHeader = request.getHeader("X-Requested-With");
		if (!StringUtils.isEmpty(XRequestHeader) && XRequestHeader.equals("XMLHttpRequest")) {
			return "ajax/home";
		}
		return "login/home";
	}

	@RequestMapping("guide")
	public String guide(Model model, HttpServletRequest request) {
		String XRequestHeader = request.getHeader("X-Requested-With");
		if (!StringUtils.isEmpty(XRequestHeader) && XRequestHeader.equals("XMLHttpRequest")) {
			return "ajax/guide";
		}
		return "login/guide";
	}

	@ResponseBody
	@RequestMapping("chat")
	@Scope("request")
	public Object chat() throws InterruptedException {
		ChatMsg msg;
		int i = 0;
		while (i < 10) {
			if ((msg = msgQueue.poll()) != null) {
				return msg;
			}
			Thread.sleep(500L);
			i++;
		}
		return null;
	}

	@ResponseBody
	@RequestMapping("chat2")
	@Scope("request")
	public Object chat2(@RequestBody ChatMsg chatMsg) throws InterruptedException {
		chatMsg.setFrom(
				((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
		chatMsg.setWhen(new Date());
		chatMap.get(StringUtils.isEmpty(chatMsg.getTo()) ? chatMsg.getFrom() : chatMsg.getTo()).offer(chatMsg);
		return null;
	}
}
