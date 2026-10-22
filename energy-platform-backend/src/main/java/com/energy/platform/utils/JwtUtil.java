package com.energy.platform.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.access-expiration:900000}") private long accessExpiration;
    @Value("${jwt.refresh-expiration:604800000}") private long refreshExpiration;
    private SecretKey key() { return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }
    public String generateAccessToken(String username) {
        return generateToken(username, "access", accessExpiration);
    }
    public String generateRefreshToken(String username) {
        return generateToken(username, "refresh", refreshExpiration);
    }
    private String generateToken(String username, String type, long expiration) {
        Date now = new Date();
        return Jwts.builder().subject(username).claim("type", type).issuedAt(now)
            .expiration(new Date(now.getTime() + expiration)).signWith(key()).compact();
    }
    public String getUsername(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }
    public boolean isTokenType(String token, String type) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        return type.equals(claims.get("type", String.class));
    }
}
