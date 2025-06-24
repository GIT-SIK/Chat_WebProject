package com.example.ws_back.chat;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

import com.example.ws_back.chat.projection.ChatRoomInfoProjection;
import com.example.ws_back.chat.projection.ChatRoomProjection;

public interface ChatService {

	public ChatRoomInfoProjection getChatRoom(String otherUserId, Authentication authentication);
	public List<ChatRoomProjection> getChatRoomList(Authentication authentication);
	public void chatMessage(ChatDto chatDto);
	public List<ChatDto> getChatMessage(String roomId);
	 public String saveAllMessagesToMongo();
}
