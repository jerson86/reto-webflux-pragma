package com.pragma.powerup.infrastructure.out.security;

import com.pragma.powerup.domain.spi.IUserValidationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalJwtAuthenticationFilter implements WebFilter {

    private final IUserValidationPort userRestPort;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        return Mono.defer(() -> userRestPort.getAuthenticatedUser(header))
                .flatMap(user -> {
                    String role = user.getRole().toUpperCase().startsWith("ROLE_")
                            ? user.getRole().toUpperCase()
                            : "ROLE_" + user.getRole().toUpperCase();

                    var auth = new UsernamePasswordAuthenticationToken(
                            user.getUserId(), null, List.of(new SimpleGrantedAuthority(role)));

                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                })
                .onErrorResume(e -> {
                    if (exchange.getResponse().isCommitted()) {
                        return Mono.empty();
                    }
                    log.error("Error en validación: {}", e.getMessage());
                    return chain.filter(exchange);
                });
    }
}