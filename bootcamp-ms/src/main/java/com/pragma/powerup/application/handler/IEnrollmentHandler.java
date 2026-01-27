package com.pragma.powerup.application.handler;

import reactor.core.publisher.Mono;

import java.util.List;

public interface IEnrollmentHandler {
    Mono<Void> enrollPerson(Long aLong, List<Long> longs);
}
