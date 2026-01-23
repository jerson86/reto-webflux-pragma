package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.mapper.ITechnologyRequestMapper;
import com.pragma.powerup.domain.api.ITechnologyServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.TechnologyShortResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TechnologyHandler implements ITechnologyHandler {
    private final ITechnologyServicePort technologyServicePort;
    private final ITechnologyRequestMapper technologyRequestMapper;

    @Override
    public Mono<Void> saveTechnology(TechnologyRequest technologyRequest) {
        return Mono.just(technologyRequest)
                .map(technologyRequestMapper::toTechnology)
                .flatMap(technologyServicePort::saveTechnology);
    }

    @Override
    public Mono<Long> countByIds(List<Long> ids) {
        return technologyServicePort.verifyTechnologiesExist(ids);
    }

    @Override
    public Flux<TechnologyShortResponse> findAllByIds(List<Long> ids) {
        return technologyServicePort.findAllByIds(ids)
                .map(tech -> new TechnologyShortResponse(tech.id(), tech.name()));
    }
}
