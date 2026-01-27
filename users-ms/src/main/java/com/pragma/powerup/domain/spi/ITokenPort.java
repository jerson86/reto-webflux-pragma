package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.User;

import java.util.Map;

public interface ITokenPort {
    String generateToken(User user);
    Map<String, Object> extractAllClaims(String token);
}
