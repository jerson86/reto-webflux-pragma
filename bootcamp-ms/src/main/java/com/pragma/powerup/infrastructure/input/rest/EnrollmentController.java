package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.handler.IEnrollmentHandler;
import com.pragma.powerup.infrastructure.input.rest.dto.EnrollmentRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final IEnrollmentHandler enrollmentHandler;

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public Mono<ResponseEntity<Void>> enroll(@Valid @RequestBody EnrollmentRequest request, Authentication authentication) {
        Long personId = (Long) authentication.getPrincipal();
        return enrollmentHandler.enrollPerson(personId, request.bootcampIds())
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.CREATED)));
    }
}