package com.insurance.quote.dto;

public record QuoteResponse(
        String productId,
        String productName,
        String productType,
        Double price,
        String customerName
) {
}
