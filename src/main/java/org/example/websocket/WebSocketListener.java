package org.example.websocket;

public interface WebSocketListener {
    void onMessage(String message);
    void onError(Throwable error);
    void onClose(int code, String reason);
}