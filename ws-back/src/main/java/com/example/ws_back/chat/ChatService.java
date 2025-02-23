package com.example.ws_back.chat;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

public interface ChatService {

	public ChatRoom getChatRoom(String otherUserId, Authentication authentication);
	public List<Map<String,Object>> getChatRoomList(Authentication authentication);
	public String chatMessage(ChatDto chatDto);

}
