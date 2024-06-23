package ru.molchmd.minibank.middle.client.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.molchmd.minibank.middle.client.UsersApi;
import ru.molchmd.minibank.middle.client.mock.repository.TelegramUserIdUuidRepository;
import ru.molchmd.minibank.middle.client.mock.repository.TelegramUserNameUuidRepository;
import ru.molchmd.minibank.middle.client.mock.util.ExtendedStatus;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.client.mock.util.ErrorResponseEntity;

import java.util.UUID;

@Component
@ConditionalOnProperty(value = "client.api.users.mock")
public class UsersMockApiV2 implements UsersApi {
    private final TelegramUserIdUuidRepository telegramUserIdUUIDRepository;
    private final TelegramUserNameUuidRepository telegramUserNameUUIDRepository;

    public UsersMockApiV2(TelegramUserIdUuidRepository telegramUserIdUUIDRepository,
                          TelegramUserNameUuidRepository telegramUserNameUUIDRepository) {
        this.telegramUserIdUUIDRepository = telegramUserIdUUIDRepository;
        this.telegramUserNameUUIDRepository = telegramUserNameUUIDRepository;
    }

    @Override
    public ResponseEntity<String> createUser(CreateUserRequest createUserRequest) {
        Long userId = createUserRequest.getUserId();
        String userName = createUserRequest.getUserName();

        if (telegramUserIdUUIDRepository.isUserExist(userId)) {
            return ErrorResponseEntity.conflict(ExtendedStatus.USER_ALREADY_EXIST);
        }

        UUID uuidUser = UUID.randomUUID();
        telegramUserIdUUIDRepository.addUser(userId, uuidUser);
        telegramUserNameUUIDRepository.addUser(userName, uuidUser);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
