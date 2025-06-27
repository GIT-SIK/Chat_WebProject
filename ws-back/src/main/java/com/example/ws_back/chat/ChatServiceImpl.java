package com.example.ws_back.chat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ws_back.chat.projection.ChatRoomInfoProjection;
import com.example.ws_back.chat.projection.ChatRoomProjection;
import com.example.ws_back.frnd.Friend;
import com.example.ws_back.frnd.FriendRepository;
import com.example.ws_back.security.CustomUserDetails;
import com.example.ws_back.usr.User;
import com.example.ws_back.usr.UserDto;
import com.example.ws_back.usr.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.jsonwebtoken.lang.Collections;

import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
	/* OracleDB CHAT */
    private final ChatOracleRepository cor;
    
    /* MongoDB */
    private final ChatMongoRepository cmr;
    
    /* OracleDB USER */
    private final UserRepository ur;
    
    /* OracleDB Friend */
    private final FriendRepository fr;
    
	/* REDIS */
	private final RedisTemplate<String, Object> redisTemplate;
	
	/* DTO <-> Entity */
    private final ModelMapper modelMapper;
    
    /* MESSAGE */
	private final SimpMessagingTemplate smt; 
	
    
	/* ************************* 채팅방 처리 **************************************** */
    /* 채팅방 생성 */
    public boolean createChatRoom(ChatRoomDto chatRoomDto) {
    	
    	User otherUserData = ur.findByUserUuid(chatRoomDto.getUserUuidB());
    	String userChatReceiveScope = otherUserData.getUserChatReceiveScope();
    	
    	/* 메시지 수신 여부 처리 */    	
    	if(!userChatReceiveScope.equalsIgnoreCase("all")) {
			List<Friend> fl = fr.findAllByFriend(chatRoomDto.getUserUuidA());	
			
    		if(!fl.stream().anyMatch(f ->
            						f.getSenderUserUuid().equalsIgnoreCase(chatRoomDto.getUserUuidB()) ||
            						f.getReceiverUserUuid().equalsIgnoreCase(chatRoomDto.getUserUuidB())
    							   )){
    			return false;
    		}

    	} else {
    		if(otherUserData.getIsPublic().equals("false")) {
    			return false;
    		}
    	}
   
    	/* 채팅방 생성 */
    	try {
    		chatRoomDto.setRoomId("chat-" + UUID.randomUUID().toString());
    		cor.save(modelMapper.map(chatRoomDto ,ChatRoom.class));
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
    
    /* 채팅방 정보 조회 */
    public ChatRoomInfoProjection getChatRoom(String otherUserUuid, Authentication authentication) {
    	
    	String userUuid = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	
    	ChatRoomInfoProjection cr = cor.findbyChatRoomWithOtherNickname(userUuid, otherUserUuid);
 
    	
    	if(cr == null) {
    		ChatRoomDto chatRoomDto = new ChatRoomDto();
    		chatRoomDto.setUserUuidA(userUuid);
    		chatRoomDto.setUserUuidB(otherUserUuid);
    		if(createChatRoom(chatRoomDto)) {;
	    		log.info("채팅방을 생성하였습니다.");
	    		cr = cor.findbyChatRoomWithOtherNickname(userUuid, otherUserUuid);
    		} else {
    			log.info("채팅방 생성도중 오류가 발생습니다.");
    		}
    		
    	}
    	
    	return cr;
    }
    
    /* 채팅방 목록 */
    public List<ChatRoomProjection> getChatRoomList(Authentication authentication) {

        String userUuid = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
     
        List<ChatRoomProjection> chatRooms = cor.findAllByChatRoomWithOtherNickname(userUuid);
        return chatRooms;
    }

    
    /* ************************* 채팅 데이터 처리 **************************************** */
    /* 
     * 처리방향 : ChatDto(DTO) <-> Chat(Entity) <-> Redis <-> MongoDB
     *  */
    
    /* MAX_REDIS_SAVE_MESSAGES : 최근 메시지 저장할 메시지 수 (초과시 Mongo에 저장)
     * MAX_MESSAGES : 출력할 메시지 수
     */
    
// ※ WARN : MAX_REDIS_SAVE_MESSAGES 기본값보다 낮출 경우 Redis내 데이터를 flushall (삭제)해주거나 MongoDB 저장할 것. 
    
    public static final int MAX_REDIS_SAVE_MESSAGES = 20;
    public static final int MAX_MESSAGES = 20;
    
    /* 채팅 데이터 구독자에게 반환  */
    public void chatMessage(ChatDto chatDto) {
    	chatDto.setDate(UtcToKst(chatDto.getDate()));
    	saveMessageToRedis(chatDto.getRoomId(), chatDto);
    	smt.convertAndSend("/event/chat/queue/" + chatDto.getRoomId(), chatDto);
    }
    
    /* [save] Redis */
    public void saveMessageToRedis(String roomId, ChatDto message){
    	log.info("REDIS 데이터를 저장합니다");
    	 String key = "roomid:" + roomId;
    	 redisTemplate.opsForList().leftPush(key, modelMapper.map(message, Chat.class));
    	 
         if (redisTemplate.opsForList().size(key) >= MAX_REDIS_SAVE_MESSAGES) {
        	 saveMessageToMongo(roomId);
         }
    }
    
    /* [get] Redis -> [save] MongoDB / [save] OracleDB  */
    @Transactional
    public void saveMessageToMongo(String roomId) {
    	log.info("MongoDB 데이터를 저장합니다");
        String key = "roomid:" + roomId;

        List<Object> rMessages = redisTemplate.opsForList().range(key, 0, -1);

        if (rMessages != null && !rMessages.isEmpty()) {
        	/* (Object -> Chat 변환) */
        	List<Chat> chatList = rMessages.stream().map( obj -> {
    			ObjectMapper om = new ObjectMapper();
    			om.registerModule(new JavaTimeModule());
    			return om.convertValue(obj, Chat.class);		
        	}).collect(Collectors.toList());
        	
        	/* Mongo 최신 채팅 저장 */
        	cmr.saveAll(chatList); 
        	
        	/* Oracle 최신 채팅 날짜 저장 */
        	ChatRoom cr = cor.findById(roomId).orElseThrow();
        	cr.setRoomUpdatedT(chatList.get(chatList.size() - 1).getDate());
        	cor.save(cr);
        	
        	/* Redis 데이터 삭제 */
            redisTemplate.delete(key);
        }
    }
    
    /* [get] Redis, [get] MongoDB */
    public List<ChatDto> getChatMessage(String roomId){
    	log.info("REDIS 데이터를 불러옵니다");
    	String key = "roomid:" + roomId;
    	/* Redis 데이터 가져오기 (Object -> Chat 변환) */
    	List<Object> rMessages = redisTemplate.opsForList().range(key, 0, MAX_MESSAGES - 1);  
    	List<Chat> messages = rMessages.stream().map( obj -> {
			ObjectMapper om = new ObjectMapper();
			om.registerModule(new JavaTimeModule());
			return om.convertValue(obj, Chat.class);		
    	}).collect(Collectors.toList());

        if (messages.size() < MAX_MESSAGES) {
        	log.info("MongoDB 데이터를 불러옵니다.");
            List<Chat> mMessages = cmr.findByRoomIdOrderByDateDesc(roomId);
            
            int getMongoChatMessageCount = MAX_MESSAGES - messages.size();
            if(mMessages != null && !mMessages.isEmpty()) {
            messages.addAll(mMessages.subList(0,Math.min(mMessages.size(), getMongoChatMessageCount)));
            }
        } else {
        	log.error("MongoDB 데이터가 없습니다.");
        	return new ArrayList<>();
        }
        return messages.stream()
        		.sorted(Comparator.comparing(Chat::getDate))
        		.map(obj -> modelMapper.map(obj, ChatDto.class))
        		.collect(Collectors.toList());
    }
    /* [자동] Redis 모든 채팅내역 저장 (주기 5분) */
    @Scheduled(fixedRate = 300000) 
    public void autoSaveAllMessagesToMongo() {
    	log.info("[AUTO] 스케줄러");
        saveAllMessagesToMongo(); 
    }
    
    /* [수동] Redis 모든 채팅내역 저장 */  
    public String saveAllMessagesToMongo() {
        log.info("MongoDB로 모든 채팅 데이터를 저장합니다.");
        Set<String> keys = redisTemplate.keys("roomid:*");
        if (keys != null && !keys.isEmpty()) {
            List<Chat> allMessages = new ArrayList<>();
            /* Redis -> List<Chat> */
            for (String key : keys) {
                List<Object> rMessages = redisTemplate.opsForList().range(key, 0, -1);
                                    
                if (rMessages != null && !rMessages.isEmpty()) {
                	/* (Object -> Chat 변환) */
                	List<Chat> chatList = rMessages.stream().map( obj -> {
            			ObjectMapper om = new ObjectMapper();
            			om.registerModule(new JavaTimeModule());
            			return om.convertValue(obj, Chat.class);		
                	}).collect(Collectors.toList());
                	
                	/* Oracle 최신 채팅 날짜 저장 */
                	ChatRoom cr = cor.findById(key.replaceFirst("^roomid:", "")).orElseThrow();
                	cr.setRoomUpdatedT(chatList.get(chatList.size() - 1).getDate());           
                	cor.save(cr);
                	
                	allMessages.addAll(chatList);
                }
                
                
            }
            /* List<Chat> -> MongoDB */
            if (!allMessages.isEmpty()) {
                cmr.saveAll(allMessages);
                log.info("MongoDB - {}개의 메시지 저장 완료", allMessages.size());
                redisTemplate.delete(keys);
                return "Redis → MongoDB : " + allMessages.size() + "개의 메시지 저장 완료";
            }
            return "Redis → MongoDB : 저장할 메시지가 없습니다.";
        } else {
            log.info("Redis 데이터가 없습니다.");
            return "Redis → MongoDB : 저장할 메시지가 없습니다.";
        }
    }

    
    
    /* 사용자 지정 함수 */
    
    /** TimeZone UTC -> KST 변환
	 * @param LocalDateTime | UTC 시간 입력
	 * @return LocalDateTime | KST 시간 반환
     */
    public LocalDateTime UtcToKst(LocalDateTime date) {
    	return date.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();	 
    }
    
    /** TimeZone KST -> UTC 변환
	 * @param LocalDateTime | KST 시간 입력
	 * @return LocalDateTime | UTC 시간 반환
     */
    public LocalDateTime KstToUtc(LocalDateTime date) {
    	return date.atZone(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();	 
    }
}
