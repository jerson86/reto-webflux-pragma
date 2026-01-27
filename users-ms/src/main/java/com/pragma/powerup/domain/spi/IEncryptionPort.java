package com.pragma.powerup.domain.spi;

public interface IEncryptionPort {
    String encode(String password);
    boolean matches(String password, String password1);
}
