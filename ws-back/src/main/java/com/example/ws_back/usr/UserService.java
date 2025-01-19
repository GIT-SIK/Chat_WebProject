package com.example.ws_back.usr;

public interface UserService {

	public boolean signup(UserDto userDto);
	public boolean isNickValid(String nickname);
	public boolean isIdValid(String id);
}
