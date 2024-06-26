package ru.molchmd.minibank.middle.client.mock.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "client.api.mock")
public class TelegramUserIdUuidRepository {
    private final Map<Long, UUID> repository = new ConcurrentHashMap<>();

    public void addUser(Long telegramUserId, UUID uuid) {
        repository.put(telegramUserId, uuid);
    }

    public UUID getUserUUID(Long telegramUserId) {
        return repository.get(telegramUserId);
    }

    public boolean isUserExist(Long telegramUserId) {
        return repository.containsKey(telegramUserId);
    }
}
