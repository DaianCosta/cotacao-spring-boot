package com.insurance.quote.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerData(
        @NotBlank String name,
        @NotBlank String document
) {
}
