package com.example.ws_back.usr;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ws_back.chat.ChatServiceImpl;
import com.example.ws_back.security.JwtUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository ur;
	private final ModelMapper modelMapper;
	private final JwtUtil jwtUtil;
	private final BCryptPasswordEncoder pwEncoder;
	/* REDIS */
	private final RedisTemplate<String, Object> redisTemplate;

	
	/**
     * 유저 데이터 반환
     * @Param : userUuid
     * @Return : User -> UserDto
     */

    public UserDto findByUserUuid(String userUuid){
        return modelMapper.map(ur.findByUserUuid(userUuid),UserDto.class);
    }
    
    
	/**
     * 유저 데이터 저장 (마이페이지)
     * @Param : userUuid
     * @Return : User -> UserDto
     */
    /**
     * Repository @Param updateUserByUserUuid(String userUuid, String userNickname, String isPublic, String userChatReceiveScope);
     */
    
    public boolean updateUser(UserDto userDto) {
    	try {
    	User user = modelMapper.map(userDto, User.class);
    	return ur.updateUserByUserUuid(user.getUserUuid(), user.getUserNickname(), user.getIsPublic(), user.getUserChatReceiveScope()) > 0 ? true : false; 	
    	} catch (Exception e) {
    	    log.error(userDto.getUserUuid() + "님 유저 데이터 저장 오류");
    		return false;
    	}
    	
    }

    /**
     * 로그인 시 유저 데이터 반환
     * @Param : id, password
     * @Return : AccessToken, RefreshToken, User -> UserDto
     */

    public LoginResponse login(String userId, String password){

        User user = ur.findByUserId(userId);
        if(user == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }
        
        if(!pwEncoder.matches(password, user.getUserPw())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        UserDto userDto = modelMapper.map(user,UserDto.class);

        String accessToken = jwtUtil.createAccessToken(userDto);
        String refreshToken = jwtUtil.createRefreshToken(userDto);
        
        return new LoginResponse(accessToken, refreshToken, userDto);
    }

    public String UserRefreshAccessToken(String refreshToken) {
    	String userUuid = jwtUtil.getUserUuid(refreshToken);
    	User user = ur.findByUserUuid(userUuid);
    	UserDto userDto = modelMapper.map(user,UserDto.class);
    	return jwtUtil.createAccessToken(userDto);
    }
    
    /**
     * 로그아웃 
     * @Param : UserUuid, Token
     *
     * 사용자 토큰 → Redis Key 블랙리스트 추가
     * 토큰 만료시간 → Redis TTL 
     */

    public void logout(String userUuid, String prefixRefreshToken){
    	log.info("REDIS (토큰) 데이터를 저장합니다.");
    	
    	String refreshToken = prefixRefreshToken.substring(7);
    	String key = "blacktoken:" + refreshToken; 
    	
    	/* REDIS 가공 클래스 */
    	UserBlackToken ubt = new UserBlackToken();
    	
    	/* 토큰 남은 시간 확인 / 해당 토큰 USERUUID */
    	log.info("Token Expiration : " + jwtUtil.getExpiration(refreshToken));
    	log.info("Logout UserUuid : " + userUuid);
    	
    	/* 객체 값 지정 */
    	ubt.setToken(refreshToken);
    	ubt.setUserUuid(userUuid);
    	ubt.setLogoutTime(LocalDateTime.now());
    	
   
    	redisTemplate.opsForValue().set(key, ubt, Duration.ofSeconds(jwtUtil.getExpiration(refreshToken)/1000));
    }
    
	/**
	 * 회원가입 처리
	 * @param UserDto | UserDto -> User 변환 후 User를 저장
	 * @return Boolean | 회원가입 여부에 따른 true, false 반환
	 */
	public boolean signup(UserDto userDto) {	
		try {
			userDto.setUserUuid("user-" + UUID.randomUUID().toString());
			userDto.setUserPw(pwEncoder.encode(userDto.getUserPw()));
			ur.save(modelMapper.map(userDto, User.class));
			return true;
		} catch (Exception e) {
			return false;
		}
	};
	
	
	/**
	 *  아이디 닉네임 여부
	 *  @param String | id, nickname 
	 *  @return Boolean | 각 값에 따라 값이 존재할 경우 true, 아니면 false
	 *   */
	public boolean isNickValid(String nickname) {
		return ur.existsByUserNickName(nickname);
	}
	public boolean isIdValid(String chkId) {
		return ur.existsByUserId(chkId);
	}
	
	/** 가공 데이터 **/
    /**
     * Front로 반환될 데이터를 가공하는 클래스
     * @return : serviceToken, User
     *
     */
    public class LoginResponse {
        private String accessToken;
        private String refreshToken;

        @JsonProperty("user")
        private UserDto userDto;

        public LoginResponse(String accessToken, String refreshToken, UserDto userDto) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.userDto = userDto;
        }

        public String getAccessToken(){
            return accessToken;

        }
        
        public String getRefreshToken(){
            return refreshToken;

        }

        public UserDto getUserDto(){
            return userDto;

        }
    }
}
