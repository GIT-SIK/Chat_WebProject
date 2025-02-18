package com.example.ws_back.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.ws_back.chat.ChatMessage;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {

}

