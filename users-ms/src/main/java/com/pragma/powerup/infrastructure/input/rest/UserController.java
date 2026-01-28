package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.IUserHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final IUserHandler userHandler;

    @PostMapping("/details-batch")
    @PreAuthorize("hasRole('ADMIN')")
    public Flux<UserDetailResponse> getUsersDetails(@RequestBody List<Long> ids) {
        return ReactiveSecurityContextHolder.getContext()
                .doOnNext(ctx -> log.info("Usuario autenticado: {} con roles: {}",
                        ctx.getAuthentication().getPrincipal(),
                        ctx.getAuthentication().getAuthorities()))
                .flatMapMany(ctx -> userHandler.getUserDetailsBatch(ids));
    }

}
