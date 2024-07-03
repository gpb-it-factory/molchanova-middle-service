package ru.molchmd.minibank.middle.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountBackendResponse {
    private final String  accountId;
    private final String accountName;
    private final String  amount;

    public AccountBackendResponse(@JsonProperty("accountId") String accountId,
                                  @JsonProperty("accountName") String accountName,
                                  @JsonProperty("amount") String  amount) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.amount = amount;
    }

    public String  getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String  getAmount() {
        return amount;
    }
}
