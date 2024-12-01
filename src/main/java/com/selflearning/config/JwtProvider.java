package com.selflearning.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

    // Use a valid, secure key
    private final SecretKey key = Keys.hmacShaKeyFor(
            JwtConstant.SECRET_KEY.getBytes()
    );

    // Generate a JWT token
    public String generateToken(Authentication auth) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 846000000)) // Token validity
                .claim("email", auth.getName()) // Add claims
                .signWith(key) // Sign with the key
                .compact();
    }

    // Extract email from the token
    public String getEmailFromToken(String jwt) {
        // Remove "Bearer " prefix if present
        if (jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt) // Parse signed JWT
                .getBody();

        return String.valueOf(claims.get("email")); // Extract email
    }
}
