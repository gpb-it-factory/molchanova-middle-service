package ru.molchmd.minibank.middle.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.client.TransferApi;
import ru.molchmd.minibank.middle.client.api.TransferRestApi;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.TransferException;

@DisplayName("Проверка сервиса по переводу средств")
public class TransferServiceTest {
    private TransferApi transferApi;
    private TransferService transferService;

    public TransferServiceTest() {
        transferApi = Mockito.mock(TransferRestApi.class);
        transferService = new TransferService(transferApi, new ObjectMapper());
    }

    @DisplayName("Успешный перевод")
    @Test
    void transferSuccess() {
        CreateTransferRequest createTransferRequest = new CreateTransferRequest("tester", "cat", "99.99");
        String accountName = "Акционный";
        var backendResponse = new ResponseEntity<String>(HttpStatus.OK);

        Mockito.when(transferApi.transferAmount(Mockito.any(CreateTransferRequest.class), Mockito.anyString(), Mockito.anyString())).thenReturn(backendResponse);

        Assertions.assertDoesNotThrow(() -> transferService.transfer(createTransferRequest.getFrom(), createTransferRequest.getTo(), createTransferRequest.getAmount()));
    }

    @DisplayName("Ошибка перевода")
    @Test
    void transferBadRequest() {
        CreateTransferRequest createTransferRequest = new CreateTransferRequest("tester", "cat", "99.99");
        String accountName = "Акционный";
        String jsonBackend = "{\"message\": \"error\",\n\"type\": \"TYPE\",\n\"code\": \"101\",\n\"traceId\": \"some-id\"}";
        var backendResponse = new ResponseEntity<String>(jsonBackend, HttpStatus.BAD_REQUEST);

        Mockito.when(transferApi.transferAmount(Mockito.any(CreateTransferRequest.class), Mockito.anyString(), Mockito.anyString())).thenReturn(backendResponse);

        Assertions.assertThrows(TransferException.class,
                () -> transferService.transfer(createTransferRequest.getFrom(), createTransferRequest.getTo(), createTransferRequest.getAmount()));
    }

    @DisplayName("Ошибка сервера backend")
    @Test
    void transferServerError() {
        CreateTransferRequest createTransferRequest = new CreateTransferRequest("tester", "cat", "99.99");
        String accountName = "Акционный";
        var backendResponse = new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(transferApi.transferAmount(Mockito.any(CreateTransferRequest.class), Mockito.anyString(), Mockito.anyString())).thenReturn(backendResponse);

        Assertions.assertThrows(InternalServerException.class,
                () -> transferService.transfer(createTransferRequest.getFrom(), createTransferRequest.getTo(), createTransferRequest.getAmount()));
    }
}
