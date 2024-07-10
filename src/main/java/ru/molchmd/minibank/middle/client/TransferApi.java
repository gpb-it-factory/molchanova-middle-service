package ru.molchmd.minibank.middle.client;

import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;

public interface TransferApi {
    int transferAmount(String fromUserName, String toUserName, String amount,
                                          String fromAccountName, String toAccountName);
}
