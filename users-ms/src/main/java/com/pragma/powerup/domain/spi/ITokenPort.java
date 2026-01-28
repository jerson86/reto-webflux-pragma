package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.User;
import io.jsonwebtoken.Claims;

import java.security.Key;
import java.util.Map;

public interface ITokenPort {
    String generateToken(User user);
    Map<String, Object> extractAllClaims(String token);

    boolean validateToken(String token);

    String extractRole(String token);

    Long extractUserId(String token);

    Claims getClaims(String token);

    Key getKey(String secret);
}
