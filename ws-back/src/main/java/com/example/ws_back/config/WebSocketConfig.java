package com.example.ws_back.config;

import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
    private final ChannelInterceptor jwtChannelInterceptor;

    public WebSocketConfig(JwtChannelInterceptor jwtChannelInterceptor) {
        this.jwtChannelInterceptor = jwtChannelInterceptor;
    }
	
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/api/topic", "/api/queue"); 
        registry.setApplicationDestinationPrefixes("/api/app"); 
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	registry.addEndpoint("/api/ws")
        .setAllowedOrigins("http://localhost:5173"); // vue 만 허용 
//        .withSockJS();
    }
    
    @Override
    public void configureClientInboundChannel(org.springframework.messaging.simp.config.ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor); // 인터셉터 등록
    }
}
