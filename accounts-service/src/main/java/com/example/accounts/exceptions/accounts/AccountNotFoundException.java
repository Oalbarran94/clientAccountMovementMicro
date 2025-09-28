package com.example.accounts.exceptions.accounts;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(UUID id) {
        super("Account not found with id: " + id);
    }
}