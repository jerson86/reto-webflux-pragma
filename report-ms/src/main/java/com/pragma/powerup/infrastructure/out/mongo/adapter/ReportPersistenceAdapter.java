package com.pragma.powerup.infrastructure.out.mongo.adapter;

import com.pragma.powerup.domain.model.BootcampReport;
import com.pragma.powerup.domain.spi.IReportPersistencePort;
import com.pragma.powerup.infrastructure.out.mongo.mapper.IReportMapper;
import com.pragma.powerup.infrastructure.out.mongo.repository.IBootcampReportMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReportPersistenceAdapter implements IReportPersistencePort {
    private final IBootcampReportMongoRepository repository;
    private final IReportMapper mapper;

    @Override
    public Mono<Void> save(BootcampReport domain) {
        return repository.save(mapper.toEntity(domain)).then();
    }
}
