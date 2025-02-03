package com.example.ws_back.usr;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ws_back.usr.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE UPPER (USER_NICKNAME) LIKE  UPPER(:nickname)", nativeQuery = true)
	boolean existsByUserNickName(String nickname);
	
	@Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN 'true' ELSE 'false' END FROM TB_USER_MA WHERE UPPER (USER_ID) LIKE UPPER(:id)", nativeQuery = true)
	boolean existsByUserId(String id);
	
	@Query(value = "SELECT * FROM TB_USER_MA WHERE UPPER(USER_ID) LIKE UPPER(:id)", nativeQuery = true)
	User findByUserId(String id);
	
	
	@Query (value = "SELECT * FROM TB_USER_MA WHERE USER_ID LIKE :id% AND IS_PUBLIC = 'true'", nativeQuery = true)
	List<User> findAllByUserId(@Param("id") String id);
}
