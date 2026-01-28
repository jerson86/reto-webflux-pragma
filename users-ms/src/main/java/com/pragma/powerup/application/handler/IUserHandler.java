package com.pragma.powerup.application.handler;

import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IUserHandler {
    Flux<UserDetailResponse> getUserDetailsBatch(List<Long> ids);
}
