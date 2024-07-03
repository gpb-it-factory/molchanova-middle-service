package ru.molchmd.minibank.middle.client;

import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;

public interface TransferApi {
    ResponseEntity<String> transferAmount(CreateTransferRequest createTransferRequest,
                                          String fromAccountName, String toAccountName);
}
