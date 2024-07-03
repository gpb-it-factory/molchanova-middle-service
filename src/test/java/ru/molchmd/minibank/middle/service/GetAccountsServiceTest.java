package ru.molchmd.minibank.middle.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.client.AccountsApi;
import ru.molchmd.minibank.middle.client.api.AccountsRestApi;
import ru.molchmd.minibank.middle.dto.response.AccountFrontendResponse;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.UserIsNotExistException;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Проверка сервиса по получению списка счетов")
public class GetAccountsServiceTest {
    private AccountsApi accountsApi;
    private GetAccountsService getAccountsService;

    public GetAccountsServiceTest() {
        accountsApi = Mockito.mock(AccountsRestApi.class);
        getAccountsService = new GetAccountsService(accountsApi, new ObjectMapper());
    }

    @DisplayName("Успешное получение списка счетов")
    @Test
    void getAccountsSuccess() {
        Long userId = 88005553535L;
        String jsonBackend = "[{\"accountId\": \"mock-id\",\n\"accountName\": \"Awesome\",\n\"amount\": \"5000.00\"}]";
        var backendResponse = new ResponseEntity<String>(jsonBackend, HttpStatus.OK);
        List<AccountFrontendResponse> EXPECTED_LIST = List.of(new AccountFrontendResponse("Awesome", "5000.00"));

        Mockito.when(accountsApi.getUserAccounts(userId)).thenReturn(backendResponse);

        var accountsList = getAccountsService.getAccountsList(userId);

        Assertions.assertEquals(EXPECTED_LIST, accountsList);
    }

    @DisplayName("Успешное получение пустого списка счетов")
    @Test
    void getAccountsEmptySuccess() {
        Long userId = 88005553535L;
        String jsonBackend = "[]";
        var backendResponse = new ResponseEntity<String>(jsonBackend, HttpStatus.OK);
        List<AccountFrontendResponse> EXPECTED_LIST =  new ArrayList<>();

        Mockito.when(accountsApi.getUserAccounts(userId)).thenReturn(backendResponse);

        var accountsList = getAccountsService.getAccountsList(userId);

        Assertions.assertEquals(EXPECTED_LIST, accountsList);
    }

    @DisplayName("Получение списка счетов, когда пользователя не существует")
    @Test
    void getAccountsWhenUserNotExist() {
        Long userId = 88005553535L;
        var backendResponse = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

        Mockito.when(accountsApi.getUserAccounts(userId)).thenReturn(backendResponse);

        Assertions.assertThrows(UserIsNotExistException.class,
                () -> getAccountsService.getAccountsList(userId));
    }

    @DisplayName("Ошибка сервера backend")
    @Test
    void getAccountsWhenServerHasError() {
        Long userId = 88005553535L;
        var backendResponse = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(accountsApi.getUserAccounts(userId)).thenReturn(backendResponse);

        Assertions.assertThrows(InternalServerException.class,
                () -> getAccountsService.getAccountsList(userId));
    }
}
