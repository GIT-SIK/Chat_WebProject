package com.example.ws_app.frnd;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ws_back.WsAppApplication;
import com.example.ws_back.chat.ChatServiceImpl;
import com.example.ws_back.frnd.FriendInfoDto;
import com.example.ws_back.frnd.FriendServiceImpl;
import com.example.ws_back.usr.UserServiceImpl;

@SpringBootTest(classes = WsAppApplication.class)
public class ServiceImplTest {

    @Autowired
    private FriendServiceImpl fsi;
    
    @Autowired
    private UserServiceImpl usi;
    
    @Autowired
    private ChatServiceImpl csi;

    private static final String USER_UUID = "user-46eb5085-5acf-49df-a6a4-6afebebb9757";
    private static final String OTHER_USER_UUID = "user-38eb3585-5acf-49df-a6a4-6afebebb9757";
    
    @Test
    public void friendServiceMethod() {
    	
    	/* 유저 UUID에 대한 친구 조회 (친구목록 (가공) 쿼리 사용)*/
	    List<FriendInfoDto> FriendInfoDto = fsi.getUserFriendList(USER_UUID);

	    FriendInfoDto.forEach(dto -> {
            System.out.print(dto.getFriendNickname() + " ");
            System.out.print(dto.getFriendUuid() + " ");
            System.out.print(dto.isSender() + " ");
            System.out.print(dto.getFriendStatus() + " ");
            System.out.print(dto.getFriendRequestedAt() + " ");
            System.out.print(dto.getFriendAcceptedAt());
            System.out.println();
        });
    }
}
