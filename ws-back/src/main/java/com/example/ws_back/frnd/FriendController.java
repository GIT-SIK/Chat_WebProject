package com.example.ws_back.frnd;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class FriendController {
	private final FriendService fs;
	
	/**
	 * 친구 수락 거절 처리
	 * @param friendDto
	 * @return Boolean | 친구 수락, 거절 여부 반환
	 */
	@RequestMapping(value = "/ufriend", method = RequestMethod.POST) 
	@ResponseBody
	public ResponseEntity<Boolean> respondToFriendRequest(FriendDto friendDto) {
		return fs.respondToFriendRequest(friendDto) ? ResponseEntity.ok(true) : ResponseEntity.status(500).body(false);	
	}
	
	/**
	 * 친구 (추가) 신청 처리
	 * @param FriendDto | FriendDto -> Friend 변환 후 Friend 저장
	 * @return Boolean | 친구 신청 성공 여부 반환
	 */
	@RequestMapping(value = "/cfriend", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Boolean> addFriend(FriendDto friendDto) {
		return fs.addFriend(friendDto) ? ResponseEntity.ok(true) : ResponseEntity.status(500).body(false);	
	}
}
