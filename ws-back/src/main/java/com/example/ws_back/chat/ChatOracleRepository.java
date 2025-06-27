/* DB : Oracle */


package com.example.ws_back.chat;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ws_back.chat.projection.ChatRoomInfoProjection;
import com.example.ws_back.chat.projection.ChatRoomProjection;

@Repository
public interface ChatOracleRepository extends JpaRepository<ChatRoom,String>{
  /* ID는 LOWER로 비교할 것.*/
	/**
	 * 채팅방 목록 (전체)
	 * 최신 채팅 업데이트 순
	 */
	@Query(value = "SELECT * FROM TB_CHATROOM_MA WHERE (LOWER(USER_UUID_A) = LOWER(:userUuid) OR LOWER(USER_UUID_B) = LOWER(:userUuid)) ORDER BY ROOM_UPDATED_T DESC", nativeQuery = true)
	List<ChatRoom> findAllByChatRoom(@Param("userUuid") String userUuid);

	
	/**
	 * 채팅방 목록 (전체)(가공)
	 * 최신 채팅 업데이트 순
	 */
	@Query(value = """
		    SELECT
		      tcm.ROOM_ID AS roomId,
		      CASE 
		        WHEN LOWER(tcm.USER_UUID_A) = LOWER(:userUuid) THEN LOWER(tcm.USER_UUID_B)
		        ELSE LOWER(tcm.USER_UUID_A)
		      END AS otherUserUuid,
		      tum.USER_NICKNAME AS otherUserNickname,
		      TO_CHAR(tcm.ROOM_UPDATED_T, 'YY-MM-DD HH24:MI') AS roomUpdatedT
		    FROM TB_CHATROOM_MA tcm
		    JOIN TB_USER_MA tum ON LOWER(tum.USER_UUID) = 
		      CASE 
		        WHEN LOWER(tcm.USER_UUID_A) = LOWER(:userUuid) THEN LOWER(tcm.USER_UUID_B)
		        ELSE LOWER(tcm.USER_UUID_A)
		      END
		    WHERE LOWER(:userUuid) IN (LOWER(tcm.USER_UUID_A), LOWER(tcm.USER_UUID_B))
		    ORDER BY tcm.ROOM_UPDATED_T DESC
		    """, nativeQuery = true)
	List<ChatRoomProjection> findAllByChatRoomWithOtherNickname(@Param("userUuid") String userUuid);

	
	@Query(value = """
		    SELECT
		      tcm.ROOM_ID AS roomId,
		      CASE 
		        WHEN LOWER(tcm.USER_UUID_A) = LOWER(:userUuid) THEN LOWER(tcm.USER_UUID_A)
		        ELSE LOWER(tcm.USER_UUID_B)
		      END AS userUuid,
		      CASE 
		        WHEN LOWER(tcm.USER_UUID_A) = LOWER(:userUuid) THEN LOWER(tcm.USER_UUID_B)
		        ELSE LOWER(tcm.USER_UUID_A)
		      END AS otherUserUuid,
		      tum.USER_NICKNAME AS otherUserNickname,
		      TO_CHAR(tcm.ROOM_CREATED_T, 'YY-MM-DD HH24:MI') as roomCreatedT,
		      TO_CHAR(tcm.ROOM_UPDATED_T, 'YY-MM-DD HH24:MI') AS roomUpdatedT
		    FROM TB_CHATROOM_MA tcm
		    JOIN TB_USER_MA tum ON LOWER(tum.USER_UUID) = LOWER(:otherUserUuid)
		    WHERE 
		      (LOWER(tcm.USER_UUID_A) = LOWER(:userUuid) AND LOWER(tcm.USER_UUID_B) = LOWER(:otherUserUuid))
		      OR 
		      (LOWER(tcm.USER_UUID_B) = LOWER(:userUuid) AND LOWER(tcm.USER_UUID_A) = LOWER(:otherUserUuid))
		    """, nativeQuery = true)
	 ChatRoomInfoProjection findbyChatRoomWithOtherNickname(@Param("userUuid") String userUuid, @Param("otherUserUuid") String otherUserUuid);
}
