package com.example.ws_back.chat;

import com.example.ws_back.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
		 * 
		 * 
		 */
		
		@MessageMapping("/api/chat/send")
		public String sendMessage(ChatDto chatDto) {	   
			String temp = cs.chatMessage(chatDto);
			return temp;
	    }
		
		
		/** 채팅방 목록
		 * 
		 * @return List<Map<String, Object>> | List -> 채팅방 ID, 상대방 ID, 마지막 업데이트일
		 */
		
		@RequestMapping(value = "/api/chat/list", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> getChatList(Authentication authentication) {
			/* 해당 사용자의 채팅방 리스트 리턴 */
			List<Map<String, Object>> crList = cs.getChatRoomList(authentication);
			return ResponseEntity.ok().body(crList);
		}
		
		/** 채팅 내역, 채팅방 생성
		 * 
		 * @param 상대방 유저 ID
		 * @return ChatRoom | 채팅방 ID, 본인ID, 상대방ID, 방 생성일, 마지막 업데이트일
		 * 채팅방 ID는 UUID로 생성됨.
		 */
		@RequestMapping(value = "/api/chat/join", method = RequestMethod.GET)
		public ResponseEntity<Map<String, Object>> getChatMessage(@RequestParam("v") String otherUserId, Authentication authentication) {
			Map<String, Object> chatRoom = cs.getChatRoom(otherUserId, authentication);
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
		
	    @RequestMapping(value = "/api/chat/uc", method = RequestMethod.GET)
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
	    	        
}


