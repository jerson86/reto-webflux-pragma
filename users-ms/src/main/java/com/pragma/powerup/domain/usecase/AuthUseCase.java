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
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return Mono.error(new DomainException("El email es requerido"));
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return Mono.error(new DomainException("La contraseña es requerida"));
        }

        String encryptedPassword = encryptionPort.encode(user.getPassword());
        User userWithEncryptedPassword = user.withEncryptedPassword(encryptedPassword);
        
        return userPersistencePort.save(userWithEncryptedPassword)
                .onErrorResume(e -> Mono.error(new DomainException("Error al registrar usuario: " + e.getMessage())));
    }

    @Override
    public Mono<String> login(String email, String password) {
        if (email == null || email.isBlank()) {
            return Mono.error(new DomainException("El email es requerido"));
        }
        if (password == null || password.isBlank()) {
            return Mono.error(new DomainException("La contraseña es requerida"));
        }

        return userPersistencePort.findByEmail(email)
                .filter(user -> encryptionPort.matches(password, user.getPassword()))
                .map(tokenPort::generateToken)
                .switchIfEmpty(Mono.error(new DomainException("Credenciales inválidas")))
                .onErrorResume(e -> {
                    if (e instanceof DomainException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new DomainException("Error al iniciar sesión: " + e.getMessage()));
                });
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
