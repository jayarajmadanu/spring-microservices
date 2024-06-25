package com.js.identity_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.js.identity_service.entity.UserCredential;
import com.js.identity_service.exception.ExpiredJwtTokenException;
import com.js.identity_service.exception.InvalidJwtTokenException;
import com.js.identity_service.repository.UserCredientialRepository;

@Service
public class AuthService {
	
	@Autowired
	private UserCredientialRepository credientialRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;

	public String saveUser(UserCredential credential) {
		credential.setPassword(passwordEncoder.encode(credential.getPassword()));
		credientialRepository.save(credential);
		return "User created";
	}
	
	public String generateToken(String userName) {
		return jwtService.generateToken(userName);
	}
	
	public String validateToken(String token) {
		 try {
			jwtService.validateToken(token);
		} catch (InvalidJwtTokenException e) {
			return "INVALID";
		} catch (ExpiredJwtTokenException e) {
			return "EXPIRED";
		} catch (Exception e) {
			return "INVALID1";
		}
		 return "VALID";
	}
}
