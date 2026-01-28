package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.AuthenticatedUser;
import com.pragma.powerup.infrastructure.input.rest.dto.InrolledPerson;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IExternalUserPort {
    Mono<AuthenticatedUser> getAuthenticatedUser(String token);
    Flux<InrolledPerson> getUsersDetails(List<Long> userIds);
}
