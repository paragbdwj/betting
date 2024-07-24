package com.elephants.betting.src.controller.websockets;

import com.elephants.betting.src.request.GiveOddsRequest;
import com.elephants.betting.src.response.GiveOddsResponse;
import com.elephants.betting.src.service.OddsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class OddsWebSocketHandler extends TextWebSocketHandler {
    private final OddsService oddsService;
    private final ObjectMapper objectMapper;
    private final Map<WebSocketSession, String> sessionMatchIdMap = new ConcurrentHashMap<>();

    public OddsWebSocketHandler(OddsService oddsService) {
        this.oddsService = oddsService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse the incoming message
        String payload = message.getPayload();
        JsonNode jsonNode = objectMapper.readTree(payload);
        String matchId = jsonNode.get("matchId").asText();

        // Store the matchId associated with the session
        sessionMatchIdMap.put(session, matchId);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Schedule the task to send odds data every 1 second
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            try {
                String matchId = sessionMatchIdMap.get(session);
                if (matchId != null) {
                    GiveOddsRequest request = new GiveOddsRequest();
                    request.setMatchId(Integer.parseInt(matchId)); // Set matchId in the request
                    GiveOddsResponse response = oddsService.giveOdds(request);
                    String jsonResponse = objectMapper.writeValueAsString(response);
                    session.sendMessage(new TextMessage(jsonResponse));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS); // Adjust the interval as needed
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Remove the session from the map when the connection is closed
        sessionMatchIdMap.remove(session);
    }
}
