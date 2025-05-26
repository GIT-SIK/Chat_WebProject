package com.example.ws_back.frnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
	/*
	 * @AuthenticationPrincipal 의 Username은 UUID로 통합
	 * 검색 및 모든 사용자 정보는 UUID나 NICKNAME으로 처리할 것.
	 * ** 5.25 **
	 */
	
	
	/**
	 * 친구 목록 가져오기
	 * @param userDto
	 * @return List<FriendInfoDto> : 친구 리스트 반환 (상태 : ACCEPTED 인 경우)
	 */
	
	@RequestMapping(value = "/gfriend", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getUserFriendList(@RequestBody UserDto userDto) {
		log.info(userDto.getUserUuid() +"님의 친구 목록을 가져옵니다.");
		List<FriendInfoDto> friendList = fs.getUserFriendList(userDto.getUserUuid());
		
		return ResponseEntity.ok(!friendList.isEmpty() ? friendList : new ArrayList<>());
	}
	
	/**
	 * (검색) 친구 목록 가져오기
	 * @param String : 검색할 단어
	 * @return List<User> : 검색한 친구 리스트 반환 (IS_PUBLIC 이 true 인 경우)
	 */
	@RequestMapping(value = "/sfriend", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseEntity<?> getSearchFriendList (@RequestParam String search, @AuthenticationPrincipal UserDetails userDetails) {
		List<Map<String, Object>> searchUserList = fs.getSearchFriendList(search, userDetails.getUsername());
		return ResponseEntity.ok(!searchUserList.isEmpty() ? searchUserList : new ArrayList<>());
	}
	
	/**
	 * 친구 수락 거절 처리
	 * @param friendDto
	 * @return Boolean | 친구 수락, 거절 여부 반환
	 */
	@RequestMapping(value = "/ufriend", method = RequestMethod.POST) 
	@ResponseBody
	public ResponseEntity<?> respondToFriendRequest(@RequestBody FriendRequestDto friendRequestDto, @AuthenticationPrincipal UserDetails userDetails) {
		return fs.respondToFriendRequest(friendRequestDto, userDetails.getUsername()) ? ResponseEntity.ok(true) : ResponseEntity.status(500).body(false);	
	}
	
	/**
	 * 친구 (추가) 신청 처리
	 * @param friendDto | 상대방 아이디
	 * @return String | 친구 신청 시 확인 문구 반환 
	 */
	@RequestMapping(value = "/cfriend", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> addFriend(@RequestParam("add") String userNickname, Authentication authentication) {
		log.info("등록할 친구닉네임 : " + userNickname);
		
		String response = fs.addFriend(userNickname, authentication);
		log.info(response);
		return ResponseEntity.ok(response);	
	}
	
}
