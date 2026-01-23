package com.pragma.powerup.infrastructure.out.webclient.adapter;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.TechnologyShort;
import com.pragma.powerup.domain.spi.IExternalTechnologyServicePort;
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
public class ExternalTechnologyClientAdapter implements IExternalTechnologyServicePort {

    private final WebClient webClientTechnologies;

    @Override
    public Mono<Long> allTechnologiesExist(List<Long> ids) {
        return webClientTechnologies.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/verify")
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToMono(Long.class)
                .doOnSuccess(responseIds -> log.info(responseIds.toString()))
                .doOnError(error -> log.info("Error technologies-ms {}", error.getMessage()))
                .onErrorResume(e -> Mono.error(new DomainException("Error al conectar con el servicio de tecnologías")));
    }

    @Override
    public Flux<TechnologyShort> getTechnologiesByIds(List<Long> ids) {
        return webClientTechnologies.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/list-by-ids")
                        .queryParam("ids", ids)
                        .build())
                .retrieve()
                .bodyToFlux(TechnologyShort.class)
                .doOnError(e -> log.error("Fallo al conectar con technologies-ms: {}", e.getMessage()))
                .onErrorResume(e -> Flux.empty());
    }
}
