package com.example.ws_back.chat;

import com.example.ws_back.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController {	

		private final ChatService cs;
		
		/** (전체) 메시지 처리
		 * 
		 * @return WSMessage | message 반환
		 */
		@MessageMapping("ws1")
		@SendTo("/api/topic/ws1")
		public ChatMessage ws1(ChatMessage message) {	   
			message.setDate(UtcToKst(message.getDate()));
			log.info("TEXT : " + message.getText());
		    return message;
	    }
		
		
		/** 채팅방 목록
		 * 
		 * [미구현] 
		 * 
		 * @return
		 */
		
		@RequestMapping(value = "/api/chat/list", method = RequestMethod.GET)
		public ResponseEntity<List<?>> getChatList() {
			/* 해당 사용자의 채팅방 리스트 리턴 */
			List<String> temp = new ArrayList<>();
			return ResponseEntity.ok().body(temp);
		}
		
		/** 채팅 내역, 채팅방 생성
		 * 
		 * @param 상대방 유저 ID
		 * @return ChatRoom | 1:1 채팅방 유저 목록, 채팅방 ID
		 * 채팅방 ID는 UUID로 생성됨.
		 */
		@RequestMapping(value = "/api/chat/join", method = RequestMethod.GET)
		@ResponseBody
		public ResponseEntity<ChatRoom> getChatMessage(@RequestParam("v") String otherUserId, Authentication authentication) {
			ChatRoom chatRoom = cs.getChatRoom(otherUserId, authentication);
			log.info("\n채팅방 정보를 가져옵니다. \n데이터 : " + chatRoom.getRoomId() + " " + chatRoom.getUserIdA() + " " + chatRoom.getUserIdB());
			return ResponseEntity.ok().body(chatRoom);
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
		public ChatUserCount returnUC(@AuthenticationPrincipal CustomUserDetails userDetails) {
	    	return new ChatUserCount(userCount.get());
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


