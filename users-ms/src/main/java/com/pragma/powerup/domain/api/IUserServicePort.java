package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.UserDetail;
import reactor.core.publisher.Flux;

import java.util.List;

public interface IUserServicePort {
    Flux<UserDetail> findAllDetailsByIds(List<Long> ids);
}
