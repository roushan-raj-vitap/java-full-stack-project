package com.freelance.server.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class JwtResponse {

	private String jwtToken;
	private String email;
	private String role;
	private String stripeAccount;
}
