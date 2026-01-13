package com.freelance.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

 @Override
 public void configureMessageBroker(MessageBrokerRegistry config) {
     config.enableSimpleBroker("/topic"); // subscribe endpoint
     config.setApplicationDestinationPrefixes("/app"); // send endpoint
 }

 @Override
 public void registerStompEndpoints(StompEndpointRegistry registry) {
     registry.addEndpoint("/ws-chat").setAllowedOriginPatterns("*").withSockJS();
 }
}
