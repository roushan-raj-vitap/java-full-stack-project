package com.freelance.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.freelance.server.dto.ApiResponse;
import com.freelance.server.dto.UserDto;
import com.freelance.server.model.Role;
import com.freelance.server.model.User;
import com.freelance.server.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	public ResponseEntity<ApiResponse> registerUser(UserDto userDto) {
		String email = userDto.getEmail().toLowerCase().trim();

        // Validate
        if (userRepository.existsByEmail(email)) {
            return new ResponseEntity<>(new ApiResponse("Email is already registered", false),HttpStatus.BAD_REQUEST);
        }

        // Handle role conversion
        Role role;
        try {
            role = Role.valueOf(userDto.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
        	return new ResponseEntity<>(new ApiResponse("Invalid role requested!", false),HttpStatus.BAD_REQUEST);
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());

        // Build and save user
        User user = User.builder()
                .email(email)
                .name(userDto.getUsername())
                .password(encodedPassword)
                .role(role)
                .build();

        userRepository.save(user);
        return ResponseEntity
        	       .status(HttpStatus.CREATED)
        	       .body(new ApiResponse("User registered successfully", true));
	}
}
