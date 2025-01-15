package com.example.ws_back.usr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ws_back.usr.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
