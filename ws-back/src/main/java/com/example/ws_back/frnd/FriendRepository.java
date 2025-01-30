package com.example.ws_back.frnd;

import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
	
	/**
	 * 친구 수락, 거절
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_FRIEND_MA SET FRIEND_ACCEPTED_AT = :friendAcceptedAt, FRIEND_STATUS = :friendStatus " +
	               "WHERE SENDER_USER_ID = :senderUserId AND RECEIVER_USER_ID = :receiverUserId", 
	       nativeQuery = true)
	int updateFriendRequestStatus(@Param("friendAcceptedAt") Timestamp friendAcceptedAt, 
	                               @Param("senderUserId") String senderUserId, 
	                               @Param("receiverUserId") String receiverUserId,
	                               @Param("friendStatus") String status );
}
