package com.ty.otp.verification.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ty.otp.verification.entity.User;
import com.ty.otp.verification.service.UserService;

@RestController
@RequestMapping("/home")
public class VeficationController {

	@Autowired
	UserService userService;
	
	@PostMapping("/login/user-verify")
	public ResponseEntity<String> verifyUser(@RequestParam String email, @RequestParam String otp) {
		String response = this.userService.verifyUser(email, otp);
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasAny('ADMIN')")
	public List<User> getUser() {
		return this.userService.getUsers();
	}
}
