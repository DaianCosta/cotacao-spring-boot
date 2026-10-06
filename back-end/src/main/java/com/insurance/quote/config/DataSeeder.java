package com.insurance.quote.config;

import com.insurance.quote.model.Product;
import com.insurance.quote.model.ProductType;
import com.insurance.quote.repository.ProductRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements ApplicationRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (productRepository.count() == 0) {
            productRepository.saveAll(List.of(
                    new Product("VIDA", ProductType.COBERTURA, 150.00),
                    new Product("RESIDENCIAL", ProductType.COBERTURA, 300.00),
                    new Product("ASSISTÊNCIA MORADIA", ProductType.ASSISTENCIA, 30.00)
            ));
        }
    }
}
