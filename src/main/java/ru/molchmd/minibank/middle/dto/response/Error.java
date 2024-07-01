package ru.molchmd.minibank.middle.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error {
    private final String type;

    public Error(@JsonProperty("type") String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
