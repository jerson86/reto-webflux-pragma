package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IReportServicePort;
import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class ReportUseCase implements IReportServicePort {
    private final IReportPersistencePort reportPersistencePort;

    @Override
    public Mono<Void> saveBootcampReport(BootcampReport report) {
        report.setRegistrationDate(LocalDateTime.now());
        return reportPersistencePort.save(report);
    }
}
