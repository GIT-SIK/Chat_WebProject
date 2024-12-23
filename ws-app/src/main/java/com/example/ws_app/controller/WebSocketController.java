package com.example.ws_app.controller;

import com.example.ws_app.dto.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
		@MessageMapping("ws1")
		@SendTo("/topic/ws1")
		public Message ws1(Message message) {
			System.out.println("message: " + message.getText());
	        return message;
	    }
}


