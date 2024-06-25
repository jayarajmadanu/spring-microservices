package com.js.identity_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.js.identity_service.exception.ExpiredJwtTokenException;
import com.js.identity_service.exception.InvalidJwtTokenException;

@Service
@Slf4j
public class JwtService {


	@Value("${security.jwt.secret-key}")
    private String SECRET;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

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
    	
		/*
		 * System.out.println("Expiration = " + jwtExpiration);
		 * if(isTokenExpired(token)) { System.out.println("TOKEN EXPIRED"); throw new
		 * ExpiredJwtTokenException("Token Expired"); }
		 */
       	 
         
    }



    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName);
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignKey()).compact();
                //.signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    private boolean isTokenExpired(String token) {
    	System.out.println(extractExpiration(token) +"");
    	System.out.println(new Date() +"");
        return extractExpiration(token).before(new Date());
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
}