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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ws_back.security.CustomUserDetails;
import com.example.ws_back.usr.UserDto;
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
	/* OracleDB */
    private final ChatOracleRepository cor;
    
    /* MongoDB */
    private final ChatMongoRepository cmr;
    
	/* REDIS */
	private final RedisTemplate<String, Object> redisTemplate;
	
	/* DTO <-> Entity */
    private final ModelMapper modelMapper;
    
    /* MESSAGE */
	private final SimpMessagingTemplate smt; 
	
    
	/* ************************* 채팅방 처리 **************************************** */
    /* 채팅방 생성 */
    public boolean createChatRoom(ChatRoomDto chatRoomDto) {
    	try {
    		chatRoomDto.setRoomId(UUID.randomUUID().toString());
    		cor.save(modelMapper.map(chatRoomDto ,ChatRoom.class));
    		return true;
    	} catch (Exception e) {
    		return false;
    	}
    }
    
    /* 채팅방 정보 조회 */
    public Map<String, Object> getChatRoom(String otherUserId, Authentication authentication) {
    	
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	List<ChatRoom> crList = cor.findAllByChatRoom(userId);
    	
    	/* 채팅방 생성 로직 */
    	if(!crList.stream()
    	.anyMatch(chatRoom -> otherUserId.equalsIgnoreCase(chatRoom.getUserIdA()) || otherUserId.equalsIgnoreCase(chatRoom.getUserIdB()))) {
    		ChatRoomDto chatRoomDto = new ChatRoomDto();
    		chatRoomDto.setUserIdA(userId);
    		chatRoomDto.setUserIdB(otherUserId);
    		if(createChatRoom(chatRoomDto)) {;
	    		log.info("채팅방을 생성하였습니다.");
	    		crList = cor.findAllByChatRoom(userId);
    		} else {
    			log.info("채팅방 생성도중 오류가 발생습니다.");
    		}
    		
    	}
    	
    	return crList.stream()
		.filter(chatRoom -> otherUserId.equals(chatRoom.getUserIdA()) || otherUserId.equals(chatRoom.getUserIdB()))
		.findFirst()
    	.map(chatRoom -> {
    		Map<String,Object> nrMap = new HashMap<>();
    		nrMap.put("roomId", chatRoom.getRoomId());
    		nrMap.put("otherUserId", !chatRoom.getUserIdA().equalsIgnoreCase(userId) ? chatRoom.getUserIdA() : chatRoom.getUserIdB());
    		return nrMap;
    	}).orElseGet(Collections::emptyMap);
    	
    	
    }
    
    /* 채팅방 목록 */
    public List<Map<String,Object>> getChatRoomList(Authentication authentication) {
    	
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	List<ChatRoom> crList = cor.findAllByChatRoom(userId);
    	
    	DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");
    	
    	System.out.println("채팅방 목록 : "+userId + "의 채팅방 전체 목록 가져옵니다. ");
    	System.out.println(crList.stream().collect(Collectors.toList()));
    	System.out.println("---------------------------");
    	
    	return crList.stream()
    		.map(chatRoom -> {
    			Map<String, Object> nrMap = new HashMap<>();
				nrMap.put("roomId", chatRoom.getRoomId());
				nrMap.put("otherUserId", !chatRoom.getUserIdA().equalsIgnoreCase(userId) ? chatRoom.getUserIdA() : chatRoom.getUserIdB());
				nrMap.put("roomUpdatedT", chatRoom.getRoomUpdatedT().format(fmtDateTime));
				return nrMap;
    		})
    		.collect(Collectors.toList());
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
    	smt.convertAndSend("/api/chat/receive/" + chatDto.getRoomId(), chatDto);
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
    
    /* [get] Redis -> [save] MongoDB  */
    @Transactional
    public void saveMessageToMongo(String roomId) {
    	log.info("MongoDB 데이터를 저장합니다");
        String key = "roomid:" + roomId;
        List<Object> rMessages = redisTemplate.opsForList().range(key, 0, -1);

        if (rMessages != null && !rMessages.isEmpty()) {
        	/* MongoDB 데이터 저장 (Object -> Chat 변환) */
        	cmr.saveAll(rMessages.stream().map( obj -> {
    			ObjectMapper om = new ObjectMapper();
    			om.registerModule(new JavaTimeModule());
    			return om.convertValue(obj, Chat.class);		
        	}).collect(Collectors.toList())); 
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
    /* Redis 재부팅, OFF시 모든 채팅내역 저장 */
    /* */
    public String saveAllMessagesToMongo() {
        log.info("MongoDB로 모든 채팅 데이터를 저장합니다.");
        Set<String> keys = redisTemplate.keys("roomid:*");
        if (keys != null && !keys.isEmpty()) {
            List<Chat> allMessages = new ArrayList<>();
            /* Redis -> List<Chat> */
            for (String key : keys) {
                List<Object> rMessages = redisTemplate.opsForList().range(key, 0, -1);
                
                if (rMessages != null && !rMessages.isEmpty()) {
                    /* Object -> Chat 변환 */
                    ObjectMapper om = new ObjectMapper();
                    om.registerModule(new JavaTimeModule());

                    List<Chat> chatList = rMessages.stream()
                        .map(obj -> om.convertValue(obj, Chat.class))
                        .collect(Collectors.toList());

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
