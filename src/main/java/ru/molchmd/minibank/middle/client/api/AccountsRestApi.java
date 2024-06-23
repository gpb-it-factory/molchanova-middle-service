package ru.molchmd.minibank.middle.client.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.molchmd.minibank.middle.client.AccountsApi;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;

@Component
@ConditionalOnProperty(value = "client.api.rest")
public class AccountsRestApi implements AccountsApi {
    private final RestTemplate rest;
    private final String createEndpoint;

    public AccountsRestApi(RestTemplate rest,
                           @Value("${client.urls.endpoints.accounts.create}") String createEndpoint) {
        this.rest = rest;
        this.createEndpoint = createEndpoint;
    }

    @Override
    public ResponseEntity<String> createAccount(Long userId, CreateAccountRequest createAccountRequest) {
        ResponseEntity<String> response = rest.postForEntity(
                createEndpoint,
                createAccountRequest,
                String.class,
                userId
        );
        return response;
    }
}
