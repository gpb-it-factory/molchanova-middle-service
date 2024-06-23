package ru.molchmd.minibank.middle.exception.entity;

public class UserAlreadyExistsException extends RuntimeException {
    public final Long userId;

    public UserAlreadyExistsException(Long userId) {
        this.userId = userId;
    }
}