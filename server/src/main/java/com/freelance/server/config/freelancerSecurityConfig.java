package com.freelance.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.freelance.server.Jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class freelancerSecurityConfig {
	
	@Autowired
	JwtAuthenticationFilter jwtFilter;

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				     .allowedOrigins("http://localhost:5173")
				     .allowedMethods("GET","POST","PUT","DELETE")
				     .allowedHeaders("*")
				     .allowCredentials(true);
			}
		};
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
		.csrf(csrf->csrf.disable())
		.cors()
		.and()
		.authorizeHttpRequests(authorize ->authorize
				.requestMatchers("/auth/login","/user/register","/api/stripe/create-account", 
		                "/api/stripe/account-link").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/stripe/webhook").permitAll()
				.anyRequest().authenticated()
				)
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManger(AuthenticationConfiguration builder) throws Exception{
		return builder.getAuthenticationManager();
	}
}
