package com.example.ws_back.chat;

import java.util.List;
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
    
    public boolean createChatRoom(ChatRoomDto chatRoomDto) {
    	try {
    		chatRoomDto.setRoomId(UUID.randomUUID().toString());
    		cor.save(modelMapper.map(chatRoomDto ,ChatRoom.class));
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
    
    public ChatRoom getChatRoom(String otherUserId, Authentication authentication) {
    	
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	List<ChatRoom> crList = cor.findAllByChatRoom(userId);
    	System.out.println("----LIST A----");
    	System.out.println(crList.stream().collect(Collectors.toList()));
    	System.out.println("------------");
    	
    	if(!crList.stream()
    	.anyMatch(chatRoom -> otherUserId.equals(chatRoom.getUserIdA()) || otherUserId.equals(chatRoom.getUserIdB()))) {
    		ChatRoomDto chatRoomDto = new ChatRoomDto();
    		chatRoomDto.setUserIdA(userId);
    		chatRoomDto.setUserIdB(otherUserId);
    		createChatRoom(chatRoomDto);
    		log.info("채팅방을 생성하였습니다.");
    		crList = cor.findAllByChatRoom(userId);
    	}
    	
    	System.out.println("----LIST B----");
    	System.out.println(crList.stream().collect(Collectors.toList()));
    	System.out.println("------------");
    	
    	return crList.stream()
    			.filter(chatRoom -> otherUserId.equals(chatRoom.getUserIdA()) || otherUserId.equals(chatRoom.getUserIdB()))
    			.findFirst()
    			.orElseThrow(() -> new IllegalArgumentException());
    }
    
//    public List<ChatRoom> getChatRoomList(String UserId) {
//    	return cor.findAllByChatRoom
//    	
//    }
    
}
