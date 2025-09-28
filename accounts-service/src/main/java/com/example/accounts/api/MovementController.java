package com.example.accounts.api;

import com.example.accounts.api.dto.movement.AccountStatementDto;
import com.example.accounts.api.dto.movement.MovementRequest;
import com.example.accounts.domain.Movement;
import com.example.accounts.service.MovementService;
import com.example.accounts.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovementController {
    private final MovementService movementService;
    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<Movement> createMovement(@RequestBody MovementRequest request) {

        Movement movement = movementService.registerMovement(
                request.getAccountId(),
                request.getAmount(),
                request.getMovementType()
        );
        return ResponseEntity.ok(movement);

    }

    @GetMapping("/{accountId}")
    public List<Movement> getMovementsByAccount(@PathVariable("accountId") UUID accountId) {
        return movementService.getMovementsByAccount(accountId);
    }

    @GetMapping("/{accountId}/range")
    public List<Movement> getMovementsByDateRange(
            @PathVariable("accountId") UUID accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime to) {
        return movementService.getMovementsByAccountAndDateRange(accountId, from, to);
    }

    @GetMapping("/reportes")
    public CompletableFuture<ResponseEntity<AccountStatementDto>>
    getAccountStatement(
            @RequestParam("clientId") UUID clientId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate) {
        return reportService.generateAccountStatement(clientId, startDate, endDate)
                .thenApply(ResponseEntity::ok);

    }


}
