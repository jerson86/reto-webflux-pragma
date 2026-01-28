package com.pragma.powerup.application.handler;

import com.pragma.powerup.infrastructure.input.rest.dto.BootcampRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.BootcampResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.SuccessfulBootcampResponse;
import reactor.core.publisher.Mono;

public interface IBootcampHandler {
    Mono<Void> saveBootcamp(BootcampRequest request);
    Mono<PageResponse<BootcampResponse>> getBootcamps(int page, int size, String sortBy, boolean asc);
    Mono<Void> deleteBootcamp(Long id);
    Mono<SuccessfulBootcampResponse> getMostSuccessfulBootcamp();
}
