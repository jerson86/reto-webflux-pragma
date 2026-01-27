package com.pragma.powerup.application.mapper;

import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.model.UserValidation;
import com.pragma.powerup.infrastructure.input.rest.dto.RegisterRequest;
import com.pragma.powerup.infrastructure.input.rest.dto.UserResponse;
import com.pragma.powerup.infrastructure.input.rest.dto.UserValidationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserRequestMapper {
    @Mapping(target = "id", ignore = true)
    User toDomain(RegisterRequest request);

    UserResponse toResponse(User user);

    UserValidationResponse toResponse(UserValidation user);
}
