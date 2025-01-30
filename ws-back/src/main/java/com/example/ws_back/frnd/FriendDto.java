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
	private Long Seq;
	private String SenderUserId;
	private String ReceiverUserId;
	private String FriendStatus;
	private String FriendRequestedAt;
	private String FriendAcceptedAt;
}
