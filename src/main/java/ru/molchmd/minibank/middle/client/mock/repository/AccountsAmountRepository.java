package ru.molchmd.minibank.middle.client.mock.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "client.api.users.mock")
public class AccountsAmountRepository {
    private final Map<UUID, BigDecimal> repository = new ConcurrentHashMap<>();

    public void setAmount(UUID uuidAccount, BigDecimal amount) {
        repository.put(uuidAccount, amount);
    }

    public BigDecimal getAmount(UUID uuidAccount) {
        return repository.get(uuidAccount);
    }
}
