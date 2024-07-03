package ru.molchmd.minibank.middle.exception.entity;

public class TransferException extends RuntimeException {
    public final String fromUserName;
    public final String toUserName;
    public final String jsonError;

    public TransferException( String fromUserName, String toUserName, String jsonError) {
        this.fromUserName = fromUserName;
        this.toUserName = toUserName;
        this.jsonError = jsonError;
    }
}
