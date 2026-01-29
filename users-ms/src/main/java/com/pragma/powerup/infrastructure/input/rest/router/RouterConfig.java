package com.pragma.powerup.infrastructure.input.rest.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> routes(AuthRouteHandler authHandler, UserRouteHandler userHandler) {
        return route()
                .path("/api/v1/auth", builder -> builder
                        .POST("/register", authHandler::register)
                        .POST("/login", authHandler::login)
                        .GET("/validate", authHandler::validate))
                .path("/api/v1/users", builder -> builder
                        .POST("/details-batch", userHandler::getUsersDetails))
                .build();
    }
}
