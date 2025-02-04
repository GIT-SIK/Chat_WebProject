package com.example.ws_back.frnd;

import java.time.ZoneId;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.ws_back.security.CustomUserDetails;
import com.example.ws_back.usr.User;
import com.example.ws_back.usr.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService{
	
	private final ModelMapper modelMapper;
	private final FriendRepository fr;
	private final UserRepository ur;
	
	
	/**
	 * 친구 목록 가져오기
	 * @param UserId | 유저 아이디
	 * @return List<Friend> | 친구 목록 반환
	 */
	public List<Friend> getUserFriendList(String UserId) {
		return fr.findAllByFriend(UserId);
		
	}
	
	public List<User> getSearchFriendList(String UserId) {
		return ur.findAllByUserId(UserId);
		
	}
	
	
	
	/**
	 * 친구 신청 처리
	 * @param FriendDto | FriendDto -> Friend 변환 후 Friend 저장
	 * @return String | 친구 신청 시 확인 문구 반환
	 */
	public String addFriend(String userId, Authentication authentication ) {
		try {
			FriendDto friendDto = new FriendDto();
			friendDto.setSenderUserId(((CustomUserDetails) authentication.getPrincipal()).getUsername());
			friendDto.setReceiverUserId(userId);
			friendDto.setFriendStatus("PENDING");
			
		
			if(!fr.findAllByAddFriend(userId)
					.stream()
					.anyMatch(friend -> userId.equals(friend.getSenderUserId()) || userId.equals(friend.getReceiverUserId()))) {
			fr.save(modelMapper.map(friendDto, Friend.class));
			} else {
				return "이미 등록된 친구입니다.";
			}
			return "친구가 추가되었습니다.";
		} catch (Exception e) {
			return "친구 추가 중 오류가 발생되었습니다.";
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
