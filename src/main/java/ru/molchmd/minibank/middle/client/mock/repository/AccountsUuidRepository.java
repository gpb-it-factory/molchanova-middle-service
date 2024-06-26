package ru.molchmd.minibank.middle.client.mock.repository;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountsUuidRepository {
    private final Map<String, UUID> repository = new ConcurrentHashMap<>();

    public void addAccount(String accountName, UUID uuid) {
        repository.put(accountName, uuid);
    }

    public UUID getAccountUUID(String accountName) {
        return repository.get(accountName);
    }

    public boolean isAccountExist(String accountName) {
        return repository.containsKey(accountName);
    }

    public Set<String> getAccountsNames() {
        return repository.keySet();
    }
}
