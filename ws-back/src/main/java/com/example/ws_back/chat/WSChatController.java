package com.example.ws_back.chat;

import com.example.ws_back.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WSChatController {	

	
		/** (전체) 메시지 처리
		 * 
		 * @return WSMessage | message 반환
		 */
		@MessageMapping("ws1")
		@SendTo("/api/topic/ws1")
		public WSMessage ws1(WSMessage message) {	   
			message.setDate(UtcToKst(message.getDate()));
			log.info("TEXT : " + message.getText());
		    return message;
	    }
		
		/** 유저 접속 수 
		 * 
		 * @return WSUserCount | 유저 수 반환 
		 * 
		 * ** Use * WebSocketEventListener.java
		 * @Method incrementUserCnt : 유저수 증가
		 * @Method decrementUserCnt : 유저수 감소
		 */
		// 동시성(동기화) 제어를 위해 Atomic 처리
		public AtomicInteger userCount = new AtomicInteger(0);
		
	    @RequestMapping(value = "/api/uc", method = RequestMethod.GET)
	    @ResponseBody
		public WSUserCount returnUC(@AuthenticationPrincipal CustomUserDetails userDetails) {
	    	return new WSUserCount(userCount.get());
	    }
	    
	    public void incrementUserCnt() {
	        userCount.incrementAndGet();
	    }

	    public void decrementUserCnt() {
	        userCount.decrementAndGet();
	    }
	    	    
	    
	    /* 사용자 지정 함수 */
	    
	    /** TimeZone UTC -> KST 변환
		 * @param LocalDateTime | UTC 시간 입력
		 * @return LocalDateTime | KST 시간 반환
	     */
	    public LocalDateTime UtcToKst(LocalDateTime date) {
	    	return date.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();	 
	    }
	    
}


