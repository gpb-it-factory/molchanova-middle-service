package ru.molchmd.minibank.middle.exception.entity;

public class AccountAlreadyExistException extends RuntimeException {
    public final Long userId;
    public final String accountName;

    public AccountAlreadyExistException(Long userId, String accountName) {
        this.userId = userId;
        this.accountName = accountName;
    }
}
