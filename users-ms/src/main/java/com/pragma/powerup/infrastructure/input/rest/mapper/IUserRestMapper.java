package com.pragma.powerup.infrastructure.input.rest.mapper;

import com.pragma.powerup.domain.model.UserDetail;
import com.pragma.powerup.infrastructure.input.rest.dto.UserDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRestMapper {

    UserDetailResponse toUserDetailResponse(UserDetail userDetail);
}
