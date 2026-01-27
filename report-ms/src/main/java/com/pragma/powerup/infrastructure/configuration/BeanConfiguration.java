package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IReportServicePort;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import com.pragma.powerup.domain.usecase.ReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IReportPersistencePort reportPersistencePort;

    @Bean
    public IReportServicePort reportServicePort() {
        return new ReportUseCase(reportPersistencePort);
    }
}
