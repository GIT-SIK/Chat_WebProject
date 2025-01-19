package com.example.ws_back.usr;

import java.util.HashMap;
import java.util.Map;

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
	 * @param user | POST JSON 타입 - UserDto 필드명 매핑
	 * @return ResponseEntity<Boolean> | 회원가입 여부에 따른 true, false 반환
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Boolean> signup(@RequestBody UserDto userDto) {
		return us.signup(userDto) ? ResponseEntity.ok(true) : ResponseEntity.status(500).body(false);	
	}
	
	/** 회원가입 아이디, 닉네임 사용 여부
	 * 
	 * @param request (userId, userNickName)
	 * @return ResponseEntity<Map<String, Boolean>> 
	 */
	@RequestMapping(value = "/userchk", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> userChk(@RequestBody Map<String, String> request) {
	    
        String type = request.get("type");
        String data = request.get("data");
        
        Map<String, Object> response = new HashMap<>();
  
        if (type.equals("id")) {
            response.put("type", "id");
            response.put("data", us.isIdValid(data));
        } else if (type.equals("nickname")) {
            response.put("type", "nickname");
            response.put("data", us.isNickValid(data));
        } else {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(response);
	}
	
}
