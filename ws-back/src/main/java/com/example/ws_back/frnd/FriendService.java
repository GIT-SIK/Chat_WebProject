package com.example.ws_back.frnd;

import java.util.List;

public interface FriendService {

	public boolean addFriend(FriendDto friendDto);
	public boolean respondToFriendRequest(FriendDto friendDto);
	public List<Friend> getFriendList(String UserId);
}
