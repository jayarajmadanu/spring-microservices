package com.js.identity_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.js.identity_service.dto.AuthRequest;
import com.js.identity_service.entity.UserCredential;
import com.js.identity_service.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AuthService authServic;
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public String addNewUser(@RequestBody UserCredential user) {
		return authServic.saveUser(user);
	}
	
	@GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
		String valid = authServic.validateToken(token);
		if(valid.equals("VALID"))
			return new ResponseEntity<String>(valid, HttpStatus.OK);
		else 
			return new ResponseEntity<String>(valid, HttpStatus.UNAUTHORIZED);
	
    }
	
	 @PostMapping("/token")
	    public String getToken(@RequestBody AuthRequest authRequest) {
	        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
	        if (authenticate.isAuthenticated()) {
	            return authServic.generateToken(authRequest.getUsername());
	        } else {
	            throw new RuntimeException("invalid access");
	        }
	    }

}
