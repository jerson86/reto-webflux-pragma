package com.pragma.powerup.infrastructure.input.rest.router;

import com.pragma.powerup.domain.utils.Constants;
import com.pragma.powerup.infrastructure.exceptionhandler.ErrorResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {
    @Bean
    @RouterOperations({
            @RouterOperation(path = "/api/v1/auth/register", beanClass = AuthRouteHandler.class, beanMethod = "register",
                    operation = @Operation(operationId = "registerUser", summary = "Registrar un nuevo usuario", tags = {Constants.AUTH_TAG},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RegisterRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
                                            content = @Content(schema = @Schema(implementation = UserResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                                    @ApiResponse(responseCode = "409", description = "El usuario ya existe",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                            })),
            @RouterOperation(path = "/api/v1/auth/login", beanClass = AuthRouteHandler.class, beanMethod = "login",
                    operation = @Operation(operationId = "login", summary = "Iniciar sesión", tags = {Constants.AUTH_TAG},
                            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = LoginRequest.class))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Login exitoso",
                                            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                                    @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                            })),
            @RouterOperation(path = "/api/v1/auth/validate", beanClass = AuthRouteHandler.class, beanMethod = "validate",
                    operation = @Operation(operationId = "validateToken", summary = "Validar token JWT", tags = {Constants.AUTH_TAG},
                            security = @SecurityRequirement(name = Constants.BEARER_KEY),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Token válido",
                                            content = @Content(schema = @Schema(implementation = UserValidationResponse.class))),
                                    @ApiResponse(responseCode = "401", description = "Token inválido o expirado",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                            })),
            @RouterOperation(path = "/api/v1/users/details-batch", beanClass = UserRouteHandler.class, beanMethod = "getUsersDetails",
                    operation = @Operation(operationId = "getUsersDetails", summary = "Obtener detalles de usuarios por lote de IDs", tags = {Constants.USER_TAG},
                            security = @SecurityRequirement(name = Constants.BEARER_KEY),
                            requestBody = @RequestBody(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Detalles de usuarios obtenidos exitosamente",
                                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDetailResponse.class)))),
                                    @ApiResponse(responseCode = "400", description = "Lista de IDs vacía",
                                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                            }))
    })
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
