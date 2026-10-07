package com.insurance.quote.exception;

import com.insurance.quote.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(NoHandlerFoundException ex) {
        String message = "Nenhum endpoint encontrado para " + ex.getHttpMethod() + " " + ex.getRequestURL();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Not Found", message));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        String message = "Método " + ex.getMethod() + " não é permitido para este endpoint. Métodos suportados: " + ex.getSupportedHttpMethods();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErrorResponse(405, "Method Not Allowed", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Bad Request", "Erro de validação", details));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Internal Server Error", "Erro interno do servidor"));
    }
}
