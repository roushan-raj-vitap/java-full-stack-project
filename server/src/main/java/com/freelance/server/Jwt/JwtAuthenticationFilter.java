package com.freelance.server.Jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	JwtAuthenticationFilterHelper jwtHelper;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String requestHeader = request.getHeader("Authorization");

		String username  = null;
		String token = null;
		
		if(requestHeader!=null&&requestHeader.contains("Bearer")) {
			token = requestHeader.substring(7);
			System.out.println("JWT: " + token);
			username = jwtHelper.getUsername(token);
			System.out.println("Extracted Username: " + username);
			if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				System.out.println("Is Token Valid: " + jwtHelper.isTokenValid(token, userDetails));
				if(jwtHelper.isTokenValid(token,userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}
		filterChain.doFilter(request, response);
	}
}
