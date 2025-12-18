package com.project.Sevana.service;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class Jwtservice {
	
	@Value("${jwt.secret}")
	private String secretkey;
	public Jwtservice() {
		
	}
	public String generatetoken(String username) {
		Map<String,Object> claims = new HashMap<>();
		return Jwts.builder()
				   .claims()
				   .add(claims)
				   .subject(username)
				   .issuedAt(new Date(System.currentTimeMillis()))
				   .expiration(new Date(System.currentTimeMillis()+1000*60*60))//1hour
				   .and()
				   .signWith(getKey())
				   .compact();
	}

	private SecretKey getKey() {
		byte[] keybyte = Decoders.BASE64.decode(secretkey);
		return Keys.hmacShaKeyFor(keybyte);
	}
	
	public String extractusername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
	
	private <T> T  extractClaim(String token, Function<Claims,T> claimResolver) {
		final Claims claims=extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {//This uses a Secret Key (getKey()) to verify the token's digital signature.
		return Jwts.parser()
				   .verifyWith(getKey())
				   .build()
				   .parseSignedClaims(token)
				   .getPayload();
	}
	
	public boolean validatetoken(String token, UserDetails userDetails) {
        final String userName = extractusername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
