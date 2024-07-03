package ru.molchmd.minibank.middle.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.metrics.endpoint.UsersMetrics;
import ru.molchmd.minibank.middle.service.CreateUserService;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UsersController {
    private final CreateUserService createUserService;
    private final UsersMetrics metric;

    public UsersController(CreateUserService createUserService, UsersMetrics metric) {
        this.createUserService = createUserService;
        this.metric = metric;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("Request -> Received request to create user: {}", createUserRequest);
        metric.post();
        createUserService.createUser(createUserRequest);
        log.info("Response -> User id[{}] name[{}] created successfully | status {}",
                createUserRequest.getUserId(), createUserRequest.getUserName(), HttpStatus.CREATED);
        metric.successPost();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
