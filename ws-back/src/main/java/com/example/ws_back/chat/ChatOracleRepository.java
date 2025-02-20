/* DB : Oracle */


package com.example.ws_back.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatOracleRepository extends JpaRepository<ChatRoom, Long>{
 
	/**
	 * 채팅방 목록 (전체)
	 */
	@Query(value = "SELECT * FROM TB_CHATROOM_MA WHERE (USER_ID_A = :userId OR USER_ID_B = :userId)", nativeQuery = true)
	List<ChatRoom> findAllByChatRoom(@Param("userId") String userId);
}
