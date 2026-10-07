package com.insurance.quote.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
