package com.example.ws_back.controller;

import com.example.ws_back.dto.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebSocketController {	
		public AtomicInteger userCount = new AtomicInteger(0);
	
		/*
		 * 메시지 처리
		 * 시간처리까지 포함
		 * 
		 */
		@MessageMapping("ws1")
		@SendTo("/topic/ws1")
		public WSMessage ws1(WSMessage message) {
			 
			/* Time Zone  
		    / UTC -> KST 변환 
			*/
	        LocalDateTime utcToKst = message.getDate()
									        		.atZone(ZoneId.of("UTC"))
									        		.withZoneSameInstant(ZoneId.of("Asia/Seoul"))
									        		.toLocalDateTime();	   
			message.setDate(utcToKst);
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
	    	return new WSUserCount(userCount.get());
	    }
	    
	    public void incrementUserCnt() {
	        userCount.incrementAndGet();
	    }

	    public void decrementUserCnt() {
	        userCount.decrementAndGet();
	    }
	    
}


