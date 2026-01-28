package com.pragma.powerup.domain;

import com.pragma.powerup.domain.exception.AlreadyExistsException;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.model.TechnologyShort;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.domain.usecase.TechnologyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort persistencePort;

    @InjectMocks
    private TechnologyUseCase technologyUseCase;

    private Technology technology;

    @BeforeEach
    void setUp() {
        technology = new Technology(1L, "Java", "Object-oriented programming language");
    }

    @Test
    void saveTechnology_Success() {
        // GIVEN
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(persistencePort.save(any(Technology.class))).thenReturn(Mono.empty());

        // WHEN & THEN
        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .verifyComplete();

        verify(persistencePort).existsByName("Java");
        verify(persistencePort).save(technology);
    }

    @Test
    void saveTechnology_ThrowsAlreadyExistsException() {
        // GIVEN
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(true));

        // WHEN & THEN
        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .expectErrorMatches(throwable -> throwable instanceof AlreadyExistsException &&
                        throwable.getMessage().equals("La tecnología ya existe"))
                .verify();

        verify(persistencePort, never()).save(any());
    }

    @Test
    void verifyTechnologiesExist_ReturnsCountOfUniqueIds() {
        // GIVEN
        List<Long> ids = List.of(1L, 2L, 2L, 3L);
        List<Long> uniqueIds = List.of(1L, 2L, 3L);
        when(persistencePort.countByIds(uniqueIds)).thenReturn(Mono.just(3L));

        // WHEN & THEN
        StepVerifier.create(technologyUseCase.verifyTechnologiesExist(ids))
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    void verifyTechnologiesExist_ReturnsMinValue_WhenListIsNullOrEmpty() {
        StepVerifier.create(technologyUseCase.verifyTechnologiesExist(List.of()))
                .expectNext(Long.MIN_VALUE)
                .verifyComplete();

        StepVerifier.create(technologyUseCase.verifyTechnologiesExist(null))
                .expectNext(Long.MIN_VALUE)
                .verifyComplete();
    }

    @Test
    void findAllByIds_ReturnsFlux_WhenIdsAreProvided() {
        // GIVEN
        List<Long> ids = List.of(1L, 2L);
        TechnologyShort tech1 = new TechnologyShort(1L, "Java");
        TechnologyShort tech2 = new TechnologyShort(2L, "Python");

        when(persistencePort.findAllByIds(ids)).thenReturn(Flux.just(tech1, tech2));

        // WHEN & THEN
        StepVerifier.create(technologyUseCase.findAllByIds(ids))
                .expectNext(tech1)
                .expectNext(tech2)
                .verifyComplete();
    }

    @Test
    void findAllByIds_ReturnsEmptyFlux_WhenIdsEmptyOrNull() {
        StepVerifier.create(technologyUseCase.findAllByIds(List.of()))
                .verifyComplete();

        StepVerifier.create(technologyUseCase.findAllByIds(null))
                .verifyComplete();
    }

    @Test
    void deleteTechnology_Success() {
        // GIVEN
        when(persistencePort.deleteById(1L)).thenReturn(Mono.empty());

        // WHEN & THEN
        StepVerifier.create(technologyUseCase.deleteTechnology(1L))
                .verifyComplete();

        verify(persistencePort).deleteById(1L);
    }
}