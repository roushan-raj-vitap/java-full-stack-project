package com.freelance.server.Jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import com.freelance.server.model.User;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtAuthenticationFilterHelper {
	
	private String secret = "thisisacodingninjasdemonstrationforsecretkeyinspringsecurityjsonwebtokenauthentication";
	private static final long JWT_TOKEN_VALIDITY = 60*60*1000;
	
	 private Key getSigningKey() {
	        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	 }
	
	public String getUsername(String token) {
		// TODO Auto-generated method stub
		Claims claims = getClaimsFromToken(token);
		return claims.getSubject();
	}
	
	public Claims getClaimsFromToken(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(getSigningKey()) // ✅ use key, not raw bytes
	            .build()
	            .parseClaimsJws(token)          // ✅ correct for signed JWTs
	            .getBody();
	}


	public boolean isTokenValid(String token,UserDetails userDetails) {
		// TODO Auto-generated method stub
		Claims claims = getClaimsFromToken(token);
		Date expDate = claims.getExpiration();
		System.out.println("Token Valid Check: " + expDate.after(new Date()));
		System.out.println("Role from JWT: " + claims.get("role"));
		return (claims.getSubject().equals(userDetails.getUsername()) && expDate.after(new Date()));
	}

	public String generateToken(UserDetails userDetails) {
	    Map<String, Object> claims = new HashMap<>();
	    
	    if (userDetails instanceof User user) {
	        claims.put("role", user.getRole().name());
	    }
	    System.out.println(userDetails.getUsername());
	    return Jwts.builder()
	            .setClaims(claims)
	            .setSubject(userDetails.getUsername()) // ✅ subject will now be included
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
	            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
	            .compact();
	}

	
}
