package com.pragma.powerup.infrastructure.out.r2dbc.adapter;

import com.pragma.powerup.domain.spi.IEnrollmentPersistencePort;
import com.pragma.powerup.infrastructure.out.r2dbc.entity.PersonBootcampEntity;
import com.pragma.powerup.infrastructure.out.r2dbc.repository.IPersonBootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EnrollmentR2dbcAdapter implements IEnrollmentPersistencePort {

    private final IPersonBootcampRepository personBootcampRepository;

    @Override
    public Mono<Void> saveAll(Long personId, List<Long> bootcampIds) {
        return Flux.fromIterable(bootcampIds)
                .map(bootcampId -> PersonBootcampEntity.builder()
                        .personId(personId)
                        .bootcampId(bootcampId)
                        .build())
                .collectList()
                .flatMapMany(personBootcampRepository::saveAll)
                .then();
    }
}
