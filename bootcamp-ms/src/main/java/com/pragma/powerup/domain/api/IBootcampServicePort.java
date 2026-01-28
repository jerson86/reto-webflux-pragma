package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Bootcamp;
import com.pragma.powerup.infrastructure.input.rest.dto.PageResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.SuccessfulBootcampResponse;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface IBootcampServicePort {
    Mono<Void> saveBootcamp(Bootcamp bootcamp);

    Mono<PageResponse<Bootcamp>> getBootcamps(int page, int size, String sortBy, boolean asc);

    @Transactional
    Mono<Void> deleteBootcamp(Long id);

    Mono<SuccessfulBootcampResponse> getMostSuccessfulBootcamp();
}
