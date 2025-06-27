package com.example.ws_back.chat.projection;


public interface ChatRoomProjection {
    String getRoomId();
    String getOtherUserUuid();
    String getOtherUserNickname();
    String getRoomUpdatedT();
}