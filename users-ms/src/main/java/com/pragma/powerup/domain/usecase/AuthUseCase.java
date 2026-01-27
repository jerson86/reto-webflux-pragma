package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.model.UserValidation;
import com.pragma.powerup.domain.spi.IEncryptionPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.spi.ITokenPort;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Map;

@AllArgsConstructor
public class AuthUseCase implements IAuthServicePort {
    private final IUserPersistencePort userPersistencePort;
    private final IEncryptionPort encryptionPort;
    private final ITokenPort tokenPort;


    @Override
    public Mono<User> register(User user) {
        user.setPassword(encryptionPort.encode(user.getPassword()));
        return userPersistencePort.save(user);
    }

    @Override
    public Mono<String> login(String email, String password) {
        return userPersistencePort.findByEmail(email)
                .filter(user -> encryptionPort.matches(password, user.getPassword()))
                .map(tokenPort::generateToken)
                .switchIfEmpty(Mono.error(new DomainException("Invalid credentials")));
    }

    @Override
    public Mono<UserValidation> validateToken(String token) {
        return Mono.fromCallable(() -> {
            Map<String, Object> claims = tokenPort.extractAllClaims(token);

            Long id = Long.valueOf(claims.get("id").toString());
            String role = (String) claims.get("role");

            return new UserValidation(id, role);
        }).onErrorResume(e -> Mono.error(new DomainException("Token inválido o corrupto")));
    }
}
