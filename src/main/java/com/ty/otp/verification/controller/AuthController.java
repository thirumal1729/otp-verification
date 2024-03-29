package com.ty.otp.verification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ty.otp.verification.entity.User;
import com.ty.otp.verification.models.JwtRequest;
import com.ty.otp.verification.models.JwtResponse;
import com.ty.otp.verification.payload.RequestDto;
import com.ty.otp.verification.payload.ResponseDto;
import com.ty.otp.verification.security.JwtHelper;
import com.ty.otp.verification.service.UserService;
import com.ty.otp.verification.util.UserRole;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtHelper helper;
	
	@Autowired
	UserService userService;
	
	@PostMapping("/user-register")
	public ResponseEntity<ResponseDto> registerUser(@RequestBody RequestDto request) {
		User user = new User();
		user.builder().name(request.getUsername()).password(request.getPassword()).email(request.getEmail()).role(UserRole.USER).build();
		ResponseDto response = this.userService.registerUser(request);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.CREATED);
	}
	
	@PostMapping("/admin-register")
	public ResponseEntity<ResponseDto> registerAdmin(@RequestBody RequestDto request) {
		User user = new User();
		user.builder().name(request.getUsername()).password(request.getPassword()).email(request.getEmail()).role(UserRole.ADMIN).build();
		ResponseDto response = this.userService.registerUser(request);
		
		return new ResponseEntity<ResponseDto>(response, HttpStatus.CREATED);
	}
	
	
	
//	@PostMapping("/user-login/email/{email}/password/{password}")
//	public ResponseEntity<?> loginUser(@PathVariable String email, @PathVariable String password) {
//		ResponseDto response = this.userService.userLogin(email, password);
//		
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}
	
	@PostMapping("/create/user-verify")
	public ResponseEntity<String> verifyUser(@RequestParam String email, @RequestParam String otp) {
		String response = this.userService.verifyUser(email, otp);
		
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		
		this.doAuthenticate(request.getEmail(), request.getPassword());


        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);

        User user = (User) userDetails;
        ResponseDto responseDto = this.userService.userLogin(user);
        
        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).message(responseDto.getMessage()).build();
        
       
        
        return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	 private void doAuthenticate(String email, String password) {

	        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
	        try {
	            authenticationManager.authenticate(authentication);


	        } catch (BadCredentialsException e) {
	            throw new BadCredentialsException(" Invalid Username or Password  !!");
	        }

	    }

	    @ExceptionHandler(BadCredentialsException.class)
	    public String exceptionHandler() {
	        return "Credentials Invalid !!";
	    }
	
}
