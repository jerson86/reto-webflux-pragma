package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.exception.BaseException;
import com.pragma.powerup.domain.exception.BusinessException;
import com.pragma.powerup.domain.exception.EmptyFieldException;
import com.pragma.powerup.domain.exception.InvalidCredentialsException;
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
            return Mono.error(new EmptyFieldException("El email es requerido"));
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return Mono.error(new EmptyFieldException("La contraseña es requerida"));
        }

        String encryptedPassword = encryptionPort.encode(user.getPassword());
        User userWithEncryptedPassword = user.withEncryptedPassword(encryptedPassword);
        
        return userPersistencePort.save(userWithEncryptedPassword)
                .onErrorResume(e -> Mono.error(new BusinessException("Error al registrar usuario: " + e.getMessage(), "USER_REG_ERROR")));
    }

    @Override
    public Mono<String> login(String email, String password) {
        if (email == null || email.isBlank()) {
            return Mono.error(new EmptyFieldException("El email es requerido"));
        }
        if (password == null || password.isBlank()) {
            return Mono.error(new EmptyFieldException("La contraseña es requerida"));
        }

        return userPersistencePort.findByEmail(email)
                .filter(user -> encryptionPort.matches(password, user.getPassword()))
                .map(tokenPort::generateToken)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Credenciales inválidas")))
                .onErrorResume(e -> {
                    if (e instanceof BaseException) {
                        return Mono.error(e);
                    }
                    return Mono.error(new BusinessException("Error al iniciar sesión: " + e.getMessage(), "LOGIN_ERROR"));
                });
    }

    @Override
    public Mono<UserValidation> validateToken(String token) {
        return Mono.fromCallable(() -> {
            Map<String, Object> claims = tokenPort.extractAllClaims(token);

            Long id = Long.valueOf(claims.get("id").toString());
            String role = (String) claims.get("role");

            return new UserValidation(id, role);
        }).onErrorResume(e -> {
            if (e instanceof BaseException) {
                return Mono.error(e);
            }
            return Mono.error(new InvalidCredentialsException("Token inválido o corrupto"));
        });
    }
}
