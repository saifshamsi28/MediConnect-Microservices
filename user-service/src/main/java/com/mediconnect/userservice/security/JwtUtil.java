package com.mediconnect.userservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final String jwtSecretKey="jhdiuqjquu8wgwu8yje8ol2msyjsokdnbeguwkmbh";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60;

    public String generateToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}
