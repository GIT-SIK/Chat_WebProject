package com.example.ws_back.frnd;

import java.util.List;

import com.example.ws_back.usr.User;

public interface FriendService {

	public boolean addFriend(FriendDto friendDto);
	public boolean respondToFriendRequest(FriendDto friendDto);
	public List<Friend> getUserFriendList(String UserId);
	public List<User> getSearchFriendList(String UserId);
}
