package com.insurance.quote.dto;

import jakarta.validation.constraints.NotBlank;

public record InsuredItem(
        @NotBlank String description
) {
}
