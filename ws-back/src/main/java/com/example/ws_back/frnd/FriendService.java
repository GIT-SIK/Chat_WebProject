package com.example.ws_back.frnd;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.example.ws_back.usr.User;

public interface FriendService {

	public String addFriend(String receiverUserId, Authentication authentication);
	public boolean respondToFriendRequest(FriendDto friendDto, String UserId);
	public List<Friend> getUserFriendList(String userId);
	public List<User> getSearchFriendList(String searchId, String userId);
}
