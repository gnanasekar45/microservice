package com.luminn.driver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.luminn.driver.filter.AuthRequestFilter;
import com.luminn.driver.service.UserInfoService;


@EnableWebSecurity
public class SecurityConfig
		extends WebSecurityConfigurerAdapter
		 {

	@Autowired
	private AuthRequestFilter authRequestFilter;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		.antMatchers("/driver/**","/v1/**","/v2/**","/v3/**", "/getUsers", "/driver-photo/**", "/v2/api-docs", "/swagger-ui.html", "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security", "/webjars/**").permitAll()
		.anyRequest().authenticated().and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userInfoService);
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
