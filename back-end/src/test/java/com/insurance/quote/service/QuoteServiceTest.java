package com.insurance.quote.service;

import com.insurance.quote.dto.CustomerData;
import com.insurance.quote.dto.InsuredItem;
import com.insurance.quote.dto.QuoteRequest;
import com.insurance.quote.dto.QuoteResponse;
import com.insurance.quote.exception.ProductNotFoundException;
import com.insurance.quote.model.Product;
import com.insurance.quote.model.ProductType;
import com.insurance.quote.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PricingService pricingService;

    @InjectMocks
    private QuoteService quoteService;

    @Test
    void quote_returnsResponse_whenProductFound() {
        Product product = new Product("RESIDENCIAL", ProductType.COBERTURA, 300.00);
        product.setId("abc123");

        when(productRepository.findById("abc123")).thenReturn(Optional.of(product));
        when(pricingService.calculatePrice(product)).thenReturn(300.00);

        QuoteRequest request = new QuoteRequest(
                new CustomerData("João da Silva", "123.456.789-00"),
                "abc123",
                new InsuredItem("Apartamento 3 quartos")
        );

        QuoteResponse response = quoteService.quote(request);

        assertThat(response.productId()).isEqualTo("abc123");
        assertThat(response.productName()).isEqualTo("RESIDENCIAL");
        assertThat(response.productType()).isEqualTo("COBERTURA");
        assertThat(response.price()).isEqualTo(300.00);
        assertThat(response.customerName()).isEqualTo("João da Silva");
    }

    @Test
    void quote_throwsProductNotFoundException_whenProductNotFound() {
        when(productRepository.findById("nonexistent")).thenReturn(Optional.empty());

        QuoteRequest request = new QuoteRequest(
                new CustomerData("João da Silva", "123.456.789-00"),
                "nonexistent",
                new InsuredItem("Apartamento 3 quartos")
        );

        assertThatThrownBy(() -> quoteService.quote(request))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("nonexistent");
    }
}
