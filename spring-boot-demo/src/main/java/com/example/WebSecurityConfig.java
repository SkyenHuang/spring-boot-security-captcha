package com.example;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.captcha.CaptchaAuthenticationFailureHandler;
import com.example.captcha.CaptchaAuthenticationFilter;
import com.example.captcha.CaptchaAuthenticationProvider;
import com.example.captcha.CaptchaAuthenticationSuccessHandler;
import com.example.captcha.LoginWithCaptchaFailureHandler;
import com.example.captcha.LoginWithCaptchaSuccessHandler;

@EnableWebSecurity(debug = true)
@ConfigurationProperties(prefix = "security")
public class WebSecurityConfig {

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	private String loginPage = "/login";

	private String homePage = "/";

	@Configuration
	@Order(1)
	public class FormSecurityConfig extends WebSecurityConfigurerAdapter {

		@Autowired
		private WebSecurityConfig webSecurityConfig;

		@Autowired
		private CaptchaAuthenticationFilter captchaAuthenticationFilter;

		@Autowired
		private LoginWithCaptchaFailureHandler loginWithCaptchaFailureHandler;

		@Autowired
		private LoginWithCaptchaSuccessHandler loginWithCaptchaSuccessHandler;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/login", "/favicon.ico", "/bootstrap-3.3.7-dist/**").permitAll()
					.anyRequest().authenticated().and().formLogin().loginPage(webSecurityConfig.getLoginPage())
					.failureHandler(loginWithCaptchaFailureHandler).successHandler(loginWithCaptchaSuccessHandler).and()
					.logout().logoutUrl("/logout").logoutSuccessUrl("/login").and()
					.addFilterBefore(captchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		}

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(myUserDetailsService).and();
		}

	}

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	@Bean
	public CaptchaAuthenticationFilter captchaAuthenticationFilter(AuthenticationManager captchaAuthenticationManager,
			CaptchaAuthenticationSuccessHandler captchaAuthenticationSuccessHandler,
			CaptchaAuthenticationFailureHandler captchaAuthenticationFailureHandler) {
		CaptchaAuthenticationFilter filter = new CaptchaAuthenticationFilter(
				new AntPathRequestMatcher("/login", "POST"));
		filter.setAuthenticationManager(captchaAuthenticationManager);
		filter.setAuthenticationFailureHandler(captchaAuthenticationFailureHandler);
		filter.setAuthenticationSuccessHandler(captchaAuthenticationSuccessHandler);
		filter.setContinueChainBeforeSuccessfulAuthentication(true);
		return filter;
	}

	@Bean(name = "captchaAuthenticationProvider")
	public CaptchaAuthenticationProvider captchaAuthenticationProvider() {
		CaptchaAuthenticationProvider captchaAuthenticationProvider = new CaptchaAuthenticationProvider();
		return captchaAuthenticationProvider;
	}

	@Bean(name = "captchaAuthenticationManager")
	public AuthenticationManager captchaAuthenticationManager(
			CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		List<AuthenticationProvider> list = new ArrayList<>();
		list.add(captchaAuthenticationProvider);
		AuthenticationManager authenticationManager = new ProviderManager(list);
		return authenticationManager;
	}

	@Bean(name = "captchaAuthenticationFailureHandler")
	public CaptchaAuthenticationFailureHandler captchaAuthenticationFailureHandler(
			CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		return new CaptchaAuthenticationFailureHandler(getLoginPage() + "?error", captchaAuthenticationProvider);
	}

	@Bean(name = "captchaAuthenticationSuccessHandler")
	public CaptchaAuthenticationSuccessHandler captchaAuthenticationSuccessHandler(
			CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		return new CaptchaAuthenticationSuccessHandler(captchaAuthenticationProvider);
	}

	@Bean(name = "loginWithCaptchaFailureHandler")
	public LoginWithCaptchaFailureHandler loginWithCaptchaFailureHandler(
			CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		return new LoginWithCaptchaFailureHandler(getLoginPage() + "?error", captchaAuthenticationProvider);
	}

	@Bean(name = "loginWithCaptchaSuccessHandler")
	public LoginWithCaptchaSuccessHandler loginWithCaptchaSuccessHandler(
			CaptchaAuthenticationProvider captchaAuthenticationProvider) {
		return new LoginWithCaptchaSuccessHandler(getHomePage(), captchaAuthenticationProvider);
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
}
