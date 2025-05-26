package com.example.ws_back.notification;

import java.time.LocalDateTime;

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
	private String userUuid;
	private String notificationMessage;
    private LocalDateTime notiCreatedAt;
	private String isRead;
	private String actionUrl;
}
