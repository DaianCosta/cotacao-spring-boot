package com.insurance.quote.service;

import com.insurance.quote.dto.QuoteRequest;
import com.insurance.quote.dto.QuoteResponse;
import com.insurance.quote.exception.ProductNotFoundException;
import com.insurance.quote.model.Product;
import com.insurance.quote.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

    private final ProductRepository productRepository;
    private final PricingService pricingService;

    public QuoteService(ProductRepository productRepository, PricingService pricingService) {
        this.productRepository = productRepository;
        this.pricingService = pricingService;
    }

    public QuoteResponse quote(QuoteRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ProductNotFoundException(request.productId()));

        Double price = pricingService.calculatePrice(product);

        return new QuoteResponse(
                product.getId(),
                product.getName(),
                product.getType().name(),
                price,
                request.customer().name()
        );
    }
}
