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
/* 친구 정보를 가공하는 DTO */
public class FriendInfoDto {
    private String friendUuid;
    private String friendNickname;
    private boolean isSender;
	private String friendStatus;
	private String friendRequestedAt;
	private String friendAcceptedAt;
}
