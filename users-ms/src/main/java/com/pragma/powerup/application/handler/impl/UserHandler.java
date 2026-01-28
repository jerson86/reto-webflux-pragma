package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.handler.IUserHandler;
import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserHandler implements IUserHandler {
    private final IUserServicePort servicePort;

    @Override
    public Flux<UserDetailResponse> getUserDetailsBatch(List<Long> ids) {
        return servicePort.findAllDetailsByIds(ids);
    }
}
