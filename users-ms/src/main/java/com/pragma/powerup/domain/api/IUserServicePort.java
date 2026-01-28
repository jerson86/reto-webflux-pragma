package com.pragma.powerup.domain.api;

import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IUserServicePort {
    Flux<UserDetailResponse> findAllDetailsByIds(List<Long> ids);
}
