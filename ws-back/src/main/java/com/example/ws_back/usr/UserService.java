package com.example.ws_back.usr;

import com.example.ws_back.usr.UserServiceImpl.LoginResponse;

public interface UserService {

	public boolean signup(UserDto userDto);
	public boolean isNickValid(String nickname);
	public boolean isIdValid(String userId);
	public UserDto findByUserUuid(String uuid);
	public LoginResponse login(String userId, String password);
	
}
