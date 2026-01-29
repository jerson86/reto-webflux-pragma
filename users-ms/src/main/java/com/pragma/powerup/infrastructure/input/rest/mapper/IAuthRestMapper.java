package com.pragma.powerup.infrastructure.input.rest.mapper;

import com.pragma.powerup.domain.enums.Role;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.model.UserValidation;
import com.pragma.powerup.infrastructure.input.rest.dto.AuthResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.RegisterRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.UserResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.UserValidationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAuthRestMapper {

    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    User toUser(RegisterRequest request);

    UserResponse toUserResponse(User user);

    default AuthResponse toAuthResponse(String token) {
        return new AuthResponse(token);
    }

    UserValidationResponse toValidationResponse(UserValidation validation);

    @Named("mapRole")
    default Role mapRole(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
