/* DB : Oracle */

package com.example.ws_back.usr;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/* ID, NICKNAME 은 LOWER 로 처리할 것. */
	/* 다만 Entity 에서는 아이디만 그렇게 설정함. */
	
	/*
	 *  닉네임 및 아이디 체크 
	 */
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE LOWER(USER_NICKNAME) LIKE LOWER(:nickname)", nativeQuery = true)
	boolean existsByUserNickName(String nickname);
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE LOWER(USER_ID) LIKE LOWER(:id)", nativeQuery = true)
	boolean existsByUserId(String id);
		
	/*
	 * 유저 저장
	 * 사용 : 마이페이지
	 */
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE TB_USER_MA u SET u.USER_NICKNAME = NVL(:userNickname, u.USER_NICKNAME), u.IS_PUBLIC = NVL(:isPublic, u.IS_PUBLIC), u.USER_CHAT_RECEIVE_SCOPE = NVL(:userChatReceiveScope, u.USER_CHAT_RECEIVE_SCOPE) WHERE u.USER_UUID = :userUuid", nativeQuery = true)
	int updateUserByUserUuid(String userUuid, String userNickname, String isPublic, String userChatReceiveScope);

	
	/*
	 * 유저 정보 
	 * 사용 : 로그인 (기본 사용자 정보)
	 */
	
	@Query(value = "SELECT * FROM TB_USER_MA WHERE LOWER(USER_ID) LIKE LOWER(:id)", nativeQuery = true)
	User findByUserId(String id);
	
	/*
	 * 유저 정보 
	 * 사용 : UUID (기본 사용자 정보)
	 */
	
	@Query(value = "SELECT * FROM TB_USER_MA WHERE LOWER(USER_UUID) = LOWER(:userUuid)", nativeQuery = true)
	User findByUserUuid(String userUuid);
	
	/*
	 * 유저 정보 
	 * 사용 : 닉네임 (기본 사용자 정보)
	 */
	
	@Query(value = "SELECT * FROM TB_USER_MA WHERE LOWER(USER_NICKNAME) LIKE LOWER(:userNickname)", nativeQuery = true)
	User findByNickName(String userNickname);
	
	
	/*
	 * 유저 정보 (다중)
	 * 사용 : UUID
	 * Collect 값들 -> IN 조회 -> LIST 반환 (다:다)
	 */
	
    @Query(value = "SELECT * FROM TB_USER_MA WHERE USER_UUID IN (:userUuids)", nativeQuery = true)
    List<User> findAllByUserUuidIn(@Param("userUuids") Collection<String> userUuids);
	
	
	/*
	 * 공개된 유저 목록
	 * 사용 : 친구 목록 검색
	 */
	
	@Query(
			  value = "SELECT * FROM TB_USER_MA WHERE LOWER(USER_NICKNAME) LIKE LOWER('%' || :searchNickName || '%') AND IS_PUBLIC = 'true' AND LOWER(USER_UUID) NOT LIKE LOWER(:userUuid)",
			  nativeQuery = true
			)
	List<User> findAllByVisibleUserNickname(@Param("searchNickName") String searchNickName, @Param("userUuid") String userUuid);
}
