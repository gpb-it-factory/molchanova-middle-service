package ru.molchmd.minibank.middle.service;

import org.springframework.stereotype.Service;
import ru.molchmd.minibank.middle.client.TransferApi;

@Service
public class TransferService {
    private final TransferApi transferApi;
    private final String generalAccountName;

    public TransferService(TransferApi transferApi) {
        this.transferApi = transferApi;
        this.generalAccountName = "Акционный";
    }

    public void transfer(String fromUserName, String toUserName, String amount) {
        transferApi.transferAmount(fromUserName, toUserName, amount, generalAccountName, generalAccountName);
    }
}
