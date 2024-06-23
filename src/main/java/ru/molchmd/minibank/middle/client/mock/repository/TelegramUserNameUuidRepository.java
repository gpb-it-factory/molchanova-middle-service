package ru.molchmd.minibank.middle.client.mock.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "client.api.users.mock")
public class TelegramUserNameUuidRepository {
    private final Map<String, UUID> repository = new ConcurrentHashMap<>();

    public void addUser(String telegramUserName, UUID uuid) {
        repository.put(telegramUserName, uuid);
    }

    public UUID getUserUUID(String telegramUserName) {
        return repository.get(telegramUserName);
    }

    public boolean isUserExist(String telegramUserName) {
        return repository.containsKey(telegramUserName);
    }
}
