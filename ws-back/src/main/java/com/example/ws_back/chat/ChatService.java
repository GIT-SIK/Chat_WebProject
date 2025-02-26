package com.example.ws_back.chat;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

public interface ChatService {

	public Map<String, Object> getChatRoom(String otherUserId, Authentication authentication);
	public List<Map<String,Object>> getChatRoomList(Authentication authentication);
	public void chatMessage(ChatDto chatDto);
	public List<ChatDto> getChatMessage(String roomId);
}
