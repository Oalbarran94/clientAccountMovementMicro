package com.example.customers.api.dto;

import com.example.customers.domain.Genre;
import jakarta.validation.constraints.*;
import java.util.UUID;

public record ClientDto(
        UUID clientId,
        @NotBlank String name,
        @NotNull Genre genre,
        @NotNull @Min(0) Integer age,
        @NotBlank String identification,
        @NotBlank String address,
        @NotBlank String phone,
        @NotNull Boolean state
) {}
