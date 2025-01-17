package com.example.ws_back.usr;

public interface UserService {

	public boolean Signup(User user);
	public boolean isNickValid(String nickname);
	public boolean isIdValid(String id);
}
