package com.example.ws_back.notification;

import java.time.LocalDateTime;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_NOTIFICATION_MA")
@DynamicInsert
public class Notification {
	@Id
	@Column(name="NOTIFICATION_ID")
	private Long notificationId;
	
	@Column(name="USER_ID")
	private String userId;
	
	@Column(name="NOTIFICATION_MESSAGE")
	private String notificationMessage;
	
	@Column(name="NOTIFICATION_CREATED_AT")
    private LocalDateTime notiCreatedAt;
	
	@Column(name="IS_READ")
	private String isRead;
	
	@Column(name="ACTION_URL")
	private String actionUrl;
}
