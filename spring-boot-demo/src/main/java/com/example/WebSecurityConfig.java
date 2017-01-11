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

import com.example.captcha.UsernamePasswordWithCaptchaAuthenticationFilter;
import com.example.captcha.DaoWithCaptchaAuthenticationProvider;
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
		private UsernamePasswordWithCaptchaAuthenticationFilter usernamePasswordWithCaptchaAuthenticationFilter;

		@Autowired
		private LoginWithCaptchaFailureHandler loginWithCaptchaFailureHandler;

		@Autowired
		private LoginWithCaptchaSuccessHandler loginWithCaptchaSuccessHandler;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/login", "/favicon.ico", "/bootstrap-3.3.7-dist/**", "/js/**")
					.permitAll().anyRequest().authenticated().and().formLogin()
					.loginPage(webSecurityConfig.getLoginPage()).failureHandler(loginWithCaptchaFailureHandler)
					.successHandler(loginWithCaptchaSuccessHandler).and().logout().logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout").and()
					.addFilterBefore(usernamePasswordWithCaptchaAuthenticationFilter,
							UsernamePasswordAuthenticationFilter.class);
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
	public UsernamePasswordWithCaptchaAuthenticationFilter usernamePasswordWithCaptchaAuthenticationFilter(
			AuthenticationManager captchaAuthenticationManager,
			LoginWithCaptchaSuccessHandler loginWithCaptchaSuccessHandler,
			LoginWithCaptchaFailureHandler loginWithCaptchaFailureHandler) {
		UsernamePasswordWithCaptchaAuthenticationFilter filter = new UsernamePasswordWithCaptchaAuthenticationFilter(
				new AntPathRequestMatcher(getLoginPage(), "POST"));
		filter.setAuthenticationManager(captchaAuthenticationManager);
		filter.setAuthenticationFailureHandler(loginWithCaptchaFailureHandler);
		filter.setAuthenticationSuccessHandler(loginWithCaptchaSuccessHandler);
		return filter;
	}

	@Bean(name = "daoWithCaptchaAuthenticationProvider")
	public DaoWithCaptchaAuthenticationProvider daoWithCaptchaAuthenticationProvider() {
		DaoWithCaptchaAuthenticationProvider provider = new DaoWithCaptchaAuthenticationProvider();
		provider.setUserDetailsService(myUserDetailsService);
		return provider;
	}

	@Bean(name = "usernamePasswordWithCaptchaAuthenticationManager")
	public AuthenticationManager usernamePasswordWithCaptchaAuthenticationManager(
			DaoWithCaptchaAuthenticationProvider daoWithCaptchaAuthenticationProvider) {
		List<AuthenticationProvider> list = new ArrayList<>();
		list.add(daoWithCaptchaAuthenticationProvider);
		AuthenticationManager authenticationManager = new ProviderManager(list);
		return authenticationManager;
	}

	@Bean(name = "loginWithCaptchaFailureHandler")
	public LoginWithCaptchaFailureHandler loginWithCaptchaFailureHandler(
			DaoWithCaptchaAuthenticationProvider daoWithCaptchaAuthenticationProvider) {
		return new LoginWithCaptchaFailureHandler(getLoginPage() + "?error", daoWithCaptchaAuthenticationProvider);
	}

	@Bean(name = "loginWithCaptchaSuccessHandler")
	public LoginWithCaptchaSuccessHandler loginWithCaptchaSuccessHandler(
			DaoWithCaptchaAuthenticationProvider daoWithCaptchaAuthenticationProvider) {
		return new LoginWithCaptchaSuccessHandler(getHomePage(), daoWithCaptchaAuthenticationProvider);
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
}
