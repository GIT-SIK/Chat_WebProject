package com.example.ws_back.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.ws_back.chat.ChatController;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessagingTemplate smt;

    @Autowired
    private ChatController wsc;


    String href = "/api/auth/topic/ws1";
    String type = "notification";

    /* 입장 퇴장 메시지 */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        String message = "사용자가 입장했습니다.";
        wsc.incrementUserCnt();
        smt.convertAndSend(href, createMessage(type, message));
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String message = "사용자가 퇴장했습니다.";
        wsc.decrementUserCnt();
        smt.convertAndSend(href, createMessage(type, message));
    }

    /* 메시지 타입 및 내용 */
    private String createMessage(String type, String message) {
        logger.info(message);
        return String.format("{\"type\":\"%s\",\"message\":\"%s\"}", type, message);
    }
}


