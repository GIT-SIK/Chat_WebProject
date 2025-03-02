package com.example.ws_back.notification;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
	
	private final ModelMapper modelMapper;
	private final NotificationRepository nr;
	
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
}
