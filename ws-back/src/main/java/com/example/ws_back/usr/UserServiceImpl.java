package com.example.ws_back.usr;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository ur;
	
	public boolean Signup(User user) {
		return true;
	};
	
	
	/**
	 *  아이디 닉네임 여부
	 *  
	 *  type : boolean 
	 *   */
	public boolean isNickValid(String nickname) {
		return ur.findByUserNickName(nickname);
	}
	public boolean isIdValid(String id) {
		return ur.findByUserId(id);
	}
}
