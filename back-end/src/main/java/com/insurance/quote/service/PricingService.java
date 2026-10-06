package com.insurance.quote.service;

import com.insurance.quote.model.Product;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

    public Double calculatePrice(Product product) {
        return product.getBasePrice();
    }
}
