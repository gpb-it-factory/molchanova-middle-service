package ru.molchmd.minibank.middle.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountFrontendResponse {
    private final String accountName;
    private final String amount;

    public AccountFrontendResponse(@JsonProperty("accountName") String accountName,
                                   @JsonProperty("amount") String amount) {
        this.accountName = accountName;
        this.amount = amount;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountFrontendResponse that = (AccountFrontendResponse) o;
        return Objects.equals(accountName, that.accountName) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName, amount);
    }
}
