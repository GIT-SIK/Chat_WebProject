package com.example.ws_back.notification;

import java.util.List;

public interface NotificationService {
	public List<NotificationDto> getNotificationList(String userId);
	public String saveNotification(NotificationDto notificationDto);
}
