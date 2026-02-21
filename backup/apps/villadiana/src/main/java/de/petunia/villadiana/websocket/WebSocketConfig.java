package de.petunia.villadiana.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(handshakeHandler())
                .setAllowedOriginPatterns("*") // oder gezielt: "https://petunia"
                .withSockJS();
    }

    private DefaultHandshakeHandler handshakeHandler() {
        return new DefaultHandshakeHandler() {
            @Override
            protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return authentication;
            }
        };
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // /queue ist für point-to-point
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user"); // 👈 wichtig für per-user Nachrichten
    }
}
