package com.elephants.betting.common.config;

import com.elephants.betting.src.controller.websockets.OddsWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final OddsWebSocketHandler oddsWebSocketHandler;

    public WebSocketConfig(OddsWebSocketHandler oddsWebSocketHandler) {
        this.oddsWebSocketHandler = oddsWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(oddsWebSocketHandler, "/ws/odds").setAllowedOrigins("*");
    }
}

