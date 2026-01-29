package com.pragma.powerup.infrastructure.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfiguration {

    @Value("${resilience.circuitbreaker.failure-rate}")
    private float failureRate;
    @Value("${resilience.circuitbreaker.wait-duration}")
    private int waitDurationInOpenState;
    @Value("${resilience.circuitbreaker.sliding-window}")
    private int slidingWindowSize;
    @Value("${resilience.retry.max-attempts}")
    private int maxAttempts;
    @Value("${resilience.retry.wait-duration}")
    private int waitDurationRetry;
    @Value("${resilience.timelimiter.timeout}")
    private int timeoutDuration;

    @Bean
    public CircuitBreaker databaseCircuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureRate)
                .waitDurationInOpenState(Duration.ofSeconds(waitDurationInOpenState))
                .slidingWindowSize(slidingWindowSize)
                .minimumNumberOfCalls(5)
                .permittedNumberOfCallsInHalfOpenState(3)
                .build();

        return CircuitBreaker.of("database", config);
    }

    @Bean
    public Retry databaseRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .waitDuration(Duration.ofMillis(waitDurationRetry))
                .retryExceptions(Exception.class)
                .build();

        return Retry.of("database", config);
    }

    @Bean
    public TimeLimiter databaseTimeLimiter() {
        TimeLimiterConfig config = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(timeoutDuration))
                .build();

        return TimeLimiter.of("database", config);
    }
}
