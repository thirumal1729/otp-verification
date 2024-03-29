package com.ty.otp.verification.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ty.otp.verification.entity.User;
import com.ty.otp.verification.repository.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// load user from database
		Optional<User> optional = this.userRepository.findByEmail(username);
		if(optional.isPresent()) {
			return optional.get();
		} else {
			return null;
		}
		
	}

}
