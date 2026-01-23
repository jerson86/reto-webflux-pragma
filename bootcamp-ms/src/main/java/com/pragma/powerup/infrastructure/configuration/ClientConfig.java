package com.pragma.powerup.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Value("${service.capabilities.url}")
    private String capabilitiesMsUrl;

    @Bean
    public WebClient webClientCapabilities() {
        return WebClient.builder()
                .baseUrl(capabilitiesMsUrl)
                .build();
    }
}
