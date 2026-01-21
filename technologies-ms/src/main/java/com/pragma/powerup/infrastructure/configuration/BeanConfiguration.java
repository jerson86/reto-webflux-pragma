package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.ITechnologyServicePort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.domain.usecase.TechnologyUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final ITechnologyPersistencePort technologyPersistencePort;

    @Bean
    public ITechnologyServicePort technologyServicePort() {
        return new TechnologyUseCase(technologyPersistencePort);
    }
}
