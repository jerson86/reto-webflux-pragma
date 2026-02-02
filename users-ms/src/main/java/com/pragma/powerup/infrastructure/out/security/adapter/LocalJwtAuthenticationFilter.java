package com.pragma.powerup.infrastructure.out.security.adapter;

import com.pragma.powerup.domain.utils.Constants;
import com.pragma.powerup.domain.spi.ITokenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocalJwtAuthenticationFilter implements WebFilter {

        private final ITokenPort jwtService;

        @Override
        @NonNull
        public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
                return Objects
                                .requireNonNull(Mono
                                                .justOrEmpty(exchange.getRequest().getHeaders()
                                                                .getFirst(HttpHeaders.AUTHORIZATION))
                                                .filter(header -> header.startsWith(Constants.BEARER_PREFIX))
                                                .map(this::extractToken)
                                                .filter(jwtService::validateToken)
                                                .flatMap(token -> {
                                                        log.info("Token validado exitosamente en filtro local");
                                                        return chain.filter(exchange)
                                                                        .contextWrite(
                                                                                        ReactiveSecurityContextHolder
                                                                                                        .withAuthentication(
                                                                                                                        createAuthentication(
                                                                                                                                        token)));
                                                })
                                                .switchIfEmpty(chain.filter(exchange)));
        }

        private String extractToken(String header) {
                return header.substring(Constants.BEARER_PREFIX.length());
        }

        private UsernamePasswordAuthenticationToken createAuthentication(String token) {
                Long userId = jwtService.extractUserId(token);
                String roleFromToken = jwtService.extractRole(token);
                String finalRole = roleFromToken.startsWith(Constants.ROLE_PREFIX)
                                ? roleFromToken
                                : Constants.ROLE_PREFIX + roleFromToken;

                return new UsernamePasswordAuthenticationToken(
                                userId,
                                token,
                                List.of(new SimpleGrantedAuthority(finalRole)));
        }
}
