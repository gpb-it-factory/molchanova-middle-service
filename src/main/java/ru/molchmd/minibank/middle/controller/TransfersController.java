package ru.molchmd.minibank.middle.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.molchmd.minibank.middle.dto.request.CreateTransferRequest;
import ru.molchmd.minibank.middle.metrics.endpoint.TransfersMetrics;
import ru.molchmd.minibank.middle.service.TransferService;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class TransfersController {
    private final TransferService transferService;
    private final TransfersMetrics metric;

    public TransfersController(TransferService transferService, TransfersMetrics metric) {
        this.transferService = transferService;
        this.metric = metric;
    }

    @PostMapping("/transfers")
    public ResponseEntity<Void> createTransfer(@RequestBody CreateTransferRequest createTransferRequest) {
        log.info("Request -> Received request to create transfer: {}", createTransferRequest);
        metric.post();
        transferService.transfer(createTransferRequest);
        log.info("Response -> Transfer was successfully completed | status {}", HttpStatus.OK);
        metric.successPost();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
