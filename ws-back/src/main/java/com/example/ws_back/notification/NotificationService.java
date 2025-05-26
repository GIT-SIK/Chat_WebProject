package com.example.ws_back.notification;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
	public List<NotificationDto> getNotificationList(String userUuid);
	public String saveNotification(NotificationDto notificationDto);
	public SseEmitter sseSubscribe(String userUuid);
	public void sendToClient(String userUuid, Object data);
	public NotificationDto createNotification(String userUuid, String notificationMessage, String url);
}
