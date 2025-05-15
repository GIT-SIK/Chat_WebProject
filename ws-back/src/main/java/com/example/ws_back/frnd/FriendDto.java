package com.example.ws_back.frnd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {
	private Long seq;
	private String senderUserId;
	private String receiverUserId;
	private String friendStatus;
	private String friendRequestedAt;
	private String friendAcceptedAt;
}
