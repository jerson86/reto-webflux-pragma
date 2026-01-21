package com.pragma.powerup.domain;

import com.pragma.powerup.domain.exception.AlreadyExistsException;
import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.domain.spi.ICapacityPersistencePort;
import com.pragma.powerup.domain.spi.IExternalTechnologyServicePort;
import com.pragma.powerup.domain.usecase.CapacityUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    private ICapacityPersistencePort persistencePort;

    @Mock
    private IExternalTechnologyServicePort externalTechnologyServicePort;

    private CapacityUseCase technologyUseCase;

    @BeforeEach
    void setUp() {
        technologyUseCase = new CapacityUseCase(persistencePort, externalTechnologyServicePort);
    }

    @Test
    void saveTechnology_WhenNameDoesNotExist_ShouldSaveSuccessfully() {
        // GIVEN
        Capacity capacity = new Capacity(null, "Java", "Lenguaje de programación", List.of());
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(persistencePort.save(any(Capacity.class))).thenReturn(Mono.just(capacity));

        // WHEN - THEN
        StepVerifier.create(technologyUseCase.saveCapability(capacity))
                .verifyComplete();

        verify(persistencePort, times(1)).save(any(Capacity.class));
    }

    @Test
    void saveTechnology_WhenNameAlreadyExists_ShouldReturnError() {
        // GIVEN
        Capacity capacity = new Capacity(null, "Java", "Lenguaje de programación", List.of());
        when(persistencePort.existsByName("Java")).thenReturn(Mono.just(true));

        // WHEN - THEN
        StepVerifier.create(technologyUseCase.saveCapability(capacity))
                .expectErrorMatches(throwable -> throwable instanceof AlreadyExistsException &&
                        throwable.getMessage().equals("La capacidad ya existe"))
                .verify();

        verify(persistencePort, never()).save(any(Capacity.class));
    }
}