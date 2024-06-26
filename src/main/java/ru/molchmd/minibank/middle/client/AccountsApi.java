package ru.molchmd.minibank.middle.client;

import org.springframework.http.ResponseEntity;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;

public interface AccountsApi {
    ResponseEntity<String> createAccount(Long userId, CreateAccountRequest createAccountRequest);
}
