package com.example.ws_back.usr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ws_back.usr.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE UPPER (USER_NICKNAME) LIKE  UPPER(:nickname)", nativeQuery = true)
	boolean findByUserNickName(String nickname);
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE UPPER (USER_ID) LIKE UPPER(:id)", nativeQuery = true)
	boolean findByUserId(String id);
}
