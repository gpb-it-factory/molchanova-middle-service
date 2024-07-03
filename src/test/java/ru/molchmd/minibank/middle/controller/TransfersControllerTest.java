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
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.TransferException;
import ru.molchmd.minibank.middle.metrics.endpoint.TransfersMetrics;
import ru.molchmd.minibank.middle.service.TransferService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Проверка контроллера /transfers")
@WebMvcTest(TransfersController.class)
public class TransfersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransferService transferService;
    @MockBean
    private TransfersMetrics metric;
    @Autowired
    private TransfersController transfersController;
    private final String url = "/api/v1/transfers";

    @BeforeEach
    void init() {
        Mockito.doNothing().when(metric).post();
        Mockito.doNothing().when(metric).successPost();
    }

    @DisplayName("Успешный перевод средств")
    @Test
    void transferSuccess() throws Exception {
        Mockito.doNothing().when(transferService).transfer(Mockito.any(CreateTransferRequest.class));
        String jsonRequest = "{\"from\": \"tester\",\n\"to\": \"cat\",\n\"amount\": \"99.99\"}";

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @DisplayName("Ошибка перевода")
    @Test
    void transferError() throws Exception {
        String jsonRequest = "{\"from\": \"tester\",\n\"to\": \"cat\",\n\"amount\": \"99.99\"}";
        String jsonResponse= "{\"type\": \"TYPE\"}";
        Mockito.doThrow(new TransferException("tester", "cat", jsonResponse)).when(transferService).transfer(Mockito.any(CreateTransferRequest.class));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(jsonResponse));
    }

    @DisplayName("Сервер backend не доступен")
    @Test
    void transferServerNotAvailable() throws Exception {
        String jsonRequest = "{\"from\": \"tester\",\n\"to\": \"cat\",\n\"amount\": \"99.99\"}";
        Mockito.doThrow(ResourceAccessException.class).when(transferService).transfer(Mockito.any(CreateTransferRequest.class));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isServiceUnavailable());
    }

    @DisplayName("Ошибка сервера")
    @Test
    void transferServerError() throws Exception {
        String jsonRequest = "{\"from\": \"tester\",\n\"to\": \"cat\",\n\"amount\": \"99.99\"}";
        Mockito.doThrow(InternalServerException.class).when(transferService).transfer(Mockito.any(CreateTransferRequest.class));

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isInternalServerError());
    }
}
