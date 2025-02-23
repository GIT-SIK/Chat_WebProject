package com.example.ws_back.config;

import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	
    private final ChannelInterceptor jwtChannelInterceptor;
    
    @Value("${websocket.endpoint}")
    private String wsEndPoint;

    @Value("${websocket.broker.prefix}")
    private String[] wsBroker;

    @Value("${websocket.app.prefix}")
    private String wsApp;

    @Value("${websocket.allowed.origins}") // CORS 허용 도메인
    private String allowedOrigins;
    
    public WebSocketConfig(JwtChannelInterceptor jwtChannelInterceptor) {
        this.jwtChannelInterceptor = jwtChannelInterceptor;
    }
	
    /* 메시지 브로커 / 라우팅 규칙 */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker(wsBroker); 
//        registry.setApplicationDestinationPrefixes(wsApp); 
    }

    /* WebSocket EndPoint */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	registry.addEndpoint(wsEndPoint)
        .setAllowedOrigins(allowedOrigins); // vue 만 허용 
//        .withSockJS();
    }
    
    @Override
    public void configureClientInboundChannel(org.springframework.messaging.simp.config.ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor); // 인터셉터 등록
    }
}
