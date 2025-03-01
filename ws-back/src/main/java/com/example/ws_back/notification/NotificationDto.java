package com.example.ws_back.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
	private Long notificationId;
	private String userId;
	private String notificationMessage;
    private String notiCreatedAt;
	private String isRead;
	private String actionUrl;
}
