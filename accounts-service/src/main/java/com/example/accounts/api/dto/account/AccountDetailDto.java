package com.example.accounts.api.dto.account;

import com.example.accounts.api.dto.movement.MovementDetailDto;

import java.math.BigDecimal;
import java.util.List;

public record AccountDetailDto(
        String accountNumber,
        String type,
        BigDecimal initialBalance,
        boolean state,
        List<MovementDetailDto> movements,
        BigDecimal availableBalance
) {}

