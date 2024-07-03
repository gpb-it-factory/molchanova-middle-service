package ru.molchmd.minibank.middle.client.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.molchmd.minibank.middle.client.TransferApi;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;

@Component
@ConditionalOnProperty(value = "client.api.rest")
public class TransferRestApi implements TransferApi {
    private final RestTemplate rest;
    private final String endpoint;

    public TransferRestApi(RestTemplate rest,
                           @Value("${client.urls.endpoints.transfers.create}") String endpoint) {
        this.rest = rest;
        this.endpoint = endpoint;
    }

    @Override
    public ResponseEntity<String> transferAmount(CreateTransferRequest createTransferRequest,
                                                 String fromAccountName, String toAccountName) {
        ResponseEntity<String> response = rest.postForEntity(
                endpoint,
                createTransferRequest,
                String.class
        );
        return response;
    }
}
