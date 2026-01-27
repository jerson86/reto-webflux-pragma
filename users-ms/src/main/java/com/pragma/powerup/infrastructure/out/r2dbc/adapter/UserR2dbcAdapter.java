package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.mapper.IUserEntityMapper;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserR2dbcAdapter implements IUserPersistencePort {
    private final UserRepository userRepository;
    private final IUserEntityMapper userMapper;

    @Override
    public Mono<User> save(User user) {
        log.info("UserR2dbcAdapter save {}", user);
        return userRepository.save(userMapper.toEntity(user))
                .map(userMapper::toDomain);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }
}
