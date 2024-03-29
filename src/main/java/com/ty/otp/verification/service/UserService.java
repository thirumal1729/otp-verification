package com.ty.otp.verification.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ty.otp.verification.entity.User;
import com.ty.otp.verification.payload.RequestDto;
import com.ty.otp.verification.payload.ResponseDto;
import com.ty.otp.verification.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	EmailService emailService;
	
	public ResponseDto registerUser(RequestDto request) {
		
		ResponseDto response = new ResponseDto();
		User existingUser = null;
		Optional<User> optional = this.userRepository.findByEmail(request.getEmail());
		
		if(optional.isPresent()) {
			existingUser = optional.get();
		} 
		
		if(existingUser != null) {
			response.setMessage("User already registered.");
		} else {
			
			Random random = new Random();
			String otp = String.format("%06d", random.nextInt(100000));
			
			User newUser = new User();
			newUser.setName(request.getUsername());
			newUser.setEmail(request.getEmail());
			newUser.setPassword(passwordEncoder.encode(request.getPassword()));
			newUser.setOtp(otp);
			newUser.setVerified(false);
			
			User savedUser = this.userRepository.save(newUser);
			
			String subject = "Email Verification";
			String body = "Hello "+savedUser.getUsername()+", Your verification OTP is "+savedUser.getOtp();
			
			// Email Send
			this.emailService.sendEmail(savedUser.getEmail(), subject, body);
			
			response.setId(savedUser.getId());
			response.setUsername(savedUser.getUsername());
			response.setEmail(savedUser.getEmail());
			response.setMessage("OTP sent successfully");
			
		}
		
		return response;
	}
	
	public String verifyUser(String email, String otp) {
		String response = "";
		System.out.println(email);
		Optional<User> optional = this.userRepository.findByEmail(email);
		User user = null;
		if(optional.isPresent()) {
			user = optional.get();
			System.out.println(user);
		}
		
		if(user != null && user.isVerified()) {
			response = "User is already verified";
		} else if(otp.equals(user.getOtp())) {
			user.setVerified(true);
			this.userRepository.save(user);
			response = "User verified successfully";
		} else {
			response = "User not verified";
		}
		
		return response;
	}
	
	public ResponseDto userLogin(User user) {
		
		ResponseDto response = new ResponseDto();
		
		if(user != null) {
			Random random = new Random();
			String otp = String.format("%06d", random.nextInt(100000));
			
			user.setOtp(otp);
			user.setVerified(false);
			this.userRepository.save(user);
			String subject = "Email Verification";
			String body = "Hello "+user.getUsername()+", Your verification OTP is "+user.getOtp();
			this.emailService.sendEmail(user.getEmail(), subject, body);
			
			response.setId(user.getId());
			response.setUsername(user.getUsername());
			response.setEmail(user.getEmail());
			response.setMessage("OTP sent successfully...");
			
		} else {
			response.setMessage("User not found");
		}
		return response;
		
	}
	
	public List<User> getUsers() {
		return this.userRepository.findAll();
	}
	
	public User findByEmail(String email) {
		User user = this.userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException());
		return user;
	}
	
	public User findByName(String name) {
		User user = this.userRepository.findByName(name);
		return user;
	}

}
