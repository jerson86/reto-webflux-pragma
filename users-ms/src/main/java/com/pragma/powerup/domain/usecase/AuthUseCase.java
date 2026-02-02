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

@AllArgsConstructor
public class AuthUseCase implements IAuthServicePort {
    private final IUserPersistencePort userPersistencePort;
    private final IEncryptionPort encryptionPort;
    private final ITokenPort tokenPort;

    @Override
    public Mono<User> register(User user) {
        return Mono.just(user)
                .flatMap(this::validateUserFields)
                .map(u -> u.withEncryptedPassword(encryptionPort.encode(u.getPassword())))
                .flatMap(userPersistencePort::save)
                .onErrorResume(e -> Mono.error(
                        new BusinessException("Error al registrar usuario: " + e.getMessage(), "USER_REG_ERROR")));
    }

    @Override
    public Mono<String> login(String email, String password) {
        return validateLoginFields(email, password)
                .then(userPersistencePort.findByEmail(email))
                .filter(user -> encryptionPort.matches(password, user.getPassword()))
                .map(tokenPort::generateToken)
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Credenciales inválidas")))
                .onErrorResume(e -> {
                    if (e instanceof BaseException) {
                        return Mono.error(e);
                    }
                    return Mono
                            .error(new BusinessException("Error al iniciar sesión: " + e.getMessage(), "LOGIN_ERROR"));
                });
    }

    private Mono<User> validateUserFields(User user) {
        return Mono.just(user)
                .filter(u -> u.getEmail() != null && !u.getEmail().isBlank())
                .switchIfEmpty(Mono.error(new EmptyFieldException("El email es requerido")))
                .filter(u -> u.getPassword() != null && !u.getPassword().isBlank())
                .switchIfEmpty(Mono.error(new EmptyFieldException("La contraseña es requerida")))
                .thenReturn(user);
    }

    private Mono<Void> validateLoginFields(String email, String password) {
        return Mono.just(email)
                .filter(e -> e != null && !e.isBlank())
                .switchIfEmpty(Mono.error(new EmptyFieldException("El email es requerido")))
                .then(Mono.just(password))
                .filter(p -> p != null && !p.isBlank())
                .switchIfEmpty(Mono.error(new EmptyFieldException("La contraseña es requerida")))
                .then();
    }

    @Override
    public Mono<UserValidation> validateToken(String token) {
        return Mono.fromCallable(() -> {
            Long id = tokenPort.extractUserId(token);
            String role = tokenPort.extractRole(token);
            return new UserValidation(id, role);
        }).onErrorResume(e -> {
            if (e instanceof BaseException) {
                return Mono.error(e);
            }
            return Mono.error(new InvalidCredentialsException("Token inválido o corrupto"));
        });
    }
}
