package com.example.accounts.domain.dto;

import java.util.UUID;

public record ClientResponseDto(
        UUID clientId,
        String name,
        String identification,
        boolean state
) {}
