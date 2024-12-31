package com.example.ws_app.controller;

import com.example.ws_app.dto.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebSocketController {
	
		@Autowired
	    private SimpMessagingTemplate smt;

		
		public AtomicInteger userCount = new AtomicInteger(0);
	
		@MessageMapping("ws1")
		@SendTo("/topic/ws1")
		public WSMessage ws1(WSMessage message) {
			System.out.println("message: " + message.getText());
	        return message;
	    }
		
		/* 
		 * 유저 접속 수
		 * 
		 */
		// 동시성(동기화) 제어를 위해 Atomic 처리
	    
	    @RequestMapping(value = "/api/uc", method = RequestMethod.GET)
	    @ResponseBody
		public WSUserCount returnUC() {
	    	WSUserCount uc = new WSUserCount(userCount.get());
	    	System.out.println(uc);
	    	return uc;
	    }
	    
	    public void incrementUserCnt() {
	        userCount.incrementAndGet();
	    }

	    public void decrementUserCnt() {
	        userCount.decrementAndGet();
	    }
	    
}


