package com.pragma.powerup.domain;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.domain.model.Capability;
import com.pragma.powerup.domain.model.Technology;
import com.pragma.powerup.domain.spi.IBootcampNotificationPort;
import com.pragma.powerup.domain.spi.IBootcampPersistencePort;
import com.pragma.powerup.domain.spi.IExternalCapabilityServicePort;
import com.pragma.powerup.domain.spi.IExternalUserPort;
import com.pragma.powerup.domain.usecase.BootcampUseCase;
import com.pragma.powerup.infrastructure.input.rest.dto.InrolledPerson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BootcampUseCaseTest {

    @Mock private IBootcampPersistencePort persistencePort;
    @Mock private IExternalCapabilityServicePort externalCapabilityPort;
    @Mock private IBootcampNotificationPort notificationPort;
    @Mock
    private IExternalUserPort externalUserPort;

    @InjectMocks
    private BootcampUseCase bootcampUseCase;

    @Test
    void saveBootcamp_Success() {
        // GIVEN
        Bootcamp bootcamp = Bootcamp.builder()
                .name("Bootcamp")
                .description("Description")
                .capabilityIds(List.of(1L))
                .build();

        Capability capDetail = new Capability(1L, "Backend", List.of(new Technology(10L, "Spring")));

        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(externalCapabilityPort.verifyCapabilitiesExist(anyList())).thenReturn(Mono.just(true));
        when(persistencePort.save(any())).thenReturn(Mono.just(bootcamp));
        when(externalCapabilityPort.getCapabilitiesWithTechs(anyList())).thenReturn(Flux.just(capDetail));

        // WHEN & THEN
        StepVerifier.create(bootcampUseCase.saveBootcamp(bootcamp))
                .verifyComplete();

        verify(notificationPort).notifyBootcampCreation(any());
        assertEquals(1, bootcamp.getTechnologyCount());
    }

    @Test
    void saveBootcamp_ThrowsException_WhenNameExists() {
        // GIVEN
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(true));

        // WHEN & THEN
        StepVerifier.create(bootcampUseCase.saveBootcamp(Bootcamp.builder().name("Bootcamp").build()))
                .expectErrorMatches(e -> e instanceof DomainException && e.getMessage().equals("El bootcamp ya existe"))
                .verify();
    }

    @Test
    void getMostSuccessfulBootcamp_Success() {
        // GIVEN
        Long bId = 1L;
        Bootcamp b = Bootcamp.builder()
                .id(bId)
                .name("Top")
                .capabilityIds(List.of(1L))
                .build();
        InrolledPerson person = new InrolledPerson("User", "user@test.com");
        Capability cap = new Capability(1L, "Cap", List.of());

        when(persistencePort.findTopBootcampId()).thenReturn(Mono.just(bId));
        when(persistencePort.findById(bId)).thenReturn(Mono.just(b));
        when(persistencePort.findPersonIdsByBootcampId(bId)).thenReturn(Flux.just(100L));
        when(externalUserPort.getUsersDetails(anyList())).thenReturn(Flux.just(person));
        when(externalCapabilityPort.getCapabilitiesWithTechs(anyList())).thenReturn(Flux.just(cap));

        // WHEN & THEN
        StepVerifier.create(bootcampUseCase.getMostSuccessfulBootcamp())
                .assertNext(res -> {
                    assertEquals("Top", res.getName());
                    assertFalse(res.getEnrolledPersons().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void getBootcamps_ReturnsEmptyPage_WhenNoDataFound() {
        // GIVEN
        int page = 0, size = 10;
        when(persistencePort.findAll(page, size, "name", true)).thenReturn(Flux.empty());

        // WHEN & THEN
        StepVerifier.create(bootcampUseCase.getBootcamps(page, size, "name", true))
                .assertNext(response -> {
                    assertTrue(response.content().isEmpty());
                    assertEquals(0, response.totalElements());
                })
                .verifyComplete();
    }

    @Test
    void deleteBootcamp_Success_DeletesExternalCapabilityWhenNotUsed() {
        // GIVEN
        Long bootcampId = 1L;
        Long capId = 50L;
        List<Long> capabilityIds = List.of(capId);

        // Al borrar el bootcamp, devuelve los IDs de sus capacidades
        when(persistencePort.deleteBootcamp(bootcampId)).thenReturn(Mono.just(capabilityIds));
        // Simulamos que la capacidad NO se usa en otros bootcamps
        when(persistencePort.isCapabilityUsedInOtherBootcamps(capId, bootcampId)).thenReturn(Mono.just(false));
        // Por lo tanto, debe llamar al borrado externo
        when(externalCapabilityPort.deleteCapability(capId)).thenReturn(Mono.empty());

        // WHEN & THEN
        StepVerifier.create(bootcampUseCase.deleteBootcamp(bootcampId))
                .verifyComplete();

        verify(externalCapabilityPort, times(1)).deleteCapability(capId);
    }

    @Test
    void deleteBootcamp_Success_DoesNotDeleteExternalWhenStillUsed() {
        // GIVEN
        Long bootcampId = 1L;
        Long capId = 50L;
        List<Long> capabilityIds = List.of(capId);

        when(persistencePort.deleteBootcamp(bootcampId)).thenReturn(Mono.just(capabilityIds));

        when(persistencePort.isCapabilityUsedInOtherBootcamps(capId, bootcampId)).thenReturn(Mono.just(true));

        // WHEN & THEN
        StepVerifier.create(bootcampUseCase.deleteBootcamp(bootcampId))
                .verifyComplete();

        verify(externalCapabilityPort, never()).deleteCapability(anyLong());
    }

    @Test
    void deleteBootcamp_Resilience_ContinuesWhenExternalDeletionFails() {
        // GIVEN
        Long bootcampId = 1L;
        Long capId = 50L;
        when(persistencePort.deleteBootcamp(bootcampId)).thenReturn(Mono.just(List.of(capId)));
        when(persistencePort.isCapabilityUsedInOtherBootcamps(capId, bootcampId)).thenReturn(Mono.just(false));

        when(externalCapabilityPort.deleteCapability(capId))
                .thenReturn(Mono.error(new RuntimeException("External Service Down")));

        // WHEN & THEN
        StepVerifier.create(bootcampUseCase.deleteBootcamp(bootcampId))
                .verifyComplete();

        verify(externalCapabilityPort).deleteCapability(capId);
    }
}