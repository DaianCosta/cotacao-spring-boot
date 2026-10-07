package com.insurance.quote.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String productId) {
        super("Produto não encontrado com o ID: " + productId);
    }
}
