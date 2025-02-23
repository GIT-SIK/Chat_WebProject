package com.example.ws_back.chat;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


public class ChatMessage {
	private String sender;
    private String text;
    private LocalDateTime date;
}