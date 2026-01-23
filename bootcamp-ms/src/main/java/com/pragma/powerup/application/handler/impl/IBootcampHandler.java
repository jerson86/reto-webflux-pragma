package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.infrastructure.input.rest.dto.BootcampRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import reactor.core.publisher.Mono;

public interface IBootcampHandler {
    Mono<Void> saveBootcamp(BootcampRequest request);

    Mono<PageResponse<BootcampResponse>> getBootcamps(int page, int size, String sortBy, boolean asc);
}
