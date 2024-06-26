package ru.molchmd.minibank.middle.exception.entity;

public class UserIsNotExistException extends RuntimeException {
    public final Long userId;

    public UserIsNotExistException(Long userId) {
        this.userId = userId;
    }
}