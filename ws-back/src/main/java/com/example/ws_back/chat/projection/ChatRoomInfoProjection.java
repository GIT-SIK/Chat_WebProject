package com.example.ws_back.chat.projection;

import java.sql.Timestamp;

public interface ChatRoomInfoProjection {
    String getRoomId();
    String getUserUuid();
    String getOtherUserUuid();
    String getOtherUserNickname();
    String getRoomCreatedT();
    String getRoomUpdatedT();
}