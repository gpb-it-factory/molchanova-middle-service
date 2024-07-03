package ru.molchmd.minibank.middle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.molchmd.minibank.middle.client.TransferApi;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;
import ru.molchmd.minibank.middle.dto.response.Error;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.TransferException;

@Service
public class TransferService {
    private final TransferApi transferApi;
    private final ObjectMapper mapper;
    private final String generalAccountName;

    public TransferService(TransferApi transferApi, ObjectMapper mapper) {
        this.transferApi = transferApi;
        this.mapper = mapper;
        this.generalAccountName = "Акционный";
    }

    public void transfer(CreateTransferRequest createTransferRequest) {
        String fromUserName = createTransferRequest.getFrom();
        String toUserName = createTransferRequest.getTo();

        ResponseEntity<String> response = transferApi.transferAmount(
                createTransferRequest, generalAccountName, generalAccountName
        );
        if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            String jsonResponse = toJsonResponse(response.getBody());
            throw new TransferException(fromUserName, toUserName, jsonResponse);
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new InternalServerException();
        }
    }

    private String toJsonResponse(String body) {
        try {
            Error error = mapper.readValue(body, Error.class);
            return mapper.writeValueAsString(error);
        } catch (JsonProcessingException exception) {
            throw new InternalServerException();
        }
    }
}
