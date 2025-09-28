package com.example.accounts.api.dto.movement;

import com.example.accounts.domain.MovementType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

public record MovementDto(
        UUID id,
        @NotNull UUID accountId,
        @NotNull MovementType movementType,
        @NotNull BigDecimal value
) {}
