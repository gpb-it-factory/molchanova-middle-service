package ru.molchmd.minibank.middle.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.molchmd.minibank.middle.client.mock.UsersMockApi;
import ru.molchmd.minibank.middle.client.mock.UsersRepository;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.exception.entity.UserAlreadyExistsException;
import ru.molchmd.minibank.middle.service.UsersService;

@DisplayName("Проверка контроллера /users")
public class UsersControllerTest {
    private final UsersController usersController = new UsersController(
            new UsersService(
                    new UsersMockApi(new UsersRepository())
            )
    );

    @DisplayName("Проверка ответа на создание пользователя")
    @Test
    void createUserTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest(88005553535L, "tester");

        var response = usersController.createUser(createUserRequest);

        Assertions.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    }

    @DisplayName("Проверка ответа на уже зарегистрированного пользователя")
    @Test
    void createConflictUserTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest(88005553535L, "tester");
        usersController.createUser(createUserRequest);

        Assertions.assertThrows(UserAlreadyExistsException.class,
                () -> usersController.createUser(createUserRequest));
    }
}
