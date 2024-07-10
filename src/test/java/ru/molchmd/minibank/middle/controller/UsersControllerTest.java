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
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.exception.entity.UserAlreadyExistsException;
import ru.molchmd.minibank.middle.metrics.endpoint.UsersMetrics;
import ru.molchmd.minibank.middle.service.CreateUserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка контроллера /users")
@WebMvcTest(UsersController.class)
public class UsersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreateUserService createUserService;
    @MockBean
    private UsersMetrics metric;
    @Autowired
    private UsersController usersController;
    private final String url = "/api/v1/users";

    @BeforeEach
    void init() {
        Mockito.doNothing().when(metric).post();
        Mockito.doNothing().when(metric).successPost();
    }

    @DisplayName("Пользователь создан успешно")
    @Test
    void createUserSuccess() throws Exception {
        Mockito.doNothing().when(createUserService).createUser(Mockito.any(CreateUserRequest.class));
        String jsonRequest = "{\"userId\": \"8800553535\",\n\"userName\": \"tester\"}";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());
    }

    @DisplayName("Пользователь уже существует")
    @Test
    void createUserConflict() throws Exception {
        Mockito.doThrow(UserAlreadyExistsException.class).when(createUserService).createUser(Mockito.any(CreateUserRequest.class));
        String jsonRequest = "{\"userId\": \"8800553535\",\n\"userName\": \"tester\"}";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @DisplayName("Сервер backend не доступен")
    @Test
    void createUserServerNotAvailable() throws Exception {
        Mockito.doThrow(ResourceAccessException.class).when(createUserService).createUser(Mockito.any(CreateUserRequest.class));
        String jsonRequest = "{\"userId\": \"8800553535\",\n\"userName\": \"tester\"}";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isServiceUnavailable());
    }

    @DisplayName("Ошибка сервера backend")
    @Test
    void createUserUndefinedError() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(createUserService).createUser(Mockito.any(CreateUserRequest.class));
        String jsonRequest = "{\"userId\": \"8800553535\",\n\"userName\": \"tester\"}";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError());
    }
}
