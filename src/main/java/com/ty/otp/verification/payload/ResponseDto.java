package com.ty.otp.verification.payload;

import lombok.Data;

@Data
public class ResponseDto {

	private long id;
	private String username;
	private String email;
	private boolean verified;
	private String message;
	
}
