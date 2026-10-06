package com.insurance.quote.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuoteRequest(
        @NotNull @Valid CustomerData customer,
        @NotBlank String productId,
        @NotNull @Valid InsuredItem insuredItem
) {
}
