package com.example.ws_back.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>{

	/**
	 * 알림 목록 
	 */
	@Query(value = "SELECT * FROM TB_NOTIFICATION_MA WHERE (LOWER(USER_UUID) = LOWER(:userUuid)", nativeQuery = true)
	List<Notification> findAllByUserUuid(@Param("userUuid") String userUuid);
	
}
