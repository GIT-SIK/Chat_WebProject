package com.example.ws_back.frnd;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

import com.example.ws_back.usr.User;

public interface FriendService {

	public String addFriend(String userNickname, Authentication authentication);
	public boolean respondToFriendRequest(FriendRequestDto friendDto, String UserId);
	public List<FriendInfoDto> getUserFriendList(String userId);
	public List<Map<String, Object>> getSearchFriendList(String searchId, String userId);
}
