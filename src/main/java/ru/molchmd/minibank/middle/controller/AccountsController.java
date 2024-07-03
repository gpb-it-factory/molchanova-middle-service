package ru.molchmd.minibank.middle.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.molchmd.minibank.middle.dto.request.CreateAccountRequest;
import ru.molchmd.minibank.middle.dto.response.AccountFrontendResponse;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.metrics.endpoint.AccountsMetrics;
import ru.molchmd.minibank.middle.service.CreateAccountService;
import ru.molchmd.minibank.middle.service.GetAccountsService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AccountsController {
    private final CreateAccountService createAccountService;
    private final GetAccountsService getAccountsService;
    private final ObjectMapper mapper;
    private final AccountsMetrics metric;

    public AccountsController(CreateAccountService createAccountService,
                              GetAccountsService getAccountsService,
                              ObjectMapper mapper,
                              AccountsMetrics metric) {
        this.createAccountService = createAccountService;
        this.getAccountsService = getAccountsService;
        this.mapper = mapper;
        this.metric = metric;
    }

    @PostMapping("/users/{id}/accounts")
    public ResponseEntity<Void> createAccount(@PathVariable Long id,
                                           @RequestBody CreateAccountRequest createAccountRequest) {
        log.info("Request -> Received request to create account userId[{}]: {}", id, createAccountRequest);
        metric.post();
        createAccountService.createAccount(id, createAccountRequest);
        log.info("Response -> Account name[{}] userId[{}] created successfully | status {}",
                createAccountRequest.getAccountName(), id, HttpStatus.CREATED);
        metric.successPost();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}/accounts")
    public ResponseEntity<String> getCurrentBalance(@PathVariable Long id) {
        log.info("Request -> Received request to get accounts userId[{}]", id);
        metric.get();
        List<AccountFrontendResponse> accountsList = getAccountsService.getAccountsList(id);
        String jsonResponse;
        try {
            jsonResponse = mapper.writeValueAsString(accountsList);
        } catch (JsonProcessingException e) {
            throw new InternalServerException();
        }
        log.info("Response -> Accounts get successfully | status " + HttpStatus.OK);
        metric.successGet();
        return new ResponseEntity<>(jsonResponse, HttpStatus.OK);
    }
}
