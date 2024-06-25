package com.js.api_gateway.util;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.js.api_gateway.exception.InvalidJwtTokenException;
import com.js.api_gateway.exception.ExpiredJwtTokenException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${security.jwt.secret-key}")
    private String SECRET;

    public void validateToken(final String token) throws Exception {
    	try {
         Jwts.parser().verifyWith(getSignKey()).build().parse(token);
    	} catch (ExpiredJwtException e) {
    		System.out.println("TOKEN EXPIRED");
    		throw new ExpiredJwtTokenException("Token Expired");
		} catch (Exception e) {
			System.out.println("TOKEN INVALID");
			throw new InvalidJwtTokenException("INVALID Token");
		}
    }
    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
