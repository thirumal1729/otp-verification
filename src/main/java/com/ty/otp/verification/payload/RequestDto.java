package com.ty.otp.verification.payload;

import lombok.Data;

@Data
public class RequestDto {

	private String username;
	private String email;
	private String password;
	
}
