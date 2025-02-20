package com.example.ws_back.chat;

import org.springframework.security.core.Authentication;

public interface ChatService {

	public ChatRoom getChatRoom(String otherUserId, Authentication authentication);
}
