package ru.molchmd.minibank.middle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateAccountRequest {
    private final String accountName;

    public CreateAccountRequest(@JsonProperty("accountName") String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }

    @Override
    public String toString() {
        return "CreateAccountRequest{" +
                "accountName='" + accountName + '\'' +
                '}';
    }
}
