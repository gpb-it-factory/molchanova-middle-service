package ru.molchmd.minibank.middle.client.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.molchmd.minibank.middle.client.UsersApi;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.dto.response.Error;

import java.util.UUID;

@Component
@ConditionalOnProperty(value = "client.api.users.mock")
public class UsersMockApi implements UsersApi {
    private final UsersRepository usersRepository;

    public UsersMockApi(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public ResponseEntity<String> createUser(CreateUserRequest createUserRequest) {
        if (usersRepository.isUserExist(createUserRequest.getUserId()))
            return new ResponseEntity<>(
                new Error(
                        "Users already exists",
                        "Conflict",
                        "409",
                        UUID.randomUUID().toString())
                        .toString(),
                HttpStatus.CONFLICT
            );
        usersRepository.addUser(createUserRequest.getUserId(), createUserRequest.getUserName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}