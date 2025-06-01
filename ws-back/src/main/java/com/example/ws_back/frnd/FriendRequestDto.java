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
/* 친구 요청/신청 처리 가공하는 DTO */
/* 
 * Request - Respond가 일부 포함되어 있어 통합하여 사용함.
 */
public class FriendRequestDto {
	private Long seq;
	private String senderUserUuid;
	private String receiverUserUuid;
	private String friendStatus;
	private String friendRequestedAt;
	private String friendAcceptedAt;
}