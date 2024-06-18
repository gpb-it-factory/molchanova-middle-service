package ru.molchmd.minibank.middle.dto.response;

public class Error {
    private final String message;
    private final String type;
    private final String code;
    private final String traceId;

    public Error(String message, String type, String code, String traceId) {
        this.message = message;
        this.type = type;
        this.code = code;
        this.traceId = traceId;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getTraceId() {
        return traceId;
    }
}
