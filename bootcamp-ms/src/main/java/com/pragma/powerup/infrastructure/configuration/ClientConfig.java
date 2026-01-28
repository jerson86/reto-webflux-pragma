package com.pragma.powerup.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Value("${service.capabilities.url}")
    private String capabilitiesMsUrl;

    @Value("${service.user.url}")
    private String userMsUrl;

    private ExchangeFilterFunction securityFilter() {
        return (request, next) -> ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(auth -> {
                    Object credentials = auth.getCredentials();
                    String token = (credentials != null) ? credentials.toString() : "";
                    return ClientRequest.from(request)
                            .header(HttpHeaders.AUTHORIZATION, token)
                            .build();
                })
                .defaultIfEmpty(request)
                .flatMap(next::exchange);
    }

    @Bean
    @Primary
    public WebClient webClientAuth(WebClient.Builder builder) {
        return builder.baseUrl(userMsUrl).build();
    }

    @Bean
    public WebClient webClientCapabilities(WebClient.Builder builder) {
        return builder
                .baseUrl(capabilitiesMsUrl)
                .filter(securityFilter())
                .build();
    }

    @Bean
    public WebClient webClientUserBatch(WebClient.Builder builder) {
        return builder
                .baseUrl(userMsUrl)
                .filter(securityFilter())
                .build();
    }
}