package com.example.ws_back.frnd;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.example.ws_back.usr.User;

public interface FriendService {

	public String addFriend(String userId, Authentication authentication);
	public boolean respondToFriendRequest(FriendDto friendDto);
	public List<Friend> getUserFriendList(String UserId);
	public List<User> getSearchFriendList(String UserId);
}
