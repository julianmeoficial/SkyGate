package com.skygate.backend.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        logger.info("WebSocket connection established: Session ID = {}", session.getId());

        session.sendMessage(new TextMessage("Connected to SkyGate WebSocket Server"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        logger.info("Received WebSocket message from {}: {}", session.getId(), payload);

        session.sendMessage(new TextMessage("Echo: " + payload));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        logger.info("WebSocket connection closed: Session ID = {}, Status = {}", session.getId(), status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        logger.error("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage());

        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    public void broadcastMessage(String message) {
        logger.info("Broadcasting message to {} active sessions", sessions.size());

        sessions.values().forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                logger.error("Error broadcasting to session {}: {}", session.getId(), e.getMessage());
            }
        });
    }

    public void sendMessageToSession(String sessionId, String message) {
        WebSocketSession session = sessions.get(sessionId);

        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                logger.info("Message sent to session {}", sessionId);
            } catch (IOException e) {
                logger.error("Error sending message to session {}: {}", sessionId, e.getMessage());
            }
        } else {
            logger.warn("Session {} not found or not open", sessionId);
        }
    }

    public int getActiveSessionsCount() {
        return sessions.size();
    }

    public Map<String, WebSocketSession> getActiveSessions() {
        return new ConcurrentHashMap<>(sessions);
    }
}
