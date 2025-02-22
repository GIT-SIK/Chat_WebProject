/* DB : Oracle */


package com.example.ws_back.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatOracleRepository extends JpaRepository<ChatRoom, Long>{
  /* ID는 LOWER로 비교할 것.*/
	/**
	 * 채팅방 목록 (전체)
	 * 최신 채팅 업데이트 순
	 */
	@Query(value = "SELECT * FROM TB_CHATROOM_MA WHERE (LOWER(USER_ID_A) = LOWER(:userId) OR LOWER(USER_ID_B) = LOWER(:userId)) ORDER BY ROOM_UPDATED_T DESC", nativeQuery = true)
	List<ChatRoom> findAllByChatRoom(@Param("userId") String userId);
}
