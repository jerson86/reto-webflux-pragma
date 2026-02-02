package com.pragma.powerup.infrastructure.input.rest.router;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
import com.pragma.powerup.infrastructure.input.rest.mapper.IUserRestMapper;
import com.pragma.powerup.infrastructure.input.rest.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRouteHandler {
        private final IUserServicePort userServicePort;
        private final IUserRestMapper userRestMapper;

        public Mono<ServerResponse> getUsersDetails(ServerRequest request) {
                return request.bodyToMono(new ParameterizedTypeReference<List<Long>>() {
                })
                                .flatMap(ids -> RequestValidator.validateNotEmpty(ids, "Lista de IDs"))
                                .flatMap(ids -> {
                                        var userDetails = ReactiveSecurityContextHolder.getContext()
                                                        .doOnNext(ctx -> log.info(
                                                                        "Usuario autenticado: {} con roles: {}",
                                                                        ctx.getAuthentication().getPrincipal(),
                                                                        ctx.getAuthentication().getAuthorities()))
                                                        .flatMapMany(ctx -> userServicePort.findAllDetailsByIds(ids)
                                                                        .map(userRestMapper::toUserDetailResponse));

                                        return ServerResponse.ok().body(Objects.requireNonNull(userDetails),
                                                        UserDetailResponse.class);
                                })
                                .onErrorResume(error -> {
                                        log.error("Error in UserRouteHandler: {}", error.getMessage());
                                        return Mono.error(error);
                                });
        }
}
