package com.example.ws_back.usr;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserBlackToken {
	private String token;
	private String userUuid;
	private LocalDateTime logoutTime;
}