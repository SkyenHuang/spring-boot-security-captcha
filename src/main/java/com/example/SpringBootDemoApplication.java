package com.example;

import java.util.HashMap;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import com.example.entities.ChatMsg;

@SpringBootApplication
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}

	@Bean
	@Scope("singleton")
	@Qualifier("chatMap")
	public HashMap<String, Queue<ChatMsg>> chatMap() {
		return new HashMap<>();
	}
}
