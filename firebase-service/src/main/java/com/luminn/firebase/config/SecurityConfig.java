package com.luminn.firebase.config;

import com.luminn.firebase.security.CustomUserDetailsService;
import com.luminn.firebase.security.JwtAuthenticationEntryPoint;
import com.luminn.firebase.security.JwtAuthenticationFilter;
import com.luminn.firebase.util.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationProvider;
import com.luminn.firebase.service.UserInfoService;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	@Autowired
	private UserInfoService userInfoService;



	private AuthenticationProvider authenticationProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	//https://stackoverflow.com/questions/48453980/spring-5-0-3-requestrejectedexception-the-request-was-rejected-because-the-url
	@Bean
	public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
		StrictHttpFirewall firewall = new StrictHttpFirewall();
		firewall.setAllowUrlEncodedSlash(true);
		return firewall;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//@formatter:off
		super.configure(web);
		web.httpFirewall(allowUrlEncodedSlashHttpFirewall());

	}

	// @Inject
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder
				.userDetailsService(customUserDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http
				.cors()
				.and()
				.csrf()
				.disable()
				.exceptionHandling()
				//part 11
				.authenticationEntryPoint(unauthorizedHandler)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.authorizeRequests()
				.antMatchers("/",
						"/favicon.ico",
						"/**/*.png",
						"/**/*.gif",
						"/**/*.svg",
						"/**/*.jpg",
						"/**/*.html",
						"/**/*.css",
						"/**/*.js",
						"/**/*.html")
				.permitAll()

				//_ah/admin/datastore?kind=USER
				.antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
						"/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-ui.html",
						"/swagger-resources/configuration/security","/_ah/admin","_ah/admin/datastore?kind=USER","_ah/admin/**")
				.permitAll() // this


				.antMatchers(SecurityConstants.SIGN_UP_DRIVER)
				.permitAll()

				.antMatchers(SecurityConstants.SIGN_UP_USER)
				.permitAll()

				.antMatchers(SecurityConstants.SIGN_IN)
				.permitAll()


				.antMatchers(SecurityConstants.SIGN_UP_URL)
				.permitAll()

				.antMatchers(SecurityConstants.USERTABLE)
				.permitAll()
				.antMatchers(SecurityConstants.SEARCH)
				.permitAll()
				.antMatchers(SecurityConstants.FORGOT)
				.permitAll()
				.antMatchers(SecurityConstants.DETAILS)
				.permitAll()
				.antMatchers(SecurityConstants.BYDETAILS)
				.permitAll()

				.antMatchers(SecurityConstants.SMS_NEW)
				.permitAll()
				.antMatchers(SecurityConstants.SMS_PASSWORD)
				.permitAll()
				.antMatchers(SecurityConstants.SMS_PASSWORD2)
				.permitAll()
				.antMatchers(SecurityConstants.CONFIG)
				.permitAll()
				.antMatchers(SecurityConstants.CONFIG_ALL)
				.permitAll()
				.antMatchers(SecurityConstants.CONTACT)
				.permitAll()

				//PART 2
				.antMatchers(HttpMethod.GET,"**/app/**").permitAll()
				.antMatchers(HttpMethod.POST,"**/app/**").permitAll()

				.antMatchers("**/app/user/v1/forgotpassword/retrieve/**").permitAll()
				.antMatchers(HttpMethod.GET, "/app/test/**")
				.permitAll()
				//PART77
				.antMatchers("/**").permitAll()
				.anyRequest()
				.authenticated();

		//PART3
		///VERY IMPORT SECURITY
		// Add our custom JWT security filter
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
