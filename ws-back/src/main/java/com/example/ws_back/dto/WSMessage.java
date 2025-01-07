package com.example.ws_back.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class WSMessage {
	private String sender;
    private String text;
    private LocalDateTime date;
}