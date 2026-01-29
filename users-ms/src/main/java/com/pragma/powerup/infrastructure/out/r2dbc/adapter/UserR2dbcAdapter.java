package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.mapper.IUserEntityMapper;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.UserRepository;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import io.github.resilience4j.reactor.retry.RetryOperator;
import io.github.resilience4j.retry.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserR2dbcAdapter implements IUserPersistencePort {
    private final UserRepository userRepository;
    private final IUserEntityMapper userMapper;
    private final CircuitBreaker databaseCircuitBreaker;
    private final Retry databaseRetry;

    @Override
    public Mono<User> save(User user) {
        log.info("UserR2dbcAdapter save {}", user);
        return userRepository.save(userMapper.toEntity(user))
                .map(userMapper::toDomain)
                .transformDeferred(CircuitBreakerOperator.of(databaseCircuitBreaker))
                .transformDeferred(RetryOperator.of(databaseRetry))
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(e -> {
                    log.error("Error saving user: {}", e.getMessage());
                    if (e instanceof DomainException) {
                        return e;
                    }
                    return new DomainException("Error al guardar usuario en la base de datos");
                });
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain)
                .transformDeferred(CircuitBreakerOperator.of(databaseCircuitBreaker))
                .transformDeferred(RetryOperator.of(databaseRetry))
                .timeout(Duration.ofSeconds(5))
                .onErrorMap(e -> {
                    log.error("Error finding user by email: {}", e.getMessage());
                    if (e instanceof DomainException) {
                        return e;
                    }
                    return new DomainException("Error al buscar usuario por email");
                });
    }

    @Override
    public Flux<User> findAllByIds(List<Long> ids) {
        return userRepository.findAllById(ids)
                .map(userMapper::toDomain)
                .transformDeferred(CircuitBreakerOperator.of(databaseCircuitBreaker))
                .transformDeferred(RetryOperator.of(databaseRetry))
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> {
                    log.error("Error finding users by ids: {}", e.getMessage());
                    if (e instanceof DomainException) {
                        return Flux.error(e);
                    }
                    return Flux.error(new DomainException("Error al buscar usuarios por IDs"));
                });
    }
}
