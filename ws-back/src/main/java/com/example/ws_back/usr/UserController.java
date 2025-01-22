package com.example.ws_back.usr;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ws_back.security.CustomUserDetails;
import com.example.ws_back.security.JwtUtil;
import com.example.ws_back.usr.UserService;
import com.example.ws_back.usr.UserServiceImpl.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {
	
	private final UserService us;
	private final JwtUtil jwtUtil;
	
	/** 
	 * 로그인 처리
	 * @param userDto | POST JSON 타입 - UserDto 필드명 매핑
	 * @return ResponseEntity<?> | userId, token
	 */
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> login(@RequestBody UserDto userDto, @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
		
        String id = userDto.getUserId();
        String password = userDto.getUserPw();
        /* Token, UserId */
        LoginResponse loginResponse = us.login(id, password);
        log.info("로그인 데이터 반환 | USER id : " + loginResponse.getUserDto().getUserId());
        try {
            return ResponseEntity.ok(loginResponse);
            
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid ID or PWD");
        }
	}
	
	/**
     * 토큰을 사용하여 유저 정보 반환
     * @param request
     * @return ResponseEntity<UserDto>
     */
    @RequestMapping(value = "/api/userinfo", method = RequestMethod.GET)
    public ResponseEntity<?> UserRefresh(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 추출
        String token = request.getHeader("Authorization");
        log.info("토큰 여부 확인 | TOKEN : " + token);
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        
        // "Bearer "를 제외한 실제 토큰 부분만 추출
        String jwtToken = token.substring(7);
        // 토큰을 이용하여 유저 정보를 가져오는 로직
        try {
            String userId = jwtUtil.getUserId(jwtToken); // jwtUtil에서 토큰에서 userId 추출 (예: userId, email 등)
            UserDto userDto = us.findByUserId(userId);  // 유저 정보를 찾기 위한 서비스 호출
            
            if (userDto == null) {
                return ResponseEntity.status(404).body("User not found");
            }
            
            return ResponseEntity.ok(userDto);  // 유저 정보를 반환
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error occurred while retrieving user info");
        }
    }

	/** 회원가입 처리
	 * 
	 * @param userDto | POST JSON 타입 - UserDto 필드명 매핑
	 * @return ResponseEntity<Boolean> | 회원가입 여부에 따른 true, false 반환
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Boolean> signup(@RequestBody UserDto userDto) {
		log.info("회원가입 처리 중...");
		return us.signup(userDto) ? ResponseEntity.ok(true) : ResponseEntity.status(500).body(false);	
	}
	
	/** 회원가입 아이디, 닉네임 사용 여부
	 * 
	 * @param request (userId, userNickName)
	 * @return ResponseEntity<Map<String, Boolean>> 
	 */
	@RequestMapping(value = "/checkuser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> checkUser(@RequestBody Map<String, String> request) {
	    
        String type = request.get("type");
        String data = request.get("data");
        
        Map<String, Object> response = new HashMap<>();
  
        if (type.equals("id")) {
            response.put("type", "id");
            response.put("data", us.isIdValid(data));
            log.info("ID 확인 성공 | 반환 : " + us.isIdValid(data));
        } else if (type.equals("nickname")) {
            response.put("type", "nickname");
            response.put("data", us.isNickValid(data));
            log.info("NICKNAME 확인 성공 | 반환 : " + us.isNickValid(data));
        } else {
        	log.info("ID, NICKNAME 확인 실패");
			return ResponseEntity.badRequest().build();
		
		}
		return ResponseEntity.ok(response);
	}
	
}
