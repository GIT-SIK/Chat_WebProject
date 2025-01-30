package com.example.ws_back.frnd;


public interface FriendService {

	public boolean addFriend(FriendDto friendDto);
	public boolean respondToFriendRequest(FriendDto friendDto);
}
