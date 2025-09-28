package com.example.accounts.repository;

import com.example.accounts.domain.Account;
import com.example.accounts.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByClientIdAndAccountType(UUID clientId, AccountType accountType);

    List<Account> findByClientId(UUID clientId);

}
