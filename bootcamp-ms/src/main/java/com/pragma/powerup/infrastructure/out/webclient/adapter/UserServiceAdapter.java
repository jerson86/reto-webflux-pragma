package com.pragma.powerup.infrastructure.out.webclient.adapter;

import com.pragma.powerup.domain.model.AuthenticatedUser;
import com.pragma.powerup.domain.spi.IUserValidationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class UserServiceAdapter implements IUserValidationPort {

    private final WebClient webClient;

    public UserServiceAdapter(WebClient.Builder builder,
                              @Value("${service.user.url}") String baseUrl) {
        this.webClient = builder.baseUrl(baseUrl).build();
    }

    @Override
    public Mono<AuthenticatedUser> getAuthenticatedUser(String token) {
        log.info("Enviando a validar token: {}", token);
        return webClient.get()
                .uri("/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse ->
                        clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("El user-ms devolvió error: {}", errorBody);
                                    return Mono.error(new RuntimeException("Error en user-ms: " + errorBody));
                                })
                )
                .bodyToMono(AuthenticatedUser.class)
                .doOnError(e -> log.error("Fallo crítico en WebClient: {}", e.getMessage()));
    }
}
