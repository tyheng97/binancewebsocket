package org.example.controller;

import org.example.entity.Price;
import org.example.service.PriceService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
@CrossOrigin(origins = "http://localhost:3000")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public  Iterable<Price> getPrices(){
        return priceService.getAllprices();
    }
}
