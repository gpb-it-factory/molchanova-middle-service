package ru.molchmd.minibank.middle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
        transferService = new TransferService(transferApi);
    }

    @DisplayName("Успешный перевод")
    @Test
    void transferSuccess() {
        String fromUserName = "tester";
        String toUserName = "cat";
        String amount = "99.99";

        Mockito.when(transferApi.transferAmount(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(1);

        Assertions.assertDoesNotThrow(() -> transferService.transfer(fromUserName, toUserName, amount));
    }

    @DisplayName("Ошибка перевода")
    @Test
    void transferBadRequest() {
        String fromUserName = "tester";
        String toUserName = "cat";
        String amount = "99.99";

        Mockito.when(transferApi.transferAmount(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(TransferException.class);

        Assertions.assertThrows(TransferException.class,
                () -> transferService.transfer(fromUserName, toUserName, amount));
    }

    @DisplayName("Ошибка сервера backend")
    @Test
    void transferServerError() {
        String fromUserName = "tester";
        String toUserName = "cat";
        String amount = "99.99";

        Mockito.when(transferApi.transferAmount(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(InternalServerException.class);

        Assertions.assertThrows(InternalServerException.class,
                () -> transferService.transfer(fromUserName, toUserName, amount));
    }
}
