package ru.molchmd.minibank.middle.client.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.molchmd.minibank.middle.client.TransferApi;
import ru.molchmd.minibank.middle.client.mock.repository.AccountsAmountRepository;
import ru.molchmd.minibank.middle.client.mock.repository.TelegramUserNameUuidRepository;
import ru.molchmd.minibank.middle.client.mock.repository.UsersAccountsRepository;
import ru.molchmd.minibank.middle.client.mock.util.ErrorResponseEntity;
import ru.molchmd.minibank.middle.client.mock.util.ExtendedStatus;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;
import ru.molchmd.minibank.middle.dto.response.Error;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.TransferException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "client.api.mock")
public class TransferMockApi implements TransferApi {
    private final TelegramUserNameUuidRepository telegramUserNameUuidRepository;
    private final UsersAccountsRepository usersAccountsRepository;
    private final AccountsAmountRepository accountsAmountRepository;
    private final BigDecimal maxTransferAmount;
    private final BigDecimal minTransferAmount;
    private final ObjectMapper mapper;

    public TransferMockApi(TelegramUserNameUuidRepository telegramUserNameUuidRepository,
                           UsersAccountsRepository usersAccountsRepository,
                           AccountsAmountRepository accountsAmountRepository,
                           @Value("${backend.transfers.amount.max}") String maxTransferAmount,
                           @Value("${backend.transfers.amount.min}") String minTransferAmount,
                           ObjectMapper mapper) {
        this.telegramUserNameUuidRepository = telegramUserNameUuidRepository;
        this.usersAccountsRepository = usersAccountsRepository;
        this.accountsAmountRepository = accountsAmountRepository;
        this.maxTransferAmount = new BigDecimal(maxTransferAmount);
        this.minTransferAmount = new BigDecimal(minTransferAmount);
        this.mapper = mapper;
    }

    @Override
    public int transferAmount(String fromUserName, String toUserName, String amount, String fromAccountName, String toAccountName) {
        ResponseEntity<String> response = transferAmountMockBackend(
                new CreateTransferRequest(fromUserName, toUserName, amount), fromAccountName, toAccountName
        );
        if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            String jsonResponse = toJsonResponse(response.getBody());
            throw new TransferException(fromUserName, toUserName, jsonResponse);
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new InternalServerException();
        }
        return ok();
    }

    private ResponseEntity<String> transferAmountMockBackend(CreateTransferRequest createTransferRequest,
                                                 String fromAccountName, String toAccountName) {
        String fromUserName = createTransferRequest.getFrom();
        String toUserName = createTransferRequest.getTo();
        String amount = createTransferRequest.getAmount();

        if (!telegramUserNameUuidRepository.isUserExist(fromUserName)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.SENDER_USER_NOT_EXIST);
        }
        UUID uuidFromUser = telegramUserNameUuidRepository.getUserUUID(fromUserName);

        Optional<ExtendedStatus> result = validateSenderUser(uuidFromUser, fromUserName, fromAccountName, toUserName);
        if (result.isPresent()) {
            return ErrorResponseEntity.badRequest(result.get());
        }

        UUID uuidFromAccount = usersAccountsRepository.getAccountsRepo(uuidFromUser).getAccountUUID(fromAccountName);

        BigDecimal transferAmount;
        try {
            transferAmount = new BigDecimal(amount).stripTrailingZeros();
        }
        catch (NumberFormatException exception) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.AMOUNT_CONVERT_NUMBER);
        }

        result = validateAmount(transferAmount, amount, uuidFromAccount);
        if (result.isPresent()) {
            return ErrorResponseEntity.badRequest(result.get());
        }

        if (!telegramUserNameUuidRepository.isUserExist(toUserName)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.RECIPIENT_USER_NOT_EXIST);
        }
        UUID uuidToUser = telegramUserNameUuidRepository.getUserUUID(toUserName);

        result = validateRecipientUser(uuidToUser, toUserName, toAccountName);
        if (result.isPresent()) {
            return ErrorResponseEntity.badRequest(result.get());
        }

        UUID uuidToAccount = usersAccountsRepository.getAccountsRepo(uuidToUser).getAccountUUID(toAccountName);

        createTransfer(uuidFromAccount, uuidToAccount, transferAmount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Optional<ExtendedStatus> validateSenderUser(UUID uuidFromUser, String fromUserName, String fromAccountName, String toUserName) {
        if (!usersAccountsRepository.getAccountsRepo(uuidFromUser).isAccountExist(fromAccountName)) {
            return Optional.of(ExtendedStatus.SENDER_USER_ACCOUNT_NOT_EXIST);
        }
        if (toUserName.equals(fromUserName)) {
            return Optional.of(ExtendedStatus.SENDER_RECIPIENT_SAME);
        }
        return Optional.empty();
    }

    private Optional<ExtendedStatus> validateRecipientUser(UUID uuidToUser, String toUserName, String toAccountName) {
        if (!usersAccountsRepository.getAccountsRepo(uuidToUser).isAccountExist(toAccountName)) {
            return Optional.of(ExtendedStatus.RECIPIENT_USER_ACCOUNT_NOT_EXIST);
        }
        return Optional.empty();
    }

    private Optional<ExtendedStatus> validateAmount(BigDecimal transferAmount, String amount, UUID uuidFromAccount) {
        if (transferAmount.scale() > 2) {
            return Optional.of(ExtendedStatus.AMOUNT_MORE_TWO_CHAR);
        }
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of(ExtendedStatus.AMOUNT_LESS_ZERO);
        }
        if (transferAmount.compareTo(minTransferAmount) < 0) {
            return Optional.of(ExtendedStatus.AMOUNT_LESS_ALLOWED_MIN);
        }
        if (transferAmount.compareTo(maxTransferAmount) > 0) {
            return Optional.of(ExtendedStatus.AMOUNT_HIGHER_ALLOWED_MAX);
        }

        if (accountsAmountRepository.getAmount(uuidFromAccount).compareTo(transferAmount) < 0) {
            return Optional.of(ExtendedStatus.AMOUNT_NOT_ENOUGH);
        }
        return Optional.empty();
    }

    private void createTransfer(UUID uuidFromAccount, UUID uuidToAccount, BigDecimal transferAmount) {
        BigDecimal currentFromUserAmount = accountsAmountRepository.getAmount(uuidFromAccount);
        accountsAmountRepository.setAmount(uuidFromAccount, currentFromUserAmount.subtract(transferAmount));
        BigDecimal currentToUserAmount = accountsAmountRepository.getAmount(uuidToAccount);
        accountsAmountRepository.setAmount(uuidToAccount, currentToUserAmount.add(transferAmount));
    }

    private String toJsonResponse(String body) {
        try {
            Error error = mapper.readValue(body, Error.class);
            return mapper.writeValueAsString(error);
        } catch (JsonProcessingException exception) {
            throw new InternalServerException();
        }
    }

    private int ok() {
        return 1;
    }
}
