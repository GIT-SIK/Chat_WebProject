package com.example.ws_back.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ChatRoom getChatRoom(String otherUserId, Authentication authentication) {
    	
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	List<ChatRoom> crList = cor.findAllByChatRoom(userId);
    	
    	/* 채팅방 생성 로직 */
    	if(!crList.stream()
    	.anyMatch(chatRoom -> otherUserId.equals(chatRoom.getUserIdA()) || otherUserId.equals(chatRoom.getUserIdB()))) {
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
    	System.out.println("채팅방 정보 : " + userId + "의 채팅방 전체 목록 가져옵니다. ");
    	System.out.println(crList.stream().collect(Collectors.toList()));
    	System.out.println("---------------------------");

    	return crList.stream()
    			.filter(chatRoom -> otherUserId.equals(chatRoom.getUserIdA()) || otherUserId.equals(chatRoom.getUserIdB()))
    			.findFirst()
    			.orElseThrow(() -> new IllegalArgumentException());
    }
    
    /* 채팅방 목록 */
    public List<Map<String,Object>> getChatRoomList(Authentication authentication) {
    	
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	List<ChatRoom> crList = cor.findAllByChatRoom(userId);
    	
    	DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
    	
    	System.out.println("채팅방 목록 : "+userId + "의 채팅방 전체 목록 가져옵니다. ");
    	crList.stream().collect(Collectors.toList());
    	System.out.println("---------------------------");
    	return crList.stream()
    		.map(chatRoom -> {
    			Map<String, Object> nrMap = new HashMap<>();
				nrMap.put("roomId", chatRoom.getRoomId());
				nrMap.put("otherUserId", !chatRoom.getUserIdA().equals(userId) ? chatRoom.getUserIdA() : chatRoom.getUserIdB());
				nrMap.put("roomUpdatedT", chatRoom.getRoomUpdatedT().format(fmtDateTime));
				return nrMap;
    		})
    		.collect(Collectors.toList());
    }
}
