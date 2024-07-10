package ru.molchmd.minibank.middle.client;

public interface TransferApi {
    int transferAmount(String fromUserName, String toUserName, String amount,
                                          String fromAccountName, String toAccountName);
}
