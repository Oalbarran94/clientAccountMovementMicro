package com.example.accounts.service;

import com.example.accounts.domain.Account;
import com.example.accounts.domain.Movement;
import com.example.accounts.domain.MovementType;
import com.example.accounts.exceptions.accounts.AccountValidationException;
import com.example.accounts.exceptions.movements.InsufficientBalanceException;
import com.example.accounts.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MovementService {

    private final MovementRepository movementRepository;
    private final AccountService accountService;

    public Movement registerMovement(UUID accountId, BigDecimal amount, MovementType movementType) {
        Account account = accountService.getAccountById(accountId);

        if (!account.getState()) {
            throw new AccountValidationException("Account not active");
        }

        BigDecimal effectiveAmount = switch (movementType) {
            case DEPOSIT -> amount;
            case RETIREMENT -> amount.negate();
        };

        BigDecimal currentBalance = movementRepository.findByAccountOrderByDateAsc(account)
                .stream()
                .map(Movement::getBalance)
                .reduce((first, second) -> second)
                .orElse(account.getInitialBalance());

        if (movementType == MovementType.RETIREMENT) {
            BigDecimal newBalance = currentBalance.add(effectiveAmount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("Not enough balance");
            }
        }


        Movement movement = Movement.builder()
                .account(account)
                .date(OffsetDateTime.now())
                .movementType(movementType)
                .value(amount)
                .balance(currentBalance.add(effectiveAmount))
                .build();

        log.info("Saving {} for account {}: {} - New balance: {}",
                movementType, account.getAccountNumber(), amount, movement.getBalance());

        return movementRepository.save(movement);
    }

    public List<Movement> getMovementsByAccountAndDateRange(
            UUID accountId,
            OffsetDateTime from,
            OffsetDateTime to) {
        Account account = accountService.getAccountById(accountId);
        return movementRepository.findByAccountAndDateBetweenOrderByDateAsc(account, from, to);
    }

    public List<Movement> getMovementsByAccount(UUID accountId) {
        Account account = accountService.getAccountById(accountId);
        return movementRepository.findByAccountOrderByDateAsc(account);
    }
}
