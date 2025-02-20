package com.example.ws_back.chat;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import com.example.ws_back.usr.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_CHATROOM_MA")
@DynamicInsert
public class ChatRoom {

    @Id
    @Column(name = "ROOM_ID")
    private String roomId;
    
    @Column(name = "USER_ID_A")
    private String userIdA;
    
    @Column(name = "USER_ID_B")
    private String userIdB;
    
    @Column(name = "ROOM_CREATED_T")
    private LocalDateTime roomCreatedT;
    
    @Column(name = "ROOM_UPDATED_T")
    private LocalDateTime roomUpdatedT;
	
}
