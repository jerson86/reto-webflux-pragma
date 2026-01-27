package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.handler.IEnrollmentHandler;
import com.pragma.powerup.domain.api.IEnrollmentServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentHandler implements IEnrollmentHandler {
    private final IEnrollmentServicePort enrollmentService;

    @Override
    public Mono<Void> enrollPerson(Long aLong, List<Long> longs) {
        return enrollmentService.enrollPerson(aLong, longs);
    }
}
