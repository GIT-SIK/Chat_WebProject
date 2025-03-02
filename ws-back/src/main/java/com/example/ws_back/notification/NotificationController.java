package com.example.ws_back.notification;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.ws_back.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/auth/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService noti;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getNotis(Authentication authentication) {
    	String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(noti.getNotificationList(userId));
    }
}