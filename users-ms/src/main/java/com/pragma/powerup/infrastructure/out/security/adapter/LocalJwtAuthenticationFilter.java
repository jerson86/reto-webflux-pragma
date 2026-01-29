package com.pragma.powerup.infrastructure.out.security.adapter;

import com.pragma.powerup.domain.utils.Constants;
import com.pragma.powerup.domain.spi.ITokenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocalJwtAuthenticationFilter implements WebFilter {

    private final ITokenPort jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Token recibido en filtro local: {}", header);
        if (header == null || !header.startsWith(Constants.BEARER_PREFIX)) {
            return chain.filter(exchange);
        }

        String token = header.startsWith(Constants.BEARER_PREFIX) ? header.substring(7) : header;

        try {
            if (jwtService.validateToken(token)) {
                log.info("Token validado exitosamente en filtro local");
                Long userId = jwtService.extractUserId(token);
                String roleFromToken = jwtService.extractRole(token);
                String finalRole = roleFromToken.startsWith(Constants.ROLE_PREFIX) ? roleFromToken : Constants.ROLE_PREFIX + roleFromToken;

                var auth = new UsernamePasswordAuthenticationToken(
                        userId,
                        token,
                        List.of(new SimpleGrantedAuthority(finalRole))
                );

                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
            }
            log.error("Token NO válido para el JwtProvider local. ¿La secret key es la misma?");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return chain.filter(exchange);
    }
}
