package com.insurance.quote.service;

import com.insurance.quote.model.Product;
import com.insurance.quote.model.ProductType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PricingServiceTest {

    private final PricingService pricingService = new PricingService();

    @Test
    void calculatePrice_returnBasePrice_forCobertura() {
        Product product = new Product("VIDA", ProductType.COBERTURA, 150.00);
        assertThat(pricingService.calculatePrice(product)).isEqualTo(150.00);
    }

    @Test
    void calculatePrice_returnBasePrice_forAssistencia() {
        Product product = new Product("ASSISTÊNCIA MORADIA", ProductType.ASSISTENCIA, 30.00);
        assertThat(pricingService.calculatePrice(product)).isEqualTo(30.00);
    }
}
