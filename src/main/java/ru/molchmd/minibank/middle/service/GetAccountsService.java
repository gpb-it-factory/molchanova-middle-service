package ru.molchmd.minibank.middle.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.molchmd.minibank.middle.client.AccountsApi;
import ru.molchmd.minibank.middle.dto.response.AccountFrontendResponse;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.UserIsNotExistException;

import java.util.List;

@Service
public class GetAccountsService {
    private final AccountsApi accountsApi;
    private final ObjectMapper mapper;

    public GetAccountsService(AccountsApi accountsApi, ObjectMapper mapper) {
        this.accountsApi = accountsApi;
        this.mapper = mapper;
    }

    public List<AccountFrontendResponse> getAccountsList(Long userId) {
        ResponseEntity<String> response = accountsApi.getUserAccounts(userId);
        if (response.getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
            throw new UserIsNotExistException(userId);
        }
        if (response.getStatusCode().is5xxServerError()) {
            throw new InternalServerException();
        }
        return getObjectAccountsList(response.getBody());
    }

    private List<AccountFrontendResponse> getObjectAccountsList(String json) {
        try {
            return mapper.readValue(
                    json,
                    new TypeReference<>() {}
            );
        } catch (JsonProcessingException e) {
            throw new InternalServerException();
        }
    }
}
