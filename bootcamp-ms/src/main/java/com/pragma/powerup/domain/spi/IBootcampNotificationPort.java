package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Bootcamp;

public interface IBootcampNotificationPort {
    void notifyBootcampCreation(Bootcamp bootcamp);
}
