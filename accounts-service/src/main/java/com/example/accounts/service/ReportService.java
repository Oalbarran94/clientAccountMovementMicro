package com.example.accounts.service;

import com.example.accounts.api.dto.account.AccountDetailDto;
import com.example.accounts.api.dto.movement.AccountStatementDto;
import com.example.accounts.api.dto.movement.MovementDetailDto;
import com.example.accounts.domain.Account;
import com.example.accounts.domain.Movement;
import com.example.accounts.domain.dto.ClientResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final AccountService accountService;
    private final MovementService movementService;
    private final WebClient webClient;

    public CompletableFuture<AccountStatementDto> generateAccountStatement(UUID clientId,
                                                                           OffsetDateTime startDate,
                                                                           OffsetDateTime endDate) {
        return webClient.get()
                .uri("/clientes/" + clientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new IllegalArgumentException("Client not found")))
                .bodyToMono(ClientResponseDto.class)
                .toFuture()
                .thenApply(client -> {
                    List<Account> accounts = accountService.getAccountsByClientId(clientId);

                    List<AccountDetailDto> accountDetails = accounts.stream()
                            .map(account -> {
                                List<Movement> movements = movementService
                                        .getMovementsByAccountAndDateRange(account.getId(), startDate, endDate);

                                return new AccountDetailDto(
                                        account.getAccountNumber(),
                                        account.getAccountType().toString(),
                                        account.getInitialBalance(),
                                        account.getState(),
                                        movements.stream()
                                                .map(mov -> new MovementDetailDto(
                                                        mov.getDate(),
                                                        mov.getMovementType(),
                                                        mov.getValue(),
                                                        mov.getBalance()))
                                                .toList(),
                                        movements.isEmpty() ? account.getInitialBalance()
                                                : movements.getLast().getBalance()
                                );
                            })
                            .toList();

                    return new AccountStatementDto(
                            OffsetDateTime.now(),
                            client,
                            accountDetails
                    );
                });
    }

}
