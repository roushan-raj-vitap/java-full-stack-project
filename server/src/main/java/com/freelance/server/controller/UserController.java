package com.freelance.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.freelance.server.dto.ApiResponse;
import com.freelance.server.dto.UserDto;
import com.freelance.server.service.UserService;

@RequestMapping("/user")
@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerUser(@RequestBody UserDto userDto){
		return userService.registerUser(userDto);
		
	}
}
