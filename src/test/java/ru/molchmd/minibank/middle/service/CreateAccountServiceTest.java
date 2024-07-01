package ru.molchmd.minibank.middle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.client.AccountsApi;
import ru.molchmd.minibank.middle.client.api.AccountsRestApi;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.exception.entity.AccountAlreadyExistException;
import ru.molchmd.minibank.middle.exception.entity.UserIsNotExistException;

@DisplayName("Проверка сервиса по созданию счета")
public class CreateAccountServiceTest {
    private AccountsApi accountsApi;
    private CreateAccountService createAccountService;

    public CreateAccountServiceTest() {
        accountsApi = Mockito.mock(AccountsRestApi.class);
        createAccountService = new CreateAccountService(accountsApi);
    }

    @DisplayName("Успешное создание счета")
    @Test
    void createAccountSuccess() {
        Long userId = 88005553535L;
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Awesome");
        var backendResponse = new ResponseEntity<String>(HttpStatus.NO_CONTENT);

        Mockito.when(accountsApi.createAccount(userId, createAccountRequest)).thenReturn(backendResponse);

        Assertions.assertDoesNotThrow(() -> createAccountService.createAccount(userId, createAccountRequest));
    }

    @DisplayName("Создание счета, когда он уже существует")
    @Test
    void createAccountConflict() {
        Long userId = 88005553535L;
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Awesome");
        var backendResponse = new ResponseEntity<String>(HttpStatus.CONFLICT);

        Mockito.when(accountsApi.createAccount(userId, createAccountRequest)).thenReturn(backendResponse);

        Assertions.assertThrows(AccountAlreadyExistException.class,
                () -> createAccountService.createAccount(userId, createAccountRequest));
    }

    @DisplayName("Создание счета, когда пользователя не существует")
    @Test
    void createAccountWhenUserNotExist() {
        Long userId = 88005553535L;
        CreateAccountRequest createAccountRequest = new CreateAccountRequest("Awesome");
        var backendResponse = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        Mockito.when(accountsApi.createAccount(userId, createAccountRequest)).thenReturn(backendResponse);

        Assertions.assertThrows(UserIsNotExistException.class,
                () -> createAccountService.createAccount(userId, createAccountRequest));
    }
}
