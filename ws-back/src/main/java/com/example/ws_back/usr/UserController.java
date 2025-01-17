package com.example.ws_back.usr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ws_back.usr.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {
	
	private final UserService us;

	/** 회원가입 처리
	 * 
	 * @param user | POST JSON 타입 - User 필드명 매
	 * @return ResponseEntity<Boolean> | 회원가입 여부에 따른 true, false 반환
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Boolean> SignUp(@RequestBody User user) {
		try {
			System.out.println(user);
		 	return ResponseEntity.ok(true);
		} catch(Exception e) {
			return ResponseEntity.status(500).body(false);
		}
		
	}
}
