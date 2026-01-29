package com.pragma.powerup.infrastructure.input.rest.validator;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.utils.Constants;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class RequestValidator {

    public static <T> Function<T, Mono<T>> validate(Predicate<T> predicate, String errorMessage) {
        return value -> predicate.test(value)
                ? Mono.just(value)
                : Mono.error(new DomainException(errorMessage));
    }

    public static Mono<String> validateNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            return Mono.error(new DomainException(fieldName + Constants.EMPTY_FIELD));
        }
        return Mono.just(value);
    }

    public static <T> Mono<List<T>> validateNotEmpty(List<T> list, String fieldName) {
        if (list == null || list.isEmpty()) {
            return Mono.error(new DomainException(fieldName + Constants.EMPTY_FIELD_FEMALE));
        }
        return Mono.just(list);
    }
}
