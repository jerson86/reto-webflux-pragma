package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IAuthServicePort;
import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.spi.IEncryptionPort;
import com.pragma.powerup.domain.spi.ITokenPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.usecase.AuthUseCase;
import com.pragma.powerup.domain.usecase.UserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IUserPersistencePort userPersistencePort;
    private final IEncryptionPort encryptionPort;
    private final ITokenPort tokenPort;

    @Bean
    public IAuthServicePort authServicePort() {
        return new AuthUseCase(userPersistencePort, encryptionPort, tokenPort);
    }

    @Bean
    public IUserServicePort userServicePort() {
        return new UserUseCase(userPersistencePort);
    }
}
