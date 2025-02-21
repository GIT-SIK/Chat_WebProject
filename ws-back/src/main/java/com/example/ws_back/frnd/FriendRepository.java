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
	 * 친구 등록 목록 (ACCEPTED)
	 */
	
	@Query(value = "SELECT * FROM TB_FRIEND_MA WHERE (LOWER(SENDER_USER_ID) = LOWER(:userId) OR LOWER(RECEIVER_USER_ID) = LOWER(:userId)) AND FRIEND_STATUS = 'ACCEPTED'", nativeQuery = true)
	List<Friend> findAllByFriend(@Param("userId") String UserId);
	
	/**
	 * 친구 등록 목록 (전체)
	 */
	@Query(value = "SELECT * FROM TB_FRIEND_MA WHERE (LOWER(SENDER_USER_ID) = LOWER(:userId) OR LOWER(RECEIVER_USER_ID) = LOWER(:userId))", nativeQuery = true)
	List<Friend> findAllByAddFriend(@Param("userId") String UserId);
	
	
	/**
	 * 친구 수락, 거절
	 */
	@Transactional
	@Modifying
	@Query(value = "UPDATE TB_FRIEND_MA SET FRIEND_ACCEPTED_AT = :friendAcceptedAt, FRIEND_STATUS = :friendStatus " +
	               "WHERE LOWER(SENDER_USER_ID) = LOWER(:senderUserId) AND LOWER(RECEIVER_USER_ID) = LOWER(:receiverUserId)", 
	       nativeQuery = true)
	int updateFriendRequestStatus(@Param("friendAcceptedAt") Timestamp friendAcceptedAt, 
	                               @Param("senderUserId") String senderUserId, 
	                               @Param("receiverUserId") String receiverUserId,
	                               @Param("friendStatus") String status );
}
