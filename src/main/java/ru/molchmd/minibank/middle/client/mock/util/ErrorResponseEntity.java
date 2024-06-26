package ru.molchmd.minibank.middle.client.mock.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class ErrorResponseEntity {
    private ErrorResponseEntity() {
    }

    public static ResponseEntity<String> badRequest(ExtendedStatus status) {
        return new ResponseEntity<>(
                "{\n" +
                        "\"message\": \"" + status.description + "\",\n" +
                        "\"type\": \"" + status.type + "\",\n" +
                        "\"code\": \"" + status.code + "\",\n" +
                        "\"traceId\": \"" + UUID.randomUUID().toString() + "\"\n" +
                        "}",
                HttpStatus.BAD_REQUEST
        );
    }

    public static ResponseEntity<String> conflict(ExtendedStatus status) {
        return new ResponseEntity<>(
                "{\n" +
                        "\"message\": \"" + status.description + "\",\n" +
                        "\"type\": \"" + status.type + "\",\n" +
                        "\"code\": \"" + status.code + "\",\n" +
                        "\"traceId\": \"" + UUID.randomUUID().toString() + "\"\n" +
                        "}",
                HttpStatus.CONFLICT
        );
    }

    public static ResponseEntity<String> internalServerError(String message) {
        return new ResponseEntity<>(
                "{\n" +
                        "\"message\": \"" + message + "\",\n" +
                        "\"type\": \"" + "INTERNAL_SERVER_ERROR" + "\",\n" +
                        "\"code\": \"" + "500" + "\",\n" +
                        "\"traceId\": \"" + UUID.randomUUID().toString() + "\"\n" +
                        "}",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
