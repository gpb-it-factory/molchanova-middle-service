package ru.molchmd.minibank.middle.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.service.CreateAccountService;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AccountsController {
    private final CreateAccountService createAccountService;

    public AccountsController(CreateAccountService createAccountService) {
        this.createAccountService = createAccountService;
    }

    @PostMapping("/users/{id}/accounts")
    public ResponseEntity<Void> createAccount(@PathVariable Long id,
                                           @RequestBody CreateAccountRequest createAccountRequest) {
        log.info("Request -> Received request to create account userId[{}]: {}", id, createAccountRequest);
        createAccountService.createAccount(id, createAccountRequest);
        log.info("Response -> Account name[{}] userId[{}] created successfully | status {}",
                createAccountRequest.getAccountName(), id, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
