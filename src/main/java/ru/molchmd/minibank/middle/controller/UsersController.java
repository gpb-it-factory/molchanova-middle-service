package ru.molchmd.minibank.middle.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.molchmd.minibank.middle.dto.request.CreateUserRequest;
import ru.molchmd.minibank.middle.service.UsersService;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("Received request to create user: {}", createUserRequest);
        usersService.createUser(createUserRequest);
        log.info("User created successfully");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
