package ru.molchmd.minibank.middle.client.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ConditionalOnProperty(value = "client.api.users.mock")
public class UsersRepository {
    private final Map<Long, UUID> usersRepository_Id_UUID = new ConcurrentHashMap<>();
    private final Map<String, UUID> usersRepository_Name_UUID = new ConcurrentHashMap<>();

    public void addUser(Long telegramUserId, String userName) {
        UUID userId = UUID.randomUUID();
        usersRepository_Id_UUID.put(telegramUserId, userId);
        usersRepository_Name_UUID.put(userName, userId);
    }

    public boolean isUserExist(Long telegramUserId) {
        return usersRepository_Id_UUID.containsKey(telegramUserId);
    }

    public boolean isUserExist(String userName) {
        return usersRepository_Name_UUID.containsKey(userName);
    }

    public UUID getUserId(Long telegramUserId) {
        return usersRepository_Id_UUID.get(telegramUserId);
    }

    public UUID getUserId(String userName) {
        return usersRepository_Name_UUID.get(userName);
    }
}
