package com.nandani.banking.securityCongiguration;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.nandani.banking.entitiy.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(UserDetails userDetails) {

        String token = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(getSignedKey())
                .compact();

        return token;

    }

    public SecretKey getSignedKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUserName(String token) {

        return Jwts.parser().verifyWith(getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();

    }

    public boolean validateToken(String token, UserDetails userDetails) {

        String reqUserName = extractUserName(token);
        String storedUserName = userDetails.getUsername();

        if (reqUserName.equals(storedUserName)) {
            return true;
        } else {
            return false;
        }

    }

}
