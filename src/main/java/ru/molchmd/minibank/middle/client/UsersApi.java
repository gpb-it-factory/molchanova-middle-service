package ru.molchmd.minibank.middle.client;

import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;

public interface UsersApi {
    ResponseEntity<String> createUser(CreateUserRequest createUserRequest);
}
