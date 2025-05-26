/* DB : Oracle */

package com.example.ws_back.frnd;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
	/* ID, NICKNAME은 LOWER 로 처리할 것. */
	
	/**
	 * 친구 등록 목록 (전체)
	 */
	
	@Query(value = "SELECT * FROM TB_FRIEND_MA WHERE (LOWER(SENDER_USER_UUID) = LOWER(:userUuid) OR LOWER(RECEIVER_USER_UUID) = LOWER(:userUuid))", nativeQuery = true)
	List<Friend> findAllByFriend(@Param("userUuid") String userUuid);
	
	
	/**
	 * 친구 수락, 거절
	 */
	@Transactional
	@Modifying
	@Query(value = """
	    UPDATE TB_FRIEND_MA
	    SET 
	        FRIEND_ACCEPTED_AT = :friendAcceptedAt,
	        FRIEND_STATUS = :friendStatus,
	        SENDER_USER_UUID = CASE 
	            WHEN LOWER(SENDER_USER_UUID) = LOWER(:receiverUserUuid) AND LOWER(RECEIVER_USER_UUID) = LOWER(:senderUserUuid)
	            THEN :senderUserUuid ELSE SENDER_USER_UUID END,
	        RECEIVER_USER_UUID = CASE 
	            WHEN LOWER(SENDER_USER_UUID) = LOWER(:receiverUserUuid) AND LOWER(RECEIVER_USER_UUID) = LOWER(:senderUserUuid)
	            THEN :receiverUserUuid ELSE RECEIVER_USER_UUID END
	    WHERE 
	        (LOWER(SENDER_USER_UUID) = LOWER(:senderUserUuid) AND LOWER(RECEIVER_USER_UUID) = LOWER(:receiverUserUuid)) 
	        OR 
	        (LOWER(SENDER_USER_UUID) = LOWER(:receiverUserUuid) AND LOWER(RECEIVER_USER_UUID) = LOWER(:senderUserUuid))
	""", nativeQuery = true)
	int updateFriendRequestStatus(@Param("friendAcceptedAt") Timestamp friendAcceptedAt,
	                              @Param("senderUserUuid") String senderUserUuid,
	                              @Param("receiverUserUuid") String receiverUserUuid,
	                              @Param("friendStatus") String status);

}
