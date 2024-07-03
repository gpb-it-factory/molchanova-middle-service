package ru.molchmd.minibank.middle.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.ResourceAccessException;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.dto.response.AccountFrontendResponse;
import ru.molchmd.minibank.middle.exception.entity.AccountAlreadyExistException;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.UserIsNotExistException;
import ru.molchmd.minibank.middle.metrics.endpoint.AccountsMetrics;
import ru.molchmd.minibank.middle.service.CreateAccountService;
import ru.molchmd.minibank.middle.service.GetAccountsService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка контроллера /users/{id}/accounts")
@WebMvcTest(AccountsController.class)
public class AccountsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreateAccountService createAccountService;
    @MockBean
    private GetAccountsService getAccountsService;
    @MockBean
    private AccountsMetrics metric;
    @Autowired
    private AccountsController accountsController;
    private final String url = "/api/v1/users/{id}/accounts";

    @BeforeEach
    void init() {
        Mockito.doNothing().when(metric).post();
        Mockito.doNothing().when(metric).get();
        Mockito.doNothing().when(metric).successPost();
        Mockito.doNothing().when(metric).successGet();
    }

    @DisplayName("Счет создан успешно")
    @Test
    void createAccountSuccess() throws Exception {
        Mockito.doNothing().when(createAccountService).createAccount(Mockito.anyLong(), Mockito.any(CreateAccountRequest.class));
        String jsonRequest = "{\"accountName\": \"Awesome\"}";

        mockMvc.perform(post(url, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @DisplayName("При создании счета пользователь не зарегистрирован")
    @Test
    void createAccountUserNotExist() throws Exception {
        Mockito.doThrow(UserIsNotExistException.class).when(createAccountService).createAccount(Mockito.anyLong(), Mockito.any(CreateAccountRequest.class));
        String jsonRequest = "{\"accountName\": \"Awesome\"}";

        mockMvc.perform(post(url, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Счет уже создан")
    @Test
    void createAccountConflict() throws Exception {
        Mockito.doThrow(AccountAlreadyExistException.class).when(createAccountService).createAccount(Mockito.anyLong(), Mockito.any(CreateAccountRequest.class));
        String jsonRequest = "{\"accountName\": \"Awesome\"}";

        mockMvc.perform(post(url, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @DisplayName("Успешное возращение списка счетов")
    @Test
    void getAccountsListSuccess() throws Exception {
        List<AccountFrontendResponse> accountsList = List.of(new AccountFrontendResponse("Awesome", "99.99"));
        Mockito.when(getAccountsService.getAccountsList(Mockito.anyLong())).thenReturn(accountsList);
        String jsonResponse = "[{\"accountName\": \"Awesome\",\n\"amount\": \"99.99\"}]";

        mockMvc.perform(get(url, 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResponse));
    }

    @DisplayName("Возращение списка счетов у незарегистрированного пользователя")
    @Test
    void getAccountsListWhenUserNotExist() throws Exception {
        Mockito.when(getAccountsService.getAccountsList(Mockito.anyLong())).thenThrow(UserIsNotExistException.class);

        mockMvc.perform(get(url, 1L))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Сервер backend не доступен")
    @Test
    void createAccountServerNotAvailable() throws Exception {
        Mockito.doThrow(ResourceAccessException.class).when(createAccountService).createAccount(Mockito.anyLong(), Mockito.any(CreateAccountRequest.class));
        String jsonRequest = "{\"accountName\": \"Awesome\"}";

        mockMvc.perform(post(url, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isServiceUnavailable());
    }

    @DisplayName("Ошибка сервера")
    @Test
    void createAccountServerError() throws Exception {
        Mockito.doThrow(InternalServerException.class).when(createAccountService).createAccount(Mockito.anyLong(), Mockito.any(CreateAccountRequest.class));
        Mockito.when(getAccountsService.getAccountsList(Mockito.anyLong())).thenThrow(InternalServerException.class);
        String jsonRequest = "{\"accountName\": \"Awesome\"}";

        mockMvc.perform(post(url, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError());

        mockMvc.perform(get(url, 1L))
                .andExpect(status().isInternalServerError());
    }
}
