package com.example.ws_back.chat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.ws_back.security.CustomUserDetails;

import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
    private final ChatMongoRepository cmr;
    private final ChatOracleRepository cor;
    private final ModelMapper modelMapper;
	private final SimpMessagingTemplate smt; 
    
    /* 채팅 데이터 처리 */
    public String chatMessage(ChatDto chatDto) {
    	chatDto.setDate(UtcToKst(chatDto.getDate()));
        System.out.println("Received chatId: " + chatDto.getRoomId());
        System.out.println("Received message: " + chatDto.getMessage());
    	smt.convertAndSend("/api/chat/receive/" + chatDto.getRoomId(), chatDto);
    	return "OK";
    }
    
    /* 채팅방 생성 */
    public boolean createChatRoom(ChatRoomDto chatRoomDto) {
    	try {
    		chatRoomDto.setRoomId(UUID.randomUUID().toString());
    		cor.save(modelMapper.map(chatRoomDto ,ChatRoom.class));
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
    
    /* 채팅방 정보 조회 */
    public Map<String, Object> getChatRoom(String otherUserId, Authentication authentication) {
    	
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	List<ChatRoom> crList = cor.findAllByChatRoom(userId);
    	
    	/* 채팅방 생성 로직 */
    	if(!crList.stream()
    	.anyMatch(chatRoom -> otherUserId.equalsIgnoreCase(chatRoom.getUserIdA()) || otherUserId.equalsIgnoreCase(chatRoom.getUserIdB()))) {
    		ChatRoomDto chatRoomDto = new ChatRoomDto();
    		chatRoomDto.setUserIdA(userId);
    		chatRoomDto.setUserIdB(otherUserId);
    		if(createChatRoom(chatRoomDto)) {;
	    		log.info("채팅방을 생성하였습니다.");
	    		crList = cor.findAllByChatRoom(userId);
    		} else {
    			log.info("채팅방 생성도중 오류가 발생습니다.");
    		}
    		
    	}
    	
    	return crList.stream()
		.filter(chatRoom -> otherUserId.equals(chatRoom.getUserIdA()) || otherUserId.equals(chatRoom.getUserIdB()))
		.findFirst()
    	.map(chatRoom -> {
    		Map<String,Object> nrMap = new HashMap<>();
    		nrMap.put("roomId", chatRoom.getRoomId());
    		nrMap.put("otherUserId", !chatRoom.getUserIdA().equalsIgnoreCase(userId) ? chatRoom.getUserIdA() : chatRoom.getUserIdB());
    		return nrMap;
    	}).orElseGet(Collections::emptyMap);
    	
    	
    }
    
    /* 채팅방 목록 */
    public List<Map<String,Object>> getChatRoomList(Authentication authentication) {
    	
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	List<ChatRoom> crList = cor.findAllByChatRoom(userId);
    	
    	DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
    	
    	System.out.println("채팅방 목록 : "+userId + "의 채팅방 전체 목록 가져옵니다. ");
    	System.out.println(crList.stream().collect(Collectors.toList()));
    	System.out.println("---------------------------");
    	
    	return crList.stream()
    		.map(chatRoom -> {
    			Map<String, Object> nrMap = new HashMap<>();
				nrMap.put("roomId", chatRoom.getRoomId());
				nrMap.put("otherUserId", !chatRoom.getUserIdA().equalsIgnoreCase(userId) ? chatRoom.getUserIdA() : chatRoom.getUserIdB());
				nrMap.put("roomUpdatedT", chatRoom.getRoomUpdatedT().format(fmtDateTime));
				return nrMap;
    		})
    		.collect(Collectors.toList());
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
