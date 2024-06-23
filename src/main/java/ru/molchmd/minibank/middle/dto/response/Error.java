package ru.molchmd.minibank.middle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    private final String message;
    private final String type;
    private final String code;

    public Error(@JsonProperty("message") String message,
                 @JsonProperty("type") String type,
                 @JsonProperty("code") String code) {
        this.message = message;
        this.type = type;
        this.code = code;
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
}
