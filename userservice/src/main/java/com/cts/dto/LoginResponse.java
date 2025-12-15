package com.cts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponse {
	
	private Long id;
	private String name;
	private String email;
	private String role;
	private String token;
	private String tokenType;
	private String message;

}
