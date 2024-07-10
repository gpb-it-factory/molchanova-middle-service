package ru.molchmd.minibank.middle.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateTransferRequest {
    private final String from;
    private final String to;
    private final String amount;

    public CreateTransferRequest(@JsonProperty("from") String from,
                                 @JsonProperty("to") String to,
                                 @JsonProperty("amount") String amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "CreateTransferRequest{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }
}
