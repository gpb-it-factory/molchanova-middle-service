package ru.molchmd.minibank.middle.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import ru.molchmd.minibank.middle.controller.UsersController;
import ru.molchmd.minibank.middle.exception.entity.UserAlreadyExistsException;

@ControllerAdvice(assignableTypes = {UsersController.class})
@Slf4j
public class UsersControllerException {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Void> handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        log.error("Response -> User id[{}] already exists error | status {}", exception.userId, HttpStatus.CONFLICT);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Void> handleResourceAccessException(ResourceAccessException exception) {
        log.error("Response -> Server is not available error | status" + HttpStatus.SERVICE_UNAVAILABLE);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception exception) {
        log.error("Response -> Undefined error | status " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
