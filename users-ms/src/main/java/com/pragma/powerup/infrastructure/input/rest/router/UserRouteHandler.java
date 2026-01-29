package com.pragma.powerup.infrastructure.input.rest.router;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
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

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRouteHandler {
    private final IUserServicePort userServicePort;

    public Mono<ServerResponse> getUsersDetails(ServerRequest request) {
        return request.bodyToMono(new ParameterizedTypeReference<List<Long>>() {})
                .flatMap(ids -> RequestValidator.validateNotEmpty(ids, "Lista de IDs"))
                .flatMap(ids -> ServerResponse.ok().body(
                        ReactiveSecurityContextHolder.getContext()
                                .doOnNext(ctx -> log.info("Usuario autenticado: {} con roles: {}",
                                        ctx.getAuthentication().getPrincipal(),
                                        ctx.getAuthentication().getAuthorities()))
                                .flatMapMany(ctx -> userServicePort.findAllDetailsByIds(ids)
                                        .map(userDetail -> new UserDetailResponse(
                                                userDetail.getName(),
                                                userDetail.getEmail()
                                        ))),
                        UserDetailResponse.class
                ))
                .onErrorResume(error -> {
                    log.error("Error in UserRouteHandler: {}", error.getMessage());
                    return Mono.error(error);
                });
    }
}
