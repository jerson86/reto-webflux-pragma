package com.pragma.powerup.infrastructure.out.security.adapter;

import com.pragma.powerup.domain.utils.Constants;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IEncryptionPort;
import com.pragma.powerup.domain.spi.ITokenPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SecurityAdapter implements IEncryptionPort, ITokenPort {

    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.E_ID, user.getId());
        claims.put(Constants.E_ROLE, user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(secret), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Map<String, Object> extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(secret))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new DomainException("Error al parsear el token JWT");
        }
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String extractRole(String token) {
        return getClaims(token).get(Constants.E_ROLE, String.class);
    }

    @Override
    public Long extractUserId(String token) {
        return getClaims(token).get(Constants.E_ID, Long.class);
    }

    @Override
    public Claims getClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody();
    }

    @Override
    public Key getKey(String secret) {
        try {
            byte[] secretBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(secretBytes);
        } catch (Exception e) {
            return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
    }
}