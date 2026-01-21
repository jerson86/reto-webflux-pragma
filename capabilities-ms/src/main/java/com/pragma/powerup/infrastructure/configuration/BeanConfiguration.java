package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.ICapacityServicePort;
import com.pragma.powerup.domain.spi.ICapacityPersistencePort;
import com.pragma.powerup.domain.spi.IExternalTechnologyServicePort;
import com.pragma.powerup.domain.usecase.CapacityUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public ICapacityServicePort technologyServicePort(ICapacityPersistencePort persistencePort,
                                                      IExternalTechnologyServicePort externalPort) {
        return new CapacityUseCase(persistencePort, externalPort);
    }
}
