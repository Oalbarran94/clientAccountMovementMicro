package com.example.accounts.repository;

import com.example.accounts.domain.Movement;
import com.example.accounts.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface MovementRepository extends JpaRepository<Movement, UUID> {
    List<Movement> findByAccountAndDateBetweenOrderByDateAsc(Account account, OffsetDateTime from, OffsetDateTime to);
    List<Movement> findByAccountOrderByDateAsc(Account account);
}
