package ru.molchmd.minibank.middle.client.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.molchmd.minibank.middle.dto.response.AccountBackendResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "client.api.mock")
public class AccountsMockApi implements AccountsApi {
    private final TelegramUserIdUuidRepository telegramUserIdUuidRepository;
    private final UsersAccountsRepository usersAccountsRepository;
    private final AccountsAmountRepository accountsAmountRepository;
    private final BigDecimal START_AMOUNT;
    private final ObjectMapper mapper;

    public AccountsMockApi(TelegramUserIdUuidRepository telegramUserIdUuidRepository,
                           UsersAccountsRepository usersAccountsRepository,
                           AccountsAmountRepository accountsAmountRepository,
                           @Value("${backend.accounts.amount.start}") String startAmount,
                           ObjectMapper mapper) {
        this.telegramUserIdUuidRepository = telegramUserIdUuidRepository;
        this.usersAccountsRepository = usersAccountsRepository;
        this.accountsAmountRepository = accountsAmountRepository;
        START_AMOUNT = new BigDecimal(startAmount).setScale(2);
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<String> createAccount(Long userId, CreateAccountRequest createAccountRequest) {
        if (!telegramUserIdUuidRepository.isUserExist(userId)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.USER_NOT_EXIST);
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

    @Override
    public ResponseEntity<String> getUserAccounts(Long userId) {
        if (!telegramUserIdUuidRepository.isUserExist(userId)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.USER_NOT_EXIST);
        }
        UUID uuidUser = telegramUserIdUuidRepository.getUserUUID(userId);
        var userAccounts = usersAccountsRepository.getAccountsRepo(uuidUser);
        Set<String> userAccountsNames = userAccounts.getAccountsNames();

        List<AccountBackendResponse> userAccountsList = new ArrayList<>();
        if (userAccountsNames.isEmpty()) {
            return toJsonResponse(userAccountsList);
        }

        for (var userAccountName : userAccountsNames) {
            userAccountsList.add(new AccountBackendResponse(
                    userAccounts.getAccountUUID(userAccountName).toString(),
                    userAccountName,
                    accountsAmountRepository.getAmount(userAccounts.getAccountUUID(userAccountName)).toString()
            ));
        }
        return toJsonResponse(userAccountsList);
    }

    private ResponseEntity<String> toJsonResponse(List<AccountBackendResponse> userAccountsList) {
        try {
            return new ResponseEntity<>(
                    mapper.writeValueAsString(userAccountsList),
                    HttpStatus.OK);
        }
        catch (JsonProcessingException exception) {
            return ErrorResponseEntity.internalServerError("Something went wrong");
        }
    }
}
