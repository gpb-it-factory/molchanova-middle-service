package ru.molchmd.minibank.middle.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.molchmd.minibank.middle.client.UsersApi;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.exception.entity.UserAlreadyExistsException;

@Service
public class UsersService {
    private final UsersApi usersApi;

    public UsersService(UsersApi usersApi) {
        this.usersApi = usersApi;
    }

    public void createUser(CreateUserRequest createUserRequest) {
        ResponseEntity<String> response = usersApi.createUser(createUserRequest);
        if (response.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT))
            throw new UserAlreadyExistsException();
    }
}
