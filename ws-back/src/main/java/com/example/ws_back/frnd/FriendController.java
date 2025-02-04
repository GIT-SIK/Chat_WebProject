package com.example.ws_back.frnd;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ws_back.security.CustomUserDetails;
import com.example.ws_back.usr.User;
import com.example.ws_back.usr.UserDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class FriendController {
	private final FriendService fs;
	
	/**
	 * (검색) 친구 목록 가져오기
	 * @param String : 검색할 단어
	 * @return List<User> : 검색한 친구 리스트 반환 (IS_PUBLIC 이 true 인 경우)
	 */
	@RequestMapping(value = "/sfriend", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<?> getSearchFriendList (@RequestParam String search) {
		List<User> userList = fs.getSearchFriendList(search);
		return !userList.isEmpty() ? ResponseEntity.ok(userList) : ResponseEntity.status(500).body(false);
	}
	
	/**
	 * 친구 수락 거절 처리
	 * @param friendDto
	 * @return Boolean | 친구 수락, 거절 여부 반환
	 */
	@RequestMapping(value = "/ufriend", method = RequestMethod.POST) 
	@ResponseBody
	public ResponseEntity<Boolean> respondToFriendRequest(@RequestBody FriendDto friendDto) {
		return fs.respondToFriendRequest(friendDto) ? ResponseEntity.ok(true) : ResponseEntity.status(500).body(false);	
	}
	
	/**
	 * 친구 (추가) 신청 처리
	 * @param FriendDto | FriendDto -> Friend 변환 후 Friend 저장
	 * @return Boolean | 친구 신청 성공 여부 반환
	 */
	@RequestMapping(value = "/cfriend", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> addFriend(@RequestParam("add") String userId, Authentication authentication) {
		return ResponseEntity.ok(fs.addFriend(userId, authentication));	
	}
	
	/**
	 * 친구 목록 가져오기
	 * @param userDto
	 * @return List<Friend> : 친구 리스트 반환 (상태 : ACCEPTED 인 경우)
	 */
	
	@RequestMapping(value = "/gfriend", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getUserFriendList(@RequestBody UserDto userDto) {
		log.info(userDto.getUserId() +"님의 친구 목록을 가져옵니다.");
		List<Friend> friendList = fs.getUserFriendList(userDto.getUserId());
		for (Friend friend : friendList) {
		    System.out.println("보낸 사람 ID: " + friend.getSenderUserId() + ", 받는 사람 ID: " + friend.getReceiverUserId());
		}
		
		return !friendList.isEmpty() ? ResponseEntity.ok(friendList) : ResponseEntity.status(500).body(false);
	}
}
