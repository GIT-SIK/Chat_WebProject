package com.example.ws_back.notification;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
	public List<NotificationDto> getNotificationList(String userId);
	public String saveNotification(NotificationDto notificationDto);
	public SseEmitter sseSubscribe(String id);
	public void sendToClient(String id, Object data);
	public NotificationDto createNotification(String userId, String notificationMessage, String url);
}
