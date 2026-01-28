package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {
    private final IUserPersistencePort userPersistencePort;

    @Override
    public Flux<UserDetailResponse> findAllDetailsByIds(List<Long> ids) {
        return userPersistencePort.findAllByIds(ids)
                .map(user -> new UserDetailResponse(user.getName(), user.getEmail()));
    }
}
