package com.example.ws_back.chat;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "chat")
public class Chat {
	private String roomId;
	private String senderUserId;
	private String message;
	private LocalDateTime date;
}
