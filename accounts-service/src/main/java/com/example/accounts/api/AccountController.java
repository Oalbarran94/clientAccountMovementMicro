package com.example.accounts.api;

import com.example.accounts.api.dto.account.AccountDto;
import com.example.accounts.domain.Account;
import com.example.accounts.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Account> create(@Valid @RequestBody AccountDto dto) {
        log.info("Creating account: {}", dto);
        return accountService.createAccount(dto);
    }


    @PutMapping("/{id}")
    public Account update(@PathVariable("id") UUID id, @Valid @RequestBody AccountDto dto) {
        return accountService.updateAccount(id, dto);
    }

    @GetMapping("/{id}")
    public Account get(@PathVariable("id") UUID id) {
        return accountService.getAccountById(id);
    }

    @GetMapping
    public List<Account> list() {
        return accountService.getAllAccounts();
    }

}
