package ru.molchmd.minibank.middle.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import ru.molchmd.minibank.middle.controller.AccountsController;
import ru.molchmd.minibank.middle.exception.entity.AccountAlreadyExistException;
import ru.molchmd.minibank.middle.exception.entity.UserIsNotExistException;

@ControllerAdvice(assignableTypes = {AccountsController.class})
@Slf4j
public class AccountsControllerException {
    @ExceptionHandler(UserIsNotExistException.class)
    public ResponseEntity<Void> handleUserIsNotExistException(UserIsNotExistException exception) {
        log.error("Response -> User id[{}] is not exist error | status {}", exception.userId, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccountAlreadyExistException.class)
    public ResponseEntity<Void> handleAccountAlreadyExistException(AccountAlreadyExistException exception) {
        log.error("Response -> Account name[{}] userId[{}] already exist error | status {}",
                exception.accountName, exception.userId, HttpStatus.CONFLICT);
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Void> handleResourceAccessException(ResourceAccessException exception) {
        log.error("Response -> Server is not available error | status " + HttpStatus.SERVICE_UNAVAILABLE);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception exception) {
        log.error("Response -> Undefined error | status " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
