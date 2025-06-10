package com.example.ws_back.frnd;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    /* Boolean 타입은 Lombok 으로 인하여 isSender → sender 변경되어 반환함 */
    @JsonProperty("isSender")
    private boolean isSender;
	private String friendStatus;
	private String friendRequestedAt;
	private String friendAcceptedAt;
}
