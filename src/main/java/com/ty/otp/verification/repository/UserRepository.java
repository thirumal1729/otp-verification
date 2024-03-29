package com.ty.otp.verification.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ty.otp.verification.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	
	User findByName(String name);

	User findByEmailAndPassword(String email, String password);
	
}
