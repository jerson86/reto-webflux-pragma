package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.handler.IBootcampHandler;
import com.pragma.powerup.application.mapper.IBootcampRequestMapper;
import com.pragma.powerup.application.mapper.IBootcampResponseMapper;
import com.pragma.powerup.domain.api.IBootcampServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.SuccessfulBootcampResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BootcampHandler implements IBootcampHandler {
    private final IBootcampServicePort bootcampServicePort;
    private final IBootcampRequestMapper mapper;
    private final IBootcampResponseMapper responseMapper;

    @Override
    public Mono<Void> saveBootcamp(BootcampRequest request) {
        return Mono.just(request)
                .map(mapper::toDomain)
                .flatMap(bootcampServicePort::saveBootcamp);
    }

    @Override
    public Mono<PageResponse<BootcampResponse>> getBootcamps(int page, int size, String sortBy, boolean asc) {
        return bootcampServicePort.getBootcamps(page, size, sortBy, asc)
                .map(pageDomain -> {
                    List<BootcampResponse> content = pageDomain.content().stream()
                            .map(responseMapper::toResponse)
                            .toList();
                    return new PageResponse<>(content, pageDomain.page(), pageDomain.size(),
                            pageDomain.totalElements(), pageDomain.totalPages());
                });
    }

    @Override
    public Mono<Void> deleteBootcamp(Long id) {
        return bootcampServicePort.deleteBootcamp(id);
    }

    @Override
    public Mono<SuccessfulBootcampResponse> getMostSuccessfulBootcamp() {
        return bootcampServicePort.getMostSuccessfulBootcamp();
    }
}
