package com.example.accounts.api.dto.movement;

import com.example.accounts.domain.MovementType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class MovementRequest {
    private UUID accountId;
    private BigDecimal amount;
    private MovementType movementType;
}

