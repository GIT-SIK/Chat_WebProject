package com.example.ws_app.frnd;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.ws_back.WsAppApplication;
import com.example.ws_back.chat.ChatOracleRepository;
import com.example.ws_back.frnd.FriendRepository;
import com.example.ws_back.frnd.projection.FriendProjection;
import com.example.ws_back.usr.UserRepository;

@SpringBootTest(classes = WsAppApplication.class)
@Transactional
public class RepositoryTest {

    @Autowired
    private FriendRepository fr;
    
    @Autowired
    private UserRepository ur;
    
    @Autowired
    private ChatOracleRepository cor;
    
    private static final String USER_UUID = "user-46eb5085-5acf-49df-a6a4-6afebebb9757";
    private static final String OTHER_USER_UUID = "user-38eb3585-5acf-49df-a6a4-6afebebb9757";
    
    @Test
    public void friendRepositoryMethod() {

    	
    	/* 유저 UUID에 대한 친구 조회 (친구목록 (가공) 쿼리 사용)*/
    	List<FriendProjection> friends = fr.findAllByFriendWithNickname(USER_UUID);
        	 
        System.out.println("친구 수: " + friends.size());



        assertNotNull(friends);
        assertFalse(friends.isEmpty());
    }
}
