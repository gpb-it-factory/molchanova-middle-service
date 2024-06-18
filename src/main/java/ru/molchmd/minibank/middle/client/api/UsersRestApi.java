package ru.molchmd.minibank.middle.client.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.molchmd.minibank.middle.client.UsersApi;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;

@Component
@ConditionalOnProperty(value = "client.api.users.rest")
public class UsersRestApi implements UsersApi {
    private final RestTemplate rest;
    private final String create_endpoint;

    public UsersRestApi(@Value("${client.urls.endpoints.users.create}") String create_endpoint,
                        RestTemplate rest) {
        this.create_endpoint = create_endpoint;
        this.rest = rest;
    }

    @Override
    public ResponseEntity<String> createUser(CreateUserRequest createUserRequest) {
        ResponseEntity<String> response = rest.postForEntity(
                create_endpoint,
                new CreateUserRequest(createUserRequest.getUserId(), createUserRequest.getUserName()),
                String.class
        );
        return response;
    }
}