package com.example.ws_back.notification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
	
	private final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
	private final ModelMapper modelMapper;
	private final NotificationRepository nr;
	
	/* * * * * * * * 비로그인 상태 : 알람 저장, 가져오기 * * * * * * * */
	
	public List<NotificationDto> getNotificationList(String userId) {
		return nr.findAllByUserId(userId).stream()
										 .map(entity -> modelMapper.map(entity, NotificationDto.class))
									     .collect(Collectors.toList());
	}
	
	public String saveNotification(NotificationDto notificationDto) {
		try {
			nr.save(modelMapper.map(notificationDto, Notification.class));
			return "알람이 저장되었습니다";
		} catch(Exception e) {
			return "알람 저장 도중 오류가 발생했습니다.";
		}	
	}
	
	/* * * * * * * *  로그인 상태 : 실시간 알람 반환 * * * * * * * */
	
	/* sse */
	
    public SseEmitter sseSubscribe(String id) {
        long timeout = 1000L * 60 * 60; // 연결 시간 : 1시간

        SseEmitter sseEmitter = new SseEmitter(timeout);
        sseEmitterMap.put(id, sseEmitter);
        
        sseEmitter.onCompletion(() -> {
            sseEmitterMap.remove(id);
        });
        sseEmitter.onTimeout(() -> {
            sseEmitter.complete();
        });
        sseEmitter.onError(throwable -> {
            sseEmitter.complete();
        });
        return sseEmitter;
    }
	
	
    public void sendToClient(String id, Object data) {
        SseEmitter sseEmitter = sseEmitterMap.get(id);
        try {
            sseEmitter.send(
                    SseEmitter
                            .event()
                            .id(id)
                            /* eventName 추가할 경우 
                              js에서 onmessage((event) => ...) 로 받는게 아닌 
                              addEventListener("evnetName", (e) => ...)로 받아야
                             */
//                          .name(eventName)
                            .data(data)                       
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
   /* 알람 DTO 생성용 메소드 */
    public NotificationDto createNotification(String userId, String notificationMessage, String url) {
    	NotificationDto nd = new NotificationDto();    
    	nd.setNotificationId(null);
    	nd.setUserId(userId);
    	nd.setActionUrl(url);
    	nd.setIsRead("false");
    	nd.setNotiCreatedAt(LocalDateTime.now());
    	nd.setNotificationMessage(notificationMessage);
    	return nd;
    }
    
    
    
    
    
    
    /* 사용자 지정 함수 */
    
    /** TimeZone UTC -> KST 변환
	 * @param LocalDateTime | UTC 시간 입력
	 * @return LocalDateTime | KST 시간 반환
     */
    public LocalDateTime UtcToKst(LocalDateTime date) {
    	return date.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();	 
    }
}
