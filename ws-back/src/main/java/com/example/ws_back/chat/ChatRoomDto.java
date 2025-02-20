package com.example.ws_back.chat;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
	
	String roomId;
	String userIdA;
	String userIdB;
    LocalDateTime roomCreatedT;
    LocalDateTime roomUpdatedT;
    
}
