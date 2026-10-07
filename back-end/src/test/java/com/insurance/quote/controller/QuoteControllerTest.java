package com.insurance.quote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.insurance.quote.dto.CustomerData;
import com.insurance.quote.dto.InsuredItem;
import com.insurance.quote.dto.QuoteRequest;
import com.insurance.quote.dto.QuoteResponse;
import com.insurance.quote.exception.GlobalExceptionHandler;
import com.insurance.quote.exception.ProductNotFoundException;
import com.insurance.quote.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class QuoteControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private QuoteService quoteService;

    @InjectMocks
    private QuoteController quoteController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(quoteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createQuote_returns200_whenRequestValid() throws Exception {
        QuoteResponse response = new QuoteResponse(
                "abc123", "RESIDENCIAL", "COBERTURA", 300.00, "João da Silva");

        when(quoteService.quote(any())).thenReturn(response);

        QuoteRequest request = new QuoteRequest(
                new CustomerData("João da Silva", "123.456.789-00"),
                "abc123",
                new InsuredItem("Apartamento 3 quartos")
        );

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("abc123"))
                .andExpect(jsonPath("$.productName").value("RESIDENCIAL"))
                .andExpect(jsonPath("$.productType").value("COBERTURA"))
                .andExpect(jsonPath("$.price").value(300.00))
                .andExpect(jsonPath("$.customerName").value("João da Silva"));
    }

    @Test
    void createQuote_returns404_whenProductNotFound() throws Exception {
        when(quoteService.quote(any()))
                .thenThrow(new ProductNotFoundException("nonexistent"));

        QuoteRequest request = new QuoteRequest(
                new CustomerData("João da Silva", "123.456.789-00"),
                "nonexistent",
                new InsuredItem("Apartamento 3 quartos")
        );

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Produto não encontrado com o ID: nonexistent"))
                .andExpect(jsonPath("$.details").doesNotExist());
    }

    @Test
    void createQuote_returns400_whenCustomerNameBlank() throws Exception {
        String body = """
                {
                  "customer": {"name": "", "document": "123.456.789-00"},
                  "productId": "abc123",
                  "insuredItem": {"description": "Apartamento"}
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void createQuote_returns400_whenProductIdMissing() throws Exception {
        String body = """
                {
                  "customer": {"name": "João", "document": "123.456.789-00"},
                  "insuredItem": {"description": "Apartamento"}
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void createQuote_returns400_whenInsuredItemMissing() throws Exception {
        String body = """
                {
                  "customer": {"name": "João", "document": "123.456.789-00"},
                  "productId": "abc123"
                }
                """;

        mockMvc.perform(post("/api/quotes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
