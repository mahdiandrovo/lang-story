package com.langstory.auth.service;

import com.langstory.auth.entity.AuthUserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateAccessToken(AuthUserEntity authUserEntity) {
        return Jwts.builder()
                .subject(authUserEntity.getId().toString())
                .claim("roles", Set.of("USER"))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10)) // 10 mins
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public UUID getUserIdFromToken(String token) {
        return UUID.fromString(getClaims(token).getSubject());
    }

    public List<String> getRolesFromToken(String token) {
        return getClaims(token).get("roles", List.class);
    }
}
