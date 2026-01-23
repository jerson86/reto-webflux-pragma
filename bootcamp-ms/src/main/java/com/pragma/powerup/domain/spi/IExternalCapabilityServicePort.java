package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Capability;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IExternalCapabilityServicePort {
    Mono<Boolean> verifyCapabilitiesExist(List<Long> ids);
    Flux<Capability> getCapabilitiesWithTechs(List<Long> ids);
}
