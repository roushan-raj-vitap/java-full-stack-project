package com.freelance.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freelance.server.dto.JwtRequest;
import com.freelance.server.dto.JwtResponse;
import com.freelance.server.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
		return new ResponseEntity<>(authService.login(jwtRequest),HttpStatus.OK);
	}
}
