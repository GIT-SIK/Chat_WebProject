package com.example.ws_back.dto;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "chat")
public class WSMessage {
	private String sender;
    private String text;
    private LocalDateTime date;
}