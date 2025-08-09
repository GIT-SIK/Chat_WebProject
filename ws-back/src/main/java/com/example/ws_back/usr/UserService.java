package com.example.ws_back.usr;

import com.example.ws_back.usr.UserServiceImpl.LoginResponse;

public interface UserService {

	public boolean signup(UserDto userDto);
	public boolean isNickValid(String nickname);
	public boolean isIdValid(String userId);
	public boolean updateUser(UserDto userDto);
	public UserDto findByUserUuid(String uuid);
	public String UserRefreshAccessToken(String refreshToken);
	public LoginResponse login(String userId, String password);
	public void logout(String token, String userUuid);
	
}
