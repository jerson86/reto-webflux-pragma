package com.pragma.powerup.infrastructure.out.webclient.adapter;

import com.pragma.powerup.domain.model.Capability;
import com.pragma.powerup.domain.spi.IExternalCapabilityServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ExternalCapabilityAdapter implements IExternalCapabilityServicePort {
    private final WebClient webClient;

    @Override
    public Mono<Boolean> verifyCapabilitiesExist(List<Long> ids) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/capabilities/verify")
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToMono(Boolean.class)
                .doOnError(e -> log.error("Fallo al conectar con verifyCapabilitiesExist capabilities-ms: {}", e.getMessage()))
                .onErrorReturn(false);
    }

    @Override
    public Flux<Capability> getCapabilitiesWithTechs(List<Long> ids) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/capabilities/list-by-ids")
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToFlux(Capability.class)
                .doOnError(e -> log.error("Fallo al conectar con getCapabilitiesWithTechs capabilities-ms: {}", e.getMessage()))
                .onErrorResume(e -> Flux.empty());
    }
}
