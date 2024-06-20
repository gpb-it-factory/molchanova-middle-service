package ru.molchmd.minibank.middle.client.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "client.api.users.mock")
public class UsersRepository {
    private final Map<Long, UUID> usersRepositoryIdUUID = new ConcurrentHashMap<>();
    private final Map<String, UUID> usersRepositoryNameUUID = new ConcurrentHashMap<>();

    public void addUser(Long telegramUserId, String userName) {
        UUID userId = UUID.randomUUID();
        usersRepositoryIdUUID.put(telegramUserId, userId);
        usersRepositoryNameUUID.put(userName, userId);
    }

    public boolean isUserExist(Long telegramUserId) {
        return usersRepositoryIdUUID.containsKey(telegramUserId);
    }

    public boolean isUserExist(String userName) {
        return usersRepositoryNameUUID.containsKey(userName);
    }

    public UUID getUserId(Long telegramUserId) {
        return usersRepositoryIdUUID.get(telegramUserId);
    }

    public UUID getUserId(String userName) {
        return usersRepositoryNameUUID.get(userName);
    }
}
