package com.example.ws_back.frnd.projection;


public interface FriendProjection {
	String getFriendUuid();
	String getFriendNickName();
	String getFriendStatus();
	String getFriendAcceptedAt();
	String getFriendRequestedAt();
	Boolean getIsSender();

}