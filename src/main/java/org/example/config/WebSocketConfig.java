package org.example.config;

import org.example.service.PriceService;
import org.example.websocket.WebSocketListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class WebSocketConfig {
    @Bean
    public WebSocketListener webSocketListener(PriceService priceService, SimpMessagingTemplate messagingTemplate) {
        return new WebSocketListener() {
            @Override
            public void onMessage(String message) {
                priceService.savePriceFromMessage(message);
                messagingTemplate.convertAndSend("/topic/prices", message);
            }

            @Override
            public void onError(Throwable error) {
                System.err.println("WebSocket Error: " + error.getMessage());
            }

            @Override
            public void onClose(int code, String reason) {
                System.out.println("WebSocket Closed: " + code + ", " + reason);
            }
        };
    }
}