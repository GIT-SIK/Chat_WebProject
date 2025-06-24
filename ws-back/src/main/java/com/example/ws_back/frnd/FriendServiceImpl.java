package com.example.ws_back.frnd;

import java.time.ZoneId;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.ws_back.frnd.projection.FriendProjection;
import com.example.ws_back.notification.NotificationDto;
import com.example.ws_back.notification.NotificationService;
import com.example.ws_back.security.CustomUserDetails;
import com.example.ws_back.usr.User;
import com.example.ws_back.usr.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendServiceImpl implements FriendService{
	
	/* status : 
					 	ACCEPTED
						PENDING
						REJECTED
	*/
	
	private final ModelMapper modelMapper;
	private final FriendRepository fr;
	private final UserRepository ur;
	
	/* 알람 */
	private final NotificationService noti;
	
	
	/**
	 * 친구 목록 가져오기 (유저 친구)(모든 목록 : 친구 신청 여부 X)
	 * @param userUuid | 유저 UUID
	 * @return List<FriendInfoDto> | 친구 목록 반환
	 */
	public List<FriendInfoDto> getUserFriendList(String userUuid) {
	    List<FriendProjection> projections = fr.findAllByFriendWithNickname(userUuid);
	    return projections.stream()
	        .map(p -> FriendInfoDto.builder()
	            .friendUuid(p.getFriendUuid())
	            .friendNickname(p.getFriendNickName())
	            .friendStatus(p.getFriendStatus())
	            .friendRequestedAt(p.getFriendRequestedAt())
	            .friendAcceptedAt(p.getFriendAcceptedAt())
	            .isSender(p.getIsSender())
	            .build())
	        .collect(Collectors.toList());
	}


	
	/** 
	 * 친구 검색 목록 가져오기 (검색)
	 * @param userUuid | 검색 유저 닉네임
	 * @return List<User> | 유저 목록 반환 (공개된 유저만)
	 */
	
	public List<Map<String, Object>> getSearchFriendList(String searchNickName, String userUuid) {
	    return ur.findAllByVisibleUserNickname(searchNickName, userUuid).stream()
	            .map(user -> {
	                Map<String, Object> map = new HashMap<>();
	                map.put("userNickname", user.getUserNickname());
	                map.put("userUuid", user.getUserUuid());
	                map.put("userCreatedAt", user.getUserCreatedAt());
	                return map;
	            })
	            .toList();
	}

	
	
	/**
	 * 친구 신청 처리
	 * @param FriendDto | FriendDto -> Friend 변환 후 Friend 저장
	 * @return String | 친구 신청 시 확인 문구 반환
	 */
	public String addFriend(String userUuid, Authentication authentication ) {
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		Timestamp timestamp = Timestamp.from(now.toInstant());
		System.out.println(userUuid);
		try {		
			User friendData = ur.findByUserUuid(userUuid);
			String receiverUserUuid = friendData.getUserUuid();
			String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
			
			List<Friend> friendList = fr.findAllByFriend(userId);
			Friend friend = friendList.stream()
					.filter(f -> receiverUserUuid.equalsIgnoreCase(f.getSenderUserUuid()) ||
							receiverUserUuid.equalsIgnoreCase(f.getReceiverUserUuid()))
				    .findFirst()
				    .orElse(null);
			
			if(friend == null) {
				FriendRequestDto friendRequestDto = new FriendRequestDto();
				friendRequestDto.setSenderUserUuid(userId);
				friendRequestDto.setReceiverUserUuid(receiverUserUuid);
				friendRequestDto.setFriendStatus("PENDING");
				fr.save(modelMapper.map(friendRequestDto, Friend.class));
			} else if(friend.getFriendStatus().equalsIgnoreCase("REJECTED")) {
				fr.updateFriendRequestStatus(timestamp, userId, receiverUserUuid, "PENDING");
				
			} else {
				return "이미 등록된 친구입니다.";
			}
			/* 친구 추가 알람 */
			try {
			NotificationDto nd = noti.createNotification(receiverUserUuid, ((CustomUserDetails) authentication.getPrincipal()).getUserNickname() +"님이 친구 신청하였습니다.", "/auth/friend");
			noti.sendToClient(receiverUserUuid, nd);
			} catch (RuntimeException e) {
				if(e.getCause() instanceof NullPointerException) {
					/* 접속 중이지 않은 친구 알람 처리 로직 */
				}
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
	public boolean respondToFriendRequest(FriendRequestDto friendRequestDto, String userUuid) {
		try {
			ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
			Timestamp timestamp = Timestamp.from(now.toInstant());
	        return fr.updateFriendRequestStatus(
	                timestamp, friendRequestDto.getSenderUserUuid(), userUuid, friendRequestDto.getFriendStatus()
	            ) > 0;
		} catch (Exception e) {
			return false;
		}
	}

	
}
