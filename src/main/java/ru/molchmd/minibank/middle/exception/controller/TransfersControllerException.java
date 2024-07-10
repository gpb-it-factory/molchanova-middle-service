package ru.molchmd.minibank.middle.exception.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import ru.molchmd.minibank.middle.controller.TransfersController;
import ru.molchmd.minibank.middle.exception.entity.InternalServerException;
import ru.molchmd.minibank.middle.exception.entity.TransferException;

@ControllerAdvice(assignableTypes = {TransfersController.class})
@Slf4j
public class TransfersControllerException {
    @ExceptionHandler(TransferException.class)
    public ResponseEntity<String> handleResourceAccessException(TransferException exception) {
        log.error("Response -> Transfer error from[{}] to [{}] | status {}",
                exception.fromUserName, exception.toUserName, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(exception.jsonError, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Void> handleResourceAccessException(ResourceAccessException exception) {
        log.error("Response -> Server is not available error | status " + HttpStatus.SERVICE_UNAVAILABLE);
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<Void> handleInternalServerException(InternalServerException exception) {
        log.error("Response -> Server error | status " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception exception) {
        log.error("Response -> Undefined error | status " + HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
