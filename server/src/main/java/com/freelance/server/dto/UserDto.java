package com.freelance.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private String username;
    private String email;
    private String password;
    private String role; // "CLIENT" or "FREELANCER"
}
