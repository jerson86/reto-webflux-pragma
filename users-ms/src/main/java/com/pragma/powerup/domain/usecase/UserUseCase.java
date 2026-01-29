package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.UserDetail;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@RequiredArgsConstructor
public class UserUseCase implements IUserServicePort {
    private final IUserPersistencePort userPersistencePort;

    @Override
    public Flux<UserDetail> findAllDetailsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Flux.error(new DomainException("La lista de IDs no puede estar vacía"));
        }

        return userPersistencePort.findAllByIds(ids)
                .map(user -> new UserDetail(user.getName(), user.getEmail()))
                .onErrorResume(e -> {
                    if (e instanceof DomainException) {
                        return Flux.error(e);
                    }
                    return Flux.error(new DomainException("Error al obtener detalles de usuarios: " + e.getMessage()));
                });
    }
}
