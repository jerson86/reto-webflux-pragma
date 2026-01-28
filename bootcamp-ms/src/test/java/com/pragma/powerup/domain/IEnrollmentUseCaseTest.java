package com.pragma.powerup.domain;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.domain.spi.IBootcampPersistencePort;
import com.pragma.powerup.domain.spi.IEnrollmentPersistencePort;
import com.pragma.powerup.domain.usecase.IEnrollmentUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IEnrollmentUseCaseTest {

    @Mock
    private IBootcampPersistencePort bootcampPersistencePort;
    @Mock private IEnrollmentPersistencePort enrollmentPersistencePort;

    @InjectMocks
    private IEnrollmentUseCase enrollmentUseCase;

    @Test
    void enrollPerson_Success_WhenNoOverlap() {
        Long personId = 1L;
        List<Long> newIds = List.of(2L);

        Bootcamp current = Bootcamp.builder().build();
        current.setStartDate(LocalDate.of(2026, 1, 1));
        current.setDurationDays(10);

        Bootcamp newB = Bootcamp.builder().name("Nuevo").build();
        newB.setStartDate(LocalDate.of(2026, 1, 15));
        newB.setDurationDays(10);

        when(bootcampPersistencePort.findAllByPersonId(personId)).thenReturn(Flux.just(current));
        when(bootcampPersistencePort.findAllByIds(newIds)).thenReturn(Flux.just(newB));
        when(enrollmentPersistencePort.saveAll(anyLong(), anyList())).thenReturn(Mono.empty());

        StepVerifier.create(enrollmentUseCase.enrollPerson(personId, newIds))
                .verifyComplete();
    }

    @Test
    void enrollPerson_Error_WhenDatesOverlap() {
        Long personId = 1L;
        List<Long> newIds = List.of(2L);

        Bootcamp current = Bootcamp.builder().name("Actual").build();
        current.setStartDate(LocalDate.of(2026, 2, 1));
        current.setDurationDays(20);

        Bootcamp newB = Bootcamp.builder().name("Conflicto").build();
        newB.setStartDate(LocalDate.of(2026, 2, 15));
        newB.setDurationDays(10);

        when(bootcampPersistencePort.findAllByPersonId(personId)).thenReturn(Flux.just(current));
        when(bootcampPersistencePort.findAllByIds(newIds)).thenReturn(Flux.just(newB));

        StepVerifier.create(enrollmentUseCase.enrollPerson(personId, newIds))
                .expectErrorMatches(e -> e instanceof DomainException &&
                        e.getMessage().contains("Conflicto de fechas"))
                .verify();
    }
}
