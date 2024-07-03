package ru.molchmd.minibank.middle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.client.UsersApi;
import ru.molchmd.minibank.middle.client.api.UsersRestApi;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.exception.entity.UserAlreadyExistsException;

@DisplayName("Проверка сервиса по созданию пользователя")
public class CreateUserServiceTest {
    private UsersApi usersApi;
    private CreateUserService createUserService;

    public CreateUserServiceTest() {
        usersApi = Mockito.mock(UsersRestApi.class);
        createUserService = new CreateUserService(usersApi);
    }

    @DisplayName("Успешное создание пользователя")
    @Test
    void createUserSuccess() {
        CreateUserRequest createUserRequest = new CreateUserRequest(100L, "tester");
        var backendResponse = new ResponseEntity<String>(HttpStatus.NO_CONTENT);

        Mockito.when(usersApi.createUser(createUserRequest)).thenReturn(backendResponse);

        Assertions.assertDoesNotThrow(() -> createUserService.createUser(createUserRequest));
    }

    @DisplayName("Создание пользователя, когда он уже существует")
    @Test
    void createUserConflict() {
        CreateUserRequest createUserRequest = new CreateUserRequest(100L, "tester");
        var backendResponse = new ResponseEntity<String>(HttpStatus.CONFLICT);

        Mockito.when(usersApi.createUser(createUserRequest)).thenReturn(backendResponse);

        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> createUserService.createUser(createUserRequest));
    }
}
