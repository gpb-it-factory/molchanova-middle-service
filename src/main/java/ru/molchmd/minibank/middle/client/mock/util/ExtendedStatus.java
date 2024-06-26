package ru.molchmd.minibank.middle.client.mock.util;

public enum ExtendedStatus {
    SENDER_USER_NOT_EXIST("SENDER_USER_NOT_EXIST", 100, "User is not exist"),
    USER_ALREADY_EXIST("USER_ALREADY_EXIST", 300, "User is already exist"),
    ACCOUNT_ALREADY_EXIST("ACCOUNT_ALREADY_EXIST", 301, "Account is already exist");

    public final String type;
    public final int code;
    public final String description;

    ExtendedStatus(String type, int code, String description) {
        this.type = type;
        this.code = code;
        this.description = description;
    }
}
