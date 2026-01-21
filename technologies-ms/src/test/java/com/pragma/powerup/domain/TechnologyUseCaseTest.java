package com.pragma.powerup.domain;

import com.pragma.powerup.domain.exception.AlreadyExistsException;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.ITechnologyPersistencePort;
import com.pragma.powerup.domain.usecase.TechnologyUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {

    @Mock
    private ITechnologyPersistencePort persistencePort;

    private TechnologyUseCase technologyUseCase;

    @BeforeEach
    void setUp() {
        technologyUseCase = new TechnologyUseCase(persistencePort);
    }

    @Test
    void saveTechnology_WhenNameDoesNotExist_ShouldSaveSuccessfully() {
        // GIVEN
        Technology technology = new Technology(null, "Java", "Lenguaje de programación");
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(persistencePort.save(any(Technology.class))).thenReturn(Mono.just(technology));

        // WHEN - THEN
        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .verifyComplete();

        verify(persistencePort, times(1)).save(any(Technology.class));
    }

    @Test
    void saveTechnology_WhenNameAlreadyExists_ShouldReturnError() {
        // GIVEN
        Technology technology = new Technology(null, "Java", "Lenguaje de programación");
        when(persistencePort.existsByName("Java")).thenReturn(Mono.just(true));

        // WHEN - THEN
        StepVerifier.create(technologyUseCase.saveTechnology(technology))
                .expectErrorMatches(throwable -> throwable instanceof AlreadyExistsException &&
                        throwable.getMessage().equals("La tecnología ya existe"))
                .verify();

        verify(persistencePort, never()).save(any(Technology.class));
    }
}