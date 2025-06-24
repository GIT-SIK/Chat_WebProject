/* DB : Oracle */

package com.example.ws_back.frnd;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ws_back.frnd.projection.FriendProjection;

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
	 * 친구 등록 목록 (전체) (가공)
	 * friendUuid, friendNickname, friendStatus, friendAcceptedAt, friendRequestedAt, isSender
	 * 
	 */
	@Query(value = """
		    SELECT
		      CASE 
		        WHEN LOWER(tfm.SENDER_USER_UUID) = LOWER(:userUuid) THEN LOWER(tfm.RECEIVER_USER_UUID)
		        ELSE LOWER(tfm.SENDER_USER_UUID)
		      END AS friendUuid,
		      tum.USER_NICKNAME AS friendNickname,
		      tfm.FRIEND_STATUS AS friendStatus,
		      tfm.FRIEND_ACCEPTED_AT AS friendAcceptedAt,
		      tfm.FRIEND_REQUESTED_AT AS friendRequestedAt, 
		        CASE
			    WHEN LOWER(tfm.SENDER_USER_UUID) = LOWER(:userUuid) THEN 'true'
			    ELSE 'false'
			  END AS isSender
		    FROM TB_FRIEND_MA tfm
		    JOIN TB_USER_MA tum ON LOWER(tum.USER_UUID) = 
		      CASE 
		        WHEN LOWER(tfm.SENDER_USER_UUID) = LOWER(:userUuid) THEN LOWER(tfm.RECEIVER_USER_UUID)
		        ELSE LOWER(tfm.SENDER_USER_UUID)
		      END
		    WHERE LOWER(:userUuid) IN (LOWER(tfm.SENDER_USER_UUID), LOWER(tfm.RECEIVER_USER_UUID))
		    """, nativeQuery = true)
		List<FriendProjection> findAllByFriendWithNickname(@Param("userUuid") String userUuid);
	
	
		
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
