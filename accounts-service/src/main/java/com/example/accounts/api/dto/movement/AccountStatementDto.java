package com.example.accounts.api.dto.movement;

import com.example.accounts.api.dto.account.AccountDetailDto;
import com.example.accounts.domain.dto.ClientResponseDto;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record AccountStatementDto(
        OffsetDateTime date,
        ClientResponseDto client,
        List<AccountDetailDto> accounts
) {}
