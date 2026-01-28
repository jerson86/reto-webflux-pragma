package com.pragma.powerup.infrastructure.out.webclient.adapter;

import com.pragma.powerup.domain.model.AuthenticatedUser;
import com.pragma.powerup.domain.spi.IExternalUserPort;
import com.pragma.powerup.infrastructure.input.rest.dto.InrolledPerson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class ExternalUserServiceAdapter implements IExternalUserPort {

    private final WebClient webClientAuth;
    private final WebClient webClientBusiness;

    public ExternalUserServiceAdapter(
            @Qualifier("webClientAuth") WebClient webClientAuth,
            @Qualifier("webClientUserBatch") WebClient webClientBusiness) {
        this.webClientAuth = webClientAuth;
        this.webClientBusiness = webClientBusiness;
    }

    @Override
    public Mono<AuthenticatedUser> getAuthenticatedUser(String token) {
        log.info("Enviando a validar token: {}", token);
        return webClientAuth.get()
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
                .doOnError(e -> log.error("Fallo crítico en getAuthenticatedUser: {}", e.getMessage()));
    }

    @Override
    public Flux<InrolledPerson> getUsersDetails(List<Long> userIds) {
        return webClientBusiness.post()
                .uri("/users/details-batch")
                .bodyValue(userIds)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        Mono.error(new RuntimeException("Error consultando usuarios masivos".concat(String.valueOf(response.statusCode().value()))))
                )
                .bodyToFlux(InrolledPerson.class)
                .doOnError(e -> log.error("Error en comunicación con User-MS: {}", e.getMessage()));
    }
}
