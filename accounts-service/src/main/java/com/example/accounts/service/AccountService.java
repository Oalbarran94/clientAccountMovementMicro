package com.example.accounts.service;

import com.example.accounts.api.dto.account.AccountDto;
import com.example.accounts.domain.Account;
import com.example.accounts.domain.dto.ClientResponseDto;
import com.example.accounts.exceptions.accounts.AccountNotFoundException;
import com.example.accounts.exceptions.accounts.AccountValidationException;
import com.example.accounts.exceptions.ClientNotActiveException;
import com.example.accounts.exceptions.ClientWithAccountAlreadyExistingException;
import com.example.accounts.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final WebClient webClient;

    @Transactional
    public CompletableFuture<Account> createAccount(AccountDto dto) {

        String clientUrl = "/clientes/" + dto.clientId();

        return webClient.get()
                .uri(clientUrl)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> Mono.error(new IllegalArgumentException("Client not found")))
                .bodyToMono(ClientResponseDto.class)
                .toFuture()
                .thenApply(client -> {
                    if (!client.state()) {
                        throw new ClientNotActiveException("Account cannot be created since client is not active");
                    }

                    Optional<Account> existingAccount = repository.findByClientIdAndAccountType(
                            dto.clientId(),
                            dto.accountType()
                    );

                    if (existingAccount.isPresent()) {
                        throw new ClientWithAccountAlreadyExistingException(
                                "Client already has an account of type " + dto.accountType()
                        );
                    }

                    Account account = Account.builder()
                            .id(dto.id())
                            .accountNumber(dto.accountNumber())
                            .accountType(dto.accountType())
                            .initialBalance(dto.initialBalance())
                            .state(dto.state())
                            .clientId(dto.clientId())
                            .build();

                    return repository.save(account);
                });
    }


    @Transactional
    public Account updateAccount(UUID id, AccountDto dto) {
        Account account = repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        if (!account.getAccountNumber().equals(dto.accountNumber())) {
            throw new AccountValidationException("Account number cannot be modified");
        }

        if (!account.getAccountType().equals(dto.accountType())) {
            throw new AccountValidationException("Account type cannot be modified");
        }

        if (!account.getClientId().equals(dto.clientId())) {
            throw new AccountValidationException("Client ID cannot be modified");
        }

        account.setInitialBalance(dto.initialBalance());
        account.setState(dto.state());

        return repository.save(account);
    }

    public Account getAccountById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public List<Account> getAccountsByClientId(UUID clientId) {
        return repository.findByClientId(clientId);
    }

    public List<Account> getAllAccounts() {
        return repository.findAll();
    }


}
