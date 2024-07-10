package ru.molchmd.minibank.middle.client.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.molchmd.minibank.middle.client.TransferApi;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;
import ru.molchmd.minibank.middle.dto.response.Error;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.TransferException;

@Component
@ConditionalOnProperty(value = "client.api.rest")
public class TransferRestApi implements TransferApi {
    private final RestTemplate rest;
    private final String endpoint;
    private final ObjectMapper mapper;

    public TransferRestApi(RestTemplate rest,
                           @Value("${client.urls.endpoints.transfers.create}") String endpoint,
                           ObjectMapper mapper) {
        this.rest = rest;
        this.endpoint = endpoint;
        this.mapper = mapper;
    }

    @Override
    public int transferAmount(String fromUserName, String toUserName, String amount,
                                                 String fromAccountName, String toAccountName) {
        ResponseEntity<String> response = rest.postForEntity(
                endpoint,
                new CreateTransferRequest(fromAccountName, toUserName, amount),
                String.class
        );
        if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            String jsonResponse = toJsonResponse(response.getBody());
            throw new TransferException(fromUserName, toUserName, jsonResponse);
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new InternalServerException();
        }
        return ok();
    }

    private String toJsonResponse(String body) {
        try {
            Error error = mapper.readValue(body, Error.class);
            return mapper.writeValueAsString(error);
        } catch (JsonProcessingException exception) {
            throw new InternalServerException();
        }
    }

    private int ok() {
        return 1;
    }
}
