package org.example.service;

import org.example.entity.Price;
import org.example.repository.PriceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class PriceService {
    private final PriceRepository priceRepository;
    private final ObjectMapper objectMapper;

    public PriceService(PriceRepository priceRepository, ObjectMapper objectMapper) {
        this.priceRepository = priceRepository;
        this.objectMapper = objectMapper;
    }

    public void savePriceFromMessage(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode data = root.path("data");
            String symbol = data.path("s").asText();
            Double price = data.path("p").asDouble();

            Price priceEntity = new Price();
            priceEntity.setSymbol(symbol);
            priceEntity.setPrice(price);
            priceEntity.setTimestamp(Instant.now());

            priceRepository.save(priceEntity);
//            System.out.println("Saved price: " + symbol + " " + price);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse price message: " + e.getMessage(), e);
        }
    }

    public Iterable<Price> getAllprices() {
        return priceRepository.findAll();
    }
}