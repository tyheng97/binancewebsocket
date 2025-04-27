package org.example.websocket;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;

@Component
public class BinanceWebSocketClient {
    private final WebSocketListener listener;

    public BinanceWebSocketClient(WebSocketListener listener) {
        this.listener = listener;
    }

    @PostConstruct
    public void init() {
        try {
            connect();
        } catch (Exception e) {
            listener.onError(e);
        }
    }

    public void connect() throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();
        client.doHandshake(new BinanceWebSocketHandler(), new WebSocketHttpHeaders(), URI.create("wss://stream.binance.com:9443/stream"))
                .addCallback(
                        session -> System.out.println("Connected to Binance WebSocket"),
                        ex -> listener.onError(ex)
                );
    }

    private class BinanceWebSocketHandler extends TextWebSocketHandler {
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            System.out.println("Connection established");
            String subscriptionMessage = "{\"method\":\"SUBSCRIBE\",\"params\":[\"btcusdt@trade\",\"ethusdt@trade\"],\"id\":1}";
            session.sendMessage(new TextMessage(subscriptionMessage));
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) {
            listener.onMessage(message.getPayload());
        }

        @Override
        public void handlePongMessage(WebSocketSession session, org.springframework.web.socket.PongMessage message) {
            System.out.println("Received pong");
        }

        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) {
            listener.onError(exception);
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
            listener.onClose(status.getCode(), status.getReason());
        }
    }
}