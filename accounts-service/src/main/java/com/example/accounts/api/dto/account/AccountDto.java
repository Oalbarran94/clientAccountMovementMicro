package com.example.accounts.api.dto.account;

import com.example.accounts.domain.AccountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record AccountDto(
        UUID id,
        @NotBlank(message = "Account number is required")
        String accountNumber,

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Initial balance is required")
        @DecimalMin(value = "0.0", inclusive = true,
                message = "Initial balance must be greater than or equal to 0")
        BigDecimal initialBalance,

        @NotNull(message = "State is required")
        Boolean state,

        @NotNull(message = "Client ID is required")
        UUID clientId
) {}
