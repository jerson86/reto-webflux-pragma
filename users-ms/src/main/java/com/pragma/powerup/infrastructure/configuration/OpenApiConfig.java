package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.utils.Constants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${openapi.server.url}") String serverUrl,
            @Value("${openapi.server.description}") String serverDescription,
            @Value("${openapi.info.terms-of-service}") String termsOfService,
            @Value("${openapi.info.license.url}") String licenseUrl,
            @Value("${spring.application.name:User Microservice}") String appName) {
        
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .version("1.0.0")
                        .description("Microservicio de Usuarios para PoC WebFlux")
                        .termsOfService(termsOfService)
                        .license(new License().name("Apache 2.0").url(licenseUrl)))
                .components(new Components()
                        .addSecuritySchemes(Constants.BEARER_KEY,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .servers(List.of(new Server().url(serverUrl).description(serverDescription)));
    }
}
