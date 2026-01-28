package com.pragma.powerup.domain;

import com.pragma.powerup.domain.enums.Role;
import com.pragma.powerup.domain.model.User;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.usecase.UserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    void findAllDetailsByIds_Success() {
        List<Long> ids = List.of(1L, 2L);
        User user1 = new User(1L, "User One", "one@mail.com", "pass", Role.CLIENT);
        User user2 = new User(2L, "User Two", "two@mail.com", "pass", Role.CLIENT);

        when(userPersistencePort.findAllByIds(ids)).thenReturn(Flux.just(user1, user2));

        StepVerifier.create(userUseCase.findAllDetailsByIds(ids))
                .assertNext(response -> {
                    assertEquals("User One", response.getName());
                    assertEquals("one@mail.com", response.getEmail());
                })
                .assertNext(response -> {
                    assertEquals("User Two", response.getName());
                    assertEquals("two@mail.com", response.getEmail());
                })
                .verifyComplete();
    }

    @Test
    void findAllDetailsByIds_EmptyResult() {
        when(userPersistencePort.findAllByIds(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(userUseCase.findAllDetailsByIds(List.of(99L)))
                .verifyComplete();
    }
}
