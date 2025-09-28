package com.example.accounts.api.dto.movement;

import com.example.accounts.domain.MovementType;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record MovementDetailDto(
        OffsetDateTime date,
        MovementType type,
        BigDecimal value,
        BigDecimal balance
) {}

