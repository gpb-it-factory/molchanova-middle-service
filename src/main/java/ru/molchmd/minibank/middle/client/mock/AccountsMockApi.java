package ru.molchmd.minibank.middle.client.mock;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.molchmd.minibank.middle.client.AccountsApi;
import ru.molchmd.minibank.middle.client.mock.repository.AccountsAmountRepository;
import ru.molchmd.minibank.middle.client.mock.repository.TelegramUserIdUuidRepository;
import ru.molchmd.minibank.middle.client.mock.repository.UsersAccountsRepository;
import ru.molchmd.minibank.middle.client.mock.util.ExtendedStatus;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.client.mock.util.ErrorResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "client.api.mock")
public class AccountsMockApi implements AccountsApi {
    private final TelegramUserIdUuidRepository telegramUserIdUuidRepository;
    private final UsersAccountsRepository usersAccountsRepository;
    private final AccountsAmountRepository accountsAmountRepository;
    private final BigDecimal START_AMOUNT;

    public AccountsMockApi(TelegramUserIdUuidRepository telegramUserIdUuidRepository,
                           UsersAccountsRepository usersAccountsRepository,
                           AccountsAmountRepository accountsAmountRepository,
                           @Value("${backend.accounts.amount.start}") String startAmount) {
        this.telegramUserIdUuidRepository = telegramUserIdUuidRepository;
        this.usersAccountsRepository = usersAccountsRepository;
        this.accountsAmountRepository = accountsAmountRepository;
        START_AMOUNT = new BigDecimal(startAmount);
    }

    @Override
    public ResponseEntity<String> createAccount(Long userId, CreateAccountRequest createAccountRequest) {
        if (!telegramUserIdUuidRepository.isUserExist(userId)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.SENDER_USER_NOT_EXIST);
        }
        UUID uuidUser = telegramUserIdUuidRepository.getUserUUID(userId);
        String accountName = createAccountRequest.getAccountName();
        if (usersAccountsRepository.areAccountsExist(uuidUser) &&
                usersAccountsRepository.getAccountsRepo(uuidUser).isAccountExist(accountName)) {
            return ErrorResponseEntity.conflict(ExtendedStatus.ACCOUNT_ALREADY_EXIST);
        }

        UUID uuidAccount = UUID.randomUUID();
        accountsAmountRepository.setAmount(uuidAccount, START_AMOUNT);
        usersAccountsRepository.getAccountsRepo(uuidUser).addAccount(accountName, uuidAccount);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
