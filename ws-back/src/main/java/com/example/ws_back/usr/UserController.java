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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ws_back.security.CustomUserDetails;
import com.example.ws_back.security.JwtUtil;
import com.example.ws_back.usr.UserService;
import com.example.ws_back.usr.UserServiceImpl.LoginResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {
	
	private final UserService us;
	private final JwtUtil jwtUtil;
	
	/** [필수 : 항상 인증된 유저를 검증하기 위해 사용되는 메소드]
     * 토큰을 사용하여 유저 정보 반환
     * @param HttpServletRequest
     * @return ResponseEntity<UserDto>
     */
    @RequestMapping(value = "/users/me", method = RequestMethod.GET)
    public ResponseEntity<?> UserRefresh(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 추출
        String token = request.getHeader("Authorization");
        if (token == null || token.equals("Bearer null") || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        
        // "Bearer "를 제외한 실제 토큰 부분만 추출
        String jwtToken = token.substring(7);
        // 토큰을 이용하여 유저 정보를 가져오는 로직
        try {
            String userUuid = jwtUtil.getUserUuid(jwtToken); // jwtUtil에서 토큰에서 userId 추출 (예: userId, email 등)
            UserDto userDto = us.findByUserUuid(userUuid);  // 유저 정보를 찾기 위한 서비스 호출
            
            if (userDto == null) {
                return ResponseEntity.status(403).body("사용자를 확인할 수 없습니다.");
            }
            
            return ResponseEntity.ok(userDto);  // 유저 정보를 반환
        
        } catch (Exception e) {
            return ResponseEntity.status(500).body("사용자를 확인할 수 없습니다.");
        }
    }
    @RequestMapping(value = "/auth/refresh", method = RequestMethod.POST)
    public ResponseEntity<?> UserRefreshAccessToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("REFRESH");
        System.out.println(token);
        
        if(!jwtUtil.validateToken(token.substring(7))) {
        	return ResponseEntity.status(401).body("사용자 인증 정보가 만료되었습니다.");
        }
        
        else if (token == null || token.equals("Bearer null") || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        String refreshJwtToken = token.substring(7);
        try {
        	String refreshAccessToken = us.UserRefreshAccessToken(refreshJwtToken);
        	System.out.println("재발급 ACCESS TOKEN : " + refreshAccessToken);
            return ResponseEntity.ok(refreshAccessToken);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body("리프레시 토큰이 만료되었습니다.");
        } catch (JwtException e) {
            return ResponseEntity.status(403).body("유효하지 않은 토큰입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    		
    }
    	
    
    /* MYPAGE (T)*/
    @RequestMapping(value = "/users/me", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @AuthenticationPrincipal UserDetails userDetails) {
    	
    	userDto.setUserUuid(userDetails.getUsername());
    	boolean isUserUpdate = us.updateUser(userDto);    	
    	return ResponseEntity.ok().body(isUserUpdate ? "저장되었습니다.": "저장할 수 없습니다.");
    }
    
    
    /********* 로그인, 회원가입 *************/
	
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
        try {
        LoginResponse loginResponse = us.login(id, password);
        log.info("로그인 데이터 반환 | USER id : " + loginResponse.getUserDto().getUserId());
            return ResponseEntity.ok(loginResponse);
            
        } catch (Exception e) {
        	log.error(e.getMessage());
            return ResponseEntity.status(401).body("아이디와 비밀번호를 확인해주세요.");
        }
	}
	
	/**
	 * 로그아웃 처리
	 * @param HttpServletRequest
	 * @return ResponseEntity<?> | 로그아웃 여부
	 * 토큰 블랙리스트 추가
	 */
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<?> logout(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {	
        String refreshToken = request.getHeader("Authorization");
        if (refreshToken == null || refreshToken.equals("Bearer null") || !refreshToken.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("잘못된 접근입니다.");
        }    
        try {
        us.logout(userDetails.getUsername(), refreshToken); 
        } catch(Exception e) {
        	return ResponseEntity.status(403).body("유효하지 않은 사용자입니다."); 
        }
        
		return ResponseEntity.ok().body(true);		
	}
    
	/** 회원가입 처리
	 * 
	 * @param userDto | POST JSON 타입 - UserDto 필드명 매핑
	 * @return ResponseEntity<Boolean> | 회원가입 여부에 따른 true, false 반환
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
		log.info("회원가입 처리 중...");
		String userId = userDto.getUserId();
		String userNickname = userDto.getUserNickname();
		String userPw = userDto.getUserPw();
		if((userId.length() < 9 && userId.length() > 2) 
			&& (userNickname.length() < 9 && userNickname.length() > 2 )
			&& (userPw.length() > 3 && userPw.length() < 16)
		) {
			return ResponseEntity.ok(us.signup(userDto));
		} else {
			return ResponseEntity.ok(false);
		}
	}
	
	/** 회원가입 아이디, 닉네임 사용 여부
	 * 
	 * @param request (userId, userNickName)
	 * @return ResponseEntity<Map<String, Boolean>> 
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> checkUser(    @RequestParam(required = false) String id,
		    @RequestParam(required = false) String nickname ) {
	    
      
        Map<String, Object> response = new HashMap<>();
  
        log.info("--------------------- 회원가입 체크 ---------------------");
        if (id != null) {
            response.put("type", "id");
            response.put("data", us.isIdValid(id));
            log.info("ID 확인 성공 | 반환 : " + us.isIdValid(id));
        } else if (nickname != null) {
            response.put("type", "nickname");
            response.put("data", us.isNickValid(nickname));
            log.info("NICKNAME 확인 성공 | 반환 : " + us.isNickValid(nickname));
        } else {
        	log.info("ID, NICKNAME 확인 실패");
        	log.info("------------------------------------------------------");
			return ResponseEntity.badRequest().build();
		
		}
        log.info("------------------------------------------------------");
		return ResponseEntity.ok(response);
	}
	
	
	/* ************************************* */
	 /**  Test 코드 : 토큰 사용자 확인 용
	  * (인가된 사용자만 접근) 
	  * 
	  * @return USER_ID
	  */
	
	@RequestMapping(value = "/admin/info", method = RequestMethod.POST)
	@ResponseBody
	public UserDto testpage(@AuthenticationPrincipal CustomUserDetails userDetails) {
	    // 인증된 사용자 정보 가져오기
	    String username = userDetails.getUsername();
	    log.info("인증된 사용자: " + username);

	    // 데이터베이스에서 사용자 정보를 조회하여 반환
	    return us.findByUserUuid(username);
	}
}
