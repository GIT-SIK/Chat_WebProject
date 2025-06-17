package com.example.ws_back.notification;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.ws_back.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService noti;

    /** 알림 리스트 반환
     * 
     * @param authentication
     * @return List<NotificationDto> | 알림 리스트
     */
    @RequestMapping(value = "/api/notification", method = RequestMethod.GET)
    public ResponseEntity<?> getNotis(Authentication authentication) {
    	String userUuid = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(noti.getNotificationList(userUuid));
    }
    
    /** 알림 구독
     * @param UserId | 알림 받을 (구독) 유저
     * @return SseEmitter
     */
    @RequestMapping(value = "/event/notification/subscribe", method = RequestMethod.GET)
    public SseEmitter sseSubscribe(@RequestParam String userUuid, Authentication authentication) {
    	String authUserUuid = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    	
        if (!userUuid.equals(authUserUuid)) {
            throw new IllegalArgumentException("[SSE] 사용자가 일치하지 않습니다.");
        }
    	
    	return noti.sseSubscribe(authUserUuid);
    }
    
}
