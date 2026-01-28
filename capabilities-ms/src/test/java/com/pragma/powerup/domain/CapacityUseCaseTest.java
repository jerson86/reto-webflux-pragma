package com.pragma.powerup.domain;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Capacity;
import com.pragma.powerup.domain.model.TechnologyShort;
import com.pragma.powerup.domain.spi.ICapacityPersistencePort;
import com.pragma.powerup.domain.spi.IExternalTechnologyServicePort;
import com.pragma.powerup.domain.usecase.CapacityUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapacityUseCaseTest {

    @Mock
    private ICapacityPersistencePort persistencePort;
    @Mock
    private IExternalTechnologyServicePort externalTechPort;

    @InjectMocks
    private CapacityUseCase capacityUseCase;

    private static final Long CAP_ID = 1L;
    private static final String CAP_NAME = "Java Backend";

    @Test
    void saveCapability_Success() {
        Capacity capacity = Capacity.builder()
                .name(CAP_NAME)
                .technologyIds(List.of(10L, 11L))
                .build();

        when(persistencePort.existsByName(CAP_NAME)).thenReturn(Mono.just(false));
        when(externalTechPort.allTechnologiesExist(anyList())).thenReturn(Mono.just(2L));
        when(persistencePort.save(any())).thenReturn(Mono.just(capacity));

        StepVerifier.create(capacityUseCase.saveCapability(capacity))
                .verifyComplete();
    }

    @Test
    void saveCapability_ThrowsError_WhenNameAlreadyExists() {
        Capacity capacity = Capacity.builder()
                .name(CAP_NAME)
                .build();

        when(persistencePort.existsByName(CAP_NAME)).thenReturn(Mono.just(true));

        StepVerifier.create(capacityUseCase.saveCapability(capacity))
                .expectErrorMatches(e -> e instanceof DomainException &&
                        e.getMessage().equals("La capacidad ya existe"))
                .verify();
    }

    @Test
    void saveCapability_ThrowsError_WhenSomeTechnologiesDoNotExist() {
        Capacity capacity = Capacity.builder()
                .name(CAP_NAME)
                .technologyIds(List.of(10L, 11L, 12L))
                .build();

        when(persistencePort.existsByName(CAP_NAME)).thenReturn(Mono.just(false));
        when(externalTechPort.allTechnologiesExist(anyList())).thenReturn(Mono.just(2L));

        StepVerifier.create(capacityUseCase.saveCapability(capacity))
                .expectErrorMatches(e -> e instanceof DomainException &&
                        e.getMessage().contains("Una o más tecnologías no existen"))
                .verify();
    }

    @Test
    void getCapabilities_Success_EnrichesWithExternalTechs() {
        Capacity cap = Capacity.builder()
                .id(CAP_ID)
                .technologyIds(List.of(10L))
                .build();

        TechnologyShort tech = new TechnologyShort(10L, "Java");

        when(persistencePort.findAll(anyInt(), anyInt(), anyString(), anyBoolean()))
                .thenReturn(Flux.just(cap));
        when(externalTechPort.getTechnologiesByIds(anyList())).thenReturn(Flux.just(tech));
        when(persistencePort.count()).thenReturn(Mono.just(1L));

        StepVerifier.create(capacityUseCase.getCapabilities(0, 10, "name", true))
                .assertNext(response -> {
                    assertEquals(1, response.content().size());
                    assertEquals("Java", response.content().getFirst().getTechnologies().getFirst().name());
                })
                .verifyComplete();
    }

    @Test
    void deleteCapability_DeletesTechnology_WhenNotUsedInOtherCaps() {
        Long techId = 100L;
        when(persistencePort.deleteById(CAP_ID)).thenReturn(Mono.just(List.of(techId)));
        when(persistencePort.isTechnologyUsedInOtherCapabilities(techId, CAP_ID)).thenReturn(Mono.just(false));
        when(externalTechPort.deleteTechnology(techId)).thenReturn(Mono.empty());

        StepVerifier.create(capacityUseCase.deleteCapability(CAP_ID))
                .verifyComplete();

        verify(externalTechPort).deleteTechnology(techId);
    }

    @Test
    void deleteCapability_DoesNotDeleteTechnology_WhenStillUsed() {
        Long techId = 100L;
        when(persistencePort.deleteById(CAP_ID)).thenReturn(Mono.just(List.of(techId)));
        when(persistencePort.isTechnologyUsedInOtherCapabilities(techId, CAP_ID)).thenReturn(Mono.just(true));

        StepVerifier.create(capacityUseCase.deleteCapability(CAP_ID))
                .verifyComplete();

        verify(externalTechPort, never()).deleteTechnology(anyLong());
    }

    @Test
    void verifyCapabilitiesExist_ReturnsTrue_WhenCountMatches() {
        List<Long> ids = List.of(1L, 2L);
        when(persistencePort.countByIds(anyList())).thenReturn(Mono.just(2L));

        StepVerifier.create(capacityUseCase.verifyCapabilitiesExist(ids))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void verifyCapabilitiesExist_ReturnsFalse_WhenIdsEmpty() {
        StepVerifier.create(capacityUseCase.verifyCapabilitiesExist(List.of()))
                .expectNext(false)
                .verifyComplete();
    }
}