/* DB : Oracle */

package com.example.ws_back.usr;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	/* ID, NICKNAME 은 UPPER 로 처리할 것. */
	
	/*
	 *  닉네임 및 아이디 체크 
	 */
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE USER_NICKNAME LIKE  UPPER(:nickname)", nativeQuery = true)
	boolean existsByUserNickName(String nickname);
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE USER_ID LIKE UPPER(:id)", nativeQuery = true)
	boolean existsByUserId(String id);
		
	/*
	 * 유저 정보 
	 * 사용 : 로그인 (기본 사용자 정보)
	 */
	
	@Query(value = "SELECT * FROM TB_USER_MA WHERE USER_ID LIKE :id", nativeQuery = true)
	User findByUserId(String id);
	
	/*
	 * 유저 정보 (공개 여부 확인) 
	 * 사용 : 친구 목록 검색
	 */
	
	@Query (value = "SELECT * FROM TB_USER_MA WHERE USER_ID LIKE :id% AND IS_PUBLIC = 'true'", nativeQuery = true)
	List<User> findAllByUserId(@Param("id") String id);
}
