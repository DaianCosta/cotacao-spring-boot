package com.insurance.quote.dto;

import java.util.List;

public record ErrorResponse(
        int status,
        String error,
        String message,
        List<String> details
) {
    public ErrorResponse(int status, String error, String message) {
        this(status, error, message, null);
    }
}
