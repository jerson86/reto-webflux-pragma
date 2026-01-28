package com.pragma.powerup.domain;

import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import com.pragma.powerup.domain.usecase.ReportUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportUseCaseTest {

    @Mock
    private IReportPersistencePort reportPersistencePort;

    @InjectMocks
    private ReportUseCase reportUseCase;

    @Test
    void saveBootcampReport_Success() {
        // GIVEN
        BootcampReport report = new BootcampReport();
        report.setBootcampId(1L);
        report.setName("Java Reactivo");

        when(reportPersistencePort.save(any(BootcampReport.class)))
                .thenReturn(Mono.empty());

        // WHEN & THEN
        StepVerifier.create(reportUseCase.saveBootcampReport(report))
                .verifyComplete();

        verify(reportPersistencePort, times(1)).save(any(BootcampReport.class));

        assertNotNull(report.getRegistrationDate(), "La fecha de registro no debería ser nula");
        assertTrue(report.getRegistrationDate().isBefore(LocalDateTime.now().plusSeconds(1)),
                "La fecha de registro debe ser cercana al momento actual");
    }

    @Test
    void saveBootcampReport_Error_WhenPersistenceFails() {
        // GIVEN
        BootcampReport report = new BootcampReport();

        when(reportPersistencePort.save(any(BootcampReport.class)))
                .thenReturn(Mono.error(new RuntimeException("Error de base de datos")));

        // WHEN & THEN
        StepVerifier.create(reportUseCase.saveBootcampReport(report))
                .expectError(RuntimeException.class)
                .verify();

        verify(reportPersistencePort).save(any(BootcampReport.class));
    }
}
