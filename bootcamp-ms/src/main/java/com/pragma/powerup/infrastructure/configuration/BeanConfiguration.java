package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IBootcampServicePort;
import com.pragma.powerup.domain.api.IEnrollmentServicePort;
import com.pragma.powerup.domain.spi.IBootcampPersistencePort;
import com.pragma.powerup.domain.spi.IEnrollmentPersistencePort;
import com.pragma.powerup.domain.spi.IExternalCapabilityServicePort;
import com.pragma.powerup.domain.usecase.BootcampUseCase;
import com.pragma.powerup.domain.usecase.IEnrollmentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IBootcampPersistencePort bootcampPersistencePort;
    private final IEnrollmentPersistencePort enrollmentPersistencePort;
    private final IExternalCapabilityServicePort externalCapabilityPort;

    @Bean
    public IBootcampServicePort bootcampServicePort() {
        return new BootcampUseCase(bootcampPersistencePort, externalCapabilityPort);
    }

    @Bean
    public IEnrollmentServicePort enrollmentServicePort() {
        return new IEnrollmentUseCase(bootcampPersistencePort, enrollmentPersistencePort);
    }
}
