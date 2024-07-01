package ru.molchmd.minibank.middle.client.mock.util;

public enum ExtendedStatus {
    USER_NOT_EXIST("USER_NOT_EXIST", 100, "User is not exist"),
    USER_ALREADY_EXIST("USER_ALREADY_EXIST", 101, "User is already exist"),
    ACCOUNT_ALREADY_EXIST("ACCOUNT_ALREADY_EXIST", 201, "Account is already exist"),
    SENDER_USER_NOT_EXIST("SENDER_USER_NOT_EXIST", 300, "Sender user is not exist"),
    SENDER_USER_ACCOUNT_NOT_EXIST("SENDER_USER_ACCOUNT_NOT_EXIST", 301, "Sender users account is not exist"),
    SENDER_RECIPIENT_SAME("SENDER_RECIPIENT_SAME", 302, "Sender and recipient user are same user"),
    RECIPIENT_USER_NOT_EXIST("RECIPIENT_USER_NOT_EXIST", 400, "Recipient user is not exist"),
    RECIPIENT_USER_ACCOUNT_NOT_EXIST("RECIPIENT_USER_ACCOUNT_NOT_EXIST", 401, "Recipient users account is not exist"),
    AMOUNT_CONVERT_NUMBER("AMOUNT_CONVERT_NUMBER", 500, "Failed to convert to a number"),
    AMOUNT_MORE_TWO_CHAR("AMOUNT_MORE_TWO_CHAR", 501, "More than 2 characters in the number after the dot"),
    AMOUNT_LESS_ZERO("AMOUNT_LESS_ZERO", 502, "Amount is less than zero"),
    AMOUNT_LESS_ALLOWED_MIN("AMOUNT_LESS_ALLOWED_MIN", 503, "Amount is less than allowed min"),
    AMOUNT_HIGHER_ALLOWED_MAX("AMOUNT_HIGHER_ALLOWED_MAX", 504, "Amount is higher than allowed max"),
    AMOUNT_NOT_ENOUGH("AMOUNT_NOT_ENOUGH", 505, "Amount in account is not enough");

    public final String type;
    public final int code;
    public final String description;

    ExtendedStatus(String type, int code, String description) {
        this.type = type;
        this.code = code;
        this.description = description;
    }
}
