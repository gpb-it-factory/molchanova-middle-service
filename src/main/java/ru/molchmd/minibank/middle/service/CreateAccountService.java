package ru.molchmd.minibank.middle.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.molchmd.minibank.middle.client.AccountsApi;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.exception.entity.AccountAlreadyExistException;
import ru.molchmd.minibank.middle.exception.entity.UserIsNotExistException;

@Service
public class CreateAccountService {
    private final AccountsApi accountsApi;

    public CreateAccountService(AccountsApi accountsApi) {
        this.accountsApi = accountsApi;
    }

    public void createAccount(Long userId, CreateAccountRequest createAccountRequest) {
        ResponseEntity<String> response = accountsApi.createAccount(userId, createAccountRequest);
        if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            throw new UserIsNotExistException(userId);
        }
        if (response.getStatusCode().isSameCodeAs(HttpStatus.CONFLICT)) {
            throw new AccountAlreadyExistException(userId, createAccountRequest.getAccountName());
        }
    }
}
