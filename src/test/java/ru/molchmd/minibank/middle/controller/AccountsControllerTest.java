package ru.molchmd.minibank.middle.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.molchmd.minibank.middle.client.AccountsApi;
import ru.molchmd.minibank.middle.client.mock.AccountsMockApi;
import ru.molchmd.minibank.middle.client.mock.UsersMockApiV2;
import ru.molchmd.minibank.middle.client.mock.repository.AccountsAmountRepository;
import ru.molchmd.minibank.middle.client.mock.repository.TelegramUserIdUuidRepository;
import ru.molchmd.minibank.middle.client.mock.repository.TelegramUserNameUuidRepository;
import ru.molchmd.minibank.middle.client.mock.repository.UsersAccountsRepository;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.exception.entity.AccountAlreadyExistException;
import ru.molchmd.minibank.middle.exception.entity.UserIsNotExistException;
import ru.molchmd.minibank.middle.service.CreateAccountService;
import ru.molchmd.minibank.middle.service.CreateUserService;

@DisplayName("Проверка контроллера /users/{id}/accounts")
public class AccountsControllerTest {
    private final TelegramUserIdUuidRepository telegramUserIdUuidRepository = new TelegramUserIdUuidRepository();
    private final TelegramUserNameUuidRepository telegramUserNameUuidRepository = new TelegramUserNameUuidRepository();
    private final UsersController usersController = new UsersController(
            new CreateUserService(
                    new UsersMockApiV2(telegramUserIdUuidRepository, telegramUserNameUuidRepository)
            )
    );
    private final AccountsController accountsController = new AccountsController(
            new CreateAccountService(
                    new AccountsMockApi(
                            telegramUserIdUuidRepository,
                            new UsersAccountsRepository(),
                            new AccountsAmountRepository(),
                            "5000.00"
                    )
            )
    );

    @DisplayName("Проверка ответа на создание счета")
    @Test
    void createAccountTest() {
        Long userId = 88005553535L;
        CreateUserRequest createUserRequest = new CreateUserRequest(userId, "tester");
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Акционный");

        usersController.createUser(createUserRequest);
        var response = accountsController.createAccount(userId, createAccountRequest);

        Assertions.assertTrue(response.getStatusCode().isSameCodeAs(HttpStatus.CREATED));
    }

    @DisplayName("Проверка ответа на уже созданный счет")
    @Test
    void createExistAccountTest() {
        Long userId = 88005553535L;
        CreateUserRequest createUserRequest = new CreateUserRequest(userId, "tester");
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Акционный");

        usersController.createUser(createUserRequest);
        accountsController.createAccount(userId, createAccountRequest);

        Assertions.assertThrows(AccountAlreadyExistException.class,
                () -> accountsController.createAccount(userId, createAccountRequest));
    }

    @DisplayName("Проверка ответа на незарегистрированного пользователя")
    @Test
    void createAccountNotExistUserTest() {
        Long userId = 88005553535L;
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Акционный");

        Assertions.assertThrows(UserIsNotExistException.class,
                () -> accountsController.createAccount(userId, createAccountRequest));
    }
}
