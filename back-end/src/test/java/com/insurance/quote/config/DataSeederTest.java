package com.insurance.quote.config;

import com.insurance.quote.model.Product;
import com.insurance.quote.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DataSeeder dataSeeder;

    @Test
    void run_seedsThreeProducts_whenRepositoryEmpty() throws Exception {
        when(productRepository.count()).thenReturn(0L);

        dataSeeder.run(null);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Iterable<Product>> captor = ArgumentCaptor.forClass(Iterable.class);
        verify(productRepository).saveAll(captor.capture());

        Collection<Product> saved = (Collection<Product>) captor.getValue();
        assertThat(saved).hasSize(3);
    }

    @Test
    void run_doesNotSeed_whenProductsAlreadyExist() throws Exception {
        when(productRepository.count()).thenReturn(3L);

        dataSeeder.run(null);

        verify(productRepository, never()).saveAll(any());
    }
}
