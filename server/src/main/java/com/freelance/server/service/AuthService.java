package com.freelance.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.freelance.server.Jwt.JwtAuthenticationFilterHelper;
import com.freelance.server.dto.JwtRequest;
import com.freelance.server.dto.JwtResponse;
import com.freelance.server.model.User;

@Service
public class AuthService {
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtAuthenticationFilterHelper jwtHelper;
	
	public JwtResponse login(JwtRequest jwtRequest) {
		// TODO Auto-generated method stub
		this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
		
		User userDetails = (User)userDetailsService.loadUserByUsername(jwtRequest.getEmail());
		String token = jwtHelper.generateToken(userDetails);
		return JwtResponse.builder()
			    .jwtToken(token)
			    .email(userDetails.getUsername())  // assuming you're logging in with email
			    .role((userDetails).getRole().name())  // cast only if your User implements UserDetails
			    .stripeAccount(userDetails.getStripeAccountId())
			    .build();
	}
	
	public void doAuthenticate(String username,String password) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
		
		try {
			authenticationManager.authenticate(authenticationToken);
		}
		catch(BadCredentialsException e) {
			throw new BadCredentialsException("Invalid username or Password");
		}
	}

}
