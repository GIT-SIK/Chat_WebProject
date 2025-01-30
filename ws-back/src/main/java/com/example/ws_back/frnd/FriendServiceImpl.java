package com.example.ws_back.frnd;

import java.time.ZoneId;
import java.sql.Timestamp;
import java.time.ZonedDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{
	
	private final ModelMapper modelMapper;
	private final FriendRepository fr;
	
	/**
	 * 친구 신청 처리
	 * @param FriendDto | FriendDto -> Friend 변환 후 Friend 저장
	 * @return Boolean | 친구 신청 성공 여부 반환
	 */
	public boolean addFriend(FriendDto friendDto) {
		try {
			fr.save(modelMapper.map(friendDto, Friend.class));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	/**
	 * 친구 수락 거절 처리
	 * @param friendDto
	 * @return Boolean | 친구 수락, 거절 여부 반환
	 */
	public boolean respondToFriendRequest(FriendDto friendDto) {
		try {
			ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
			Timestamp timestamp = Timestamp.from(now.toInstant());
			/* status : 
			 	ACCEPTED
				PENDING
				REJECTED
			 *  */
	        return fr.updateFriendRequestStatus(
	                timestamp, friendDto.getSenderUserId(), friendDto.getReceiverUserId(), friendDto.getFriendStatus()
	            ) > 0;
		} catch (Exception e) {
			return false;
		}
	}

	
}
