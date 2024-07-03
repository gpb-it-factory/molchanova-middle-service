package ru.molchmd.minibank.middle.client.mock;

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

import java.math.BigDecimal;
import java.util.UUID;

@Component
@ConditionalOnProperty(value = "client.api.mock")
public class TransferMockApi implements TransferApi {
    private final TelegramUserNameUuidRepository telegramUserNameUuidRepository;
    private final UsersAccountsRepository usersAccountsRepository;
    private final AccountsAmountRepository accountsAmountRepository;
    private final BigDecimal maxTransferAmount;
    private final BigDecimal minTransferAmount;

    public TransferMockApi(TelegramUserNameUuidRepository telegramUserNameUuidRepository,
                           UsersAccountsRepository usersAccountsRepository,
                           AccountsAmountRepository accountsAmountRepository,
                           @Value("${backend.transfers.amount.max}") String maxTransferAmount,
                           @Value("${backend.transfers.amount.min}") String minTransferAmount) {
        this.telegramUserNameUuidRepository = telegramUserNameUuidRepository;
        this.usersAccountsRepository = usersAccountsRepository;
        this.accountsAmountRepository = accountsAmountRepository;
        this.maxTransferAmount = new BigDecimal(maxTransferAmount);
        this.minTransferAmount = new BigDecimal(minTransferAmount);
    }

    @Override
    public ResponseEntity<String> transferAmount(CreateTransferRequest createTransferRequest,
                                                 String fromAccountName, String toAccountName) {
        String fromUserName = createTransferRequest.getFrom();
        String toUserName = createTransferRequest.getTo();
        String amount = createTransferRequest.getAmount();

        if (!telegramUserNameUuidRepository.isUserExist(fromUserName)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.SENDER_USER_NOT_EXIST);
        }
        UUID uuidFromUser = telegramUserNameUuidRepository.getUserUUID(fromUserName);
        if (!usersAccountsRepository.getAccountsRepo(uuidFromUser).isAccountExist(fromAccountName)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.SENDER_USER_ACCOUNT_NOT_EXIST);
        }
        if (toUserName.equals(fromUserName)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.SENDER_RECIPIENT_SAME);
        }

        BigDecimal transferAmount;
        try {
            transferAmount = new BigDecimal(amount).stripTrailingZeros();
            if (transferAmount.scale() > 2) {
                return ErrorResponseEntity.badRequest(ExtendedStatus.AMOUNT_MORE_TWO_CHAR);
            }
        }
        catch (NumberFormatException exception) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.AMOUNT_CONVERT_NUMBER);
        }

        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.AMOUNT_LESS_ZERO);
        }
        if (transferAmount.compareTo(minTransferAmount) < 0) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.AMOUNT_LESS_ALLOWED_MIN);
        }
        if (transferAmount.compareTo(maxTransferAmount) > 0) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.AMOUNT_HIGHER_ALLOWED_MAX);
        }

        UUID uuidFromAccount = usersAccountsRepository.getAccountsRepo(uuidFromUser).getAccountUUID(fromAccountName);
        if (accountsAmountRepository.getAmount(uuidFromAccount).compareTo(transferAmount) < 0) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.AMOUNT_NOT_ENOUGH);
        }

        if (!telegramUserNameUuidRepository.isUserExist(toUserName)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.RECIPIENT_USER_NOT_EXIST);
        }
        UUID uuidToUser = telegramUserNameUuidRepository.getUserUUID(toUserName);
        if (!usersAccountsRepository.getAccountsRepo(uuidToUser).isAccountExist(toAccountName)) {
            return ErrorResponseEntity.badRequest(ExtendedStatus.RECIPIENT_USER_ACCOUNT_NOT_EXIST);
        }
        UUID uuidToAccount = usersAccountsRepository.getAccountsRepo(uuidToUser).getAccountUUID(toAccountName);

        createTransfer(uuidFromAccount, uuidToAccount, transferAmount);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void createTransfer(UUID uuidFromAccount, UUID uuidToAccount, BigDecimal transferAmount) {
        BigDecimal currentFromUserAmount = accountsAmountRepository.getAmount(uuidFromAccount);
        accountsAmountRepository.setAmount(uuidFromAccount, currentFromUserAmount.subtract(transferAmount));
        BigDecimal currentToUserAmount = accountsAmountRepository.getAmount(uuidToAccount);
        accountsAmountRepository.setAmount(uuidToAccount, currentToUserAmount.add(transferAmount));
    }
}
