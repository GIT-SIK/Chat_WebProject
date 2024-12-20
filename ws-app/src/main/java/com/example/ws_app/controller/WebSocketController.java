package com.example.ws_app.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
		@MessageMapping("ws1")
		@SendTo("/topic/ws1")
		public String ws1(String message) {
	        return message;
	    }
}


