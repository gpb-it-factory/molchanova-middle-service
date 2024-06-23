package ru.molchmd.minibank.middle.client.mock.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "client.api.users.mock")
public class UsersAccountsRepository {
    private final Map<UUID, AccountsUuidRepository> repository = new ConcurrentHashMap<>();

    @Deprecated
    private void addAccount(UUID uuidUser, String accountName, UUID uuidAccount) {
        if (!areAccountsExist(uuidUser)) {
            repository.put(uuidUser, new AccountsUuidRepository());
        }
        repository.get(uuidUser).addAccount(accountName, uuidAccount);
    }

    public AccountsUuidRepository getAccountsRepo(UUID uuidUser) {
        if (!areAccountsExist(uuidUser)) {
            repository.put(uuidUser, new AccountsUuidRepository());
        }
        return repository.get(uuidUser);
    }

    public boolean areAccountsExist(UUID uuidUser) {
        return repository.containsKey(uuidUser);
    }
}
