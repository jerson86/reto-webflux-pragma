package com.pragma.powerup.domain;

import com.pragma.powerup.domain.enums.Role;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IEncryptionPort;
import com.pragma.powerup.domain.spi.ITokenPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.usecase.AuthUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;
    @Mock
    private IEncryptionPort encryptionPort;
    @Mock
    private ITokenPort tokenPort;

    @InjectMocks
    private AuthUseCase authUseCase;

    @Test
    void register_Success() {
        User user = new User(1L, "Test", "test@mail.com", "plainPassword", Role.ADMIN);
        when(encryptionPort.encode("plainPassword")).thenReturn("encodedPassword");
        when(userPersistencePort.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(authUseCase.register(user))
                .assertNext(savedUser -> {
                    assertEquals("encodedPassword", user.getPassword());
                    verify(userPersistencePort).save(user);
                })
                .verifyComplete();
    }

    @Test
    void login_Success() {
        User user = new User(1L, "Test", "test@mail.com", "encodedPassword", Role.ADMIN);
        when(userPersistencePort.findByEmail("test@mail.com")).thenReturn(Mono.just(user));
        when(encryptionPort.matches("plainPassword", "encodedPassword")).thenReturn(true);
        when(tokenPort.generateToken(user)).thenReturn("valid.jwt.token");

        StepVerifier.create(authUseCase.login("test@mail.com", "plainPassword"))
                .expectNext("valid.jwt.token")
                .verifyComplete();
    }

    @Test
    void login_Failure_InvalidCredentials() {
        when(userPersistencePort.findByEmail(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(authUseCase.login("wrong@mail.com", "pass"))
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals("Invalid credentials"))
                .verify();
    }

    @Test
    void validateToken_Success() {
        String token = "jwt.token";
        Map<String, Object> claims = Map.of("id", 1, "role", "ADMIN");
        when(tokenPort.extractAllClaims(token)).thenReturn(claims);

        StepVerifier.create(authUseCase.validateToken(token))
                .assertNext(validation -> {
                    assertEquals(1L, validation.getUserId());
                    assertEquals("ADMIN", validation.getRole());
                })
                .verifyComplete();
    }
}
